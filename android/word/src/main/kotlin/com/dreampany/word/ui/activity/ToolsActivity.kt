package com.dreampany.word.ui.activity

import android.os.Bundle
import com.dreampany.framework.misc.SmartAd
import com.dreampany.framework.ui.activity.BaseActivity
import com.dreampany.framework.ui.activity.WebActivity
import com.dreampany.word.R
import com.dreampany.word.misc.Constants
import com.dreampany.word.ui.enums.UiSubtype
import com.dreampany.word.ui.enums.UiType
import com.dreampany.word.ui.fragment.*
import com.dreampany.word.ui.model.UiTask
import com.google.android.gms.ads.AdView
import dagger.Lazy
import im.delight.android.webview.AdvancedWebView
import javax.inject.Inject

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class ToolsActivity : BaseActivity() {

    @Inject
    lateinit var settingsProvider: Lazy<SettingsFragment>
    @Inject
    lateinit var licenseProvider: Lazy<LicenseFragment>
    @Inject
    lateinit var aboutProvider: Lazy<AboutFragment>
    @Inject
    lateinit var wordProvider: Lazy<WordFragment>
    @Inject
    lateinit var visionProvider: Lazy<WordsVisionFragment>
    @Inject
    lateinit var ad: SmartAd

    override fun getLayoutId(): Int {
        return R.layout.activity_tools
    }

    override fun isFullScreen(): Boolean {
        val uiTask = getCurrentTask<UiTask<*>>(true)
        return uiTask?.fullscreen ?: super.isFullScreen()
    }

    override fun getScreen(): String {
        return Constants.tools(this)
    }

    override fun onStartUi(state: Bundle?) {
        setTitle(null)
        val uiTask = getCurrentTask<UiTask<*>>(false) ?: return
        val type = uiTask.type
        val subtype = uiTask.subtype
        if (type.equals(null) || subtype.equals(null)) {
            return
        }

        ad.initAd(
            this,
            getScreen(),
            findViewById<AdView>(R.id.adview),
            R.string.interstitial_ad_unit_id,
            R.string.rewarded_ad_unit_id
        )
        ad.loadAd(getScreen())

        when (type) {
            UiType.MORE -> {
                when (subtype) {
                    UiSubtype.SETTINGS -> {
                        commitFragment(SettingsFragment::class.java, settingsProvider, R.id.layout, uiTask)
                    }
                    UiSubtype.LICENSE -> {
                        commitFragment(LicenseFragment::class.java, licenseProvider, R.id.layout, uiTask)
                    }
                    UiSubtype.ABOUT -> {
                        commitFragment(AboutFragment::class.java, aboutProvider, R.id.layout, uiTask)
                    }
                    else -> {
                    }
                }
            }
            UiType.WORD -> {
                when (subtype) {
                    UiSubtype.VIEW -> {
                        commitFragment(WordFragment::class.java, wordProvider, R.id.layout, uiTask)
                        //ad.loadInterstitial(R.string.interstitial_ad_unit_id)
                    }
                }
            }
            UiType.OCR -> {
                when (subtype) {
                    UiSubtype.VIEW -> {
                        commitFragment(WordsVisionFragment::class.java, visionProvider, R.id.layout, uiTask)
                        //ad.loadInterstitial(R.string.interstitial_ad_unit_id)
                    }
                }
            }
            UiType.SITE -> {
                when (subtype) {
                    UiSubtype.VIEW -> {
                        if (AdvancedWebView.Browsers.hasAlternative(this)) {
                            AdvancedWebView.Browsers.openUrl(this, uiTask.comment)
                            finish()
                        } else {
                            openActivity(WebActivity::class.java, uiTask, true)
                        }
                    }
                }
            }
        }
    }

    override fun onStopUi() {
        ad.destroyBanner(getScreen())
    }

    override fun onResume() {
        super.onResume()
        ad.resumeBanner(getScreen())
    }

    override fun onPause() {
        ad.pauseBanner(javaClass.simpleName)
        super.onPause()
    }

/*    override fun onDestroy() {
        try {
            super.onDestroy()
        } catch (e: Exception) {
            Timber.e(e)
            getApp().getAnalytics().logEvent(e.toString(), getBundle())
        }
    }

    override fun onBackPressed() {
        val fragment = currentFragment
        if (fragment != null && fragment.hasBackPressed()) {
            return
        }
        finish()
    }*/
}
