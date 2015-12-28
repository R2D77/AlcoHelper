package com.fd2r.alcohelper;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // --- https://android-arsenal.com/details/1/210 ---

    private GoogleMap mMap;

    String link = null;
    private LocationManager locationManager;
    double Lat;
    double startLatitude;
    double finishLatitude;
    double Lon;
    double startLongitude;
    double finishLongitude;
    String geo_address, geo_city, geo_state, geo_country;
    SharedPreferences sPref;
    final String TAXI_NUMBER = "taxi number";
    float boozLevel = (float) 0;

    Fragment_main fragmentMain;
    Fragment_alco fragmentAlco;
    FragmentTransaction fTrans;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fragmentMain = new Fragment_main();

        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.fragment, fragmentMain);
        fTrans.commit();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
            alertDialog.setTitle("Check GPS settings");
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location.getAccuracy() <= 2000) {    //!!!!!!!!!!!!!!!!!!
                Lat = location.getLatitude();
                Lon = location.getLongitude();

                Fragment fragment1;
                fragment1 = getSupportFragmentManager().findFragmentById(R.id.fragment);
                ((Button) fragment1.getView().findViewById(R.id.bEntryPoint)).setEnabled(true);
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void closeDrink(View view) {
        fragmentMain = new Fragment_main();
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment, fragmentMain);
        fTrans.commit();
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            fm.popBackStack();
        }
    }

    public void boozCalc(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        EditText etMl = (EditText) findViewById(R.id.etMl);
        float shot = (float) 0;

        if (etMl.getText().toString().trim().length() != 0) {
            shot = Float.parseFloat(etMl.getText().toString());
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        switch (spinner.getSelectedItem().toString()) {
            case "Beer":
                shot = (float) (shot * 0.046);
                break;
            case "Vine":
                shot = (float) (shot * 0.16);
                break;
            case "Rum":
                shot = (float) (shot * 0.42);
                break;
        }

        boozLevel = boozLevel + shot;
        closeDrink(view);

        if (boozLevel > 100.0) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
            alertDialog.setTitle("STOP");
            alertDialog.setMessage("You are drunk. Call taxi and go home");
            alertDialog.setPositiveButton("Call screen", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent;
                    intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + loadTaxiNumber()));
                    startActivity(intent);
                }
            });
            alertDialog.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.fd2r.alcohelper/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.fd2r.alcohelper/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    public class SendApi extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            finishLatitude = 50.021817;
            finishLongitude = 19.827423;

            String www = "https://maps.googleapis.com/maps/api/directions/json";
            String origin = "?origin=" + startLatitude + "," + startLongitude;
            String dest = "&destination=" + finishLatitude + "," + finishLongitude;
            String mode = "&mode=walking";
            String key = "&key=" + getString(R.string.direction_api_key);

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(www + origin + dest + mode + key);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                link = buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(link);
        }
    }

    public class AddressEP extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

            do {
                try {
                    addresses = geocoder.getFromLocation(startLatitude, startLongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    geo_address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    geo_city = addresses.get(0).getLocality();
                    geo_state = addresses.get(0).getAdminArea();
                    geo_country = addresses.get(0).getCountryName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (geo_country.equals(null));

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            LatLng latLng = new LatLng(startLatitude, startLongitude);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(geo_address);
            mMap.addMarker(markerOptions);
            //tvDir.setText(geo_address + "\n" + geo_city + "\n" + geo_state + "\n" + geo_country);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option1:
                Intent intent;
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + loadTaxiNumber()));
                startActivity(intent);
                break;

            case R.id.option2:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
                alertDialog.setTitle(R.string.entry_point);
                alertDialog.setMessage(geo_address + "\n" + geo_city + "\n" + geo_state + "\n" + geo_country);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                break;

            case R.id.option3:
                AlertDialog.Builder editTaxiNumber = new AlertDialog.Builder(this);
                editTaxiNumber.setTitle("Edit taxi number");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                input.setText(loadTaxiNumber());
                editTaxiNumber.setView(input);
                editTaxiNumber.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!input.getText().toString().equals("")) {
                            saveTaxiNumber(input.getText().toString());
                        }
                    }
                });
                editTaxiNumber.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                editTaxiNumber.show();
                break;
            case R.id.option4:
                fragmentAlco = new Fragment_alco();
                fTrans = getSupportFragmentManager().beginTransaction();
                fTrans.replace(R.id.fragment, fragmentAlco);
                fTrans.addToBackStack(null);
                fTrans.commit();



                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String loadTaxiNumber() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString(TAXI_NUMBER, "");
        return savedText;
    }

    void saveTaxiNumber(String inph) {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(TAXI_NUMBER, inph);
        ed.commit();
    }

    public void fromFragmentEP() {
        AddressEP adr = new AddressEP();
        adr.execute();
        startLatitude = Lat;
        startLongitude = Lon;
    }

    public void fromFragmentBMB() {
        SendApi sendApi = new SendApi();
        sendApi.execute();
        finishLatitude = Lat;
        finishLongitude = Lon;
    }

}