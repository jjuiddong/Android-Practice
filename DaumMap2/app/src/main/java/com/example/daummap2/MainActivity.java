package com.example.daummap2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.maps.helper.Utility.getPackageInfo;


public class MainActivity extends AppCompatActivity
        implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener {

    public static final String API_KEY = "3ea4a6853f6c601330e9762bc346e557";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String key = getKeyHash(getApplicationContext());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("hashkey");
        alertDialog.setMessage(key);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();

        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey(API_KEY);
        mapView.setOpenAPIKeyAuthenticationResultListener(this);
        mapView.setMapViewEventListener(this);
        //mapView.setMapType(MapView.MapType.Standard);
        //setContentView(mapView);

        RelativeLayout container = (RelativeLayout ) findViewById(R.id.map_view);
        container.addView(mapView);

        int result0 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if ( PackageManager.PERMISSION_GRANTED == result1)
        {
            int a = 0;
        }
        else
        {
            int a = 0;
        }
    }

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                //Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }



    @Override
    public void onMapViewInitialized(MapView mapView) {
        //Log.i(LOG_TAG, "MapView had loaded. Now, MapView APIs could be called safely");
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.537229,127.005515), 2, true);
    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int resultCode, String resultMessage) {
//        Log.i(LOG_TAG,	String.format("Open API Key Authentication Result : code=%d, message=%s", resultCode, resultMessage));
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("result = "+resultCode);
        alertDialog.setMessage("");//String.format("Double-Tap on (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
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
