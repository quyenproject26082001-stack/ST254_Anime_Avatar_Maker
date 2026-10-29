package com.baba.bobo.ui

import android.view.LayoutInflater
import com.baba.bobo.R
import com.baba.bobo.core.base.BaseActivity
import com.baba.bobo.core.extensions.gone
import com.baba.bobo.core.extensions.handleBackLeftToRight
import com.baba.bobo.core.extensions.policy
import com.baba.bobo.core.extensions.select
import com.baba.bobo.core.extensions.setImageActionBar
import com.baba.bobo.core.extensions.setTextActionBar
import com.baba.bobo.core.extensions.shareApp
import com.baba.bobo.core.extensions.startIntentRightToLeft
import com.baba.bobo.core.extensions.visible
import com.baba.bobo.core.utils.key.IntentKey
import com.baba.bobo.core.utils.state.RateState
import com.baba.bobo.databinding.ActivitySettingsBinding
import com.baba.bobo.ui.language.LanguageActivity
import com.baba.bobo.core.extensions.tap
import com.baba.bobo.core.helper.MusicHelper
import com.baba.bobo.core.helper.RateHelper
import kotlin.jvm.java

class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {
    override fun setViewBinding(): ActivitySettingsBinding {
        return ActivitySettingsBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
       // binding.tvMusic.select()
        initRate()
        initMusic()
    }

    private fun initMusic() {
        updateMusicUI(sharePreference.isMusicEnabled())
    }

    private fun updateMusicUI(isEnabled: Boolean) {
      //  binding.btnMusic.setImageResource(
//            if (isEnabled) R.drawable.ic_sw_on else R.drawable.ic_sw_off_ms
//        )
    }

    private fun toggleMusic() {
        val isEnabled = !sharePreference.isMusicEnabled()
        sharePreference.setMusicEnabled(isEnabled)
        updateMusicUI(isEnabled)
        if (isEnabled) {
            MusicHelper.play()
        } else {
            MusicHelper.pause()
        }
    }

    override fun viewListener() {
        binding.apply {
            actionBar.btnActionBarLeft.tap { handleBackLeftToRight() }
        //    layoutMusic.tap { toggleMusic() }
            btnLang.tap { startIntentRightToLeft(LanguageActivity::class.java, IntentKey.INTENT_KEY) }
            btnShareApp.tap(1500) { shareApp() }
            btnRate.tap {
                RateHelper.showRateDialog(this@SettingsActivity, sharePreference){ state ->
                    if (state != RateState.CANCEL){
                        btnRate.gone()
                        showToast(R.string.have_rated)
                    }
                }
            }
            btnPolicy.tap(1500) { policy() }
        }
    }

    override fun initText() {
    }

    override fun initActionBar() {
        binding.actionBar.apply {
            setImageActionBar(btnActionBarLeft, R.drawable.ic_back)
            tvStart.gone()
        }
    }

    private fun initRate() {
        if (sharePreference.getIsRate(this)) {
            binding.btnRate.gone()
        } else {
            binding.btnRate.visible()
        }
    }
}
