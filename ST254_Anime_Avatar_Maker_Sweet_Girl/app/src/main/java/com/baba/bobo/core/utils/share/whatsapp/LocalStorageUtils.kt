package com.baba.bobo.core.utils.share.whatsapp

import android.content.Context
import com.baba.bobo.data.local.PersistenceRepository

object LocalStorageUtils {

    fun readData(context: Context, key: String): Any? {
        return PersistenceRepository.getStringSetting(context, key, "")
            .takeIf { it.isNotEmpty() }
    }
}
