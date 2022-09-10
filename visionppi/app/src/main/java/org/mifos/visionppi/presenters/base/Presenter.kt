package org.mifos.visionppi.presenters.base

import org.mifos.visionppi.ui.views.base.MVPView

/**
 * @author HARSH-nith
 * @since 13/07/2022
 */
interface Presenter<V : MVPView?> {
    fun attachView(mvpView: V)
    fun detachView()
}
