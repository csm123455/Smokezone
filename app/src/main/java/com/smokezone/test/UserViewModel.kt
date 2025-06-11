package com.smokezone.test

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

class UserViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {



}

class UserRepository {
    suspend fun getUser(): Int {
        return coroutineScope {
            Random.nextInt()
        }
    }
}
