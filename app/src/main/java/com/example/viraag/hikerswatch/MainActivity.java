package com.example.viraag.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
           startListening();
            }
    }
    public void startListening()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        }

    }

    public void updateLocationInfo(Location location)
    {
        Log.i("location info", location.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
                TextView latTextView=findViewById(R.id.latTextView);
                TextView lonTextView=findViewById(R.id.lonTextView);
                TextView accTextView=findViewById(R.id.accTextView);
                TextView altTextView=findViewById(R.id.altTextView);
                TextView addTextView=findViewById(R.id.addresTextView);
                latTextView.setText("Latitude:" +location.getLatitude());
                lonTextView.setText("Longitude:" +location.getLongitude());
                altTextView.setText("Altitude:" +location.getAltitude());
                accTextView.setText("Accuracy:" +location.getAccuracy());
                Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
                try {
                    String address="Address:";
                    List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                   if(addressList!=null && addressList.size()>0) {
                       Log.i("Place ifo", addressList.get(0).toString());

                       if( addressList.get(0).getFeatureName()!=null)
                       {
                           address+=addressList.get(0).getFeatureName()+ "\n";
                       }
                       if( addressList.get(0).getSubAdminArea()!=null)
                       {
                           address+=addressList.get(0).getSubAdminArea()+ "\n";
                       }
                       if( addressList.get(0).getAdminArea()!=null)
                       {
                           address+=addressList.get(0).getAdminArea()+ "\n";
                       }
                       if( addressList.get(0).getCountryName()!=null)
                       {
                           address+=addressList.get(0).getCountryName()+ "\n";
                       }
                       addTextView.setText(address);
                   }
                   } catch (IOException e) {
                    e.printStackTrace();
                }
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
        };
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
       }
       else {
           if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
           {
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

           }
           else {
               locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
               Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
               if(location!=null)
               {
                   updateLocationInfo(location);
               }
           }
       }

    }
}
