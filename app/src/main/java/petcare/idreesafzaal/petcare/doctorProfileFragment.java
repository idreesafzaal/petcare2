package petcare.idreesafzaal.petcare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.hbb20.CountryCodePicker;
import com.scrounger.countrycurrencypicker.library.Buttons.CountryCurrencyButton;
import com.scrounger.countrycurrencypicker.library.Currency;
import com.scrounger.countrycurrencypicker.library.Listener.CountryCurrencyPickerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;

public class doctorProfileFragment extends Fragment {
    String imagename;
    localhost host = new localhost();
    byte[] imageBytes;
    Animation animation;
    int IMG_REQUEST = 1;
    private Bitmap bitmap;
    TextView name, email, info, number, country, address, med, password;
    Button updatebtn;
    String doc_id, imagedoc;
    String updateurl = "http://" + host.api + "/andriodfiles/updatedoctorprofile.php";
    String url = "http://" + host.api + "/andriodfiles/searchDoctor.php";
    ImageView imageView;
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
    Date currentLocalTime = cal.getTime();
    DateFormat date = new SimpleDateFormat("HH:mm a");


    String localTime = date.format(currentLocalTime);


    EditText doc_name, doc_email, doc_info, med_lisence, doc_password, doc_number;

    CountryCodePicker ccp;
    AppCompatAutoCompleteTextView doc_address;
    String updatedocnumber, updatedocname, updatedocinfo, updatedocpassword, updatedocemail, updatemed_liecense,
            updatedoc_country, updatedocaddress, Name, Email, Phone, Med, Info, Country, Address, Password;
    boolean Registered;
    Button regbtn;
    CountryCurrencyButton button;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    private PlaceAutocompleteAdapter mplaceAutocompleteAdapter;
    private GoogleApiClient mgoogleapiclient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136)
    );
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctorprofiledesign, container, false);
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            doc_id = sharedPreferences.getString("doctorId", null);
            updatebtn = (Button) view.findViewById(R.id.updatedocProfileBtn);
            name = (TextView) view.findViewById(R.id.docName);
            email = (TextView) view.findViewById(R.id.docEmail);
            info = (TextView) view.findViewById(R.id.docInfo);
            number = (TextView) view.findViewById(R.id.docNumber);
            country = (TextView) view.findViewById(R.id.docCountry);
            address = (TextView) view.findViewById(R.id.docAddress);
            med = (TextView) view.findViewById(R.id.docliscese);
            password = (TextView) view.findViewById(R.id.docpassword);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        imagedoc = jsonObject.getString("image");
                        Password = jsonObject.getString("password");
                        Name = jsonObject.getString("user_name");
                        name.setText(Name);
                        Email = jsonObject.getString("email");
                        email.setText(Email);
                        Phone = jsonObject.getString("phone");
                        number.setText(Phone);
                        Med = jsonObject.getString("med");
                        med.setText(Med);
                        Info = jsonObject.getString("info");
                        info.setText(Info);
                        Country = jsonObject.getString("country");
                        country.setText(Country);
                        Address = jsonObject.getString("address");
                        address.setText(Address);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "internet problem", Toast.LENGTH_LONG).show();
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("doctorid", doc_id);
                    return params;
                }
            };
            mysingleton.getmInstance(getContext()).addtorequestque(stringRequest);
        } catch (Exception e) {

        }

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {


                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view1 = layoutInflater.inflate(R.layout.doctorprofileupdatelayout, null);
                    regbtn = (Button) view1.findViewById(R.id.doc_regBtn);
                    regbtn.setEnabled(false);
                    regbtn.setBackgroundColor(Color.GRAY);
                    imageView = (ImageView) view1.findViewById(R.id.imge);
                    imageView.setClickable(true);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectImage();
                        }
                    });
                    doc_name = (EditText) view1.findViewById(R.id.docname);
                    doc_name.setText(Name);
                    doc_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            regbtn.setEnabled(true);
                            regbtn.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    doc_email = (EditText) view1.findViewById(R.id.docemail);
                    doc_email.setText(Email);
                    doc_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            regbtn.setEnabled(true);
                            regbtn.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    doc_info = (EditText) view1.findViewById(R.id.doctor_info);
                    doc_info.setText(Info);
                    doc_info.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            regbtn.setEnabled(true);
                            regbtn.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    med_lisence = (EditText) view1.findViewById(R.id.medical_linces);
                    med_lisence.setText(Med);
                    med_lisence.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            regbtn.setEnabled(true);
                            regbtn.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    doc_password = (EditText) view1.findViewById(R.id.docpassword);
                    doc_password.setText(Password);
                    doc_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            regbtn.setEnabled(true);
                            regbtn.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    doc_number = (EditText) view1.findViewById(R.id.docnumber);
                    doc_number.setText(Phone);
                    doc_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            regbtn.setEnabled(true);
                            regbtn.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });

                    if (imagedoc != null) {
                        imageBytes = Base64.decode(imagedoc, Base64.DEFAULT);
                        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        imageView.setImageBitmap(decodedImage);
                        animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotateimage);
                        imageView.setAnimation(animation);
                    }
                    doc_address = (AppCompatAutoCompleteTextView) view1.findViewById(R.id.address);
                    doc_address.setText(Address);
                    doc_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            regbtn.setEnabled(true);
                            regbtn.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    regbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            updatedocname = doc_name.getText().toString();
                            updatedocemail = doc_email.getText().toString();
                            updatedocinfo = doc_info.getText().toString();
                            final String imgname = doc_id;
                            //number=doc_number.getText().toString();
                            updatedocpassword = doc_password.getText().toString();
                            updatemed_liecense = med_lisence.getText().toString();
                            updatedocaddress = doc_address.getText().toString();
                            updatedocnumber = doc_number.getText().toString();

                            if (updatedocname.isEmpty()) {

                                doc_name.setError("Enter name");
                                doc_name.requestFocus();
                                return;

                            }
                            if (updatedocemail.isEmpty() || !updatedocemail.matches(emailPattern)) {
                                doc_email.setError("Enter email");
                                doc_email.requestFocus();
                                return;

                            }
                            if (updatedocinfo.isEmpty()) {
                                doc_info.setError("Enter info");
                                doc_info.requestFocus();
                                return;
                            }
                            if (updatedocnumber.isEmpty()) {
                                doc_number.setError("Enter number");
                                doc_number.requestFocus();
                                return;
                            }
                            if (updatedocaddress.isEmpty()) {
                                doc_address.setError("Enter clinic address");
                                doc_address.requestFocus();
                                return;
                            }
                            if (updatemed_liecense.isEmpty()) {
                                med_lisence.setError("Enter medical license");
                                med_lisence.requestFocus();
                                return;
                            }
                            if (updatedocpassword.isEmpty()) {
                                doc_password.setError("password is required");
                                doc_password.requestFocus();
                                return;
                            }

                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, updateurl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String message = jsonObject.getString("message");
                                        if (message.equals("upadateprofile")) {
                                            Toast.makeText(getContext(), "profile is update now", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getContext(), doctorHome.class);
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    try {
                                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                                    } catch (Exception e) {
                                    }
                                }
                            }) {
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("image", imagetostring(bitmap));
                                    params.put("imgname", imgname);
                                    params.put("name", updatedocname);
                                    params.put("doctorid", doc_id);
                                    params.put("email", updatedocemail);
                                    params.put("info", updatedocinfo);
                                    params.put("number", updatedocnumber);
                                    params.put("country", updatedoc_country);
                                    params.put("address", updatedocaddress);
                                    params.put("medical_licence", updatemed_liecense);
                                    params.put("password", updatedocpassword);
                                    return params;
                                }
                            };
                            mysingleton.getmInstance(getContext()).addtorequestque(stringRequest1);

                        }


                    });
                    mGeoDataClient = Places.getGeoDataClient(getContext(), null);
                    mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext(), null);
                    //mgoogleapiclient=new GoogleApiClient.Builder(getContext()).addApi(Places.GEO_DATA_API)
                    // .addApi(Places.PLACE_DETECTION_API).enableAutoManage(getActivity(), (GoogleApiClient.OnConnectionFailedListener) getContext()).build();
                    mplaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getContext(), mGeoDataClient, LAT_LNG_BOUNDS, null);
                    doc_address.setAdapter(mplaceAutocompleteAdapter);
                    button = (CountryCurrencyButton) view1.findViewById(R.id.country);
                    updatedoc_country = button.getCountry().getName().toString();
                    button.setOnClickListener(new CountryCurrencyPickerListener() {
                        @Override
                        public void onSelectCountry(com.scrounger.countrycurrencypicker.library.Country country) {
                            if (country.getCurrency() == null) {
                                updatedoc_country = country.getName();
                                Toast.makeText(getContext(),
                                        String.format("name: %s\ncode: %s", country.getName(), country.getCode())
                                        , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(),
                                        String.format("name: %s\ncurrencySymbol: %s", country.getName(), country.getCurrency().getSymbol())
                                        , Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onSelectCurrency(Currency currency) {

                        }
                    });


                    builder.setView(view1);
                    builder.show();
                } catch (Exception e) {

                }
            }
        });

        return view;

    }

    public void updatedoctorprofile() {

    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imagetostring(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        if (bitmap == null) {
            imageView.setImageBitmap(bitmap);
        }
        int a = bitmap.getAllocationByteCount();
        if (a >= 2000) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        }
        byte[] imagByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imagByte, Base64.DEFAULT);
    }
}
