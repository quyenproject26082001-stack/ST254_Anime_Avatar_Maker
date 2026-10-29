package com.baba.bobo.ui.splash

import android.annotation.SuppressLint
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.lvt.ads.callback.InterCallback
import com.lvt.ads.util.Admob
import com.baba.bobo.R
import com.baba.bobo.core.base.BaseActivity
import com.baba.bobo.core.extensions.checkShowSplashWhenFail
import com.baba.bobo.core.extensions.loadNativeCollabAds
import com.baba.bobo.core.extensions.loadSplashInterAds
import com.baba.bobo.core.utils.state.HandleState
import com.baba.bobo.databinding.ActivitySplashBinding
import com.baba.bobo.ui.intro.IntroActivity
import com.baba.bobo.ui.language.LanguageActivity
import com.baba.bobo.ui.home.DataViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    var intentActivity: Intent? = null
    private val dataViewModel: DataViewModel by viewModels()
    var interCallBack: InterCallback? = null

    private val MIN_SPLASH_MS = 2000L
    private val DATA_FALLBACK_MS = 8000L
    private val loadingDrawable = SplashLoadingDrawable()
    private var loadingAnimator: ValueAnimator? = null
    private var minTimePassed = false
    private var dataReady = false
    private var triggered = false
    private var navigated = false

    override fun setViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        if (!isTaskRoot &&
            intent.hasCategory(Intent.CATEGORY_LAUNCHER) &&
            intent.action != null &&
            intent.action.equals(Intent.ACTION_MAIN)) {
            finish(); return
        }

        binding.ivLoading.setImageDrawable(loadingDrawable)
        loadingAnimator = ValueAnimator.ofInt(0, 80).apply {
            duration = MIN_SPLASH_MS
            addUpdateListener { loadingDrawable.level = (it.animatedValue as Int) * 100 }
            start()
        }

        intentActivity = if (sharePreference.getIsFirstLang()) {
            Intent(this, LanguageActivity::class.java)
        } else {
            Intent(this, IntroActivity::class.java)
        }
//        Admob.getInstance().setTimeLimitShowAds(30000)
//        Admob.getInstance().setTimeCountdownNativeCollab(20000)
//        Admob.getInstance().setOpenShowAllAds(false)
        interCallBack = object : InterCallback() {
            override fun onNextAction() {
                super.onNextAction()
                tryProceed()
            }
        }
        dataViewModel.saveAndReadData(this)

        lifecycleScope.launch {
            kotlinx.coroutines.delay(MIN_SPLASH_MS)
            minTimePassed = true
            tryProceed()
        }

        lifecycleScope.launch {
            delay(DATA_FALLBACK_MS)
            dataReady = true
            tryProceed()
        }
    }

    override fun dataObservable() {
        lifecycleScope.launch {
            dataViewModel.dataLoadFinished.collect { finished ->
                if (finished) {
                    // Data is ready, no need to call API again
                    // (API already called in saveAndReadData if needed)
                    dataReady = true
                    tryProceed()
                }
            }
        }
    }

    private fun tryProceed() {
        if (triggered) return
        if (!minTimePassed || !dataReady) return

        triggered = true

//        loadSplashInterAds(getString(R.string.inter_splash), 30000, 2000, interCallBack)
        loadingAnimator?.cancel()
        loadingDrawable.level = 8000
        loadingAnimator = ValueAnimator.ofInt(80, 100).apply {
            duration = 400L
            addUpdateListener { loadingDrawable.level = (it.animatedValue as Int) * 100 }
            addListener(object : AnimatorListenerAdapter() {
                private var cancelled = false

                override fun onAnimationCancel(animation: Animator) {
                    cancelled = true
                }

                override fun onAnimationEnd(animation: Animator) {
                    if (!cancelled) navigateNext()
                }
            })
            start()
        }
    }

    private fun navigateNext() {
        if (navigated || isFinishing || isDestroyed) return
        navigated = true
        startActivity(intentActivity)
        finishAffinity()
    }

    override fun onDestroy() {
        loadingAnimator?.cancel()
        super.onDestroy()
    }

    override fun viewListener() {
    }

    override fun initText() {}

    override fun initActionBar() {}

    @SuppressLint("GestureBackNavigation", "MissingSuperCall")
    override fun onBackPressed() {}

//    override fun initAds() {
//        initNativeCollab()
//    }

//    fun initNativeCollab() {
//
//        loadNativeCollabAds(R.string.native_splash, binding.flNativeCollab)
//
//
//    }

    override fun onResume() {
        super.onResume()
//        checkShowSplashWhenFail(interCallBack, 1000)
    }

    override fun shouldPlayBackgroundMusic(): Boolean = false
}
