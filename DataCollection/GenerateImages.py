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
from scipy.ndimage.interpolation import map_coordinates
from scipy.ndimage.filters import gaussian_filter

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

def add_shadow(image, darkness_factor=0.5, x_offset=25, y_offset=25):
    # Convert image to grayscale
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    # Apply a binary threshold to create a mask of the image
    ret, mask = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY_INV+cv2.THRESH_OTSU)
    # Blur the mask to create a softer shadow effect
    mask_blur = cv2.GaussianBlur(mask, (21, 21), 0)
    # Create a copy of the original image and apply the mask to it
    shadow = np.copy(image)
    shadow[mask_blur>0] = (shadow[mask_blur>0]*darkness_factor).astype(np.uint8)
    # Shift the shadow image by the specified x and y offsets
    M = np.float32([[1, 0, x_offset], [0, 1, y_offset]])
    shadow = cv2.warpAffine(shadow, M, (image.shape[1], image.shape[0]))
    # Combine the original image and the shadow image using a bitwise OR operation
    result = cv2.bitwise_or(image, shadow)
    return result

def add_glare(image, brightness_factor=0.5, x_offset=25, y_offset=25):
    # Create a copy of the original image
    glare = np.copy(image)
    # Set the pixels in the top left corner of the image to white
    glare[:100, :100, :] = [255, 255, 255]
    # Blend the glare image with the original image using the specified brightness factor
    glare = cv2.addWeighted(image, 1 - brightness_factor, glare, brightness_factor, 0)
    # Shift the glare image by the specified x and y offsets
    M = np.float32([[1, 0, x_offset], [0, 1, y_offset]])
    glare = cv2.warpAffine(glare, M, (image.shape[1], image.shape[0]))
    # Combine the original image and the glare image using a bitwise OR operation
    result = cv2.bitwise_or(image, glare)
    return result

def add_fog(image, brightness=50, density=0.5):
    # Convert the image to grayscale
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    # Generate a mask with random noise
    mask = np.zeros_like(gray)
    h, w = mask.shape[:2]
    noise = cv2.randu(mask, 0, 255)
    mask = cv2.GaussianBlur(mask, (51, 51), 0)
    mask = cv2.threshold(mask, 240, 255, cv2.THRESH_BINARY)[1]
    # Blend the mask with the original image
    mask = cv2.cvtColor(mask, cv2.COLOR_GRAY2BGR)
    mask = np.float32(mask) / 255.0
    result = ((1.0 - density) * image + density * (brightness * mask))
    result = np.clip(result, 0, 255)
    result = np.uint8(result)
    return result

def add_speckle_noise(image, mean=0, std=50):
    # Generate random noise
    h, w, c = image.shape
    noise = np.random.normal(mean, std, size=(h, w, c))
    # Add noise to the image
    noisy_image = image + image * noise / 255.0
    noisy_image = np.clip(noisy_image, 0, 255).astype(np.uint8)
    return noisy_image

