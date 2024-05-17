package world.neptuns.core.base.api.utils

interface ClassRegistry<T> {
    val elements: MutableSet<T>
}