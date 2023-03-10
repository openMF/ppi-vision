# python GenerateImages.py [-h] -i INPUT -o OUTPUT [-n NUMBER]

# Import all the needed modules

from torchvision import transforms
from scipy.stats import norm
from PIL import Image
import numpy as np
import argparse
import decimal
import random
import glob
import cv2

# Helper function to generate a mask for parallel light method
def generate_parallel_light_mask(mask_size,
                                 max_brightness=255,
                                 min_brightness=0,
                                 mode="gaussian"):

    
    pos_x = random.randint(0, mask_size[0])
    pos_y = random.randint(0, mask_size[1])
    
    direction = random.randint(0, 360)
        
    padding = int(max(mask_size) * np.sqrt(2))
    canvas_x = padding * 2 + mask_size[0]
    canvas_y = padding * 2 + mask_size[1]
    mask = np.zeros(shape=(canvas_y, canvas_x), dtype=np.float32)
    init_mask_ul = (int(padding), int(padding))
    init_mask_br = (int(padding+mask_size[0]), int(padding+mask_size[1]))
    init_light_pos = (padding + pos_x, padding + pos_y)
    
    for i in range(canvas_y):
        i_value = _decayed_value_in_norm(i, max_brightness, min_brightness, init_light_pos[1], mask_size[1])
        mask[i] = i_value
        
    rotate_M = cv2.getRotationMatrix2D(init_light_pos, direction, 1)
    mask = cv2.warpAffine(mask, rotate_M, (canvas_x,  canvas_y))

    mask = mask[init_mask_ul[1]:init_mask_br[1], init_mask_ul[0]:init_mask_br[0]]
    mask = np.asarray(mask, dtype=np.uint8)

    mask = cv2.medianBlur(mask, 9)
    mask = 255 - mask
    
    return mask

# Helper function for parallel light method
def _decayed_value_in_norm(x, max_value, min_value, center, range):

    radius = range / 3
    center_prob = norm.pdf(center, center, radius)
    x_prob = norm.pdf(x, center, radius)
    x_value = (x_prob / center_prob) * (max_value - min_value) + min_value
    return x_value

# Helper function for parallel light method
def _decayed_value_in_linear(x, max_value, padding_center, decay_rate):

    x_value = max_value - abs(padding_center - x) * decay_rate
    if x_value < 0:
        x_value = 1
    return x_value

# Helper function to generate a mask for the spot light method
def generate_spot_light_mask(mask_size,
                             max_brightness = 255,
                             min_brightness = 0,
                             mode = "gaussian",
                             speedup = False):

    position = [(random.randint(0, mask_size[0]), random.randint(0, mask_size[1]))]

    mask = np.zeros(shape=(mask_size[1], mask_size[0]), dtype=np.float32)
    
    mu = np.sqrt(mask.shape[0]**2+mask.shape[1]**2)
    dev = mu / 3.5
    mask = _decay_value_radically_norm_in_matrix(mask_size, position, max_brightness, min_brightness, dev)
    mask = np.asarray(mask, dtype=np.uint8)
    
    mask = cv2.medianBlur(mask, 5)
    mask = 255 - mask
    
    return mask

# Helper function for the spot light method
def _decay_value_radically_norm_in_matrix(mask_size, centers, max_value, min_value, dev):

    center_prob = norm.pdf(0, 0, dev)
    x_value_rate = np.zeros((mask_size[1], mask_size[0]))
    
    for center in centers:
        coord_x = np.arange(mask_size[0])
        coord_y = np.arange(mask_size[1])
        xv, yv = np.meshgrid(coord_x, coord_y)
        
        dist_x = xv - center[0]
        dist_y = yv - center[1]
        dist = np.sqrt(np.power(dist_x, 2) + np.power(dist_y, 2))
        
        x_value_rate += norm.pdf(dist, 0, dev) / center_prob

    mask = x_value_rate * (max_value - min_value) + min_value
    mask[mask > 255] = 255
    
    return mask

# Helper function for the spot light method
def _decay_value_radically_norm(x, centers, max_value, min_value, dev):

    center_prob = norm.pdf(0, 0, dev)
    x_value_rate = 0
    
    for center in centers:
        distance = np.sqrt((center[0]-x[0])**2 + (center[1]-x[1])**2)
        x_value_rate += norm.pdf(distance, 0, dev) / center_prob
        
    x_value = x_value_rate * (max_value - min_value) + min_value
    x_value = 255 if x_value > 255 else x_value
    
    return x_value

