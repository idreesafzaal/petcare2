package petcare.idreesafzaal.petcare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class petRegistration extends AppCompatActivity implements View.OnClickListener {
    static final int DATE_START_DIALOG_ID = 0;
    private int startYear = 2010;
    private int startMonth = 6;
    private int startDay = 15;
    TextView tv;
    String totalAGE;
    String hbd, weight;
    localhost host = new localhost();
    ImageView imageView;
    String imagename;
    int IMG_REQUEST = 1;
    private Bitmap bitmap;
    int currentyear;
    int currentday;
    Calendar calendar;
    RadioButton malecheck, femalecheck;
    private ageCalculation age = null;
    String selectedCategory;
    String subcategory;
    AlertDialog.Builder builder;
    ArrayList<String> listitem = new ArrayList<String>();
    ArrayList<String> list;
    Spinner sp1, sp2, sp3;
    EditText petinfo, petname;
    String petInfo, petName;
    Button pickAge, regBTN;
    String url3 = "http://" + host.api + "/andriodfiles/registerPet.php";
    String url = "http://" + host.api + "/andriodfiles/selectcategory.php";
    String url1 = "http://" + host.api + "/andriodfiles/subcategory.php";
    String url2 = "http://" + host.api + "/andriodfiles/catSelected.php";
    ArrayAdapter<String> adapter;
    public static ArrayAdapter<String> adapter1;
    public static ArrayAdapter<String> adapter2;
    String userId, gender;
    ArrayList<String> listitem1 = new ArrayList<String>();
    ArrayList<String> listitem2 = new ArrayList<String>();
    String[] categoriesId;
    Toolbar toolbar;
    //----------------------------------------------------------age

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_registration);
        builder = new AlertDialog.Builder(petRegistration.this);
        malecheck = (RadioButton) findViewById(R.id.maleCheck);
        femalecheck = (RadioButton) findViewById(R.id.femaleCheck);
        regBTN = (Button) findViewById(R.id.addpetBtn);
        petname = (EditText) findViewById(R.id.petname);
        toolbar = (Toolbar) findViewById(R.id.meassge_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(petRegistration.this, home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

//        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putBoolean("bt", true);
//        editor.putBoolean("lt", true);
//        editor.putBoolean("dt", true);
//        editor.apply();

        //weight = sharedPref.getString("weight", null);

        calendar = Calendar.getInstance();
        currentyear = calendar.get(Calendar.YEAR);
        currentday = calendar.get(Calendar.DAY_OF_MONTH);

        imageView = (ImageView) findViewById(R.id.imageView2);
        pickAge = (Button) findViewById(R.id.age);
        petinfo = (EditText) findViewById(R.id.petinfo);
        pickAge.setOnClickListener(petRegistration.this);
        age = new ageCalculation();
        tv = (TextView) findViewById(R.id.tv);
        tv.setVisibility(View.INVISIBLE);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(petRegistration.this);
        userId = sharedPreferences.getString("userId", null);
        //petinfo.setText(userId);
        sp1 = (Spinner) findViewById(R.id.spinner1);
        sp2 = (Spinner) findViewById(R.id.spinner2);
        sp3 = (Spinner) findViewById(R.id.spinner3);
        sp3.setVisibility(View.INVISIBLE);
        sp2.setVisibility(View.INVISIBLE);
        age.getCurrentDate();

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listitem);

        adapter1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listitem1);

        adapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listitem2);

        //getimage from gallery---------------------------------------------------------------
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(petRegistration.this,"add pic here",Toast.LENGTH_LONG).show();
                selectImage();
            }
        });

