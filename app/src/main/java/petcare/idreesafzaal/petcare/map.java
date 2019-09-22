package petcare.idreesafzaal.petcare;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class map extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private static final CharSequence[] MAP_TYPE_ITEMS =
            {"Road Map", "Hybrid", "Satellite", "Terrain"};
    private GoogleMap mMap;


    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/latlag.php";
    String appurl = "http://" + host.api + "/andriodfiles/ownerappointmentrequest.php";
    String urlgetmarkerprofile = "http://" + host.api + "/andriodfiles/getdataonclicklatlag.php";
    LocationManager locationManager;
    private final static int REQUEST_CALL = 1;
    LatLng currentLocation;
    Location location;
    String locality;
    float distance;
    String number;
    JSONArray data;
    double lat, lng, latitude, langtiude, clicklat, clicklong;
    List<LatLng> listpoint;
    private List<Polyline> polylines;
    TextView name, info, adrees, id;
    Button call, appoitment;
    ImageView imageView;
    String doctorname, doctorinfo, doctoraddress, doctorimage, phonenumber;
    private DatePickerDialog.OnDateSetListener listener;
    int cuurentmonth, day, currentyear;
    String petownerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listpoint = new ArrayList<>();
        polylines = new ArrayList<>();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {


            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                // Permission already Granted
                //Do your work here
                //Perform operations here only which requires permission

                location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

                onLocationChanged(location);
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                onLocationChanged(location);
            }
        } catch (Exception e) {
        }
        // showMapTypeSelectorDialog();
    }


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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Add a marker in Sydney and move the camera
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng sydney = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title("you location" + locality).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            data = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String lt = jsonObject.getString("lat");
                                String lg = jsonObject.getString("long");

                                if (!lg.isEmpty() && !lt.isEmpty()) {
                                    latitude = Double.valueOf(lt);
                                    langtiude = Double.valueOf(lg);
                                    Location currentLocation = new Location("currentlocation");
                                    currentLocation.setLatitude(lat);
                                    currentLocation.setLongitude(lng);


                                    Location newLocation = new Location("newlocation");
                                    newLocation.setLatitude(latitude);
                                    newLocation.setLongitude(langtiude);
                                    distance = currentLocation.distanceTo(newLocation) / 1000;
                                    // Log.d("distane",String.valueOf(dist));

                                }
                                if (distance <= 15) {

                                    LatLng sydney = new LatLng(latitude, langtiude);

                                    mMap.addMarker(new MarkerOptions().position(sydney).title("doctor is here").icon(BitmapDescriptorFactory.fromResource(R.drawable.newdocicon)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

        };
        mysingleton.getmInstance(this).addtorequestque(stringRequest);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {

                if (arg0 != null && arg0.getTitle().equals(arg0.getTitle().toString()))
                    ; // if marker  source is clicked
                Toast.makeText(map.this, arg0.getTitle(), Toast.LENGTH_LONG).show();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude), 16.0f));
                //Polyline polyline = googleMap.addPolyline(new PolylineOptions().add(new LatLng(lat, lng))
                //  new LatLng(Marker.getPosition().latitude, Marker.getPosition().longitude)).width(2).color(Color.RED).geodesic(true);
                LatLng doctor = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);
                clicklat = arg0.getPosition().latitude;
                clicklong = arg0.getPosition().longitude;
                LatLng currentuser = new LatLng(lat, lng);
                //  LatLng point2=new LatLng(a,b);
                //getRouteToMarker(point1,point2);
                //listpoint.add(point1);
                // listpoint.add(point2);
                if (clicklat != lat && clicklong != lng) {
                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, urlgetmarkerprofile,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                                        doctorname = jsonObject.getString("name").toString();
                                        doctoraddress = jsonObject.getString("address").toString();
                                        String doctorinfo = jsonObject.getString("info").toString();
                                        doctorimage = jsonObject.getString("image");
                                        number = jsonObject.getString("contact");
                                        final String doctorid = jsonObject.getString("doctorid");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(map.this);
                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        View view = inflater.inflate(R.layout.layoutforrecycleviewappointment, null);
                                        imageView = (ImageView) view.findViewById(R.id.img1doc);
                                        if (doctorimage.equals("")) {

                                        } else {
                                            byte[] imageBytes = Base64.decode(doctorimage, Base64.DEFAULT);
                                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                            imageView.setImageBitmap(decodedImage);
                                        }


                                        name = (TextView) view.findViewById(R.id.docname);
                                        name.setText(doctorname);
                                        adrees = (TextView) view.findViewById(R.id.docaddress);
                                        adrees.setText(doctoraddress);
                                        info = (TextView) view.findViewById(R.id.docinfo);
                                        info.setText(doctorinfo);
                                        id = (TextView) view.findViewById(R.id.doctorid);
                                        id.setText(doctorid);
                                        call = (Button) view.findViewById(R.id.callBtn);
                                        appoitment = (Button) view.findViewById(R.id.appBtn);


                                        appoitment.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final Button dateBtn, continueBtn;
                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(map.this);
                                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                View view = inflater.inflate(R.layout.pickappointmentdate, null);
                                                dateBtn = (Button) view.findViewById(R.id.datepickerBtn);
                                                dateBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Calendar calendar = Calendar.getInstance();
                                                        day = calendar.get(Calendar.DAY_OF_MONTH);
                                                        cuurentmonth = calendar.get(Calendar.MONTH);
                                                        currentyear = calendar.get(Calendar.YEAR);
                                                        DatePickerDialog dialog = new DatePickerDialog(map.this,
                                                                android.R.style.Theme_Holo_Dialog_MinWidth, listener,
                                                                currentyear, cuurentmonth, day);
                                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                        dialog.show();
                                                    }
                                                });
                                                listener = new DatePickerDialog.OnDateSetListener() {
                                                    @Override
                                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                        month = month + 1;
                                                        if (dayOfMonth >= day && month >= cuurentmonth && year == currentyear) {

                                                            String date = dayOfMonth + "/" + month + "/" + year;
                                                            dateBtn.setText(date);
                                                        } else {
                                                            Toast.makeText(map.this, "Select valid date"
                                                                    , Toast.LENGTH_LONG).show();
                                                            dateBtn.setText("");
                                                        }
                                                    }
                                                };


                                                continueBtn = (Button) view.findViewById(R.id.okaybtn);
                                                continueBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {


                                                        final String appointmentdate = dateBtn.getText().toString();
                                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(map.this);
                                                        petownerid = sharedPreferences.getString("userId", null);

                                                        if (appointmentdate.isEmpty()) {
                                                            dateBtn.setError("Pick date here");
                                                            dateBtn.requestFocus();
                                                            return;
                                                        }
                                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, appurl, new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                try {
                                                                    JSONArray jsonArray = new JSONArray(response);
                                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                                                    String message = jsonObject1.getString("message");
                                                                    if (message.equals("success")) {
                                                                        Toast.makeText(map.this, "Your request is send to doctor " +
                                                                                "you will get email after proceeding request", Toast.LENGTH_LONG).show();
                                                                    } else {
                                                                        Toast.makeText(map.this, "You have already send this request "
                                                                                , Toast.LENGTH_LONG).show();
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Toast.makeText(map.this, "Something went wrong check your internet connection "
                                                                        , Toast.LENGTH_LONG).show();
                                                            }
                                                        }) {
                                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                                Map<String, String> params = new HashMap<String, String>();
                                                                params.put("doctorid", doctorid);
                                                                params.put("ownerid", petownerid);
                                                                params.put("appdate", appointmentdate);
                                                                return params;
                                                            }
                                                        };
                                                        mysingleton.getmInstance(map.this).addtorequestque(stringRequest);

                                                    }
                                                });
                                                builder1.setView(view);
                                                builder1.show();

                                            }
                                        });


                                        call.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(map.this);
                                                builder.setTitle("Call to docotor");
                                                builder.setMessage("If you want to call press ok" + number);
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {


                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        makephonecall();

                                                    }
                                                });
                                                builder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                builder.show();
                                            }
                                        });
                                        builder.setView(view);
                                        builder.show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("lat", String.valueOf(clicklat));
                            params.put("long", String.valueOf(clicklong));
                            return params;
                        }
                    };
                    mysingleton.getmInstance(map.this).addtorequestque(stringRequest1);


