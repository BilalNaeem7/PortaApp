package com.android.porta.pk;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.porta.pk.R;
import com.android.porta.pk.utils.LogUtils;
import com.android.porta.pk.responses.ParentCategoriesResponse;


public class CategoryActivity extends FragmentActivity {

    private static final String TAG = "CategoryActivity";
    private ParentCategoriesResponse mParentCatResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        View backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView header = (TextView) findViewById(R.id.header);
        header.setText(R.string.label_catalog);

        if (savedInstanceState == null) {
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_right);
            t.replace(R.id.frame, CategoryFragment.createInstance(getIntent().getExtras()));
            t.commit();
            LogUtils.LOGD(TAG, "Added fragment");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
