package io.sws.watchstack.data.local

import io.sws.watchstack.domain.model.Anime
import io.sws.watchstack.domain.model.PagedAnime

/**
 * Simple in-memory TTL cache for list endpoints (offline-ish / rate-limit soft fallback).
 */
class MemoryAnimeCache(
    private val ttlMs: Long = 5 * 60 * 1000L
) {
    private data class Entry(val data: PagedAnime, val storedAt: Long)

    private val store = mutableMapOf<String, Entry>()

    fun get(key: String, now: Long = currentTime()): PagedAnime? {
        val entry = store[key] ?: return null
        if (now - entry.storedAt > ttlMs) {
            store.remove(key)
            return null
        }
        return entry.data
    }

    fun put(key: String, data: PagedAnime, now: Long = currentTime()) {
        store[key] = Entry(data, now)
    }

    fun getAnime(malId: Int, now: Long = currentTime()): Anime? {
        val entry = store["detail-$malId"] ?: return null
        if (now - entry.storedAt > ttlMs) {
            store.remove("detail-$malId")
            return null
        }
        return entry.data.items.firstOrNull()
    }

    fun putAnime(anime: Anime, now: Long = currentTime()) {
        store["detail-${anime.malId}"] = Entry(
            PagedAnime(items = listOf(anime), page = 1, hasNext = false),
            now
        )
    }

    private fun currentTime(): Long = io.sws.watchstack.core.currentTimeMillis()
}
