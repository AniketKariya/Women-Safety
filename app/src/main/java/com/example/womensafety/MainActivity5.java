package com.example.womensafety;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.mapviewlite.MapScene;
import com.here.sdk.mapviewlite.MapStyle;
import com.here.sdk.mapviewlite.MapViewLite;

public class MainActivity5 extends AppCompatActivity {

    private MapViewLite mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        // Get a MapViewLite instance from the layout.
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        loadMapScene();
    }

    private void loadMapScene() {
        // Load a scene from the SDK to render the map with a map style.
        mapView.getMapScene().loadScene(MapStyle.NORMAL_DAY, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapScene.ErrorCode errorCode) {
                if (errorCode == null) {

                    Intent intent = getIntent();
                    double lat = intent.getDoubleExtra("lat", 0);
                    double longitude = intent.getDoubleExtra("long", 0);

                    mapView.getCamera().setTarget(new GeoCoordinates(lat, longitude));
                    mapView.getCamera().setZoomLevel(14);

//                    MapImage mapImage = MapImageFactory.fromResource(MainActivity5.this.getResources(), R.drawable.ic_help);
//                    MapMarker mapMarker = new MapMarker(new GeoCoordinates(lat, longitude));
//                    mapMarker.addImage(mapImage, new MapMarkerImageStyle());
//                    mapView.getMapScene().addMapMarker(mapMarker);
                } else {
                    Log.d("Map", "onLoadScene failed: " + errorCode.toString());
                }
            }
        });
    }

}