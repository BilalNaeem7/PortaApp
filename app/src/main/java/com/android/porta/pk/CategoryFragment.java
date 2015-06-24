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
import com.android.porta.pk.responses.ParentCategoriesResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Talha on 6/22/15.
 */
public class CategoryFragment extends ConnectedFragment {

    private static final String TAG = "CategoryFragment";
    private ParentCategoriesResponse mParentCatResponse = null;
    private CategoryAdapter mAdapter = null;

    public static CategoryFragment createInstance(Bundle args) {
        CategoryFragment frag = new CategoryFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ParentCategoriesResponse data = mParentCatResponse = ParentCategoriesResponse.get(getArguments());
        if (data == null) {
            getActivity().finish();
            return;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new CategoryAdapter(getActivity(), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.LOGD(TAG, "Parent Category Item Clicked position; " + position);
                ParentCategoriesResponse.ParentCategory selectedCat =
                        (ParentCategoriesResponse.ParentCategory)mAdapter.getItem(position);
                if (selectedCat != null) {
                    addCategoryChildRequest(selectedCat);
                }
            }
        });
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);
        mAdapter.swapData(mParentCatResponse);

    }

    private void addCategoryChildRequest(final ParentCategoriesResponse.ParentCategory pCat) {
        ModalProgress.show(getActivity(), pCat.category_name, "please wait...", 3000);
        Request<SubCategoryResponse> request = SubCategoryResponse.getSubCategoryRequest(pCat.id,
                new Response.Listener<SubCategoryResponse>() {
                    @Override
                    public void onResponse(SubCategoryResponse response) {
                        if (response.isSuccessful()) {
                            Intent childCatIntent = new Intent(getActivity(), SubCategoryActivity.class);
                            Bundle args = new Bundle();
                            response.putSelf(args);
                            args.putString("header_text", pCat.category_name);
                            childCatIntent.putExtras(args);
                            ModalProgress.hide(getActivity());
                            startActivity(childCatIntent);
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

    private static class CategoryAdapter extends BaseAdapter {

        private final Context mContext;
        private final AdapterView.OnItemClickListener mOnItemClickListener;
        private final View.OnClickListener mOnClickListener;
        private final LayoutInflater mInflater;

        private List<ParentCategoriesResponse.ParentCategory> data =
                new ArrayList<ParentCategoriesResponse.ParentCategory>();
        private static int ids[] = {R.drawable.care, R.drawable.network,
                R.drawable.locate, R.drawable.quality,
                R.drawable.tech};

        public CategoryAdapter(Context context, AdapterView.OnItemClickListener onItemClickListener) {
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

        public void swapData(ParentCategoriesResponse data) {
            if (data.data == null) {
                data.data = new ArrayList<ParentCategoriesResponse.ParentCategory>();
            }
            for (ParentCategoriesResponse.ParentCategory pCat : data.data) {
                this.data.add(pCat);
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
                convertView = mInflater.inflate(R.layout.row_category, null);
                ViewHolder.setTag(convertView);
            }

            final ParentCategoriesResponse.ParentCategory cat = this.data.get(position);
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
