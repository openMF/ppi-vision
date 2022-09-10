package org.mifos.visionppi.injection.module

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import org.mifos.visionppi.injection.ActivityContext

/**
 * @author HARSH-nith
 * @since 13/07/2022
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
