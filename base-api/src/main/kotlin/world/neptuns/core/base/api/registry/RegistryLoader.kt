package world.neptuns.core.base.api.registry

interface RegistryLoader<T> {

    fun register(type: T) {
        register(type, type!!::class.java)
    }

    fun register(type: T, clazz: Class<out T>)

    fun <R : T> get(clazz: Class<R>): R?

}