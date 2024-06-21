package world.neptuns.core.base.api.util

import net.kyori.adventure.audience.Audience

interface PageConverter<T> {

    val data: List<T>

    suspend fun showPage(audience: Audience, elementsPerPage: Int, pageNumber: Int, result: (List<T>) -> Unit)

}