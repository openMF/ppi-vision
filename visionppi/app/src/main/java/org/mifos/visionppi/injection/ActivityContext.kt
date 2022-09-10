package org.mifos.visionppi.injection

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Qualifier

/**
 * @author HARSH-nith
 * @since 13/07/2022
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
annotation class ActivityContext
