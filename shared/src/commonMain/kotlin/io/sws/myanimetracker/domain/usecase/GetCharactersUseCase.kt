package io.sws.myanimetracker.domain.usecase

import io.sws.myanimetracker.domain.model.Character
import io.sws.myanimetracker.domain.repository.AnimeRepository

class GetCharactersUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(malId: Int): Result<List<Character>> =
        repository.getCharacters(malId)
}
