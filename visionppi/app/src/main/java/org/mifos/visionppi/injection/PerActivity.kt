package org.mifos.visionppi.injection

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Scope

/**
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the Activity to be memorised in the
 * correct component.
 *
 * @author yashk2000
 * @since 22/06/2020
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
annotation class PerActivity