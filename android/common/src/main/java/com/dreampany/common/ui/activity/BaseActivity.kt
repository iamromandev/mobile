package com.dreampany.common.ui.activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dreampany.common.R
import com.dreampany.common.misc.func.Executors
import com.google.android.material.appbar.MaterialToolbar
import com.kaopiz.kprogresshud.KProgressHUD

import javax.inject.Inject

/**
 * Created by roman on 7/11/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class BaseActivity<T> : AppCompatActivity(),
    SearchView.OnQueryTextListener where T : ViewDataBinding {

    @Inject
    protected lateinit var ex: Executors
    protected lateinit var binding: T

    protected var toolbar: MaterialToolbar? = null
    protected var menu : Menu? = null

    private var progress: KProgressHUD? = null
    //private var sheetDialog: BottomSheetMaterialDialog? = null

    @get:LayoutRes
    open val layoutRes: Int = 0

    @get:MenuRes
    open val menuRes: Int = 0

    @get:IdRes
    open val toolbarId: Int = 0

    @get:IdRes
    open val searchMenuItemId: Int = 0

    open val fullScreen: Boolean = false

    open val homeUp: Boolean = false

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutRes != 0) {
            binding = DataBindingUtil.setContentView(this, layoutRes)
            binding.lifecycleOwner = this
            initToolbar()
        }
        onStartUi(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        if (menuRes != 0) { //this need clear
            menu.clear()
            menuInflater.inflate(menuRes, menu)
            ex.postToUi(Runnable {
                //onMenuCreated(menu)
                initSearch()
            })
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        onStopUi()
        super.onDestroy()
    }

    override fun onQueryTextChange(newText: String?): Boolean = false

    override fun onQueryTextSubmit(query: String?): Boolean = false

    protected fun findMenuItemById(menuItemId: Int): MenuItem? = menu?.findItem(menuItemId)

    protected fun getSearchMenuItem(): MenuItem? = findMenuItemById(searchMenuItemId)

    protected fun getSearchView(): SearchView? {
        val view = getSearchMenuItem()?.actionView ?: return null
        return view as SearchView
    }

    private fun initToolbar() {
        if (toolbarId != 0) {
            toolbar = findViewById<MaterialToolbar>(toolbarId)
            setSupportActionBar(toolbar)
            if (fullScreen)
                supportActionBar?.hide()
            if (homeUp) {
                val actionBar = supportActionBar
                actionBar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeButtonEnabled(true)
                }
            }
        }
    }

    private fun initSearch() {
        val searchView = getSearchView()
        searchView?.apply {
            inputType = InputType.TYPE_TEXT_VARIATION_FILTER
            imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_FULLSCREEN
            queryHint = getString(R.string.search)
            val searchManager =
                context.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            setSearchableInfo(searchManager.getSearchableInfo(this@BaseActivity.componentName))
            setOnQueryTextListener(this@BaseActivity)
            isIconified = false
        }
    }

    protected fun showProgress() {
        if (progress == null) {
            progress = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
        }
        progress?.show()
    }

    protected fun hideProgress() {
        progress?.run {
            if (isShowing) dismiss()
        }
    }

    protected fun progress(progress: Boolean) {
        if (progress) showProgress() else hideProgress()
    }

    protected fun showDialogue(
        @StringRes titleRes: Int,
        @StringRes messageRes: Int = 0,
        message: String? = null,
        @StringRes positiveTitleRes: Int = R.string.ok,
        @DrawableRes positiveIconRes: Int = R.drawable.ic_baseline_done_24,
        @StringRes negativeTitleRes: Int = R.string.cancel,
        @DrawableRes negativeIconRes: Int = R.drawable.ic_baseline_close_24,
        cancellable: Boolean = false,
        onPositiveClick: () -> Unit,
        onNegativeClick: () -> Unit
    ) {
/*        if (sheetDialog == null) {
            val builder = BottomSheetMaterialDialog.Builder(this)
                .setTitle(getString(titleRes))
                .setMessage(
                    message
                        ?: if (messageRes != 0) getString(messageRes) else Constant.Default.STRING
                )
                .setCancelable(cancellable)
                .setPositiveButton(
                    getString(positiveTitleRes),
                    positiveIconRes,
                    { dialog: DialogInterface, which: Int ->
                        onPositiveClick()
                        dialog.dismiss()
                        sheetDialog = null
                    })
                .setNegativeButton(
                    getString(negativeTitleRes),
                    negativeIconRes,
                    { dialog: DialogInterface, which: Int ->
                        onNegativeClick()
                        dialog.dismiss()
                        sheetDialog = null
                    })
            sheetDialog = builder.build()
        }
        sheetDialog?.show()*/
    }

    protected fun hideDialog() {
        /*    sheetDialog?.run {
                dismiss()
            }
            sheetDialog = null*/
    }
}