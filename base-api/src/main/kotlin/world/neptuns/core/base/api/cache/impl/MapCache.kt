package world.neptuns.core.base.api.cache.impl

import world.neptuns.core.base.api.cache.Cache

open class MapCache<K, V> : Cache {

    private val mapCache = mutableMapOf<K, V>()

    fun insert(key: K, value: V) {
        this.mapCache[key] = value
    }

    fun update(key: K, value: V) {
        this.mapCache.replace(key, value)
    }

    fun delete(key: K) {
        this.mapCache.remove(key)
    }

    fun getAll(): List<V> {
        return this.mapCache.values.toList()
    }

    fun getAll(predicate: (V) -> Boolean): List<V> {
        return this.mapCache.values.filter { predicate(it) }
    }

    fun get(key: K): V? {
        return this.mapCache[key]
    }

    fun contains(key: K): Boolean {
        return this.mapCache.containsKey(key)
    }

}