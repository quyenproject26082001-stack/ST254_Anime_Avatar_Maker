package com.baba.bobo.ui.add_character.adapter

import com.baba.bobo.core.base.BaseAdapter
import com.baba.bobo.core.extensions.loadImage
import com.baba.bobo.core.extensions.loadImageSticker
import com.baba.bobo.core.extensions.tap
import com.baba.bobo.data.model.SelectedModel
import com.baba.bobo.databinding.ItemStickerBinding

class StickerAdapter : BaseAdapter<SelectedModel, ItemStickerBinding>(ItemStickerBinding::inflate) {
    var onItemClick : ((String) -> Unit) = {}
    override fun onBind(binding: ItemStickerBinding, item: SelectedModel, position: Int) {
        binding.apply {
            loadImageSticker(root, item.path, imvSticker)
            root.tap { onItemClick.invoke(item.path) }
        }
    }
}