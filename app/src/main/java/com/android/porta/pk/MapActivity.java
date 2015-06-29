package com.android.porta.pk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.porta.pk.responses.DealersResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DealersResponse mDealersResponse;
    private List<DealersResponse.Dealer> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        TextView header = (TextView) findViewById(R.id.header);
        header.setText("Dealers");

        View backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDealersResponse = DealersResponse.get(getIntent().getExtras());
        mData = new ArrayList<>();

        addData(mDealersResponse);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng pakistan = new LatLng(33.667, 73.166);

        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pakistan, 5));

        for(DealersResponse.Dealer dealer : mData) {
            googleMap.addMarker(new MarkerOptions()
                    .title(dealer.dealer)
                    .snippet("")
                    .position(new LatLng(dealer.latitude, dealer.longitude)));
        }
    }

    public void addData(DealersResponse data) {
        if(data.data == null) {
            data.data = new ArrayList<>();
        }
        for(DealersResponse.Dealer dealer : data.data) {
            this.mData.add(dealer);
        }
    }
}
