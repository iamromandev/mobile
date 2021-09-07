/*
package com.dreampany.frame.data.source.pref;

import android.content.Context;
import androidx.core.util.Pair;

import com.dreampany.frame.R;
import com.dreampany.frame.util.TimeUtil;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

*/
/**
 * Created by Hawladar Roman on 6/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 *//*

public class FramePref extends BasePref {

    private static final long NOTIFY_EXPIRE_TIME = TimeUnit.HOURS.toMillis(24);
    private static final int DEFAULT_POINTS = 100;

    private static final String VERSION_CODE = "version_code";
    private static final String TOUR = "tour";
    private static final String COUNTRY = "country";
    private static final String NOTIFY_TIME = "notify_time";
    private static final String POINTS = "points";
    private static final String USED = "used";
    private static final String FLAG_ORDER = "flag_order";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String INTERSTITIAL_COUNT = "interstitial_count";
    private static final String REWARDED_COUNT = "rewarded_count";
    private static final String LIST_OR_GRID = "list_grid";
    private static String NOTIFICATION;
    private static String LANGUAGE;
    private static String THEME;
    private static String SPEAK;
    private static String TRANSLATION;
    private static String FONT_SIZE;


    protected FramePref(Context context) {
        super(context);
        NOTIFICATION = context.getString(R.string.key_notification);
        LANGUAGE = context.getString(R.string.key_language);
        THEME = context.getString(R.string.key_theme);
        SPEAK = context.getString(R.string.key_speak);
        TRANSLATION = context.getString(R.string.key_translation);
        FONT_SIZE = context.getString(R.string.key_font_size);
    }

    public void setVersionCode(int versionCode) {
        publicPref.put(VERSION_CODE, versionCode);
    }

    public int getVersionCode() {
        return publicPref.get(VERSION_CODE, Integer.class, 0);
    }

    public void commitTour() {
        privatePref.put(TOUR, true);
    }

    public void setLanguage(String code) {
        publicPref.put(LANGUAGE, code);
    }

    public void setCountry(String code) {
        publicPref.put(COUNTRY, code);
    }

    public void setFontSize(int fontSize) {
        publicPref.put(FONT_SIZE, fontSize);
    }

    public boolean hasTour() {
        return privatePref.get(TOUR, Boolean.class, false);
    }

    public String getLanguageCode() {
        return publicPref.get(LANGUAGE, String.class, Locale.getDefault().getLanguage().toLowerCase());
    }

    public String getCountryCode() {
        String code = publicPref.get(COUNTRY, String.class, Locale.getDefault().getCountry().toLowerCase());
        if (code == null) {
            code = Locale.ENGLISH.getCountry().toLowerCase();
        }
        return code;
    }

    public int getFontSize() {
        return publicPref.get(FONT_SIZE, Integer.class, 16);
    }

    public void setNotifyTime(long time) {
        privatePref.put(NOTIFY_TIME, time);
    }


    public void addPoints(int points) {
        if (points <= 0) {
            return;
        }
        int exists = getPoints();
        privatePref.put(POINTS, points + exists);
    }

    public void addUsedPoints(int points) {
        if (points <= 0) {
            return;
        }
        int exists = getUsedPoints();
        privatePref.put(POINTS + USED, points);
    }

    public boolean isNotifyTimeExpired() {
        long time = privatePref.get(NOTIFY_TIME, Long.class, 0L);
        return TimeUtil.isExpired(time, NOTIFY_EXPIRE_TIME);
    }

    public int getPoints() {
        return privatePref.get(POINTS, Integer.class, DEFAULT_POINTS);
    }

    public int getUsedPoints() {
        return privatePref.get(POINTS + USED, Integer.class, 0);
    }

    public int getAvailablePoints() {
        int points = getPoints();
        int used = getUsedPoints();
        return points - used;
    }

    public void setFlagOrder(int flagOrder) {
        privatePref.put(FLAG_ORDER, flagOrder);
    }

    public int getFlagOrder() {
        return privatePref.get(FLAG_ORDER, Integer.class, 0);
    }

    public boolean hasTheme() {
        return publicPref.get(THEME, Boolean.class, false);
    }

    public boolean isSpeak() {
        return publicPref.get(SPEAK, Boolean.class, true);
    }

    public boolean hasNotification() {
        return publicPref.get(NOTIFICATION, Boolean.class, true);
    }

    public boolean hasTranslation() {
        return publicPref.get(TRANSLATION, Boolean.class, true);
    }

    public void setLocation(double latitude, double longitude) {
        privatePref.put(LATITUDE, latitude);
        privatePref.put(LONGITUDE, longitude);
    }

    public Pair<Double, Double> getLocation() {
        double latitude = privatePref.get(LATITUDE, Double.class, 0d);
        double longitude = privatePref.get(LONGITUDE, Double.class, 0d);
        if (latitude != 0d && longitude != 0d) {
            return Pair.create(latitude, longitude);
        }
        return null;
    }

    public double getLatitude() {
        return privatePref.get(LATITUDE, Double.class, 0d);
    }

    public double getLongitude() {
        return privatePref.get(LONGITUDE, Double.class, 0d);
    }

    public void setInterstitialCount(int count) {
        privatePref.put(INTERSTITIAL_COUNT, count);
    }

    public void setRewardedCount(int count) {
        privatePref.put(REWARDED_COUNT, count);
    }

    public int getInterstitialCount() {
        return privatePref.get(INTERSTITIAL_COUNT, Integer.class, 0);
    }

    public int getRewardedCount() {
        return privatePref.get(REWARDED_COUNT, Integer.class, 0);
    }

    public void setListOrGrid(boolean listOrGrid) {
        privatePref.put(LIST_OR_GRID , listOrGrid);
    }

    public boolean toggleListOrGrid() {
        boolean isListOrGrid = isListOrGrid();
        setListOrGrid(!isListOrGrid);
        return !isListOrGrid;
    }

    public boolean isListOrGrid() {
        return privatePref.get(LIST_OR_GRID, Boolean.class, true);
    }
}
*/
