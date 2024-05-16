package world.neptuns.core.base.api.player

import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.controller.api.service.NeptunService

interface NeptunOnlinePlayer : NeptunOfflinePlayer {

    var currentProxyName: String
    var currentServiceName: String

    fun getCurrentProxy(): NeptunService? = NeptunControllerProvider.api.serviceController.getService(this.currentProxyName)
    fun getCurrentService(): NeptunService? = NeptunControllerProvider.api.serviceController.getService(this.currentServiceName)

}