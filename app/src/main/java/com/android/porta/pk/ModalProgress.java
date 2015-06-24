package com.android.porta.pk;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.porta.pk.R;

/**
 * Created by Talha on 6/20/15.
 */
public class ModalProgress extends DialogFragment {

    public static void show(FragmentActivity activity, String title,
                            String message) {
        show(activity, title, message, -1);
    }

    public static void show(FragmentActivity activity, String title,
                            String message, int autoHideTimeOutMillis) {
        if (activity == null) {
            return;
        }
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        hideInternal(ft, fm);
        // Create and show the dialog.
        DialogFragment newFragment = new ModalProgress();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        if (autoHideTimeOutMillis != -1) {
            args.putInt("autoHideTimeOutMillis", autoHideTimeOutMillis);
        }
        newFragment.setArguments(args);
        newFragment.show(ft, "dialog");
    }

    private static void hideInternal(FragmentTransaction ft, FragmentManager fm) {
        Fragment prev = fm.findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
    }

    public static void hide(FragmentActivity activity) {
        if (activity == null) {
            return;
        }
        if (!activity.isFinishing()) {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            hideInternal(ft, fm);
            ft.commitAllowingStateLoss();
        }
    }

    private Handler mHandler = new Handler();

    public ModalProgress() {
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final FragmentActivity activity = getActivity();
        Dialog builder = new Dialog(activity, R.style.ThemeCustomDialog);
        View view = View.inflate(getActivity(), R.layout.modal_dialog, null);
        TextView titleTv = (TextView) view.findViewById(R.id.title);
        TextView msgTv = (TextView) view.findViewById(R.id.msg);

        String title = args.getString("title");
        String msg = args.getString("message");
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            msgTv.setText(msg);
        }
        builder.setContentView(view);
        int autoHideTime = args.getInt("autoHideTimeOutMillis", -1);
        if (autoHideTime != -1) {
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    hide(activity);
                }
            }, autoHideTime);
        }

        return builder;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
