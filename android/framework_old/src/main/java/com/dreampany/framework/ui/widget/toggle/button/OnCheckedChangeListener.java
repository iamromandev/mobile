package com.dreampany.framework.ui.widget.toggle.button;

import android.view.View;
import android.widget.Checkable;

public interface OnCheckedChangeListener {

    <T extends View & Checkable> void onCheckedChanged(T view, boolean isChecked);

}
