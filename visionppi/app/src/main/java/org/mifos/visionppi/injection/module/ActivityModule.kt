package org.mifos.visionppi.injection.module

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import org.mifos.visionppi.injection.ActivityContext

/**
 * @author yashk2000
 * @since 22/06/2020
 */
@Module
class ActivityModule(private val activity: Activity) {
    @Provides
    fun providesActivity(): Activity {
        return activity
    }

    @Provides
    @ActivityContext
    fun providesContext(): Context {
        return activity
    }

}