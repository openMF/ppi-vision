package org.mifos.visionppi.ui.home

import org.mifos.visionppi.base.MVPView

/**
 * Created by Apoorva M K on 27/06/19.
 */

interface MainMVPView:MVPView {

    fun search(string: String)
    fun searchError()
    fun searchUnsuccessful()
}