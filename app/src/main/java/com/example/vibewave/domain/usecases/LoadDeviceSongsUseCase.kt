package com.example.vibewave.domain.usecases

import com.example.vibewave.domain.repositories.SongsRepository

class LoadDeviceSongsUseCase(
    private val repository: SongsRepository
) {
    suspend operator fun invoke(){
         repository.load()
    }
}