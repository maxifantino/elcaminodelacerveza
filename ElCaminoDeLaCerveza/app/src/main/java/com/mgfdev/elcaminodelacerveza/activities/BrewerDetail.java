package com.mgfdev.elcaminodelacerveza.activities;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;
import com.squareup.picasso.Picasso;

/**
 * Created by Maxi on 19/12/2017.
 */

public class BrewerDetail  extends FragmentActivity {
    private TextView title;
    private TextView description;
    private TextView address;
    private TextView phone;
    private TextView facebookLink;
    private TextView instagramLink;
    private TextView twitterLink;
    private ImageView banner;
    private MapView mMapView;
    private Context ctx;
    private  BrewerInfo data;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_beer_detail);

        data = (BrewerInfo) getIntent().getSerializableExtra("Brewer");
        ctx = this;
        createViews();
        populateViews(data);
        configGmap(savedInstanceState);
    }

    private void createViews(){
        title = (TextView) findViewById(R.id.beerDetailTitle);
        address = (TextView) findViewById(R.id.beerDetailAddress);
        phone = (TextView) findViewById(R.id.beerDetailPhone);
        instagramLink = (TextView) findViewById(R.id.beerDetailInstagram);
        facebookLink = (TextView) findViewById(R.id.beerDetailFacebook);
        twitterLink = (TextView) findViewById(R.id.beerDetailTwitter);
        description = (TextView) findViewById(R.id.beerDetailDescription);
        banner = (ImageView) findViewById(R.id.detailImage);
    }

    private void populateViews( BrewerInfo data){
        title.setText(data.getBrewery());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            description.setText(Html.fromHtml(data.getContent(),Html.FROM_HTML_MODE_LEGACY));
        } else {
            description.setText(Html.fromHtml(data.getContent()));
        }

        address.setText(data.getAddress());
        phone.setText(data.getPhone());
        instagramLink.setMovementMethod(LinkMovementMethod.getInstance());
        instagramLink.setText(data.getInstagram());
        facebookLink.setMovementMethod(LinkMovementMethod.getInstance());
        facebookLink.setText(data.getFacebook());
        twitterLink.setMovementMethod(LinkMovementMethod.getInstance());
        twitterLink.setText(data.getTwitter());
        Picasso.with(this).load(data.getDetailImages()).into(banner);

    }

    private void configGmap(Bundle savedInstanceState){
        mMapView = (MapView) findViewById(R.id.mapViewDetail);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(ctx.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                drawMap(mMap);
            }
        });
    }

    private void drawMap (GoogleMap  googleMap){
        BitmapDescriptor beerIcon = BitmapDescriptorFactory.fromResource(R.drawable.icon_chop);
        LatLng currentLatLng = new LatLng(data.getLatitude(), data.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(currentLatLng).title(data.getBrewery()).icon(beerIcon));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15.0f));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
