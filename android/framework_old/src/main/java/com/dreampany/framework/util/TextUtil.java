package com.dreampany.framework.util;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.dreampany.framework.R;
import com.google.common.base.Strings;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hawladar Roman on 5/24/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public final class TextUtil {

    public static final String WORD_REGEX = "[a-zA-Z]+";
    public static final String WORD_REGEX_FULL = "([^a-zA-Z']+)'*\\1*";

    private TextUtil() {
    }

    public static String toUpper(CharSequence charSequence) {
        return toLower(trim(charSequence.toString()));
    }

    public static String toUpper(String string) {
        return string.toUpperCase(Locale.getDefault());
    }

    public static String toLower(CharSequence charSequence) {
        return toLower(trim(charSequence.toString()));
    }

    public static String toLower(String string) {
        return toLower(string, Locale.getDefault());
    }

    public static String toLower(String string, Locale locale) {
        return string.toLowerCase(locale);
    }

    private static String trim(String string) {
        return string.trim();
    }

    public static Spanned toHtml(String text) {
        if (Strings.isNullOrEmpty(text)) return null;
        text = removeImg(text);
        return Html.fromHtml(text);
    }

    public static String removeImg(String text) {
        return text.replaceAll("(<(/)img>)|(<img.+?>)", "");
    }

    public static String stripHtml(String html) {
        if (AndroidUtil.Companion.hasNougat()) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }

    public static SpannableString toUnderscore(Context context, @StringRes int resId) {
        String text = TextUtil.getString(context, resId);
        return toUnderscore(text);
    }

    public static SpannableString toUnderscore(String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        return content;
    }

/*    private static void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int startUi = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onUiItemClick(View view) {
                // Do something with span.getURL() to handle the link uiItemClickListener...
                Util.log("Hello");
            }
        };
        strBuilder.setSpan(clickable, startUi, end, flags);
        strBuilder.removeSpan(span);
    }

    public static Spanned toHtml(String text) {
        if (Util.isAbsoluteEmpty(text)) return null;
        // text = removeImg(text);

        CharSequence t = Html.fromHtml(text);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(t);
        URLSpan[] urls = strBuilder.getSpans(0, t.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }

        return strBuilder;
    }

    private static String removeImg(String text) {
        return text.replaceAll("(<(/)img>)|(<img.+?>)", "");
    }*/

    public static int countVowel(String text) {
        return text.length() - text.replaceAll("a|e|i|o|u|", "").length();
    }

    public static int countConsonant(String text) {
        return text.length() - countVowel(text);
    }

    public static boolean isAlpha(String text) {
        char[] chars = text.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    public static int getWeight(String name) {

        char[] chars = name.toCharArray();

        int alphaCount = 0;

        for (char c : chars) {
            if (Character.isLetter(c)) {
                alphaCount += 1;
                if (TextUtil.isVowel(c)) {
                    alphaCount += 1;
                }
            }
        }

        return alphaCount;
    }

    public static boolean isVowel(char c) {
        switch (c) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'U':
                return true;
        }
        return false;
    }

    public static String toTitleCase(String text) {

        if (text == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(text);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to setTitle case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }


    public static String[] getStrings(Context context, int... resourceIds) {
        String[] strings = new String[resourceIds.length];
        for (int index = 0; index < resourceIds.length; index++) {
            strings[index] = getString(context, resourceIds[index]);
        }
        return strings;
    }

    @Nullable
    public static String getString(@Nullable Context context, @StringRes int resId) {
        if (context == null) {
            return null;
        }
        return context.getString(resId);
    }

    @Nullable
    public static String getString(@Nullable Context context, @StringRes int resId, Object... args) {
        if (context == null) {
            return null;
        }
        return context.getString(resId, args);
    }

    public static String[] getStringArray(@NonNull Context context, @ArrayRes int arrayId) {
        return context.getResources().getStringArray(arrayId);
    }

    public static List<String> getStringList(Context context, int arrayId) {
        return Arrays.asList(context.getResources().getStringArray(arrayId));
    }

    public static String toString(int value) {
        return String.valueOf(value);
    }

    public static String shuffle(String text) {
        List<Character> chars = new ArrayList<>(text.length());

        for (char ch : text.toCharArray()) {
            chars.add(ch);
        }

        Collections.shuffle(chars); //shuffle the list

        StringBuilder sb = new StringBuilder(); //now rebuild the word
        for (char ch : chars)
            sb.append(ch);

        return sb.toString();
    }

    public static String remove(String parent, String child) {
        return parent.replaceAll(child, "");
    }

    public static ArrayList<String> getWords(String text) {
        //String regex = "([^a-zA-Z']+)'*\\1*";
        //String[] split = text.split(regex);

        Matcher matcher = Pattern.compile(WORD_REGEX).matcher(text);
        Set<String> items = new HashSet<>();
        while (matcher.find()) {
            items.add(matcher.group());
        }
        return new ArrayList<>(items);
    }

    public static int getWordsCount(String text) {
        //String regex = "([^a-zA-Z']+)'*\\1*";
        //String[] split = text.split(regex);

        Matcher matcher = Pattern.compile(WORD_REGEX).matcher(text);
        Set<String> items = new HashSet<>();
        while (matcher.find()) {
            items.add(matcher.group());
        }
        return items.size();
    }

    public static boolean setSpan(TextView view, List<String> items, String bold,
                                  Link.OnClickListener clickListener,
                                  Link.OnLongClickListener longClickListener) {
        List<Link> links = new ArrayList<>();
        Link link;
        for (String item : items) {
            if (DataUtil.isEmpty(item)) {
                continue;
            }
            link = new Link(item)
                    .setUnderlined(false)
                    .setTextColor(ColorUtil.Companion.getColor(view.getContext(), R.color.material_grey700))
                    .setTextColorOfHighlightedLink(ColorUtil.Companion.getColor(view.getContext(), R.color.colorAccent));
            if (clickListener != null) {
                link.setOnClickListener(clickListener);
            }
            if (longClickListener != null) {
                link.setOnLongClickListener(longClickListener);
            }
            links.add(link);
        }

        if (!DataUtil.isEmpty(bold)) {
            link = new Link(bold)
                    .setUnderlined(false)
                    .setBold(true)
                    .setTextColor(ColorUtil.Companion.getColor(view.getContext(), R.color.material_grey900))
                    .setTextColorOfHighlightedLink(ColorUtil.Companion.getColor(view.getContext(), R.color.colorAccent));
            links.add(link);
        }

        LinkBuilder.on(view)
                .addLinks(links)
                .build();

        return false;
    }

    public static boolean setSpan(TextView view, List<String> items,
                                  @ColorRes int textColor,
                                  @ColorRes int textColorOfLink,
                                  Link.OnClickListener clickListener,
                                  Link.OnLongClickListener longClickListener) {
        List<Link> links = new ArrayList<>();
        Link link;
        for (String item : items) {
            if (DataUtil.isEmpty(item)) {
                continue;
            }
            link = new Link(item)
                    .setUnderlined(false)
                    .setTextColor(ColorUtil.Companion.getColor(view.getContext(), textColor))
                    .setTextColorOfHighlightedLink(ColorUtil.Companion.getColor(view.getContext(), textColorOfLink));
            if (clickListener != null) {
                link.setOnClickListener(clickListener);
            }
            if (longClickListener != null) {
                link.setOnLongClickListener(longClickListener);
            }
            links.add(link);
        }

        LinkBuilder.on(view)
                .addLinks(links)
                .build();

        return false;
    }
}