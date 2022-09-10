package org.mifos.visionppi.injection

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Scope

/**
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the Activity to be memorised in the
 * correct component.
 *
 * @author HARSH-nith
 * @since 13/07/2022
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
annotation class PerActivity
