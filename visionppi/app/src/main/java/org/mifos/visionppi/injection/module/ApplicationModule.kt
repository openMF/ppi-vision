package org.mifos.visionppi.injection.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import org.mifos.visionppi.api.BaseApiManager
import org.mifos.visionppi.api.local.PreferencesHelper
import org.mifos.visionppi.injection.ApplicationContext
import javax.inject.Singleton

/**
 * @author yashk2000
 * @since 22/06/2020
 */
@Module
class ApplicationModule(private val application: Application) {
    @Provides
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun providePrefManager(@ApplicationContext context: Context?): PreferencesHelper {
        return PreferencesHelper(context)
    }

    @Provides
    @Singleton
    fun provideBaseApiManager(preferencesHelper: PreferencesHelper?): BaseApiManager {
        return BaseApiManager(preferencesHelper)
    }

}