package com.baba.bobo.ui.intro

import android.content.Context
import com.baba.bobo.core.base.BaseAdapter
import com.baba.bobo.core.extensions.loadImage
import com.baba.bobo.core.extensions.select
import com.baba.bobo.core.extensions.strings
import com.baba.bobo.data.model.IntroModel
import com.baba.bobo.databinding.ItemIntroBinding

class IntroAdapter(val context: Context) : BaseAdapter<IntroModel, ItemIntroBinding>(
    ItemIntroBinding::inflate
) {
    override fun onBind(binding: ItemIntroBinding, item: IntroModel, position: Int) {
        binding.apply {
            loadImage(root, item.image, imvImage, false)
            tvContent.text = context.strings(item.content)
            tvContent.select()
        }
    }
}