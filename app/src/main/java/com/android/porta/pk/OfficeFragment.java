package com.android.porta.pk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.porta.pk.responses.OfficesResponse;
import com.android.porta.pk.responses.OfficesResponse.Office;
import com.android.porta.pk.responses.ParentCategoriesResponse;
import com.android.porta.pk.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Talha on 6/24/15.
 */
public class OfficeFragment extends ConnectedFragment {

    private static final String TAG = "OfficeFragment";
    private OfficesResponse mOfficesResponse;
    private OfficeAdapter mAdapter;

    public static OfficeFragment createInstance(Bundle args) {
        OfficeFragment frag = new OfficeFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final OfficesResponse data = mOfficesResponse = OfficesResponse.get(getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_office, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new OfficeAdapter(getActivity(), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.LOGD(TAG, "clicked at index: " + position);
            }
        });

        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);
        mAdapter.swapData(mOfficesResponse);
    }

    private static class OfficeAdapter extends BaseAdapter {

        private final Context mContext;
        private final AdapterView.OnItemClickListener mOnItemClickListener;
        private final View.OnClickListener mOnClickListener;
        private final LayoutInflater mInflater;

        private List<Office> data = new ArrayList<Office>();

        public OfficeAdapter(Context context, AdapterView.OnItemClickListener onItemClickListener) {
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

        public void swapData(OfficesResponse data) {
            if (data.data == null) {
                data.data = new ArrayList<Office>();
            }
            for (Office office : data.data) {
                this.data.add(office);
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
                convertView = mInflater.inflate(R.layout.row_office, null);
                ViewHolder.setTag(convertView);
            }

            final Office office = this.data.get(position);
            final ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.office.setText(office.office);
            if (TextUtils.isEmpty(office.address)) {
                holder.layoutAddress.setVisibility(View.GONE);
            } else {
                holder.address.setText(office.address);
                holder.layoutAddress.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(office.telephone)) {
                holder.layoutPhone.setVisibility(View.GONE);
            } else {
                holder.layoutPhone.setVisibility(View.VISIBLE);
                holder.phone.setText(office.telephone);
            }

            if(TextUtils.isEmpty(office.fax)) {
                holder.layoutFax.setVisibility(View.GONE);
            } else {
                holder.faxNumber.setText(office.fax);
                holder.layoutFax.setVisibility(View.VISIBLE);
            }
            if(TextUtils.isEmpty(office.email)) {
                holder.layoutEmail.setVisibility(View.GONE);
            } else {
                holder.layoutEmail.setVisibility(View.VISIBLE);
                holder.emailAddress.setText(office.email);
            }

            // holder.container.setTag(position);
            // holder.container.setOnClickListener(mOnClickListener);
            return convertView;
        }

        private static class ViewHolder {

            // View container;
            TextView office;
            ImageView map;
            TextView address;
            LinearLayout layoutAddress;
            ImageView call;
            TextView phone;
            LinearLayout layoutPhone;
            ImageView fax;
            TextView faxNumber;
            LinearLayout layoutFax;
            ImageView email;
            TextView emailAddress;
            LinearLayout layoutEmail;


            public static void setTag(View convertView) {
                ViewHolder holder = new ViewHolder();

                holder.office = (TextView) convertView.findViewById(R.id.officeName);

                holder.map = (ImageView) convertView.findViewById(R.id.imgMap);
                holder.address = (TextView) convertView.findViewById(R.id.officeAddress);
                holder.layoutAddress = (LinearLayout) convertView.findViewById(R.id.layoutAddress);

                holder.call = (ImageView) convertView.findViewById(R.id.imgCall);
                holder.phone = (TextView) convertView.findViewById(R.id.phone);
                holder.layoutPhone = (LinearLayout) convertView.findViewById(R.id.layoutPhone);

                holder.fax = (ImageView) convertView.findViewById(R.id.imgFax);
                holder.faxNumber = (TextView) convertView.findViewById(R.id.fax);
                holder.layoutFax = (LinearLayout) convertView.findViewById(R.id.layoutFax);

                holder.email = (ImageView) convertView.findViewById(R.id.imgEmail);
                holder.emailAddress = (TextView) convertView.findViewById(R.id.email);
                holder.layoutEmail = (LinearLayout) convertView.findViewById(R.id.layoutEmail);

                // holder.container = convertView.findViewById(R.id.container);
                convertView.setTag(holder);
            }
        }
    }
}
