package world.neptuns.core.base.common.api.cache

import world.neptuns.core.base.api.cache.Cache
import world.neptuns.core.base.api.cache.CacheLoader

class CacheLoaderImpl : CacheLoader {

    private val caches = mutableMapOf<Class<out Cache>, Cache>()

    override fun register(type: Cache, clazz: Class<out Cache>) {
        this.caches[clazz] = type
    }

    @Suppress("UNCHECKED_CAST")
    override fun <R : Cache> get(clazz: Class<R>): R? {
        return this.caches[clazz] as R?
    }

}