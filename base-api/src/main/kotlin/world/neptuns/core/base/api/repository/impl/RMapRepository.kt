package world.neptuns.core.base.api.repository.impl

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.redisson.api.RMapCache
import org.redisson.api.RedissonClient
import org.redisson.api.map.event.EntryCreatedListener
import org.redisson.api.map.event.EntryRemovedListener
import org.redisson.api.map.event.EntryUpdatedListener
import world.neptuns.core.base.api.repository.Repository

open class RMapRepository<K, V>(private val redissonClient: RedissonClient, private val cacheName: String) : Repository<K, V> {

    private val rMapCache: RMapCache<K, V> = this.redissonClient.getMapCache(this.cacheName)

    override suspend fun insert(key: K, value: V) {
        withContext(Dispatchers.IO) {
            rMapCache.put(key, value)
        }
    }

    override suspend fun update(key: K, value: V) {
        withContext(Dispatchers.IO) {
            rMapCache.replace(key, value)
        }
    }

    override suspend fun delete(key: K) {
        withContext(Dispatchers.IO) {
            rMapCache.remove(key)
        }
    }

    override suspend fun getAll(): Deferred<List<V>> = withContext(Dispatchers.IO) {
        async {
            rMapCache.values.toList()
        }
    }

    override suspend fun getRemainingTTL(key: K): Deferred<Long> = withContext(Dispatchers.IO) {
        async {
            rMapCache.remainTimeToLive(key)
        }
    }

    override suspend fun contains(key: K): Deferred<Boolean> = withContext(Dispatchers.IO) {
        async {
            rMapCache.contains(key)
        }
    }

    override suspend fun get(key: K): Deferred<V?> = withContext(Dispatchers.IO) {
        async {
            rMapCache[key]
        }
    }

    suspend fun onInsertion(result: suspend (K, V) -> Unit) = withContext(Dispatchers.IO) {
        rMapCache.addListener(EntryCreatedListener<K, V> {
            suspend {
                result(it.key, it.value)
            }
        })
    }

    suspend fun onRemoval(result: suspend (K, V) -> Unit) = withContext(Dispatchers.IO) {
        rMapCache.addListener(EntryRemovedListener<K, V> {
            suspend {
                result(it.key, it.value)
            }
        })
    }

    suspend fun onUpdate(result: suspend (K, V) -> Unit) = withContext(Dispatchers.IO) {
        rMapCache.addListener(EntryUpdatedListener<K, V> {
            suspend {
                result(it.key, it.value)
            }
        })
    }
}