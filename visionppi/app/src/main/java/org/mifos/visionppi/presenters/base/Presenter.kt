package org.mifos.visionppi.presenters.base

import org.mifos.visionppi.ui.views.base.MVPView

/**
 * @author yashk2000
 * @since 22/06/2020
 */
interface Presenter<V : MVPView?> {
    fun attachView(mvpView: V)
    fun detachView()
}