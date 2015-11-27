package aengelus.apps.gps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener {

    // Location manager including
    LocationManager locationManager;
    TextView positionTextView;
    TextView limit;
    RelativeLayout myLayout;
    // global variable to store the maxSpeed
    double maxSpeed;
    // global variable to store the value of the numberPicker
    int pickedNumber;
    NumberPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        positionTextView = (TextView) findViewById(R.id.locationTextView);
        // Stores the view of the speedLimit textView into a variable
        limit = (TextView) findViewById(R.id.speedlimit);
        myLayout = (RelativeLayout) findViewById(R.id.layout);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        maxSpeed = 0;

        picker = (NumberPicker) findViewById(R.id.numberPicker);
        picker.setMinValue(5);
        pickedNumber = picker.getMinValue();
        picker.setMaxValue(100);
        picker.setWrapSelectorWheel(true);
        // This will set the NumberPicker
        limit.setText("" + pickedNumber);

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                pickedNumber = newVal;
                limit.setText("" + pickedNumber);
                //findViewById(R.id.Speedlimit).setBackgroundColor(getResources().getColor(R.color.red, null));

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Network provider is not accurate, GPS is more accurate but not available in rooms
        // GPS has a accurance from 2m
        // in the cradle file is a possibility that you can include the earlier level of the api, than the feature is available

        // this part is needed starting with android M, due to the possibility that you can turn off permissions for the apps
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        }


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            locationManager.removeUpdates(this);
        } catch (SecurityException e){
            e.printStackTrace();
            Log.e("GeoApp", e.getMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if(location.getSpeed() > maxSpeed){
            maxSpeed = location.getSpeed();
        }

        if((location.getSpeed() * 3.6) > pickedNumber){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                myLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundFast,null));
            } else {
                myLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundFast));
            }
        } else {
            myLayout.setBackgroundColor(getResources().getColor(R.color.colorBackground));
        }

        positionTextView.setText(
                "Provider " + location.getProvider() + "\n" +
                "lat: "+ location.getLatitude() + "\n" +
                "lon: " + location.getLongitude() + "\n" +
                "Speed:  " + location.getSpeed() * 3.6 + " km/h \n" +
                "MaxSpeed:  " + maxSpeed *3.6+ " km/h \n" +
                "Höhe: " + location.getAltitude() + "\n");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
