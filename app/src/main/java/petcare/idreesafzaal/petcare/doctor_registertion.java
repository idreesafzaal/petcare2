package petcare.idreesafzaal.petcare;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.hbb20.CountryCodePicker;
import com.scrounger.countrycurrencypicker.library.Buttons.CountryCurrencyButton;
import com.scrounger.countrycurrencypicker.library.Country;
import com.scrounger.countrycurrencypicker.library.Currency;
import com.scrounger.countrycurrencypicker.library.Listener.CountryCurrencyPickerListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class doctor_registertion extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    EditText doc_name, doc_email, doc_info, med_lisence, doc_password, doc_number;
    CountryCodePicker ccp;
    AppCompatAutoCompleteTextView doc_address;
    String number, name, info, password, email, med_liecense, doc_country, address;
    boolean Registered;
    Button regbtn;
    CountryCurrencyButton button;
    double lat;
    double lng;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    private PlaceAutocompleteAdapter mplaceAutocompleteAdapter;
    private GoogleApiClient mgoogleapiclient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136)
    );
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_doctor_registertion);
        doc_name = (EditText) findViewById(R.id.docname);
        doc_email = (EditText) findViewById(R.id.docemail);
        doc_info = (EditText) findViewById(R.id.doctor_info);
        med_lisence = (EditText) findViewById(R.id.medical_linces);
        doc_password = (EditText) findViewById(R.id.docpassword);
        doc_number = (EditText) findViewById(R.id.docnumber);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(doc_number);
        doc_address = (AppCompatAutoCompleteTextView) findViewById(R.id.address);
        regbtn = (Button) findViewById(R.id.doc_regBtn);

        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mgoogleapiclient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API).enableAutoManage(this, this).build();
        mplaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, LAT_LNG_BOUNDS, null);
        doc_address.setAdapter(mplaceAutocompleteAdapter);

        doc_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//       // CountryCurrencyButton button = (CountryCurrencyButton) findViewById(R.id.country);
//        button.setOnClickListener((CountryCurrencyPickerListener) doctor_registertion.this);
//        button.setCountry("DE");
//        button.setShowCurrency(false);
        button = (CountryCurrencyButton) findViewById(R.id.country);
        doc_country = button.getCountry().getName().toString();
        button.setOnClickListener(new CountryCurrencyPickerListener() {
            @Override
            public void onSelectCountry(Country country) {
                if (country.getCurrency() == null) {
                    doc_country = country.getName();
                    Toast.makeText(doctor_registertion.this,
                            String.format("name: %s\ncode: %s", country.getName(), country.getCode())
                            , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(doctor_registertion.this,
                            String.format("name: %s\ncurrencySymbol: %s", country.getName(), country.getCurrency().getSymbol())
                            , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSelectCurrency(Currency currency) {

            }
        });
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                validateField();


            }
        });
    }

    public void validateField() {
        name = doc_name.getText().toString().toLowerCase();
        email = doc_email.getText().toString().toLowerCase();
        info = doc_info.getText().toString();
        //number=doc_number.getText().toString();
        password = doc_password.getText().toString().toLowerCase();
        med_liecense = med_lisence.getText().toString();
        address = doc_address.getText().toString();
        Geocoder gc = new Geocoder(doctor_registertion.this, Locale.getDefault());
        try {
            List<Address> list = gc.getFromLocationName(address, 1);
            if (list != null && list.size() > 0) {
                Address address = list.get(0);
                String locality = address.getAddressLine(0);
                Toast.makeText(doctor_registertion.this, locality, Toast.LENGTH_LONG).show();
                lat = address.getLatitude();
                lng = address.getLongitude();

            } else {
                Toast.makeText(doctor_registertion.this, "nothing", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        number = ccp.getFullNumberWithPlus();
        if (name.isEmpty()) {

            doc_name.setError("Enter name");
            doc_name.requestFocus();
            return;

        }
        if (email.isEmpty() || !email.matches(emailPattern)) {
            doc_email.setError("Enter email");
            doc_email.requestFocus();
            return;

        }
        if (info.isEmpty()) {
            doc_info.setError("Enter info");
            doc_info.requestFocus();
            return;
        }
        if (number.isEmpty()) {
            doc_number.setError("Enter number");
            doc_number.requestFocus();
            return;
        }
        if (address.isEmpty()) {
            doc_address.setError("Enter clinic address");
            doc_address.requestFocus();
            return;
        }
        if (med_liecense.isEmpty()) {
            med_lisence.setError("Enter medical license");
            med_lisence.requestFocus();
            return;
        }
        if (password.isEmpty() || password.length() < 6 || password.length() > 12) {
            doc_password.setError("password is required ");
            doc_password.requestFocus();
            return;
        }

        // number=ccp.getFullNumberWithPlus();
        Intent intent = new Intent(doctor_registertion.this, phoneverification.class);
        intent.putExtra("name", name);
        intent.putExtra("doctor", "2");
        intent.putExtra("email", email);
        intent.putExtra("info", info);
        intent.putExtra("password", password);
        intent.putExtra("number", number);
        intent.putExtra("medical_licence", med_liecense);
        intent.putExtra("address", address);
        intent.putExtra("lat", String.valueOf(lat));
        intent.putExtra("lng", String.valueOf(lng));
        intent.putExtra("country", doc_country);
        startActivity(intent);


    }

    @Override
    protected void onStart() {
        super.onStart();
//       final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//       Registered = sharedPref.getBoolean("doctorlogedin", false);
//
//        if (Registered == true) {
//            Intent intent = new Intent(doctor_registertion.this, doctorHome.class);
//           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
//           startActivity(intent);
//        }
    }

}
