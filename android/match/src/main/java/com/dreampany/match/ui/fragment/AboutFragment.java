package com.dreampany.match.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreampany.framework.misc.ActivityScope;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.dreampany.framework.util.AndroidUtil;
import com.dreampany.framework.util.TextUtil;
import com.dreampany.match.R;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by Hawladar Roman on 6/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class AboutFragment extends BaseFragment {

    @Inject
    public AboutFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setTitle(R.string.title_about);
        Context context = inflater.getContext();
        AboutPage page = new AboutPage(context)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher)
                .setDescription(TextUtil.getString(context, R.string.app_name))
                .addItem(getVersion(context))
                .addItem(getPrivacyPolicy(context))
                .addGroup("Connect with us")
                .addEmail(TextUtil.getString(context, R.string.email)/*, TextUtil.getString(context, R.string.title_email)*/)
                .addWebsite(TextUtil.getString(context, R.string.website)/*, TextUtil.getString(context, R.string.title_website)*/)
                .addPlayStore(AndroidUtil.getPackageName(context)/*, TextUtil.getString(context, R.string.title_play_store)*/)
                .addGitHub(TextUtil.getString(context, R.string.id_github)/*, TextUtil.getString(context, R.string.title_github)*/)
                /*.addItem(getYandexTranslation(context))*/;

        return page.create();
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {

    }

    @Override
    protected void onStopUi() {

    }

    private Element getVersion(Context context) {
        String version = AndroidUtil.getVersionName(context);
        Element element = new Element()
                .setTitle(TextUtil.getString(context, R.string.summary_app_version, version));
        return element;
    }

    private Element getPrivacyPolicy(Context context) {
        Element element = new Element()
                .setTitle(TextUtil.getString(context, R.string.title_privacy_policy))
                .setOnClickListener(view -> {
                    String url = TextUtil.getString(context, R.string.url_privacy);
                    Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                    urlIntent.setData(Uri.parse(url));
                    startActivity(urlIntent);
                });
        return element;
    }
}
