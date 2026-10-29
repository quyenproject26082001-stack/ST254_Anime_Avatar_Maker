package com.baba.bobo.ui.customize

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.baba.bobo.R
import com.baba.bobo.core.base.BaseAdapter
import com.baba.bobo.core.extensions.tap
import com.baba.bobo.data.model.custom.ItemColorModel
import com.baba.bobo.databinding.ItemColorBinding

class ColorLayerCustomizeAdapter(val context: Context) :
    BaseAdapter<ItemColorModel, ItemColorBinding>(ItemColorBinding::inflate) {
    var onItemClick: ((Int) -> Unit) = {}
    override fun onBind(binding: ItemColorBinding, item: ItemColorModel, position: Int) {
        binding.apply {
            val colorSize = root.resources.getDimensionPixelSize(
                if (item.isSelected) R.dimen.dp_28 else R.dimen.dp_24
            )
            imvImage.layoutParams = imvImage.layoutParams.apply {
                width = colorSize
                height = colorSize
            }
            imvImage.background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(item.color.toColorInt())
            }
            if(item.isSelected){
                binding.imvFocus.setImageResource(R.drawable.bg_focus_color)
            }else
            {
                binding.imvFocus.setImageResource(R.drawable.bg_unfocus_color)

            }

            root.tap {
                val rv = root.parent as? RecyclerView ?: return@tap
                val currentPosition = rv.getChildAdapterPosition(root)
                if (currentPosition != RecyclerView.NO_POSITION) {
                    onItemClick.invoke(currentPosition)
                }
            }
        }
    }
}
