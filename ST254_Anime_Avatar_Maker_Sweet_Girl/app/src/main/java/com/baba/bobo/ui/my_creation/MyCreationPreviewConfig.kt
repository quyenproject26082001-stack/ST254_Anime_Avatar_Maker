package com.baba.bobo.ui.my_creation

import com.baba.bobo.R
import com.baba.bobo.data.model.MyAlbumModel

object MyCreationPreviewConfig {
    const val SHOW_FAKE_ITEMS = false
    const val FAKE_ITEM_COUNT = 20

    /**
     * Keeps the base action bar flow (select all/delete) when false.
     * Set to true to expose download/share/delete in selection mode:
     * btnActionBarRight, btnActionBarNextRight, btnActionBarNextRight1.
     */
    const val USE_SELECTION_ACTIONBAR_ACTIONS = true

    /** Keeps the bottom layout in its normal position and disables selection clipping. */
    const val PRESERVE_SELECTION_LAYOUT = true

    fun createFakeItems(type: String): ArrayList<MyAlbumModel> {
        return List(FAKE_ITEM_COUNT.coerceAtLeast(0)) { index ->
            MyAlbumModel(
                path = "fake://$type/$index",
                previewResId = R.drawable.img_splash_avatar,
                isFake = true
            )
        }.toCollection(ArrayList())
    }
}
