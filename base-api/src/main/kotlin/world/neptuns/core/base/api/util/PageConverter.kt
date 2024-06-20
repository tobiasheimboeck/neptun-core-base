package world.neptuns.core.base.api.util

interface PageConverter<T> {

    val data: List<T>

    suspend fun <P> showPage(player: P, elementsPerPage: Int, pageNumber: Int, result: (List<T>) -> Unit)

}