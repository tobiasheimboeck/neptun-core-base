package world.neptuns.core.base.api.repository.impl

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.redisson.api.RMultimapCache
import org.redisson.api.RedissonClient
import world.neptuns.core.base.api.repository.Repository

open class RMultimapRepository<K, V>(private val redissonClient: RedissonClient, private val cacheName: String) : Repository {

    private val rMultimapCache: RMultimapCache<K, V> = this.redissonClient.getListMultimapCache(this.cacheName)

    suspend fun insert(key: K, value: V) {
        withContext(Dispatchers.IO) {
            rMultimapCache.put(key, value)
        }
    }

    suspend fun updateValue(key: K, predicate: (V) -> Boolean, newValue: V) {
        withContext(Dispatchers.IO) {
            val values = rMultimapCache.get(key).toMutableList()
            val value = values.firstOrNull(predicate) ?: return@withContext

            values[values.indexOf(value)] = newValue
            rMultimapCache.replaceValues(key, values)
        }
    }

    suspend fun delete(key: K) {
        withContext(Dispatchers.IO) {
            rMultimapCache.removeAll(key)
        }
    }

    suspend fun deleteValue(key: K, value: V) {
        withContext(Dispatchers.IO) {
            rMultimapCache.remove(key, value)
        }
    }

    suspend fun getAll(): Deferred<List<V>> = withContext(Dispatchers.IO) {
        async {
            rMultimapCache.values().toList()
        }
    }

    suspend fun getValues(key: K): Deferred<List<V>> = withContext(Dispatchers.IO) {
        async {
            rMultimapCache[key].toList()
        }
    }

    suspend fun getValue(key: K, predicate: (V) -> Boolean): Deferred<V?> = withContext(Dispatchers.IO) {
        async {
            val values = rMultimapCache.get(key).toMutableList()
            values.firstOrNull(predicate)
        }
    }

    suspend fun contains(key: K): Deferred<Boolean> = withContext(Dispatchers.IO) {
        async {
            rMultimapCache.containsKey(key)
        }
    }

    suspend fun containsEntry(key: K, value: V): Deferred<Boolean> = withContext(Dispatchers.IO) {
        async {
            rMultimapCache.containsEntry(key, value)
        }
    }

}