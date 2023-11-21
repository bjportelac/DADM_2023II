package com.dadm.gpsmaps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private MapView map = null;
    private TextView radiusTextView;
    private SeekBar Bar;
    GeoPoint myLocation;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);

        CustomZoomButtonsController.Visibility visibility = CustomZoomButtonsController.Visibility.NEVER;
        map.getZoomController().setVisibility(visibility);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(18.0);

        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx),map);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableFollowLocation();

        mLocationOverlay.runOnFirstFix(() -> {
            myLocation = mLocationOverlay.getMyLocation();
            mapController.setCenter(myLocation);
        });
        map.getOverlays().add(mLocationOverlay);

        getNecessaryPermissions(new String[] {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        });

        Bar = findViewById(R.id.radius);

        radiusTextView = findViewById(R.id.radius_val);
        radiusTextView.setText("Radius: "+Bar.getProgress() + "km");

        Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int val = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                val = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Nothing here yet
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                double pgr = (Bar.getMax() - val) + 10;
                radiusTextView.setText("Radius: "+ val + "km");
                mapController.setZoom(pgr);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>(Arrays.asList(permissions).subList(0, grantResults.length));
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), 1);
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getNecessaryPermissions(String [] permissions){
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for(String s: permissions){
            if (ContextCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED){
                permissionsToRequest.add(s);
            }
        }

        if(permissionsToRequest.size() > 0){
            ActivityCompat.requestPermissions(this,permissionsToRequest.toArray(new String[0]),1);
        }
    }

}