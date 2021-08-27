package com.mco.mchat.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class PrefsDataStore(context: Context, fileName: String) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(fileName)
    val mDataStore: DataStore<Preferences> = context.dataStore
}

class PrefsDataStoreManager(context: Context) : PrefsDataStore(context = context, PREF_FILE_UI_MODE){

    // used to get the data from datastore
    val userID: Flow<String>
        get() = mDataStore.data.map { preferences ->
            val uiMode = preferences[UI_MODE_KEY] ?: ""
            uiMode
        }


    // used to save the ui preference to datastore
    suspend fun setUserID(id: String) {
        mDataStore.edit { preferences ->
            preferences[UI_MODE_KEY] = id
        }
    }


    companion object {
        private const val PREF_FILE_UI_MODE = "preference_data_storage"
        private val UI_MODE_KEY = stringPreferencesKey("user_id")
    }
}