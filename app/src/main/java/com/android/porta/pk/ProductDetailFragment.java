package com.android.porta.pk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.porta.pk.R;
import com.android.porta.pk.responses.ProductImageResponse;
import com.android.porta.pk.utils.LogUtils;
import com.android.porta.pk.responses.Product;
import com.android.porta.pk.toolbox.Urls;
import com.squareup.picasso.Picasso;

/**
 * Created by Talha on 6/23/15.
 */
public class ProductDetailFragment extends ConnectedFragment {
    private static final String TAG = "ProductDetailFragment";
    private Product mProduct;
    private ImageView imgView;

    public static ProductDetailFragment createInstance(Bundle args) {
        ProductDetailFragment frag = new ProductDetailFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Product data = mProduct = Product.get(getArguments());
        if (data == null) {
            LogUtils.LOGD(TAG, "Product  is null :(.");
            getActivity().finish();
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgView = (ImageView) view.findViewById(R.id.image);
        addImageRequest();
        bindUi(view);
    }

    private void bindUi(final View rootView) {
        LogUtils.LOGD(TAG, "Inside bindUi, " + mProduct.toString());
        TextView code = (TextView) rootView.findViewById(R.id.labelCode);
        code.setText(mProduct.code);
        TextView name = (TextView) rootView.findViewById(R.id.labelName);
        name.setText(mProduct.product_name);
        TextView shortDesc = (TextView) rootView.findViewById(R.id.labelShortDescription);
        shortDesc.setText(mProduct.short_desc);
    }

    private void addImageRequest() {
        LogUtils.LOGD(TAG, "addImageRequest");
        Request<ProductImageResponse> request = ProductImageResponse.getImageRequest(mProduct.product_id,
                new Listener<ProductImageResponse>() {
                    @Override
                    public void onResponse(ProductImageResponse response) {
                        LogUtils.LOGD(TAG, "addImageRequest>> " + response.isSuccessful());
                        if(response.isSuccessful()) {
                            LogUtils.LOGD(TAG, "addImageRequest is successful with path " + response.data.get(0).image);
                            loadImageViaPicasso(response.data.get(0).image);
                        }
                    }
                }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // ignore
                    }
                });
        addRequest(request);
    }

    private void loadImageViaPicasso(String imagePath) {
        final String url = Urls.prependHost("/" + imagePath);
        LogUtils.LOGD(TAG, "loadImageViaPicasso with url: " + url);
        Picasso.with(getActivity()).load(url).into(imgView);
    }

}
