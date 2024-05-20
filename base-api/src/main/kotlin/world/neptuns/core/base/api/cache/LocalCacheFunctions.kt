package world.neptuns.core.base.api.cache

interface LocalCacheFunctions<K, V> {

    suspend fun load(key: K, value: V)
    suspend fun unload(key: K)

}