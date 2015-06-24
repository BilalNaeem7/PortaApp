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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.porta.pk.R;
import com.android.porta.pk.utils.LogUtils;
import com.android.porta.pk.responses.SubCategoryResponse;
import com.android.porta.pk.responses.CategoryProductsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Talha on 6/22/15.
 */
public class SubCategoryFragment extends ConnectedFragment {

    private static final String TAG = "SubCategoryFragment";
    private SubCategoryResponse mCatChildrenResp;
    private CategoryChildAdapter mAdapter;

    public static SubCategoryFragment createInstance(Bundle args) {
        SubCategoryFragment frag = new SubCategoryFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SubCategoryResponse data = mCatChildrenResp = SubCategoryResponse.get(getArguments());
        if(data == null) {
            getActivity().finish();
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_child, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new CategoryChildAdapter(getActivity(), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.LOGD(TAG, "Clicked sub-category at index " + position);
                SubCategoryResponse.CategoryChild catChild =
                        (SubCategoryResponse.CategoryChild) mAdapter.getItem(position);
                if(catChild != null) {
                    LogUtils.LOGD(TAG, "selected sub-cat is " + catChild.category_name + " last_child>>" + catChild.last_child);
                    if (catChild.last_child == 0) {
                        // If last_child is 0 then category has children
                        // we should request sub-categories
                        addSubCategoryRequest(catChild);
                    } else {
                        // If last_child is 1 then category has no children.
                        // we should request products
                        addProductsRequest(catChild);
                    }
                }
            }
        });
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);
        mAdapter.swapData(mCatChildrenResp);
    }

    private void addSubCategoryRequest(final SubCategoryResponse.CategoryChild pCat) {
        ModalProgress.show(getActivity(), pCat.category_name, "please wait...", 3000);
        Request<SubCategoryResponse> request = SubCategoryResponse.getSubCategoryRequest(pCat.id,
                new Response.Listener<SubCategoryResponse>() {
                    @Override
                    public void onResponse(SubCategoryResponse response) {
                        if (response.isSuccessful()) {
                            Intent subCatIntent = new Intent(getActivity(), SubCategoryActivity.class);
                            Bundle args = new Bundle();
                            response.putSelf(args);
                            args.putString("header_text", pCat.category_name);
                            subCatIntent.putExtras(args);
                            ModalProgress.hide(getActivity());
                            startActivity(subCatIntent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ModalProgress.hide(getActivity());
                        onError(error);
                    }
                });
        addRequest(request);
    }


    private void addProductsRequest(final SubCategoryResponse.CategoryChild catChild) {
        ModalProgress.show(getActivity(), catChild.category_name, "please wait", 3000);
        Request<CategoryProductsResponse> request = CategoryProductsResponse.getCategoryProductRequest(catChild.id,
                new Response.Listener<CategoryProductsResponse>() {
                    @Override
                    public void onResponse(CategoryProductsResponse response) {
                        ModalProgress.hide(getActivity());
                        if(response.isSuccessful()) {
                            Intent productsIntent = new Intent(getActivity(), ProductsActivity.class);
                            Bundle args = new Bundle();
                            args.putString("header_text", catChild.category_name);
                            response.putSelf(args);
                            productsIntent.putExtras(args);
                            startActivity(productsIntent);
                        }
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ModalProgress.hide(getActivity());
                        onError(error);
                    }
                });
        addRequest(request);
    }

    private static class CategoryChildAdapter extends BaseAdapter {

        private final Context mContext;
        private final AdapterView.OnItemClickListener mOnItemClickListener;
        private final View.OnClickListener mOnClickListener;
        private final LayoutInflater mInflater;

        private List<SubCategoryResponse.CategoryChild> data =
                new ArrayList<SubCategoryResponse.CategoryChild>();
        private static int ids[] = {R.drawable.care, R.drawable.network,
                R.drawable.locate, R.drawable.quality,
                R.drawable.tech};

        public CategoryChildAdapter(Context context, AdapterView.OnItemClickListener onItemClickListener) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mOnItemClickListener = onItemClickListener;
            mOnClickListener = new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    if(mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(null, v, position, getItemId(position));
                    }
                }
            };
        }

        public void swapData(SubCategoryResponse data) {
            if (data == null) {
                return;
            }

            if (data.data == null) {
                data.data = new ArrayList<SubCategoryResponse.CategoryChild>();
            }

            for (SubCategoryResponse.CategoryChild childCat : data.data) {
                this.data.add(childCat);
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
            return this.data.get(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.row_subcategory, null);
                ViewHolder.setTag(convertView);
            }

            final SubCategoryResponse.CategoryChild cat = this.data.get(position);
            final ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.name.setText(cat.category_name);
            holder.image.setImageResource(ids[position % ids.length]);
            holder.container.setTag(position);
            holder.container.setOnClickListener(mOnClickListener);
            return convertView;
        }

        private static class ViewHolder {
            ImageView image;
            TextView name;
            View container;
            public static void setTag(View convertView) {
                ViewHolder holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.imgCategory);
                holder.name = (TextView) convertView.findViewById(R.id.txtCategoryName);
                holder.container = convertView.findViewById(R.id.container);
                convertView.setTag(holder);
            }
        }
    }

}
