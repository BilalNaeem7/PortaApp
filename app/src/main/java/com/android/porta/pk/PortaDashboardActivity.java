package com.android.porta.pk;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.android.porta.pk.R;

/**
 * Created by Talha on 6/20/15.
 */
public class PortaDashboardActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (savedInstanceState == null) {
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_right);
            t.replace(R.id.frame, DashboardFragment.createInstance(getIntent().getExtras()));
            t.commit();
        }
    }

}
