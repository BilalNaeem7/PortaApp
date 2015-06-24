package com.android.porta.pk;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.porta.pk.R;
import com.android.porta.pk.utils.LogUtils;
import com.android.porta.pk.responses.ParentCategoriesResponse;

public class DashboardFragment extends ConnectedFragment {

    private static final String TAG = "DashboardFragment";
    ImageButton productButton;
    ImageButton networkButton;
    ImageButton techButton;
    ImageButton qualityButton;
    ImageButton careButton;
    ImageButton locateButton;

    private ParentCategoriesResponse mParentCatResponse;

    public static DashboardFragment createInstance(Bundle args) {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        productButton = (ImageButton) v.findViewById(R.id.product_button);
        productButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategoriesRequest();
                /*startCategoryActivity("Product");*/
            }
        });

        networkButton = (ImageButton) v.findViewById(R.id.network_button);
        networkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startCategoryActivity("Network");
                showToast("Network");
            }
        });

        techButton = (ImageButton) v.findViewById(R.id.tech_button);
        techButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startCategoryActivity("Technology");
                showToast("Technology");
            }
        });

        qualityButton = (ImageButton) v.findViewById(R.id.quality_button);
        qualityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startCategoryActivity("Quality");
                showToast("Quality");
            }
        });

        careButton = (ImageButton) v.findViewById(R.id.care_button);
        careButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startCategoryActivity("Customer Care");
                showToast("Customer Care");
            }
        });

        locateButton = (ImageButton) v.findViewById(R.id.locate_button);
        locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startCategoryActivity("Locate Us");
                showToast("Locate Us");
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startCategoryActivity(String msg) {
        Intent i = new Intent(getActivity(), CategoryActivity.class);
        // i.putExtra(MainActivity.EXTRA_MSG, msg);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
    }

    private void addCategoriesRequest() {
        ModalProgress.show(getActivity(), "Loading Catalog", "Please wait...", 3000);
        final Request<ParentCategoriesResponse> request = ParentCategoriesResponse.getParentCategoriesRequest(
                new Listener<ParentCategoriesResponse>() {
                    @Override
                    public void onResponse(ParentCategoriesResponse response) {
                        if (response != null && response.isSuccessful()) {
                            LogUtils.LOGD(TAG, "ParentCategoriesResponse is " + response.toString());
                            mParentCatResponse = response;
                            Intent categoryIntent = new Intent(getActivity(), CategoryActivity.class);
                            Bundle args = new Bundle();
                            mParentCatResponse.putSelf(args);
                            categoryIntent.putExtras(args);
                            startActivity(categoryIntent);
                            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
                        } else {
                            LogUtils.LOGD(TAG, "else case");
                        }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogUtils.LOGD(TAG, "Error fetching Parent Categories");
                        ModalProgress.hide(getActivity());
                        onError(error);
                    }
                }
        );
        addRequest(request);
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}