package com.example.daummaptrack;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.kakao.util.maps.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity
        implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener {

    public static final String API_KEY = "78973054b72e9beacfa889dd92abbf74";
    LocationManager mLocMan;
    MapView mMapView;
    MapPolyline mPolyline;
    LocationListener mListener;
    int mCircleTag = 1000;
    SharedPreferences mConfig;
    int mFileId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConfig = getSharedPreferences("Config", MODE_PRIVATE);
        mFileId = mConfig.getInt("file-id", 1);

        mMapView = new MapView(this);
        mMapView.setDaumMapApiKey(API_KEY);
        mMapView.setOpenAPIKeyAuthenticationResultListener(this);
        mMapView.setMapViewEventListener(this);
        //mapView.setMapType(MapView.MapType.Standard);
        //setContentView(mapView);

        RelativeLayout container = (RelativeLayout ) findViewById(R.id.map_view);
        container.addView(mMapView);

         mLocMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        mPolyline = new MapPolyline();
        mPolyline.setTag(1000);
        mPolyline.setLineColor(Color.argb(128, 255, 51, 0));
        mMapView.addPolyline(mPolyline);


        try {
            FileInputStream fis = openFileInput("TrackPos" + mFileId + ".txt");
            FileChannel inChannel = fis.getChannel();

            final int s = fis.available();
            ByteBuffer buff = ByteBuffer.allocate(fis.available());
            buff.clear();

            inChannel.read(buff);

            buff.rewind();

            final int posSize = s/8;
            double[] pos = new double[posSize];
            buff.asDoubleBuffer().get(pos);

            inChannel.close();

            for (int i=0; i < posSize; i+=2)
            {
                mPolyline.addPoint(MapPoint.mapPointWithGeoCoord(pos[i], pos[i+1]));

                MapCircle circle1 = new MapCircle(
                        MapPoint.mapPointWithGeoCoord(pos[i], pos[i+1]), // center
                        3, // radius
                        Color.argb(128, 255, 0, 0), // strokeColor
                        Color.argb(128, 0, 255, 0) // fillColor
                );
                circle1.setTag(mCircleTag++);
                mMapView.addCircle(circle1);
            }

            fis.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        Snackbar.make(mMapView, "Open Track File" + mFileId, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();


        mListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(location.getLatitude(),location.getLongitude())
                        , mMapView.getZoomLevel(), true);

                mPolyline.addPoint(MapPoint.mapPointWithGeoCoord(location.getLatitude(),location.getLongitude()));
                mMapView.removePolyline(mPolyline);
                mMapView.addPolyline(mPolyline);

                MapCircle circle1 = new MapCircle(
                        MapPoint.mapPointWithGeoCoord(location.getLatitude(),location.getLongitude()), // center
                        3, // radius
                        Color.argb(128, 255, 0, 0), // strokeColor
                        Color.argb(128, 0, 255, 0) // fillColor
                );
                circle1.setTag(mCircleTag++);
                mMapView.addCircle(circle1);

                Snackbar.make(mMapView, "Receive Location Count = " + mPolyline.getPointCount(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                try {
                    FileOutputStream fos = openFileOutput ("TrackPos" + mFileId + ".txt", Context.MODE_APPEND | Context.MODE_WORLD_READABLE);
                    FileChannel outChannel = fos.getChannel();

                    double[] pos = new double[2];
                    pos[0] = location.getLatitude();
                    pos[1] = location.getLongitude();

                    ByteBuffer buff = ByteBuffer.allocate(8*2);
                    buff.clear();
                    buff.asDoubleBuffer().put(pos);

                    outChannel.write(buff);
                    outChannel.close();
                    fos.close();

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
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

        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.NO_REQUIREMENT);
        crit.setPowerRequirement(Criteria.NO_REQUIREMENT);
        crit.setAltitudeRequired(true);
        crit.setCostAllowed(false);
        String best = mLocMan.getBestProvider(crit, true);
        mLocMan.requestLocationUpdates(best, 20000, 30, mListener);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.clearButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Next Track " + (mFileId+1), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                mMapView.removeAllCircles();
                mMapView.removePolyline(mPolyline);
                mPolyline = new MapPolyline(); // clear line
                mMapView.addPolyline(mPolyline);

                // next track file
                mFileId += 1;

                SharedPreferences.Editor edit = mConfig.edit();
                edit.putInt("file-id", mFileId);
                edit.apply();
                edit.commit();

                //String dir = getFilesDir().getAbsolutePath();
                //File f0 = new File(dir, "TrackPos1.txt");
                //f0.delete();
            }
        });
    }


    public void onResume() {
        super.onResume();
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

//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//        alertDialog.setTitle("DaumMapLibrarySample");
//        alertDialog.setMessage(String.format("Double-Tap on (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
//        alertDialog.setPositiveButton("OK", null);
//        alertDialog.show();
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
