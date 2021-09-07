package com.dreampany.framework.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceFragmentCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dreampany.framework.app.BaseApp
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.data.model.Color
import com.dreampany.framework.data.model.Task
import com.dreampany.framework.data.source.pref.ConfigPref
import com.dreampany.framework.misc.AppExecutor
import com.dreampany.framework.misc.Constants
import com.dreampany.framework.ui.activity.BaseActivity
import com.dreampany.framework.ui.callback.UiCallback
import com.dreampany.framework.ui.enums.UiType
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.framework.util.Animato
import com.dreampany.framework.util.TextUtil
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.*
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.helpers.EmptyViewHelper
import javax.inject.Inject


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseFragment : PreferenceFragmentCompat(), HasAndroidInjector,
    ViewTreeObserver.OnWindowFocusChangeListener,
    UiCallback<BaseActivity, BaseFragment, Task<*>, ViewModelProvider.Factory, ViewModel>,
    View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, PermissionListener,
    MultiplePermissionsListener, PermissionRequestErrorListener, SearchView.OnQueryTextListener,
    FlexibleAdapter.OnItemClickListener, FlexibleAdapter.OnItemLongClickListener,
    FlexibleAdapter.EndlessScrollListener, EmptyViewHelper.OnEmptyViewListener,
    TextWatcher {

    @Inject
    protected lateinit var configPref: ConfigPref
    @Inject
    protected lateinit var ex: AppExecutor
    @Inject
    internal lateinit var childInjector: DispatchingAndroidInjector<Any>
    protected lateinit var binding: ViewDataBinding
    protected var task: Task<*>? = null
    protected var childTask: Task<*>? = null
    protected var currentView: View? = null
    protected var color: Color? = null
    protected var fireOnStartUi: Boolean = true
    protected lateinit var activityCallback: UiCallback<BaseActivity, BaseFragment, Task<*>, ViewModelProvider.Factory, ViewModel>
    protected lateinit var fragmentCallback: UiCallback<BaseActivity, BaseFragment, Task<*>, ViewModelProvider.Factory, ViewModel>

/*    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return childInjector
    }*/

    override fun androidInjector(): AndroidInjector<Any> {
        return childInjector
    }

    open fun getScreen(): String {
        return javaClass.simpleName
    }

    @LayoutRes
    open fun getLayoutId(): Int = Constants.Default.INT

    open fun getPrefLayoutId(): Int {
        return Constants.Default.INT
    }

    open fun getTitleResId(): Int {
        return Constants.Default.INT
    }

    open fun hasColor(): Boolean {
        val activity = getParent()
        return if (activity != null) activity.hasColor() else false
    }

    open fun applyColor(): Boolean {
        val activity = getParent()
        return if (activity != null) activity.applyColor() else false
    }

    open fun hasBackPressed(): Boolean {
        return false
    }

/*    open fun hasBus(): Boolean {
        return false
    }*/

    open fun getCurrentFragment(): BaseFragment? {
        return this
    }

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val prefLayoutId = getPrefLayoutId()
        if (prefLayoutId != 0) {
            addPreferencesFromResource(prefLayoutId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (currentView != null) {
            if (currentView?.getParent() != null) {
                (currentView?.getParent() as ViewGroup).removeView(currentView)
            }
            return currentView
        }
        val layoutId = getLayoutId()
        if (layoutId != 0) {
            binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            binding.setLifecycleOwner(this)
            currentView = binding.root
        } else {
            currentView = super.onCreateView(inflater, container, savedInstanceState)
        }
        //currentView!!.viewTreeObserver.addOnWindowFocusChangeListener(this)
        return currentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = activity
        if (BaseActivity::class.java.isInstance(activity) && UiCallback::class.java.isInstance(
                activity
            )
        ) {
            activityCallback =
                activity as UiCallback<BaseActivity, BaseFragment, Task<*>, ViewModelProvider.Factory, ViewModel>
        }

        // this will be worked when parent and child fragment relation
        val parentFragment = parentFragment
        if (BaseFragment::class.java.isInstance(parentFragment) && UiCallback::class.java.isInstance(
                parentFragment
            )
        ) {
            fragmentCallback =
                parentFragment as UiCallback<BaseActivity, BaseFragment, Task<*>, ViewModelProvider.Factory, ViewModel>
        }

        val titleResId = getTitleResId()
        if (titleResId != 0) {
            setTitle(titleResId)
        }

        if (hasColor()) {
            color = getParent()?.getUiColor()
        }

        if (fireOnStartUi) {
            onStartUi(savedInstanceState)
            val screen = getScreen()
            configPref.setScreen(UiType.FRAGMENT, screen)
            getApp()?.throwAnalytics(Constants.Event.FRAGMENT, screen)
        }
    }

/*    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        val needUpdate = isResumed && isVisibleToUser != userVisibleHint
        super.setUserVisibleHint(isVisibleToUser)
        if (needUpdate) {
            if (isVisibleToUser) {
                this.onVisible()
            } else {
                this.onInvisible()
            }
        }
    }*/

    override fun onStart() {
        super.onStart()
/*        if (hasBus()) {
            Events.register(this)
        }*/
    }

    override fun onStop() {
/*        if (hasBus()) {
            Events.unregister(this)
        }*/
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        if (this.userVisibleHint) {
            this.onVisible()
        }
    }

    override fun onPause() {
        this.onInvisible()
        super.onPause()
    }

    override fun onDestroyView() {
        onStopUi()
        if (currentView != null) {
            //currentView!!.viewTreeObserver.removeOnWindowFocusChangeListener(this)
            val parent = currentView?.parent
            if (parent != null) {
                (parent as ViewGroup).removeAllViews()
            }
        }
        super.onDestroyView()
    }

    override fun getContext(): Context? {
        if (AndroidUtil.hasMarshmallow()) {
            return super.getContext()
        }
        return if (currentView != null) {
            currentView?.context
        } else {
            getParent()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUiActivity(): BaseActivity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUiFragment(): BaseFragment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun set(t: Task<*>) {
        childTask = t
    }

    override fun get(): ViewModelProvider.Factory {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getX(): ViewModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun execute(t: Task<*>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(v: View) {
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        return false
    }

    override fun onItemLongClick(position: Int) {
    }

    override fun noMoreLoad(newItemsSize: Int) {
    }

    override fun onLoadMore(lastPosition: Int, currentPage: Int) {
    }

    override fun onUpdateEmptyDataView(size: Int) {
    }

    override fun onUpdateEmptyFilterView(size: Int) {
    }

    override fun onRefresh() {
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?, token: PermissionToken?
    ) {

    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport) {

    }

    override fun onPermissionRationaleShouldBeShown(
        permissions: List<PermissionRequest>, token: PermissionToken
    ) {

    }

    override fun onError(error: DexterError) {

    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }

    fun onVisible() {

    }

    fun onInvisible() {

    }

    fun getApp(): BaseApp? {
        return getParent()?.getApp()
    }

    fun getAppContext(): Context? {
        return context?.applicationContext
    }

    open fun getUiColor(): Color? {
        return color
    }

    protected fun getParent(): BaseActivity? {
        val activity = activity
        return if (BaseActivity::class.java.isInstance(activity) && AndroidUtil.isAlive(activity)) {
            activity as BaseActivity
        } else {
            null
        }
    }

    protected fun isParentAlive(): Boolean {
        return AndroidUtil.isAlive(getParent())
    }

    protected fun <T> getIntentValue(key: String): T? {
        val bundle = getBundle()
        return getIntentValue<T>(key, bundle)
    }

    protected fun <T> getIntentValue(key: String, bundle: Bundle?): T? {
        var t: T? = null
        if (bundle != null) {
            t = bundle.getParcelable<Parcelable>(key) as T?
        }
        if (bundle != null && t == null) {
            t = bundle.getSerializable(key) as T?
        }
        return t
    }

    protected fun getBundle(): Bundle? {
        return arguments
    }

    protected fun <T : Task<*>> getCurrentTask(intent: Intent): T? {
        val task = getIntentValue<T>(Constants.Task.TASK, intent.extras)
        return task
    }

    protected fun <T : Task<*>> getCurrentTask(): T? {
        return getCurrentTask(false)
    }

    protected fun <T : Task<*>> getCurrentTask(freshTask: Boolean): T? {
        if (task == null || freshTask) {
            task = getIntentValue<T>(Constants.Task.TASK)
        }
        return task as T?
    }

    protected fun <T : Base> getInput(): T? {
        val task: Task<*>? = getCurrentTask()
        return task?.input as T?
    }

    protected fun <T : View> findViewById(@IdRes id: Int): T? {
        var current = currentView
        if (current == null) {
            current = view
        }
        return if (current != null) current.findViewById(id) else null
    }

    @SuppressLint("ResourceType")
    protected fun setTitle(@StringRes resId: Int) {
        if (resId <= 0) {
            return
        }
        setTitle(context?.let { TextUtil.getString(it, resId) })
    }

    @SuppressLint("ResourceType")
    protected fun setSubtitle(@StringRes resId: Int) {
        if (resId <= 0) {
            return
        }
        setSubtitle(context?.let { TextUtil.getString(it, resId) })
    }

    protected fun setTitle(title: String?) {
        val activity = activity
        if (BaseActivity::class.java.isInstance(activity)) {
            (activity as BaseActivity).setTitle(title)
        }
    }

    protected fun setSubtitle(subtitle: String? = null) {
        val activity = activity
        if (BaseActivity::class.java.isInstance(activity)) {
            (activity as BaseActivity).setSubtitle(subtitle)
        }
    }

    protected fun bindLocalCast(receiver: BroadcastReceiver, filter: IntentFilter) {
        LocalBroadcastManager.getInstance(context!!).registerReceiver(receiver, filter)
    }

    protected fun debindLocalCast(receiver: BroadcastReceiver) {
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(receiver)
    }

    fun openActivity(target: Class<*>) {
        AndroidUtil.openActivity(this, target)
    }

    fun openActivity(target: Class<*>, requestCode: Int) {
        AndroidUtil.openActivity(this, target, requestCode)
    }

    fun openActivity(target: Class<*>, task: Task<*>) {
        AndroidUtil.openActivity(this, target, task)
    }

    fun openActivity(target: Class<*>, task: Task<*>, requestCode: Int) {
        AndroidUtil.openActivity(this, target, task, requestCode)
    }

    fun checkPermissions(vararg permissions: String) {
        val parent = getParent();
        parent?.let {
            Dexter.withActivity(it).withPermissions(*permissions).withListener(this).check()
        }

    }

    fun showInfo(info: String) {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        parent?.showInfo(info)
    }

    protected fun showError(error: String) {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        parent?.showError(error)
    }

    fun showAlert(title: String, text: String, backgroundColor: Int, timeout: Long) {
        showAlert(title, text, backgroundColor, timeout, null)
    }

    fun showAlert(
        title: String,
        text: String,
        backgroundColor: Int,
        timeout: Long,
        listener: View.OnClickListener?
    ) {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        parent?.showAlert(title, text, backgroundColor, timeout, listener)
    }

    fun hideAlert() {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        parent?.hideAlert()
    }

    protected fun forResult(okay: Boolean = true) {
        val task = getCurrentTask<Task<*>>(false)
        forResult(task, okay)
    }

    protected fun forResult(task: Task<*>? = null, okay: Boolean = true) {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        val intent = Intent()
        intent.putExtra(Constants.Task.TASK, task as Parcelable)
        if (okay) {
            parent?.setResult(Activity.RESULT_OK, intent)
        } else {
            parent?.setResult(Activity.RESULT_CANCELED, intent)
        }
        parent?.run {
            finish()
            Animato.animateSlideRight(this)
        }
    }

    protected fun isOkay(resultCode: Int): Boolean {
        return resultCode == Activity.RESULT_OK
    }

/*    protected fun showProgress(message: String) {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        parent?.showProgress(message)
    }

    protected fun hideProgress() {
        if (!isParentAlive()) {
            return
        }
        val parent = getParent()
        parent?.hideProgress()
    }*/
}