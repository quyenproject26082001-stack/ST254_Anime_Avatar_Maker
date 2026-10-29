package com.baba.bobo.ui.add_character.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import com.bumptech.glide.Glide
import com.baba.bobo.R
import com.baba.bobo.core.base.BaseAdapter
import com.baba.bobo.core.extensions.gone
import com.baba.bobo.core.extensions.tap
import com.baba.bobo.core.extensions.visible
import com.baba.bobo.data.model.SelectedModel
import com.baba.bobo.databinding.ItemBackgroundColorBinding

class BackgroundColorAdapter :
    BaseAdapter<SelectedModel, ItemBackgroundColorBinding>(ItemBackgroundColorBinding::inflate) {
    var onChooseColorClick: (() -> Unit) = {}
    var onBackgroundColorClick: ((Int, Int) -> Unit) = {_,_ ->}

    var currentSelected = -1
    override fun onBind(binding: ItemBackgroundColorBinding, item: SelectedModel, position: Int) {

        binding.apply {
            if (item.isSelected) {
                cardCtn.setStrokeColor(Color.parseColor("#FF667F"))
            } else {
                cardCtn.setStrokeColor(Color.TRANSPARENT)
            }
            btnNone.strokeWidth = cardCtn.strokeWidth
            btnNone.setStrokeColor(cardCtn.strokeColor)
            btnNone.gone()
            cardCtn.visible()
            Glide.with(root.context).clear(imvColor)
            imvColor.setImageDrawable(null)
            imvColor.background = null
            imvColor.setPadding(0, 0, 0, 0)
            imvColor.imageTintList = null
            when (position) {
                NONE_ITEM_POSITION -> {
                    cardCtn.gone()
                    btnNone.visible()
                    btnNone.tap { onBackgroundColorClick.invoke(Color.TRANSPARENT, position) }
                    root.tap { onBackgroundColorClick.invoke(Color.TRANSPARENT, position) }
                }
                ADD_COLOR_POSITION -> {
                    Glide.with(root.context).load(R.drawable.i_add_color_bg).into(imvColor)
                    root.tap { onChooseColorClick.invoke() }
                }
                else -> {
                    imvColor.background = GradientDrawable().apply { setColor(item.color) }
                    root.tap { onBackgroundColorClick.invoke(item.color, position) }
                }
            }
        }
    }

    override fun submitList(list: List<SelectedModel>) {
        currentSelected = list.indexOfFirst { it.isSelected }
        super.submitList(list)
    }

    companion object {
        const val NONE_ITEM_POSITION = 0
        const val ADD_COLOR_POSITION = 1
    }

    fun submitItem(position: Int, list: ArrayList<SelectedModel>){

        items.clear()
        items.addAll(list)

        if (position != currentSelected){
            if (currentSelected >= 0) notifyItemChanged(currentSelected)
            notifyItemChanged(position)
            currentSelected = position
        } else {
            notifyItemChanged(position)
        }
    }
}
