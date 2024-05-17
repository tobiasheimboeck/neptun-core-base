package world.neptuns.core.base.common.file

import world.neptuns.core.base.api.file.NeptunFile

data class MariaDbCredentials(val hostname: String, val port: Int = 3306, val database: String, val user: String, val password: String) : NeptunFile