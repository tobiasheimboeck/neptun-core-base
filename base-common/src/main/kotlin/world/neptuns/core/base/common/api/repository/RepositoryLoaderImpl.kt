package world.neptuns.core.base.common.api.repository

import world.neptuns.core.base.api.repository.Repository
import world.neptuns.core.base.api.repository.RepositoryLoader

class RepositoryLoaderImpl : RepositoryLoader {

    private val repositories = mutableMapOf<Class<out Repository>, Repository>()

    override fun register(type: Repository, clazz: Class<out Repository>) {
        this.repositories[clazz] = type
    }

    @Suppress("UNCHECKED_CAST")
    override fun <R : Repository> get(clazz: Class<R>): R? {
        return this.repositories[clazz] as R?
    }

}