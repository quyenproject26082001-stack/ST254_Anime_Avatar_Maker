package com.baba.bobo.ui.add_character.adapter

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.baba.bobo.core.base.BaseAdapter
import com.baba.bobo.R
import com.baba.bobo.core.utils.DataLocal
import com.baba.bobo.core.extensions.gone
import com.baba.bobo.core.extensions.tap
import com.baba.bobo.core.extensions.visible
import com.baba.bobo.data.model.SelectedModel
import com.baba.bobo.databinding.ItemBackgroundImageBinding
import com.facebook.shimmer.ShimmerDrawable

class BackgroundImageAdapter :
    BaseAdapter<SelectedModel, ItemBackgroundImageBinding>(ItemBackgroundImageBinding::inflate) {
    var onAddImageClick: (() -> Unit) = {}
    var onBackgroundImageClick: ((String, Int) -> Unit) = { _, _ -> }
    var currentSelected = -1

    override fun onBind(binding: ItemBackgroundImageBinding, item: SelectedModel, position: Int) {
        binding.apply {
            tvAddImg.isSelected=true
            if (item.isSelected) {
                containerCard.setStrokeColor(Color.parseColor("#FF667F"))
            } else {
                containerCard.setStrokeColor(Color.TRANSPARENT)
            }

            lnlAddItem.gone()
            btnNone.gone()
            imvImage.gone()

            when (position) {
                ADD_IMAGE_POSITION -> {
                    lnlAddItem.visible()
                    lnlAddItem.tap(800) { onAddImageClick.invoke() }
                }

                NONE_ITEM_POSITION -> {
                    btnNone.visible()
                    btnNone.tap { onBackgroundImageClick.invoke(item.path, position) }
                }

                else -> {
                    imvImage.visible()
                    val cornerRadiusPx =
                        root.resources.getDimensionPixelSize(R.dimen.dp_4)
                    val shimmerDrawable = ShimmerDrawable().apply {
                        setShimmer(DataLocal.shimmer)
                    }
                    Glide.with(root)
                        .load(item.path)
                        .placeholder(shimmerDrawable)
                        .error(shimmerDrawable)
                        .override(256, 256)
                        .encodeQuality(60)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .transform(RoundedCorners(cornerRadiusPx))
                        .into(imvImage)
                    imvImage.tap { onBackgroundImageClick.invoke(item.path, position) }
                }
            }
        }
    }

    override fun submitList(list: List<SelectedModel>) {
        currentSelected = list.indexOfFirst { it.isSelected }
        super.submitList(list)
    }

    fun submitItem(position: Int, list: ArrayList<SelectedModel>) {
        if (position != currentSelected) {
            items.clear()
            items.addAll(list)

            if (currentSelected >= 0) notifyItemChanged(currentSelected)
            notifyItemChanged(position)

            currentSelected = position
        }
    }

    companion object {
        const val ADD_IMAGE_POSITION = 1
        const val NONE_ITEM_POSITION = 0
    }
}
