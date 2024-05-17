package world.neptuns.core.base.common.api.utils

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.player.PlayerAdapter
import world.neptuns.core.base.api.utils.PageConverter

class PageConverterImpl<T>(override val data: List<T>) : PageConverter<T> {

    @Suppress("UNCHECKED_CAST")
    override fun <P> showPage(player: P, elementsPerPage: Int, pageNumber: Int, result: (List<T>) -> Unit) {
        val pages = splitTextToPages(elementsPerPage)
        val playerAdapter = NeptunCoreProvider.api.playerAdapter(player!!::class.java) as PlayerAdapter<P>

        if (pageNumber < 0) {
            playerAdapter.sendMessage(player, "core.page.numberToLow")
            return
        }

        val securePageNumber = if (pages.size == 1) 0 else (if (pageNumber == 0) 0 else pageNumber - 1)

        if (data.isEmpty() || securePageNumber > pages.size) {
            playerAdapter.sendMessage(player, "elytra.page.notExist")
            return
        }

        val pageNumberTag = Placeholder.parsed(
            "pagenumber",
            (if (pages.size == 1) 1 else if (pageNumber == 0) 1 else pageNumber).toString()
        )

        val pageAmountTag = Placeholder.parsed("pages", pages.size.toString())

        playerAdapter.sendMessage(player, "elytra.page.title", pageNumberTag, pageAmountTag)

        result.invoke(pages[securePageNumber])
    }

    private fun splitTextToPages(elementsPerPage: Int): List<List<T>> {
        return this.data.chunked(elementsPerPage)
    }

}