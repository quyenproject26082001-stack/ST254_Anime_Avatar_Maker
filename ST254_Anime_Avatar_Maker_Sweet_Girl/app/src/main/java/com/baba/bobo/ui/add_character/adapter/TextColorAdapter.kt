package com.baba.bobo.ui.add_character.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.util.Log
import androidx.core.view.isVisible
import com.baba.bobo.R
import com.baba.bobo.core.base.BaseAdapter
import com.baba.bobo.core.extensions.gone
import com.baba.bobo.core.extensions.tap
import com.baba.bobo.core.extensions.visible
import com.baba.bobo.data.model.SelectedModel
import com.baba.bobo.databinding.ItemTextColorBinding

class TextColorAdapter : BaseAdapter<SelectedModel, ItemTextColorBinding>(ItemTextColorBinding::inflate) {
    var onChooseColorClick: (() -> Unit) = {}
    var onTextColorClick: ((Int, Int) -> Unit) = { _, _ -> }

    private var currentSelected = 1


    override fun onBind(binding: ItemTextColorBinding, item: SelectedModel, position: Int) {

        binding.apply {
            vFocus.isVisible = item.isSelected || position != 0
            if (item.isSelected) {
                vFocus.setBackgroundResource(R.drawable.bg_stroke_gradient_circle_color_text)
            }
            else if (position != 0) {
                vFocus.setBackgroundResource(R.drawable.bg_uslt_text_color)
            } else {
                vFocus.background = null
            }

            if (position == 0) {
                imvColor.visible()

                // Set margin to 0dp for position 0 to make it bigger
                val layoutParams = imvColor.layoutParams as android.widget.FrameLayout.LayoutParams
                layoutParams.setMargins(0, 0, 0, 0)
                imvColor.layoutParams = layoutParams

                // First clear any existing background drawable
                imvColor.background = null
                // Set background to transparent
                imvColor.setBackgroundColor(android.graphics.Color.TRANSPARENT)

                // Also clear the parent FrameLayout's background
                val parentFrame = imvColor.parent as? android.widget.FrameLayout
                parentFrame?.setBackgroundColor(android.graphics.Color.TRANSPARENT)

                // Now set the image resource
                imvColor.setImageResource(R.drawable.img0text_color)
                btnAddColor.visible()
                root.tap { onChooseColorClick.invoke() }
            } else {
                imvColor.visible()

                // Set margin to 2dp for other positions (keep normal size)
                val layoutParams = imvColor.layoutParams as android.widget.FrameLayout.LayoutParams
                val margin = imvColor.resources.getDimensionPixelSize(R.dimen.dp_1)
                layoutParams.setMargins(margin, margin, margin, margin)
                imvColor.layoutParams = layoutParams

                imvColor.setImageResource(0) // Clear image resource
                btnAddColor.gone()

                // Ensure parent FrameLayout background is transparent
                val parentFrame = imvColor.parent as? android.widget.FrameLayout
                parentFrame?.setBackgroundColor(android.graphics.Color.TRANSPARENT)

                // Create circular drawable for color
                val drawable = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(item.color)
                }
                imvColor.background = drawable

                root.tap { onTextColorClick.invoke(item.color, position) }
            }
        }
    }

    fun submitItem(position: Int, list: ArrayList<SelectedModel>) {


        items.clear()
        items.addAll(list)

        if (position != currentSelected) {
            notifyItemChanged(currentSelected)
            notifyItemChanged(position)
            currentSelected = position
        } else {
            notifyItemChanged(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitListReset(list: ArrayList<SelectedModel>){
        items.clear()
        items.addAll(list)
        currentSelected = 1
        notifyDataSetChanged()
    }
}
