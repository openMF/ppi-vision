package org.mifos.visionppi.injection

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Qualifier

/**
 * @author yashk2000
 * @since 22/06/2020
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
annotation class ApplicationContext
