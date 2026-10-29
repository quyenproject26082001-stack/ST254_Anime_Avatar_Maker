package com.baba.bobo.ui.choose_character

import androidx.core.content.ContextCompat
import com.baba.bobo.R
import com.baba.bobo.core.base.BaseAdapter
import com.baba.bobo.core.extensions.gone
import com.baba.bobo.core.extensions.loadImage
import com.baba.bobo.core.extensions.tap
import com.baba.bobo.data.model.custom.CustomizeModel
import com.baba.bobo.databinding.ItemChooseAvatarBinding

class ChooseCharacterAdapter : BaseAdapter<CustomizeModel, ItemChooseAvatarBinding>(ItemChooseAvatarBinding::inflate) {
    var onItemClick: ((position: Int) -> Unit) = {}


    override fun onBind(binding: ItemChooseAvatarBinding, item: CustomizeModel, position: Int) {
        binding.apply {

            loadImage(item.avatar, imvImage, onDismissLoading = {
                sflShimmer.stopShimmer()
                sflShimmer.gone()
            })
            root.tap { onItemClick.invoke(position) }
        }
    }
}