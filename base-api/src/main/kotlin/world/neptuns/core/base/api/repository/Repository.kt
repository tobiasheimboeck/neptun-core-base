package world.neptuns.core.base.api.repository

import kotlinx.coroutines.Deferred

interface Repository<K, V> {

    suspend fun insert(key: K, value: V)

    suspend fun update(key: K, value: V)

    suspend fun delete(key: K)

    suspend fun getAll(): Deferred<List<V>>

    suspend fun get(key: K): Deferred<V?>

    suspend fun contains(key: K): Deferred<Boolean>

    suspend fun getRemainingTTL(key: K): Deferred<Long>

}