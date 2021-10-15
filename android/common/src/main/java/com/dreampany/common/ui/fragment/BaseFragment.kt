package com.dreampany.common.ui.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.annotation.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dreampany.common.R
import com.dreampany.common.misc.func.Executors
import com.kaopiz.kprogresshud.KProgressHUD
import javax.inject.Inject

/**
 * Created by roman on 7/11/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class BaseFragment<T> : Fragment(),
    SwipeRefreshLayout.OnRefreshListener,
    SearchView.OnQueryTextListener where T : ViewDataBinding {

    @Inject
    protected lateinit var ex: Executors
    protected lateinit var binding: T

    protected var menu: Menu? = null

    private var progress: KProgressHUD? = null
    //private var sheetDialog: BottomSheetMaterialDialog? = null

    @get:LayoutRes
    open val layoutRes: Int = 0

    @get:MenuRes
    open val menuRes: Int = 0

    @get:StringRes
    open val titleRes: Int = 0

    @get:StringRes
    open val subtitleRes: Int = 0

    @get:IdRes
    open val searchMenuItemId: Int = 0

    open val hasBackPressed: Boolean = false

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(menuRes != 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (layoutRes != 0) {
            binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
            binding.lifecycleOwner = this
            return binding.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        if (menuRes != 0) { //this need clear
            menu.clear()
            inflater.inflate(menuRes, menu)
            //onMenuCreated(menu)
            initSearch()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onStartUi(savedInstanceState)
    }

    override fun onDestroyView() {
        onStopUi()
        super.onDestroyView()
    }

    override fun onRefresh() {}

    override fun onQueryTextChange(newText: String?): Boolean = false

    override fun onQueryTextSubmit(query: String?): Boolean = false

    protected fun findMenuItemById(menuItemId: Int): MenuItem? = menu?.findItem(menuItemId)

    protected fun getSearchMenuItem(): MenuItem? = findMenuItemById(searchMenuItemId)

    protected fun getSearchView(): SearchView? {
        val view = getSearchMenuItem()?.actionView ?: return null
        return view as SearchView
    }

    protected fun applyProgress(progress: Boolean) {
        if (progress) showProgress() else hideProgress()
    }

    protected fun showProgress() {
        if (progress == null) {
            progress = KProgressHUD.create(activity)
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
        /*if (sheetDialog == null) {
            activity?.run {
                sheetDialog = BottomSheetMaterialDialog.Builder(this)
                    .setTitle(getString(titleRes))
                    .setMessage(message ?: getString(messageRes))
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
                    .build()
            }
        }
        sheetDialog?.show()*/
    }

    protected fun hideDialog() {
/*        sheetDialog?.run { dismiss() }
        sheetDialog = null*/
    }

    protected fun hideSearchView() {
        //getSearchView()?.setIconified(true)
        getSearchMenuItem()?.collapseActionView()
    }

    private fun initSearch() {
        getSearchView()?.apply {
            inputType = InputType.TYPE_TEXT_VARIATION_FILTER
            imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_FULLSCREEN
            //queryHint = getString(R.string.search)
            val searchManager =
                context.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            setOnQueryTextListener(this@BaseFragment)
            isIconified = false
        }
    }
}