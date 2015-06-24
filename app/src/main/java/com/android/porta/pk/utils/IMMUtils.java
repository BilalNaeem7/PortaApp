package com.android.porta.pk.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class IMMUtils {
	public static void hideSoftInput(View focusedView) {
		if (focusedView == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) focusedView.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
		}
	}
}
