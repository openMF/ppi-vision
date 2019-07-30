package org.mifos.visionppi.ui.client_profile

import org.mifos.visionppi.base.MVPView
import org.mifos.visionppi.objects.Client

interface ClientProfileMVPView : MVPView {

    fun setClientDetails(client: Client)
}