//                PolylineOptions rectOptions = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
//                        rectOptions.add(new LatLng(lat, lng));
//                        rectOptions.add(new LatLng(a, b));
//                mMap.addPolyline(rectOptions);

                    String url = getRequestUrl(doctor, currentuser);
                    TaskRequestDirection taskRequestDirection = new TaskRequestDirection();
                    taskRequestDirection.execute(url);

//                Intent intent = new Intent(map.this, docapp.class);
//                startActivity(intent);
                    // display toast
                } else {
                    Toast.makeText(map.this, "this is your current locaton", Toast.LENGTH_LONG).show();
                }
                return true;

            }

        });

    }

    public void makephonecall() {
        if (ContextCompat.checkSelfPermission(map.this,
                Manifest.permission.CALL_PHONE
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(map.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            try {


                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number.trim()));
                startActivity(intent);
            } catch (Exception e) {

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makephonecall();
            } else {
                Toast.makeText(map.this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getRequestUrl(LatLng orign, LatLng dest) {
        String str_orgn = "origin=" + orign.latitude + "," + orign.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        //enable sensor
        String sensor = "sensor=false";
        // find direction
        String mode = "mode=driving";
        //ful param
        String key = "key=AIzaSyDPomwB1Hr6E3rxn5Ab0dpZecL0XxgI4OE";
       //key=AIzaSyDPomwB1Hr6E3rxn5Ab0dpZecL0XxgI4OE
        String param = str_orgn + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;
        //outpot format
        String output = "json";
        //build request url
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }

    private String requestDirection(String regUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(regUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //responxe

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {

                stringBuffer.append(line);
            }
            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    public void showmessage() {
        Toast.makeText(this, "there is not doctor in this area", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        Geocoder gc = new Geocoder(map.this, Locale.getDefault());
        try {
            List<Address> list = gc.getFromLocation(lat, lng, 1);
            if (list != null && list.size() > 0) {
                Address address = list.get(0);
                locality = address.getAddressLine(0);
            } else {
                Toast.makeText(map.this, "nothing", Toast.LENGTH_LONG).show();
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

    private void showMapTypeSelectorDialog() {
        // Prepare the dialog by setting up a Builder.
        final String fDialogTitle = "Select Map Type";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(fDialogTitle);

        // Find the current map type to pre-check the item representing the current state.
        // int checkItem = mMap.getMapType()-1 ;
        int checkItem = 1;
        // Add an OnClickListener to the dialog, so that the selection will be handled.
        builder.setSingleChoiceItems(
                MAP_TYPE_ITEMS,
                checkItem,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {
                        // Locally create a finalised object.

                        // Perform an action depending on which item was selected.
                        switch (item) {
                            case 1:
                                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;
                            case 2:
                                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;
                            case 3:
                                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                break;
                            default:
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        }
                        dialog.dismiss();
                    }
                }
        );

        // Build the dialog and show it.
        AlertDialog fMapTypeDialog = builder.create();
        fMapTypeDialog.setCanceledOnTouchOutside(true);
        fMapTypeDialog.show();

    }


    public class TaskRequestDirection extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseSting = "";
            try {
                responseSting = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseSting;
        }

        @Override
        protected void onPostExecute(String s) {
            TaskPraser taskPraser = new TaskPraser();
            taskPraser.execute(s);
        }
    }

    public class TaskPraser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            ArrayList points = null;
            PolylineOptions polylineOptions = null;
            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();
                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));
                    points.add(new LatLng(lat, lon));
                }
                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);

            }
            if (polylineOptions != null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "direction not found", Toast.LENGTH_LONG).show();
            }

        }
    }
}
