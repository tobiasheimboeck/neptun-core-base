package world.neptuns.core.base.api.utils

interface PageConverter<T> {

    val data: List<T>

    fun <P> showPage(player: P, elementsPerPage: Int, pageNumber: Int, result: (List<T>) -> Unit)

}