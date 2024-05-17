package world.neptuns.core.base.common.file

import world.neptuns.core.base.api.file.NeptunFile

data class RedisCredentials(val hostname: String, val port: Int, val password: String) : NeptunFile