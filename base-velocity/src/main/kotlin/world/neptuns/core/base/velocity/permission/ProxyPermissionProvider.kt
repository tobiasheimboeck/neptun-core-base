package world.neptuns.core.base.velocity.permission

import com.velocitypowered.api.permission.PermissionFunction
import com.velocitypowered.api.permission.PermissionProvider
import com.velocitypowered.api.permission.PermissionSubject
import com.velocitypowered.api.permission.Tristate
import com.velocitypowered.api.proxy.Player

class ProxyPermissionProvider : PermissionProvider {

    override fun createFunction(subject: PermissionSubject): PermissionFunction {
        if (subject !is Player) return PermissionFunction { null }
        return VelocityPermissionFunction(subject)
    }

    private data class VelocityPermissionFunction(val player: Player) : PermissionFunction {
        private val permissions = listOf("velocity.command.plugins")

        private fun hasPermission(permission: String): Boolean {
            return permissions.contains(permission)
        }

        override fun getPermissionValue(permission: String): Tristate {
            // val playerHandler: PermissionPlayerHandler = ElytraProvider.getApi().getPermissionPlayerHandler()
            // val permissionPlayer: PermissionPlayer = playerHandler.getCachedPermissionPlayer(player.uniqueId).orElse(null) ?: return Tristate.FALSE

            // return Tristate.fromBoolean(permissionPlayer.hasPermission(permission))

            return Tristate.fromBoolean(hasPermission(permission))
        }
    }


}