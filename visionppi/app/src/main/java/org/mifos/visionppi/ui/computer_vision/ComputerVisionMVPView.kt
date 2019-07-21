package org.mifos.visionppi.ui.computer_vision

import org.mifos.visionppi.base.MVPView

interface ComputerVisionMVPView : MVPView {

    fun fetchFromGallery()

    fun fetchFromCamera()

    fun analyzeImages()

}