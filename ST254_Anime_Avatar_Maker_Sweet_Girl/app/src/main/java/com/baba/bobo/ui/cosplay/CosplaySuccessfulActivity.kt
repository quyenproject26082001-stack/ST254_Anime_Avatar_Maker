package com.baba.bobo.ui.cosplay

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.baba.bobo.R
import com.baba.bobo.core.base.BaseActivity
import com.baba.bobo.core.extensions.checkPermissions
import com.baba.bobo.core.extensions.goToSettings
import com.baba.bobo.core.extensions.handleBackLeftToRight
import com.baba.bobo.core.extensions.loadImage
import com.baba.bobo.core.extensions.requestPermission
import com.baba.bobo.core.extensions.setImageActionBar
import com.baba.bobo.core.extensions.showInterAll
import com.baba.bobo.core.extensions.startIntentRightToLeft
import com.baba.bobo.core.extensions.startIntentWithClearTop
import com.baba.bobo.core.extensions.tap
import com.baba.bobo.core.extensions.gone
import com.baba.bobo.core.extensions.visible
import com.baba.bobo.core.utils.key.IntentKey
import com.baba.bobo.core.utils.key.RequestKey
import com.baba.bobo.core.utils.key.ValueKey
import com.baba.bobo.core.utils.state.HandleState
import com.baba.bobo.databinding.ActivityCosplaySuccessfulBinding
import com.baba.bobo.ui.home.HomeActivity
import com.baba.bobo.ui.my_creation.MyCreationActivity
import com.baba.bobo.ui.permission.PermissionViewModel
import com.baba.bobo.ui.success.SuccessViewModel
import com.mbridge.msdk.foundation.same.report.metrics.c
import kotlinx.coroutines.launch

class CosplaySuccessfulActivity : BaseActivity<ActivityCosplaySuccessfulBinding>() {

    private val permissionViewModel: PermissionViewModel by viewModels()

    private var characterPosition = 0

    private val viewModel: SuccessViewModel by viewModels()

    private var finalProgress = 0

    override fun setViewBinding(): ActivityCosplaySuccessfulBinding {
        return ActivityCosplaySuccessfulBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        val path = intent.getStringExtra(IntentKey.COSPLAY_RESULT_PATH_KEY) ?: ""
        finalProgress = intent.getIntExtra(IntentKey.COSPLAY_PROGRESS_KEY, 0)
        characterPosition = intent.getIntExtra(
            IntentKey.COSPLAY_CHARACTER_POSITION_KEY,
            0
        )

        binding.includeLayoutBottom.tvShare.isSelected = true
        viewModel.setPath(path)
        updateProgressDisplay()
        binding.actionBar.apply {

            btnActionBarLeft.setImageResource(R.drawable.ic_retry)
            btnActionBarRight.visible()
            btnActionBarRight.setImageResource(R.drawable.ic_home)
            setImageActionBar(btnActionBarNextRight, R.drawable.ic_mycreation)
            setImageActionBar(btnActionBarNextRight1, R.drawable.ic_share)

        }
    }

    override fun dataObservable() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pathInternal.collect { path ->
                    if (path.isNotEmpty()) {
                        loadImage(this@CosplaySuccessfulActivity, path, binding.imvImage)
                    }
                }
            }
        }
    }

    override fun viewListener() {
        binding.apply {
            actionBar.btnActionBarRight.tap {
                showInterAll {
                    startIntentWithClearTop(HomeActivity::class.java)
                }

            }

            actionBar.btnActionBarNextRight.tap {
                startIntentRightToLeft(
                    MyCreationActivity::class.java,
                    IntentKey.TAB_KEY,
                    ValueKey.AVATAR_TYPE
                )
                finish()
            }

            actionBar.btnActionBarNextRight1.tap(2000) {

                viewModel.shareFiles(this@CosplaySuccessfulActivity)

            }

            actionBar.btnActionBarLeft.tap {
            val intent = Intent(this@CosplaySuccessfulActivity, CosplayCustomizeActivity::class.java).apply {
                putExtra(IntentKey.COSPLAY_CHARACTER_POSITION_KEY,characterPosition)
            }
                startActivity(intent)
                finish()
            }

            includeLayoutBottom.btnDownload1.tap(1000) {
                checkStoragePermission()
            }
//            includeLayoutBottom.btnTryAgain.tap(800) {
//                showInterAll {
//                    val intent = Intent(
//                        this@CosplaySuccessfulActivity,
//                        CosplayCustomizeActivity::class.java
//                    ).apply {
//                        putExtra(
//                            IntentKey.COSPLAY_CHARACTER_POSITION_KEY,
//                            characterPosition
//                        )
//                    }
//
//                    startActivity(intent)
//                    finish()
//                }
//            }


        }
    }

    private fun checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            handleDownload()
        } else {
            val perms = permissionViewModel.getStoragePermissions()
            if (checkPermissions(perms)) {
                handleDownload()
            } else if (permissionViewModel.needGoToSettings(sharePreference, true)) {
                goToSettings()
            } else {
                requestPermission(perms, RequestKey.STORAGE_PERMISSION_CODE)
            }
        }
    }

    private fun handleDownload() {
        lifecycleScope.launch {
            viewModel.downloadFiles(this@CosplaySuccessfulActivity).collect { state ->
                when (state) {
                    HandleState.LOADING -> showLoading()
                    HandleState.SUCCESS -> {
                        dismissLoading()
                        showToast(R.string.download_success)
                    }

                    else -> {
                        dismissLoading()
                        showToast(R.string.download_failed_please_try_again_later)
                    }
                }
            }
        }
    }

    override fun initActionBar() {
        binding.actionBar.apply {
            btnActionBarLeft.apply {
                visible()
                setImageResource(R.drawable.ic_retry)
            }
            btnActionBarRight.visible()
            btnActionBarRight.setImageResource(R.drawable.ic_home)
        }
    }

    private fun updateProgressDisplay() {
        val progress = finalProgress.coerceIn(0, 100)
        binding.tvProgress.text = "$progress/100"
        val starRes = when {
            progress == 0 -> R.drawable.zero_star_ss
            progress < 40 -> R.drawable.one_star_ss
            progress < 99 -> R.drawable.two_star_ss
            else -> R.drawable.three_star_ss
        }
        binding.starSS.setImageResource(starRes)
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        handleBackLeftToRight()
    }
}
