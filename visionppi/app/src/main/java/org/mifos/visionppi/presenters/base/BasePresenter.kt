package org.mifos.visionppi.presenters.base

import android.content.Context
import org.mifos.visionppi.ui.views.base.MVPView

/**
 * @author yashk2000
 * @since 22/06/2020
 */
open class BasePresenter<T : MVPView?> protected constructor(protected var context: Context) : Presenter<T> {
    var mvpView: T? = null
        private set

    override fun attachView(mvpView: T) {
        this.mvpView = mvpView
    }

    override fun detachView() {
        mvpView = null
    }

    val isViewAttached: Boolean
        get() = mvpView != null

    fun checkViewAttached() {
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

    class MvpViewNotAttachedException : RuntimeException("Please call Presenter.attachView(MvpView) before" +
            " requesting data to the Presenter")

}