# Allowing users to give input as command line arguments
ap = argparse.ArgumentParser()

ap.add_argument("-i", "--input", required=True, 
    help="path to the folder containing images")
ap.add_argument("-o", "--output", required=True, 
    help="path to output folder for storing augmented images")

args = vars(ap.parse_args())

# Reading all images for a given folder
path = args["input"]
ext = ['png', 'jpg']    # Add image formats here

files = []
[files.extend(glob.glob(path + '*.' + e)) for e in ext]

images = [cv2.imread(file) for file in files]

# Starting with augmentation
output = args["output"]
i = 1
for image in images:
    
    # Augmentation by flipping images
    flip = cv2.flip(image, 0) # Flip an image vertically
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, flip)

    flip = cv2.flip(image, 1) # Flip an image horizontally
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, flip)

    flip = cv2.flip(image, -1) # Flip an image both vertically and horizontally
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, flip)

    # Changing brightness out an image
    for num in range (0, 5):
        gamma = float(decimal.Decimal(random.randrange(10, 1000))/100)
        invGamma = 1.0 / gamma
        table = np.array([((i / 255.0) ** invGamma) * 255
            for i in np.arange(0, 256)]).astype("uint8")
        bright = cv2.LUT(image, table)
        savePath = output + str(i) + ".png"
        i += 1
        cv2.imwrite(savePath, bright)

    # Changing image to black and white
    r,g,b = image[:,:,0], image[:,:,1], image[:,:,2]
    gray = 0.2989 * r + 0.5870 * g + 0.1140 * b
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, gray)


    # Changing contrast of the image
    lab = cv2.cvtColor(image, cv2.COLOR_BGR2LAB)

    l, a, b = cv2.split(lab)
    
    for num in range(0, 5):
        value = float(decimal.Decimal(random.randrange(10, 1000))/100)
        clahe = cv2.createCLAHE(clipLimit=value, tileGridSize=(8,8))
        cl = clahe.apply(l)

        limg = cv2.merge((cl,a,b))

        final = cv2.cvtColor(limg, cv2.COLOR_LAB2BGR)

        savePath = output + str(i) + ".png"
        i += 1
        cv2.imwrite(savePath, final)

    # Augmentation using cropping
    crops = []

    (h, w) = image.shape[:2]
    width = w - 150
    height = h - 100
    coords = [
        [0, 0, width, height],
        [w - width, 0, w, height],
        [w - width, h - height, w, h],
        [0, h - height, width, h]]

    dW = int(0.5 * (w - width))
    dH = int(0.5 * (h - height))
    coords.append([dW, dH, w - dW, h - dH])

    for (startX, startY, endX, endY) in coords:
        crop = image[startY:endY, startX:endX]
        crop = cv2.resize(crop, (width, height), interpolation=cv2.INTER_AREA)
        crops.append(crop)

    for c in crops:
        savePath = output + str(i) + ".png"
        i += 1
        cv2.imwrite(savePath, c)

    # Average blurring
    blur = cv2.blur(image,(5,5))
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, blur)

    # Gaussian blur
    blur = cv2.GaussianBlur(image,(5,5),0)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, blur)

    # Median Blur
    median = cv2.medianBlur(image,5)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, median)

    # RGB image
    imgRGB=cv2.imread(path,cv2.COLOR_BAYER_BG2RGB)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, imgRGB)

    # Bilateral Filtering
    blur = cv2.bilateralFilter(image,9,75,75)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, blur)

    # Adding padding to image
    padded = cv2.copyMakeBorder(image, 20, 20, 20, 20, cv2.BORDER_CONSTANT)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, padded)

    # Translation
    num_rows, num_cols = image.shape[:2]
    translation_matrix = np.float32([ [1,0,70], [0,1,110]])
    dst = cv2.warpAffine(image, translation_matrix, (num_cols, num_rows))
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, dst)

    # Translation with lesser cropping"
    dst = cv2.warpAffine(image, translation_matrix, (num_cols + 90, num_rows + 150))
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, dst)

    # Translation with image in the midddle of a bigger frame",
    dst = cv2.warpAffine(image, translation_matrix, (num_cols + 70, num_rows + 110))
    translation_matrix = np.float32([ [1,0,-30], [0,1,-50] ])
    dst = cv2.warpAffine(dst, translation_matrix, (num_cols + 70 + 30, num_rows + 110 + 50))
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, dst)

    # Histogram Equalization
    B, G, R = cv2.split(image)
    B = cv2.equalizeHist(B)
    G = cv2.equalizeHist(G)
    R = cv2.equalizeHist(R)
    equalized = cv2.merge((B, G, R))
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, equalized)

    #CLAHE - Contrast Limited Adaptive Histogram Equalization
    B, G, R = cv2.split(image)
    clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8,8))
    cl1 = clahe.apply(B)
    cl2 = clahe.apply(G)
    cl3 = clahe.apply(R)
    claheImage = cv2.merge((B, G, R))
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, claheImage)

    # Saturation
    img = Image.fromarray(cv2.cvtColor(image, cv2.COLOR_BGR2RGB)) # Convert openCV image to PIL image
    loader_transform = transforms.ColorJitter(saturation=1)
    imgOut = loader_transform(img)
    savePath = output + str(i) + ".png"
    i += 1
    numpy_image = np.array(imgOut)  # converting PIL image back to openCV
    imgOut=cv2.cvtColor(numpy_image, cv2.COLOR_RGB2BGR) # the color is converted from RGB to BGR format
    cv2.imwrite(savePath, imgOut)

    # Hue
    img = Image.fromarray(cv2.cvtColor(image, cv2.COLOR_BGR2RGB)) # Convert openCV image to PIL image
    loader_transform = transforms.ColorJitter(hue=0.2)
    imgOut = loader_transform(img)
    savePath = output + str(i) + ".png"
    i += 1
    numpy_image = np.array(imgOut)  # Converting PIL image back to openCV
    imgOut=cv2.cvtColor(numpy_image, cv2.COLOR_RGB2BGR) # the color is converted from RGB to BGR format
    cv2.imwrite(savePath, imgOut)

    # Adaptive Guassian Thresholding
    B ,G ,R = cv2.split(image)
    B = cv2.adaptiveThreshold(B, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
                            cv2.THRESH_BINARY_INV, 11, 2)
    G = cv2.adaptiveThreshold(G, 255 , cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
                            cv2.THRESH_BINARY_INV, 11, 2)
    R = cv2.adaptiveThreshold(R, 255 , cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
                            cv2.THRESH_BINARY_INV, 11, 2)
    imgOut = cv2.merge([B, G, R])
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, imgOut)

    # Affine Transformation
    rows, cols = image.shape[:2]
    src = np.float32([[0, 0 ],
                    [cols - 1 , 0],
                    [ 0 ,rows - 1 ]])

    dst = np.float32([[0, 0],
                    [int(0.6 * (cols - 1 )), 0],
                    [int(0.4 * (cols - 1 )), rows - 1 ]])

    affine = cv2.getAffineTransform(src, dst)
    transformed = cv2.warpAffine(image, affine, (cols,rows))
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, transformed)

    # Salt and pepper noise
    row, col, ch = image.shape
    s_vs_p = 0.5
    amount = 0.004
    out = np.copy(image)

    # salt noise
    num_salt = np.ceil(amount * image.size * s_vs_p)
    coords = [np.random.randint(0, 
                                i - 1,
                                int(num_salt)) for i in image.shape]
    out[coords] = 1

    # pepper noise

    num_pepper = np.ceil(amount* image.size * (1. - s_vs_p))
    coords = [np.random.randint(0,
                                i - 1,
                                int(num_pepper)) for i in image.shape]
    out[coords] = 0

    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, out)

    # Parallel light
    frame = image
    transparency = random.uniform(0.5, 0.85)
    height, width, _ = frame.shape
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    mask = generate_parallel_light_mask(mask_size=(width, height),
                                        max_brightness = 255,
                                        min_brightness = 0,
                                        mode = "gaussian")
    hsv[:, :, 2] = hsv[:, :, 2] * transparency + mask * (1 - transparency)
    frame = cv2.cvtColor(hsv, cv2.COLOR_HSV2BGR)
    frame[frame > 255] = 255
    frame = np.asarray(frame, dtype=np.uint8)

    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, frame)

    # Spotlight method 
    frame = image
    height, width, _ = frame.shape
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    mask = generate_spot_light_mask(mask_size=(width, height),
                                    max_brightness = 255,
                                    min_brightness = 0,
                                    mode = "gaussian")
    hsv[:, :, 2] = hsv[:, :, 2] * transparency + mask * (1 - transparency)
    frame = cv2.cvtColor(hsv, cv2.COLOR_HSV2BGR)
    frame[frame > 255] = 255
    frame = np.asarray(frame, dtype=np.uint8)

    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, frame)
