package world.neptuns.core.base.api.registry

interface RegistryLoader<T> {

    fun register(type: T) {
        register(type, type!!::class.java)
    }

    fun register(type: T, clazz: Class<out T>)

    fun get(clazz: Class<out T>): T?

    fun getAll(): List<T>

}