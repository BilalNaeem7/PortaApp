package com.android.porta.pk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.porta.pk.R;
import com.android.porta.pk.responses.CategoryProductsResponse;
import com.android.porta.pk.responses.Product;
import com.android.porta.pk.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Talha on 6/22/15.
 */
public class ProductsFragment extends ConnectedFragment {

    private static final String TAG = "ProductsFragment";
    private CategoryProductsResponse mProducts;
    private ProductsAdapter mAdapter;

    public static ProductsFragment createInstance(Bundle args) {
        ProductsFragment frag = new ProductsFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final CategoryProductsResponse data = mProducts = CategoryProductsResponse.get(getArguments());
        if (data == null) {
            LogUtils.LOGD(TAG, "Data  is null.");
            getActivity().finish();
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new ProductsAdapter(getActivity(), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.LOGD(TAG, "Clicked on Product opening detail: " + position);
                Product prd = (Product)mAdapter.getItem(position);
                if (prd != null) {
                    LogUtils.LOGD(TAG, prd.toString());
                    Intent detailIntent = new Intent(getActivity(), ProductDetailActivity.class);
                    Bundle args = new Bundle();
                    prd.putSelf(args);
                    args.putString("header_text", prd.product_name);
                    detailIntent.putExtras(args);
                    startActivity(detailIntent);
                }
            }
        });
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);
        mAdapter.swapData(mProducts);

        TextView emptyView = (TextView) view.findViewById(R.id.message);
        if(mAdapter.getCount() == 0) {
            emptyView.setText(mProducts.message);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    private static class ProductsAdapter extends BaseAdapter {

        private final Context mContext;
        private final AdapterView.OnItemClickListener mOnItemClickListener;
        private final View.OnClickListener mOnClickListener;
        private final LayoutInflater mInflater;

        private List<Product> data =
                new ArrayList<Product>();
        private static int ids[] = {R.drawable.care, R.drawable.network,
                R.drawable.locate, R.drawable.quality,
                R.drawable.tech};

        public ProductsAdapter(Context context, AdapterView.OnItemClickListener onItemClickListener) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mOnItemClickListener = onItemClickListener;
            mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(null, v, position, getItemId(position));
                    }
                }
            };
        }

        public void swapData(CategoryProductsResponse data) {
            if (data.data == null) {
                data.data = new ArrayList<Product>();
            }
            for (Product prd : data.data) {
                this.data.add(prd);
            }

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.data.size();
        }

        @Override
        public Object getItem(int position) {
            return this.data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return this.data.get(position).product_id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.row_products, null);
                ViewHolder.setTag(convertView);
            }

            final Product prd = this.data.get(position);
            final ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.name.setText(prd.product_name);
            holder.code.setText(prd.code);
            holder.price.setText("PKR: " + prd.price);
            // TODO load images
            holder.image.setImageResource(ids[position % ids.length]);
            holder.container.setTag(position);
            holder.container.setOnClickListener(mOnClickListener);
            return convertView;
        }

        private static class ViewHolder {
            ImageView image;
            TextView name;
            TextView code;
            TextView price;
            View container;

            public static void setTag(View convertView) {
                ViewHolder holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.imgProduct);
                holder.name = (TextView) convertView.findViewById(R.id.txtProductName);
                holder.code = (TextView) convertView.findViewById(R.id.txtCode);
                holder.price = (TextView) convertView.findViewById(R.id.txtPrice);
                holder.container = convertView.findViewById(R.id.container);
                convertView.setTag(holder);
            }
        }
    }

}
