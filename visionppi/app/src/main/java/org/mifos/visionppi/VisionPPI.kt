package org.mifos.visionppi

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import java.util.Locale
import org.mifos.visionppi.injection.component.ApplicationComponent
import org.mifos.visionppi.injection.component.DaggerApplicationComponent
import org.mifos.visionppi.injection.module.ApplicationModule
import org.mifos.visionppi.utils.LanguageHelper.onAttach

/**
 * @author HARSH-nith
 * @since 13/07/2022
 */
class VisionPPI : MultiDexApplication() {
    var applicationComponent: ApplicationComponent? = null

    companion object {
        private var instance: VisionPPI? = null
        operator fun get(context: Context): VisionPPI {
            return context.applicationContext as VisionPPI
        }

        val context: Context?
            get() = instance

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        Fabric.with(this, Crashlytics())
        instance = this
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(onAttach(base, Locale.getDefault().language))
    }

    fun component(): ApplicationComponent? {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(ApplicationModule(this))
                    .build()
        }
        return applicationComponent
    }
}
