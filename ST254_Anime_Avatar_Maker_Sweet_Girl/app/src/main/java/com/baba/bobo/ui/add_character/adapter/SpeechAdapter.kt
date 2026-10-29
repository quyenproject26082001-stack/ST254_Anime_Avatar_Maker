package com.baba.bobo.ui.add_character.adapter

import com.baba.bobo.core.base.BaseAdapter
import com.baba.bobo.core.extensions.loadImageSticker
import com.baba.bobo.core.extensions.tap
import com.baba.bobo.data.model.SelectedModel
import com.baba.bobo.databinding.ItemSpeechBinding

class SpeechAdapter : BaseAdapter<SelectedModel, ItemSpeechBinding>(ItemSpeechBinding::inflate) {
    var onItemClick: ((String) -> Unit) = {}

    override fun onBind(binding: ItemSpeechBinding, item: SelectedModel, position: Int) {
        binding.apply {
            loadImageSticker(root, item.path, imvSpeech)
            root.tap { onItemClick.invoke(item.path) }
        }
    }
}
