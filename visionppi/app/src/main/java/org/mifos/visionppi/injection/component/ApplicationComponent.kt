package org.mifos.visionppi.injection.component

import android.app.Application
import android.content.Context
import dagger.Component
import javax.inject.Singleton
import org.mifos.visionppi.api.BaseApiManager
import org.mifos.visionppi.api.DataManager
import org.mifos.visionppi.api.local.PreferencesHelper
import org.mifos.visionppi.injection.ApplicationContext
import org.mifos.visionppi.injection.module.ApplicationModule

/**
 * @author yashk2000
 * @since 22/06/2020
 */
@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    @ApplicationContext
    fun context(): Context?
    fun application(): Application?
    fun dataManager(): DataManager?
    fun prefManager(): PreferencesHelper?
    fun baseApiManager(): BaseApiManager?
}
