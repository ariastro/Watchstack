package io.sws.watchstack.domain.usecase

import io.sws.watchstack.domain.model.Character
import io.sws.watchstack.domain.repository.AnimeRepository

class GetCharactersUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(malId: Int): Result<List<Character>> =
        repository.getCharacters(malId)
}
