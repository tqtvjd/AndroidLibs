package com.imoji.android.lib.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * DataStore 封装工具类
 */
class EasyDataStore(context: Context, storeName: String) {

    // 创建DataStore，同一时间，不能创建两个同名的dataStore实例，否则会抛出异常
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = storeName
    )

    // DataStore变量
    private val dataStore: DataStore<Preferences> = context.dataStore

    /**
     * 存数据，支持基础数据类型
     */
    fun <T> put(key: String, value: T) {
        runBlocking {
            when (value) {
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Double -> putDouble(key, value)
                else -> throw IllegalArgumentException("This type cannot be saved to the Data Store")
            }
        }
    }

    /**
     * 取数据
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, defaultValue: T): T {
        val data = when (defaultValue) {
            is Int -> getInt(key, defaultValue)
            is Long -> getLong(key, defaultValue)
            is String -> getString(key, defaultValue)
            is Boolean -> getBoolean(key, defaultValue)
            is Float -> getFloat(key, defaultValue)
            is Double -> getDouble(key, defaultValue)
            else -> throw IllegalArgumentException("This type cannot be saved to the Data Store")
        }
        return data as T
    }

    /**
     * 清空数据
     */
    fun clear() = runBlocking { dataStore.edit { it.clear() } }

    /**
     * 存放Int数据
     */
    private suspend fun putInt(key: String, value: Int) = dataStore.edit {
        it[intPreferencesKey(key)] = value
    }

    /**
     * 存放Long数据
     */
    private suspend fun putLong(key: String, value: Long) = dataStore.edit {
        it[longPreferencesKey(key)] = value
    }

    /**
     * 存放String数据
     */
    private suspend fun putString(key: String, value: String) = dataStore.edit {
        it[stringPreferencesKey(key)] = value
    }

    /**
     * 存放Boolean数据
     */
    private suspend fun putBoolean(key: String, value: Boolean) = dataStore.edit {
        it[booleanPreferencesKey(key)] = value
    }

    /**
     * 存放Float数据
     */
    private suspend fun putFloat(key: String, value: Float) = dataStore.edit {
        it[floatPreferencesKey(key)] = value
    }

    /**
     * 存放Double数据
     */
    private suspend fun putDouble(key: String, value: Double) = dataStore.edit {
        it[doublePreferencesKey(key)] = value
    }

    /**
     * 取出Int数据
     */
    fun getInt(key: String, default: Int = 0): Int = runBlocking {
        return@runBlocking dataStore.data.map {
            it[intPreferencesKey(key)] ?: default
        }.first()
    }

    /**
     * 取出Long数据
     */
    fun getLong(key: String, default: Long = 0): Long = runBlocking {
        return@runBlocking dataStore.data.map {
            it[longPreferencesKey(key)] ?: default
        }.first()
    }

    /**
     * 取出String数据
     */
    fun getString(key: String, default: String? = null): String = runBlocking {
        return@runBlocking dataStore.data.map {
            it[stringPreferencesKey(key)] ?: default
        }.first()!!
    }

    /**
     * 取出Boolean数据
     */
    fun getBoolean(key: String, default: Boolean = false): Boolean = runBlocking {
        return@runBlocking dataStore.data.map {
            it[booleanPreferencesKey(key)] ?: default
        }.first()
    }

    /**
     * 取出Float数据
     */
    fun getFloat(key: String, default: Float = 0.0f): Float = runBlocking {
        return@runBlocking dataStore.data.map {
            it[floatPreferencesKey(key)] ?: default
        }.first()
    }

    /**
     * 取出Double数据
     */
    fun getDouble(key: String, default: Double = 0.00): Double = runBlocking {
        return@runBlocking dataStore.data.map {
            it[doublePreferencesKey(key)] ?: default
        }.first()
    }


}