package com.baba.bobo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.baba.bobo.data.local.dao.AppSettingDao
import com.baba.bobo.data.local.dao.CharacterDao
import com.baba.bobo.data.local.dao.JsonCacheDao
import com.baba.bobo.data.local.dao.MediaItemDao
import com.baba.bobo.data.local.dao.SavedAvatarDao
import com.baba.bobo.data.local.entity.AppSettingEntity
import com.baba.bobo.data.local.entity.CharacterColorEntity
import com.baba.bobo.data.local.entity.CharacterEntity
import com.baba.bobo.data.local.entity.CharacterLayerEntity
import com.baba.bobo.data.local.entity.CharacterLayerGroupEntity
import com.baba.bobo.data.local.entity.JsonCacheEntity
import com.baba.bobo.data.local.entity.MediaItemEntity
import com.baba.bobo.data.local.entity.SavedAvatarEntity

@Database(
    entities = [
        AppSettingEntity::class,
        JsonCacheEntity::class,
        SavedAvatarEntity::class,
        MediaItemEntity::class,
        CharacterEntity::class,
        CharacterLayerGroupEntity::class,
        CharacterLayerEntity::class,
        CharacterColorEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appSettingDao(): AppSettingDao
    abstract fun jsonCacheDao(): JsonCacheDao
    abstract fun savedAvatarDao(): SavedAvatarDao
    abstract fun mediaItemDao(): MediaItemDao
    abstract fun characterDao(): CharacterDao
}
