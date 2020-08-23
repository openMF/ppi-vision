package org.mifos.visionppi.base

/**
 * Created by Apoorva M K on 25/06/19.
 */

open class BasePresenter<T : MVPView> {

    private lateinit var mMVPView: T

    public fun getMVPView(): T {
        return mMVPView
    }
}
