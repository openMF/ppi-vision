package org.mifos.visionppi.injection.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import org.mifos.visionppi.api.BaseApiManager
import org.mifos.visionppi.api.local.PreferencesHelper
import org.mifos.visionppi.injection.ApplicationContext

/**
 * @author HARSH-nith
 * @since 13/07/2022
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
