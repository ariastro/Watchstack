package io.sws.watchstack.domain

import io.sws.watchstack.domain.model.Anime
import kotlin.test.Test
import kotlin.test.assertEquals

class PagedAnimeDedupeTest {

    @Test
    fun distinctByMalId_keepsFirst() {
        val items = listOf(
            Anime(malId = 1, title = "A"),
            Anime(malId = 1, title = "A-dup"),
            Anime(malId = 2, title = "B")
        )
        val deduped = items.distinctBy { it.malId }
        assertEquals(2, deduped.size)
        assertEquals("A", deduped.first().title)
    }
}
