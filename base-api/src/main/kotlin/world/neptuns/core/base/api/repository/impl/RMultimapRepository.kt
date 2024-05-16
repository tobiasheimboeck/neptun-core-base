package world.neptuns.core.base.api.repository.impl

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.redisson.api.RMultimapCache
import org.redisson.api.RedissonClient
import world.neptuns.core.base.api.repository.Repository

open class RMultimapRepository<K, V>(private val redissonClient: RedissonClient, private val cacheName: String) : Repository<K, V> {

    private val rMultimapCache: RMultimapCache<K, V> = this.redissonClient.getListMultimapCache(this.cacheName)

    override suspend fun insert(key: K, value: V) {
        withContext(Dispatchers.IO) {
            rMultimapCache.put(key, value)
        }
    }

    @Deprecated("The multimap repository has its own updating logic", ReplaceWith("updateValue(key, predicate, newValue)"))
    override suspend fun update(key: K, value: V) {
        throw NotImplementedError("This function is deprecated has no implementation.")
    }

    suspend fun updateValue(key: K, predicate: (V) -> Boolean, newValue: V) {
        withContext(Dispatchers.IO) {
            val values = rMultimapCache.get(key).toMutableList()
            val value = values.firstOrNull(predicate) ?: return@withContext

            values[values.indexOf(value)] = newValue
            rMultimapCache.replaceValues(key, values)
        }
    }

    override suspend fun delete(key: K) {
        withContext(Dispatchers.IO) {
            rMultimapCache.removeAll(key)
        }
    }

    suspend fun deleteValue(key: K, value: V) {
        withContext(Dispatchers.IO) {
            rMultimapCache.remove(key, value)
        }
    }

    override suspend fun getAll(): Deferred<List<V>> = withContext(Dispatchers.IO) {
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

    @Deprecated("The multimap repository has no ttl logic")
    override suspend fun getRemainingTTL(key: K): Deferred<Long> {
        throw NotImplementedError("This function is deprecated has no implementation.")
    }

    override suspend fun contains(key: K): Deferred<Boolean> = withContext(Dispatchers.IO) {
        async {
            rMultimapCache.containsKey(key)
        }
    }

    suspend fun containsEntry(key: K, value: V): Deferred<Boolean> = withContext(Dispatchers.IO) {
        async {
            rMultimapCache.containsEntry(key, value)
        }
    }

    @Deprecated("The multimap repository has its own get logic")
    override suspend fun get(key: K): Deferred<V?> {
        throw NotImplementedError("This function is deprecated has no implementation.")
    }

}