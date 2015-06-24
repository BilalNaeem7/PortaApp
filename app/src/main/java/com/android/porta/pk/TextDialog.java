package com.android.porta.pk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.porta.pk.R;

/**
 * Created by Talha on 6/20/15.
 */
public class TextDialog extends DialogFragment {
    public static final String MESSAGE = "current_classroom";
    public static final String LABEL_YES_BTN = "yes_button_label";
    public static final String LABEL_NO_BTN = "no_button_label";
    public static final String SHOW_YES_NO = "show_yes_no";
    public static final String QUESTION_INDEX = "question_index";

    public static TextDialog createInstance(String message) {
        TextDialog dialog = new TextDialog();
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        dialog.setArguments(args);
        return dialog;
    }

    public static TextDialog createInstance(String message, String okLabel, String noLabel, boolean showTwoButtons, int index) {
        TextDialog dialog = new TextDialog();
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        args.putString(LABEL_YES_BTN, okLabel);
        args.putString(LABEL_NO_BTN, noLabel);
        args.putBoolean(SHOW_YES_NO, showTwoButtons);
        args.putInt(QUESTION_INDEX, index);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = "";
        String btnOkayLabel = "";
        String btnNoLabel = "";
        boolean hasTwoButtons = false;
        int questionIndex = -1;
        Bundle args = getArguments();
        if (args != null) {
            message = args.getString(MESSAGE, "");
            btnOkayLabel = args.getString(LABEL_YES_BTN, "");
            btnNoLabel = args.getString(LABEL_NO_BTN, "");
            hasTwoButtons = args.getBoolean(SHOW_YES_NO, false);
            questionIndex = args.getInt(QUESTION_INDEX, -1);
        }
        return getTextDialog(getArguments().getString(MESSAGE), btnOkayLabel, btnNoLabel, hasTwoButtons, questionIndex);
    }

    private Dialog getTextDialog(String message, String yesButtonLabel,
                                 String noButtonLabel, boolean showYesAndNo, final int index) {
        final Context context = getActivity();
        final Dialog builder = new Dialog(context, R.style.ThemeCustomDialog);
        View view = View.inflate(getActivity(), R.layout.dialog_error_text, null);
        ((TextView) view.findViewById(R.id.msg)).setText(message);
        Button ok = (Button) view.findViewById(R.id.button1);
        if (!TextUtils.isEmpty(yesButtonLabel)) {
            ok.setText(yesButtonLabel);
        } else {
            ok.setText("OK");
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment target = getTargetFragment();
                if (target != null) {
                    Intent toReturn = new Intent();
                    toReturn.setAction(null);
                    toReturn.putExtra("QUESTION_INDEX", index);
                    target.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, toReturn);
                }
                builder.dismiss();
            }
        });

        Button no = (Button) view.findViewById(R.id.button2);
        no.setVisibility(showYesAndNo ? View.VISIBLE : View.GONE);
        if(showYesAndNo) {
            if (!TextUtils.isEmpty(noButtonLabel)) {
                no.setText(noButtonLabel);
            } else {
                no.setText("No");
            }
            no.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Fragment target = getTargetFragment();
                    builder.dismiss();
                }
            });
        }

        view.setVisibility(View.VISIBLE);
        builder.setContentView(view);
        return builder;
    }
}
