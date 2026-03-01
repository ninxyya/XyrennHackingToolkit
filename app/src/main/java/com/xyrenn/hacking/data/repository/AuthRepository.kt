package com.xyrenn.hacking.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.xyrenn.hacking.data.local.dao.UserDao
import com.xyrenn.hacking.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val userDao: UserDao
) {

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    val isLoggedIn: Boolean
        get() = auth.currentUser != null

    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                saveUserToLocal(user)
                Result.success(user)
            } ?: Result.failure(Exception("Login failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                createUserProfile(user)
                saveUserToLocal(user)
                Result.success(user)
            } ?: Result.failure(Exception("Registration failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        auth.signOut()
        userDao.deleteAll()
    }

    private suspend fun saveUserToLocal(user: FirebaseUser) {
        val userEntity = UserEntity(
            uid = user.uid,
            email = user.email ?: "",
            displayName = user.displayName ?: "",
            photoUrl = user.photoUrl?.toString() ?: ""
        )
        userDao.insert(userEntity)
    }

    private suspend fun createUserProfile(user: FirebaseUser) {
        // Create user profile in Firestore or Realtime Database
    }

    fun getLocalUser(): Flow<UserEntity?> {
        return userDao.getCurrentUser()
    }
}