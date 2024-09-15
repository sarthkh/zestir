package com.sarthkh.zestir.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    companion object {
        private val CURRENT_STEP = intPreferencesKey("current_onboarding_step")
        private val IS_ONBOARDING_COMPLETE = booleanPreferencesKey("is_onboarding_complete")
    }

    fun getCurrentStep(): Flow<Int> = dataStore.data.map { preferences ->
        preferences[CURRENT_STEP] ?: 1
    }

    fun isOnboardingComplete(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_ONBOARDING_COMPLETE] == true
    }

    suspend fun saveCurrentStep(step: Int) {
        dataStore.edit { preferences ->
            preferences[CURRENT_STEP] = step
        }
    }

    suspend fun completeOnboarding() {
        dataStore.edit { preferences ->
            preferences[IS_ONBOARDING_COMPLETE] = true
        }
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        firestore.collection("users").document(userId).update("onboardingCompleted", true)
    }

    suspend fun resetOnboarding() {
        dataStore.edit { preferences ->
            preferences[CURRENT_STEP] = 1
            preferences[IS_ONBOARDING_COMPLETE] = false
        }
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        firestore.collection("users").document(userId).update("onboardingCompleted", false)
    }

    suspend fun saveOnboardingData(data: Map<String, Any>) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        firestore.collection("users").document(userId).set(data, SetOptions.merge())
    }

    suspend fun getOnboardingData(): Map<String, Any>? {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        return firestore.collection("users").document(userId).get().await().data
    }
}