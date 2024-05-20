package world.neptuns.core.base.api.cache.impl

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import world.neptuns.core.base.api.cache.Cache

open class MultimapCache<K, V> : Cache {

    private val multimapCache: Multimap<K, V> = ArrayListMultimap.create()

    fun insert(key: K, value: V) {
        this.multimapCache.put(key, value)
    }

    fun update(key: K, predicate: (V) -> Boolean, newValue: V) {
        val values = this.multimapCache.get(key).toMutableList()
        val value = values.firstOrNull(predicate) ?: return

        values[values.indexOf(value)] = newValue
        this.multimapCache.replaceValues(key, values)
    }

    fun delete(key: K, value: V) {
        this.multimapCache.remove(key, value)
    }

    fun deleteAll(key: K) {
        this.multimapCache.removeAll(key)
    }

    fun getAll(): List<V> {
        return this.multimapCache.values().toList()
    }

    fun getValues(key: K): List<V> {
        return this.multimapCache[key].toList()
    }

    fun getValue(key: K, predicate: (V) -> Boolean): V? {
        val values = this.multimapCache[key]
        return values.firstOrNull(predicate)
    }

    fun contains(key: K): Boolean {
        return this.multimapCache.containsKey(key)
    }

    fun containsEntry(key: K, value: V): Boolean {
        return this.multimapCache.containsEntry(key, value)
    }

}