//pet register here--------------------------------------------------------------------------------------------------------------------------------------------

        regBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petName = petname.getText().toString();
                imagename = petName;
                petInfo = petinfo.getText().toString();
                if (femalecheck.isChecked() && malecheck.isChecked()) {
                    Toast.makeText(petRegistration.this, "You cant checked both checkboxes", Toast.LENGTH_LONG).show();
                    femalecheck.setChecked(false);
                    malecheck.setChecked(false);
                }
                if (femalecheck.isChecked()) {
                    gender = "Female";
                } else {
                    gender = "Male";
                }
                if (petName.isEmpty()) {
                    petname.setError("Enter pet name");
                    petname.requestFocus();
                    return;
                }
                if (petInfo.isEmpty()) {
                    petinfo.setError("Enter pet info");
                    petinfo.requestFocus();
                    return;
                }
                if (pickAge.getText().toString().isEmpty()) {
                    pickAge.setError("pick date ");
                    pickAge.requestFocus();
                    return;
                }


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String message = jsonObject.getString("code");
                            if (message.equals("you_have_already_add_this_pet")) {
                                Toast.makeText(petRegistration.this, "pet is already register", Toast.LENGTH_LONG).show();
                            }
                            if (message.equals("petadd")) {
                                Intent intent = new Intent(petRegistration.this, caloriesCalculation.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("category", selectedCategory);
                                intent.putExtra("subcategory", subcategory);
                                intent.putExtra("day", age.getDay());
                                intent.putExtra("month", age.getMonth());
                                intent.putExtra("year", age.getResult());
                                intent.putExtra("petname", petName);
                                startActivity(intent);

                                Toast.makeText(petRegistration.this, "pet is register now", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        builder.setTitle("Something went wrong!");
                        displayalert("Check your internet connection");
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("image", imagetostring(bitmap));
                        params.put("imgname", imagename);
                        params.put("userid", userId);
                        params.put("category", selectedCategory);
                        params.put("subcategory", subcategory);
                        params.put("petname", petName);
                        params.put("petinfo", petInfo);
                        params.put("hbd", hbd);
                        params.put("currentage", totalAGE);
                        params.put("gender", gender);
                        return params;
                    }
                };
                mysingleton.getmInstance(petRegistration.this).addtorequestque(stringRequest);

            }
        });


//first sipneer main main categoery of pet ko add kare ga----------------------------------------------------------------------------------------------------------------
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayAdapter<String> arrayAdapter;
                            JSONArray jsonArray = new JSONArray(response);
                            String category_Id;
                            String category_Id1;
                            categoriesId = new String[jsonArray.length()];
                            if (jsonArray != null) {
                                String na;
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    listitem.add(jsonArray.getJSONObject(i).getString("categoryName"));
                                    categoriesId[i] = (jsonArray.getJSONObject(i).getString("categoryid"));
                                    //category_Id=jsonArray.getJSONObject(0).getString("categoryid");
                                    // Log.d("cvbb",category_Id);
                                    // category_Id1=jsonArray.getJSONObject(1).getString("categoryid");
                                    // Log.d("cvbb",category_Id1);
                                }
                                sp1.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(petRegistration.this, "YOU DONT HAVE INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        mysingleton.getmInstance(petRegistration.this).addtorequestque(stringRequest);

//first spinner say jab category select ho ge to tab kia krana ha-------------------------------------------------------------------------------------------------------------
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedCategory = sp1.getSelectedItem().toString();
                String id = categoriesId[i];
                selectsubcategory(id);
//
////
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subcategory = sp2.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //age calculation.................................................................----------------------===============


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_START_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        startYear, startMonth, startDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener
            = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            try {


            if (selectedYear > currentyear && selectedDay > currentday) {
                Toast.makeText(petRegistration.this, "Select valid date", Toast.LENGTH_LONG).show();
            } else {
                startYear = selectedYear;
                startMonth = selectedMonth;
                startDay = selectedDay;
                age.setDateOfBirth(startYear, startMonth, startDay);
                pickAge.setText(selectedDay + ":" + (startMonth + 1) + ":" + startYear);

                hbd = pickAge.getText().toString();
                //petinfo.setText(hbd);
                calculateAge();
            }
            }catch (Exception e){}
        }
    };

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.age:
                showDialog(DATE_START_DIALOG_ID);
                break;

            default:
                break;
        }
    }

    private void calculateAge() {
        age.calcualteYear();
        age.calcualteMonth();
        age.calcualteDay();
        Toast.makeText(getBaseContext(), "your pet is" + "\n" + age.getDay() + "\n" + age.getMonth() + "\n " + "" + age.getResult() + "\n " + "old", Toast.LENGTH_LONG).show();

        tv.setText(age.getDay() + ":" + age.getMonth() + ":" + age.getResult());
        totalAGE = tv.getText().toString();

    }


    public void displayalert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                petname.setText("");
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imagetostring(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imagByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imagByte, Base64.DEFAULT);
    }


    public void selectsubcategory(final String type) {

        adapter1.clear();
        sp2.setVisibility(View.VISIBLE);
        sp3.setVisibility(View.INVISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayAdapter<String> arrayAdapter;
                            //listitem1=null;
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray != null) {
                                String na;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    listitem1.add(jsonArray.getJSONObject(i).getString("subcategoryName"));
                                }
                                sp2.setAdapter(adapter1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(petRegistration.this, "YOU DONT HAVE INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", type);
                return params;
            }
        };
        mysingleton.getmInstance(petRegistration.this).addtorequestque(stringRequest);


    }

}