def apply_local_distortion(image, distortion_intensity=0.5):
    # Generate random noise with the same shape as the image
    noise = np.random.normal(scale=distortion_intensity, size=image.shape)
    # Add the noise to the image
    distorted_image = image + noise
    # Clip the pixel values to [0, 255] range
    distorted_image = np.clip(distorted_image, 0, 255)
    # Convert the image to uint8 format
    distorted_image = distorted_image.astype(np.uint8)
    return distorted_image

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
    for num in range (0, 6):
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
    
    for num in range(0, 6):
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

    # rotation of image to 90^
    imgRot90=cv2.rotate(image,cv2.ROTATE_90_CLOCKWISE)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, imgRot90)

    # rotation of image to 180^
    imgRot180=cv2.rotate(image,cv2.ROTATE_180)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, imgRot180)

    # rotation of image to 270^
    imgRot270=cv2.rotate(image,cv2.ROTATE_90_COUNTERCLOCKWISE)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, imgRot270)

    # bgr image
    bgrImg=cv2.imread(path)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, bgrImg)

    # rgb image
    imgRGB=cv2.imread(path,cv2.COLOR_BAYER_BG2RGB)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, imgRGB)

    # lab image
    imgLAB=cv2.imread(path,cv2.COLOR_BGR2LAB)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, imgLAB)

    # xyz image
    imgXYZ=cv2.imread(path,cv2.COLOR_BGR2XYZ)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, imgXYZ)

    # center crop
    height, width = image.shape[:2]
    crop_width = crop_height = min(width, height)
    center_x = int(width/2)
    center_y = int(height/2)
    x = center_x - int(crop_width/2)
    y = center_y - int(crop_height/2)
    w = h = crop_width
    cropImg=image[y:y+h, x:x+w]
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, cropImg)

    # mean blur of image
    kernel_size = 3
    mean_filtered = cv2.blur(image, (kernel_size, kernel_size)) 
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, mean_filtered)

    #median blur
    kernel_size = 3
    median_filtered = cv2.medianBlur(image, kernel_size)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, median_filtered)

    #interpolation of image
    height,width,ch=image.shape
    dim=[height//2,width//2]
    imgAreaResize=cv2.resize(image,dim,interpolation=cv2.INTER_AREA)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, imgAreaResize)

    #interpolation 2nd technique
    height,width,ch=image.shape
    dim=[height//2,width//2]
    imgCubicResize=cv2.resize(image,dim,interpolation=cv2.INTER_CUBIC)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, imgCubicResize)

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

    # Augmentation by scaling images
    height, width = image.shape[:2]
    
    # Scale the image by a factor of 0.5
    scale = 0.5
    resized = cv2.resize(image, (int(width*scale), int(height*scale)))
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, resized)

    # Scale the image by a factor of 1.5
    scale = 1.5
    resized = cv2.resize(image, (int(width*scale), int(height*scale)))
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, resized)
    
    # Scale the image by a factor of 2
    scale = 2
    resized = cv2.resize(image, (int(width*scale), int(height*scale)))
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, resized)

    # Augmentation by color channel swapping
    # Swap Red and Blue channels
    swapped = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, swapped)

    # Swap Green and Blue channels
    swapped = image[:, :, ::-1]
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, swapped)

    # Swap Red and Green channels
    swapped = image[:, ::-1, :]
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, swapped)

    # Augmentation by adding random noise and blurring
    # Add Gaussian noise to the image
    mean = 0
    variance = 50
    noise = np.random.normal(mean, variance, image.shape)
    noisy_image = np.clip(image + noise, 0, 255).astype(np.uint8)

    # Apply Gaussian blur to the noisy image
    ksize = (5, 5)
    sigmaX = 5
    blurred = cv2.GaussianBlur(noisy_image, ksize, sigmaX)

    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, blurred)

    # Add Salt-and-Pepper noise to the image
    s_vs_p = 0.5
    amount = 0.05
    noisy_image = image.copy()
    # Salt mode
    num_salt = np.ceil(amount * image.size * s_vs_p)
    coords = [np.random.randint(0, i - 1, int(num_salt)) for i in image.shape]
    noisy_image[coords] = 255

    # Pepper mode
    num_pepper = np.ceil(amount * image.size * (1. - s_vs_p))
    coords = [np.random.randint(0, i - 1, int(num_pepper)) for i in image.shape]
    noisy_image[coords] = 0

    # Apply Median blur to the noisy image
    ksize = 5
    blurred = cv2.medianBlur(noisy_image, ksize)

    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, blurred)

    # Augmentation by randomly rotating and zooming
    # Randomly rotate the image by a random angle between -10 and 10 degrees
    angle = np.random.uniform(-10, 10)
    rotated = ndimage.rotate(image, angle, reshape=False)

    # Randomly zoom the rotated image by a random factor between 0.9 and 1.1
    zoom_factor = np.random.uniform(0.9, 1.1)
    zoomed = ndimage.zoom(rotated, zoom_factor)

    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, zoomed)

        # Augmentation by randomly dropping color channels
    channels = cv2.split(image)
    num_channels = len(channels)

    # Randomly drop one or more color channels
    num_dropped = np.random.randint(1, num_channels + 1)
    dropped_channels = np.random.choice(num_channels, num_dropped, replace=False)
    for j in dropped_channels:
        channels[j][:] = 0

    # Merge the remaining channels
    merged = cv2.merge(channels)

    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, merged)

    # Augmentation by cutout with random erasing
    height, width, channels = image.shape

    # Define the size and position of the cutout
    cutout_size = int(min(height, width) * 0.2) # Cutout size is 20% of the smaller dimension
    cutout_x = np.random.randint(0, width - cutout_size + 1)
    cutout_y = np.random.randint(0, height - cutout_size + 1)

    # Apply cutout to the image
    cutout_image = np.copy(image)
    cutout_image[cutout_y:cutout_y+cutout_size, cutout_x:cutout_x+cutout_size, :] = 0

    # Define the size and position of the random erasing
    erase_size = int(min(height, width) * 0.1) # Erase size is 10% of the smaller dimension
    erase_x = np.random.randint(0, width - erase_size + 1)
    erase_y = np.random.randint(0, height - erase_size + 1)

    # Apply random erasing to the image
    erase_image = np.copy(cutout_image)
    erase_image[erase_y:erase_y+erase_size, erase_x:erase_x+erase_size, :] = np.random.randint(0, 256, (erase_size, erase_size, channels))

    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, erase_image)

    # Augmentation by geometric transformations
    height, width, channels = image.shape

    # Skew transformation
    skew_x = np.random.randint(-int(width * 0.1), int(width * 0.1))
    skew_y = np.random.randint(-int(height * 0.1), int(height * 0.1))
    pts1 = np.float32([[0, 0], [width, 0], [0, height], [width, height]])
    pts2 = np.float32([[0, 0], [width, 0], [skew_x, height+skew_y], [width+skew_x, height+skew_y]])
    M = cv2.getPerspectiveTransform(pts1, pts2)
    skew_image = cv2.warpPerspective(image, M, (width, height))

    # Stretch transformation
    stretch_x = np.random.uniform(0.8, 1.2)
    stretch_y = np.random.uniform(0.8, 1.2)
    stretch_image = cv2.resize(image, (int(width*stretch_x), int(height*stretch_y)))

    # Twist transformation
    twist_x = np.random.randint(-int(width * 0.1), int(width * 0.1))
    twist_y = np.random.randint(-int(height * 0.1), int(height * 0.1))
    M = cv2.getRotationMatrix2D((width/2, height/2), 15, 1)
    M[0][2] += twist_x
    M[1][2] += twist_y
    twist_image = cv2.warpAffine(image, M, (width, height))

    # Save the transformed images
    savePath = output + str(i) + "_skew.png"
    cv2.imwrite(savePath, skew_image)
    i += 1
    savePath = output + str(i) + "_stretch.png"
    cv2.imwrite(savePath, stretch_image)
    i += 1
    savePath = output + str(i) + "_twist.png"
    cv2.imwrite(savePath, twist_image)
    i += 1

    # Augmentation by local pixelization
    height, width, channels = image.shape
    pixel_size = int(min(height, width) * 0.1) # Size of each pixel is 10% of the smaller dimension
    for y in range(0, height, pixel_size):
        for x in range(0, width, pixel_size):
            image[y:y+pixel_size, x:x+pixel_size, :] = np.mean(image[y:y+pixel_size, x:x+pixel_size, :], axis=(0,1), keepdims=True)

    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, image)

    
    # Shadow Effect
    shadow_image = add_shadow(image, darkness_factor=0.7, x_offset=50, y_offset=50)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, shadow_image)

    # Glare Effect
    glare_image = add_glare(image, brightness_factor=0.7, x_offset=50, y_offset=50)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, glare_image)

    # Fog Effect
    fog_image = add_fog(image, brightness=80, density=0.6)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, fog_image)

    # Speckle Noise
    noisy_image = add_speckle_noise(image, mean=0, std=50)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, noisy_image)

    # Local Distortion
    distorted_image  = apply_local_distortion(image, distortion_intensity=0.5)
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, distorted_image)

    # Applying the Patch Gaussian augmentation
    augmented_image = image.copy()
    rows, cols, channels = augmented_image.shape
    for _ in range(5):
        # Generate random patch
        patch_size = np.random.randint(20, 50)
        x = np.random.randint(0, cols - patch_size)
        y = np.random.randint(0, rows - patch_size)
        patch = augmented_image[y:y+patch_size, x:x+patch_size].copy()

        # Apply Gaussian blur to patch
        patch = cv2.GaussianBlur(patch, (15, 15), 0)

        # Blend patch into original image
        alpha = np.random.uniform(0.3, 0.7)
        augmented_image[y:y+patch_size, x:x+patch_size] = cv2.addWeighted(patch, alpha, augmented_image[y:y+patch_size, x:x+patch_size], 1-alpha, 0)
    
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, augmented_image)

    # Perspective cropping
    height, width = image.shape[:2]
    pts1 = np.float32([[0, 0], [width, 0], [0, height], [width, height]])
    pt1 = 0.15*width
    pt2 = 0.85*width
    pts2 = np.float32([[pt1, 0], [pt2, 0], [0, height], [width, height]])
    M = cv2.getPerspectiveTransform(pts1, pts2)
    perspective = cv2.warpPerspective(image, M, (width, height))
    savePath = output + str(i) + ".png"
    i += 1
    cv2.imwrite(savePath, perspective)