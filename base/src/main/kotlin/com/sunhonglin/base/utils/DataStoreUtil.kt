package com.sunhonglin.base.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "MyFrameDataStore")

object DataStoreUtil {

    private lateinit var dataStore: DataStore<Preferences>

    fun init(context: Context) {
        dataStore = context.dataStore
    }

    suspend fun <U> putData(key: String, value: U) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[getKey(key, value)] = value
        }
    }

    fun <U> putSyncData(key: String, value: U) {
        runBlocking { putData(key, value) }
    }

    fun <U> getSyncData(key: String, default: U): U {
        var value = default
        runBlocking {
            dataStore.data.first {
                value = it[getKey(key, default)] ?: default
                true
            }
        }
        return value
    }

    fun <U> getData(key: String, default: U): Flow<U> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[getKey(key, default)] ?: default
            }

    fun readBooleanFlow(key: String, default: Boolean = false): Flow<Boolean> =
        getData(key, default)

    fun readBooleanData(key: String, default: Boolean = false): Boolean = getSyncData(key, default)

    fun readIntFlow(key: String, default: Int = 0): Flow<Int> = getData(key, default)

    fun readIntData(key: String, default: Int = 0): Int = getSyncData(key, default)

    fun readStringFlow(key: String, default: String = ""): Flow<String> = getData(key, default)

    fun readStringData(key: String, default: String = ""): String = getSyncData(key, default)

    fun readFloatFlow(key: String, default: Float = 0f): Flow<Float> = getData(key, default)

    fun readFloatData(key: String, default: Float = 0f): Float = getSyncData(key, default)

    fun readLongFlow(key: String, default: Long = 0L): Flow<Long> = getData(key, default)

    fun readLongData(key: String, default: Long = 0L): Long = getSyncData(key, default)

    fun removeBooleanData(key: String) = remove(key, false)
    fun removeIntData(key: String) = remove(key, 0)
    fun removeStringData(key: String) = remove(key, "")
    fun removeFloatData(key: String) = remove(key, 0f)
    fun removeLongData(key: String) = remove(key, 0L)

    @Suppress("UNCHECKED_CAST")
    private fun <U> getKey(key: String, value: U): Preferences.Key<U> {
        val data = when (value) {
            is Long -> longPreferencesKey(key)
            is Int -> intPreferencesKey(key)
            is Boolean -> booleanPreferencesKey(key)
            is Float -> floatPreferencesKey(key)
            is Double -> doublePreferencesKey(key)
            else -> stringPreferencesKey(key)
        }
        return data as Preferences.Key<U>
    }

    private fun <U> remove(key: String, value: U) {
        dataStore.data.map {
            it.toMutablePreferences().remove(getKey(key, value))
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    fun clearSync() {
        runBlocking {
            dataStore.edit {
                it.clear()
            }
        }
    }
}