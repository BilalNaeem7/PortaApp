package com.android.porta.pk;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.os.Handler;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.porta.pk.R;
import com.android.porta.pk.utils.LogUtils;
import com.android.porta.pk.utils.RequestUtils;


/**
 * Created by Talha on 6/20/15.
 */
public class ConnectedFragment extends Fragment {

    private RequestQueue mRequestQueue;
    private Handler mHandler = new Handler();
    public static final String TEXT_DIALOG = "TextDialog";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mRequestQueue = ((PortaApplication) activity.getApplication()).getRequestQueue();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRequestQueue.cancelAll(getId());
    }

    protected RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    protected void addRequest(Request<?> request) {
        if (mRequestQueue != null && request != null) {
            RequestUtils.preAdd(getActivity(), request);
            request.setTag(getId());
            mRequestQueue.add(request);
            LogUtils.LOGD(">>", ">" + request);
        }
    }

    /**
     * This function does the following in case of Volley Error
     * 1.. Hides the modal progress if any
     * 2.. Checks if error code was 403: launches loginResponse flow in that case
     * 3.. Displays a general 'Try again message' using Crouton bar
     *
     * @param error
     */
    public void onError(VolleyError error) {
        // following won't work in case of 403
        FragmentActivity ac = getActivity();
        if (ac != null) {
            ModalProgress.hide(ac);// hide progress that might be showing
            showInfo(getErrorMessage(error));
        }
    }

    protected void onGeneralError() {
        FragmentActivity ac = getActivity();
        if (ac != null) {
            showInfo(R.string.general_error);
        }
    }

    protected void showInfo(int msg) {
        showInfo(readXmlString(msg));
    }

    protected void showInfo(final String msg) {
        // aggregate
        if (mHandler == null) {
            return;
        }
        mHandler.removeCallbacksAndMessages(null);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!isAdded()) {
                    return;
                }
                try {
                    final android.support.v4.app.FragmentManager manager = getChildFragmentManager();
                    Fragment fragment = manager.findFragmentByTag(TEXT_DIALOG);
                    if (fragment != null) {
                        manager.beginTransaction().remove(fragment).commit();
                    }
                    DialogFragment dialog = TextDialog.createInstance(msg);
                    dialog.show(manager, TEXT_DIALOG);
                } catch (IllegalStateException ilse) {
                    ilse.printStackTrace();
                }
            }
        });
    }

    protected String readXmlString(int stringId) {
        String toRet = null;
        try {
            // getString() uses getResources().getString()
            // getResources can throw IllegalStateException
            // getString can throw Resources.NotFoundException
            if(isAdded()) {
                toRet = getString(stringId);
            }
        } catch (IllegalStateException ilse) {
            LogUtils.LOGE("ParentFragment", "error reading string from xml file with id " + stringId, ilse);
        } catch (Exception ex) {
            LogUtils.LOGE("ParentFragment", "general error in reading string from xml file", ex);
        }

        if (TextUtils.isEmpty(toRet)) {
            toRet = "We are unable to complete your request, please try again.";
        }
        return toRet;
    }

    protected String getErrorMessage(VolleyError error) {
        LogUtils.LOGD("Porta-Error", error != null ? error.getMessage() : "error is null");
        String errMessage = readXmlString(R.string.general_error);
        if (error == null) {
            // in fact error should not be null
            return errMessage;
        } else {
            if (error instanceof NetworkError) {
                errMessage = readXmlString(R.string.volley_network_error);
            } else if (error.networkResponse != null) {
                final int statusCode = error.networkResponse.statusCode;
                if (statusCode >= 500) {
                    errMessage = readXmlString(R.string.general_error_server);
                } else {
                    // it should not be a 403: that's handled in handleAuthError
                    errMessage = readXmlString(R.string.general_error);
                }
            }
        }
        return errMessage;
    }
}
