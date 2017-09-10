package com.example.daummaptrack;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.kakao.util.maps.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity
        implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener {

    public static final String API_KEY = "78973054b72e9beacfa889dd92abbf74";
    LocationManager mLocMan;
    MapView mMapView;
    LocationListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = new MapView(this);
        mMapView.setDaumMapApiKey(API_KEY);
        mMapView.setOpenAPIKeyAuthenticationResultListener(this);
        mMapView.setMapViewEventListener(this);
        //mapView.setMapType(MapView.MapType.Standard);
        //setContentView(mapView);

        RelativeLayout container = (RelativeLayout ) findViewById(R.id.map_view);
        container.addView(mMapView);

         mLocMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        List<String> arProvider = mLocMan.getProviders(false);
        String result = "";
        for (int i=0; i < arProvider.size(); i++) {
            result += ("Provider " + i + " : " + arProvider.get(i) + "\n");
        }

        mListener = new LocationListener() {
             int circleTag = 1000;

            @Override
            public void onLocationChanged(Location location) {
                mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(location.getLatitude(),location.getLongitude())
                        , 2, true);

                MapCircle circle1 = new MapCircle(
                        MapPoint.mapPointWithGeoCoord(location.getLatitude(),location.getLongitude()),
                        5, // radius
                        Color.argb(128, 255, 0, 0), // strokeColor
                        Color.argb(128, 0, 255, 0) // fillColor
                );

                circle1.setTag(circleTag++);
                mMapView.addCircle(circle1);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Toast.makeText(MainActivity.this, "onStatusChanged " + s, Toast.LENGTH_SHORT);
            }

            @Override
            public void onProviderEnabled(String s) {
                Toast.makeText(MainActivity.this, "onProviderEnabled " + s, Toast.LENGTH_SHORT);
            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(MainActivity.this, "onProviderDisabled " + s, Toast.LENGTH_SHORT);
            }
        };

    }


    public void onResume() {
        super.onResume();

        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.NO_REQUIREMENT);
        crit.setPowerRequirement(Criteria.NO_REQUIREMENT);
        crit.setAltitudeRequired(false);
        crit.setCostAllowed(false);
        String best = mLocMan.getBestProvider(crit, true);

        mLocMan.requestLocationUpdates(best, 3000, 3, mListener);
    }


    @Override
    public void onMapViewInitialized(MapView mapView) {
        //Log.i(LOG_TAG, "MapView had loaded. Now, MapView APIs could be called safely");
        //mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        //mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.537229,127.005515), 2, true);
    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int resultCode, String resultMessage) {
//        Log.i(LOG_TAG,	String.format("Open API Key Authentication Result : code=%d, message=%s", resultCode, resultMessage));
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//        alertDialog.setTitle("result = "+resultCode);
//        alertDialog.setMessage("");//String.format("Double-Tap on (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
//        alertDialog.setPositiveButton("OK", null);
//        alertDialog.show();
//        System.out.println("resultCode"+resultCode);
    }


    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapCenterPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapCenterPoint.getMapPointGeoCoord();
        //Log.i(LOG_TAG, String.format("MapView onMapViewCenterPointMoved (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("DaumMapLibrarySample");
        alertDialog.setMessage(String.format("Double-Tap on (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("DaumMapLibrarySample");
        alertDialog.setMessage(String.format("Long-Press on (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
//        Log.i(LOG_TAG, String.format("MapView onMapViewSingleTapped (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));

        Toast.makeText(MainActivity.this, "onMapViewSingleTapped", Toast.LENGTH_SHORT);
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
//        Log.i(LOG_TAG, String.format("MapView onMapViewDragStarted (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
//        Log.i(LOG_TAG, String.format("MapView onMapViewDragEnded (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
//        Log.i(LOG_TAG, String.format("MapView onMapViewMoveFinished (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {
//        Log.i(LOG_TAG, String.format("MapView onMapViewZoomLevelChanged (%d)", zoomLevel));
    }

}
