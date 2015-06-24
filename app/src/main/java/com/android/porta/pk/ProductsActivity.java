package com.android.porta.pk;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.android.porta.pk.R;
import com.android.porta.pk.utils.LogUtils;

/**
 * Created by Talha on 6/22/15.
 */
public class ProductsActivity extends FragmentActivity {

    private static final String TAG = "ProductsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        View backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView header = (TextView) findViewById(R.id.header);
        header.setText(getIntent().getExtras().getString("header_text", ""));

        if (savedInstanceState == null) {
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_right);
            t.replace(R.id.frame, ProductsFragment.createInstance(getIntent().getExtras()));
            t.commit();
            LogUtils.LOGD(TAG, "Added Product Fragment");
        }
    }

}
