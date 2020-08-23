package org.mifos.visionppi.injection.component

import dagger.Component
import org.mifos.visionppi.injection.PerActivity
import org.mifos.visionppi.injection.module.ActivityModule
import org.mifos.visionppi.ui.activities.LoginActivity
import org.mifos.visionppi.ui.activities.SplashActivity

/**
 * @author yashk2000
 * @since 22/06/2020
 */
@PerActivity
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(loginActivity: LoginActivity?)
    fun inject(splashActivity: SplashActivity?)
}
