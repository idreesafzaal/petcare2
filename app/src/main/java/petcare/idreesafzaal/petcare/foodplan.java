package petcare.idreesafzaal.petcare;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class foodplan extends AppCompatActivity implements View.OnClickListener {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.hm:
                    Intent intent = new Intent(foodplan.this, home.class);
                    startActivity(intent);
                    return true;
                case R.id.cc:
                    intent = new Intent(foodplan.this, profile.class);
                    startActivity(intent);
                    return true;
                case R.id.chat:
                    intent = new Intent(foodplan.this, chat.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
    localhost host = new localhost();
    String petid;
    String selectedDay, breakFast, Lunch, Dinner;
    TextView breakfast, lunch, dinner;
    Button luchTimeBtn, dinnerTimeBtn, breakTimeBtn, selectefdbtn, total_calories, total_lunch_cal, total_dinner_cal;
    Button foodplantn;
    String breakfasttime, lunchtime, dinnertime;

    final foodhelper fd = new foodhelper();

    Calendar calendar;
    TimePickerDialog timePickerDialog;
    EditText chosetime;
    int currentHour;
    int currentMinute;
    String amPm;
    String url = "http://" + host.api + "/andriodfiles/createFoodPlan.php";
    AlertDialog.Builder builder;


    // ArrayList<String> listitem=new ArrayList<String>();
    String[] listitem;
    String[] calores;
    boolean[] checkeditem;
    String[] combineArray;

    String UserId;
    String lunch_calrs, breakfast_calrs, dinner_clrs;
    ArrayList<Integer> mselecteditem = new ArrayList<>();
    ArrayList<Integer> mlselecteditem = new ArrayList<>();
    ArrayList<Integer> mdselecteditem = new ArrayList<>();
    String avaibleFoodURL = "http://" + host.api + "/andriodfiles/avaibleFOOD.php";
    String caler, petname;
    Toolbar toolbar;
    //------------------pet id class--------------------------------------------
    getPetId afd = new getPetId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodplan);
        total_calories = (Button) findViewById(R.id.breakfastcal);
        total_lunch_cal = (Button) findViewById(R.id.lunchcal);
        total_dinner_cal = (Button) findViewById(R.id.dinnercal);
        //total_calories.setVisibility(View.INVISIBLE);
        toolbar = (Toolbar) findViewById(R.id.meassge_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(foodplan.this, petRegistration.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(foodplan.this);
        UserId = sharedPreferences.getString("userId", null);
        //petid=sharedPreferences.getString("petids",null);
        Intent intent = getIntent();
        caler = intent.getStringExtra("calories");
        Bundle bundle = getIntent().getExtras();

        //petName=intent.getStringExtra("pet_name");


        foodplantn = (Button) findViewById(R.id.foodplanBtn);
        selectefdbtn = (Button) findViewById(R.id.dietselectedbtn);

        breakTimeBtn = (Button) findViewById(R.id.breakfastTimebtn);
        luchTimeBtn = (Button) findViewById(R.id.lunchTimebtn);
        dinnerTimeBtn = (Button) findViewById(R.id.dinnerTimebtn);

        breakfast = (TextView) findViewById(R.id.breakfast);
        breakfast.setOnClickListener(foodplan.this);
        lunch = (TextView) findViewById(R.id.lunch);
        lunch.setOnClickListener(foodplan.this);
        dinner = (TextView) findViewById(R.id.dinner);
        dinner.setOnClickListener(foodplan.this);

        petname = bundle.getString("pname");
        Cache cache = new DiskBasedCache(getCacheDir(), 1025 * 1025);
        Network network = new BasicNetwork(new HurlStack());
        afd.getpetInfo(UserId, petname, cache, network, foodplan.this);

        // dinner.setText(caler);


//--------------------------------------------------------avaible food selelction code-------------------------------------------------------------------------

        fd.getavaiablefood(foodplan.this);
        selectefdbtn.setText("Total calories" + " :" + caler);


        //-------------------------------------------------------------------------------------------------------------------------------------------------------------


        breakTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecttime(breakTimeBtn);

            }
        });


        luchTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecttime(luchTimeBtn);

            }
        });


        dinnerTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecttime(dinnerTimeBtn);

            }
        });


        // BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bot);
        // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        final Spinner spiner = findViewById(R.id.food);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(adapter);


        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedDay = spiner.getSelectedItem().toString();

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        builder = new AlertDialog.Builder(foodplan.this);
        foodplantn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    petid = afd.get_pet_id();

                    breakfast_calrs = total_calories.getText().toString();
                    lunch_calrs = total_lunch_cal.getText().toString();
                    dinner_clrs = total_dinner_cal.getText().toString();
                    double b_cal = Double.parseDouble(breakfast_calrs);
                    double l_cal = Double.parseDouble(lunch_calrs);
                    double d_cal = Double.parseDouble(dinner_clrs);
                    final double toatalfoodcalores = l_cal + d_cal;
                    final String calers = String.valueOf(toatalfoodcalores);
                    breakFast = breakfast.getText().toString();
                    Lunch = lunch.getText().toString();
                    Dinner = dinner.getText().toString();
                    breakfasttime = breakTimeBtn.getText().toString();
                    lunchtime = luchTimeBtn.getText().toString();
                    dinnertime = dinnerTimeBtn.getText().toString();
                    if (breakFast.isEmpty()) {
                        breakfast.setError("Select breakfast");
                        breakfast.requestFocus();
                        return;
                    }
                    if (Lunch.isEmpty()) {
                        lunch.setError("Select lunch");
                        lunch.requestFocus();
                        return;
                    }
                    if (Dinner.isEmpty()) {
                        dinner.setError("Select dinner");
                        dinner.requestFocus();
                        return;
                    }
                    if (breakfasttime.equals(lunchtime) || breakfasttime.equals(dinnertime) || breakfasttime.isEmpty()) {
                        breakTimeBtn.setError("Select breakfast time");
                        breakTimeBtn.requestFocus();
                        return;
                    }
                    if (dinnertime.equals(breakfasttime) || dinnertime.equals(lunchtime) || dinnertime.isEmpty()) {
                        dinnerTimeBtn.setError("Select dinner time");
                        dinnerTimeBtn.requestFocus();
                        return;
                    }
                    if (lunchtime.equals(breakfasttime) || lunchtime.equals(dinnertime) || lunchtime.isEmpty()) {
                        luchTimeBtn.setError("select lunch time");
                        luchTimeBtn.requestFocus();
                        return;
                    }

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String message = jsonObject.getString("message");
                                if (message.equals("alreadyHave")) {
                                    //Toast.makeText(foodplan.this, "Already", Toast.LENGTH_SHORT).show();
                                    builder.setTitle("Something Went Wrong");
                                    displayalert("You Have already add food plan at same day ");

                                } else {
                                    Toast.makeText(foodplan.this, "YOU HAVE SUCCESFULY CREATE FOOD PLAN", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            builder.setTitle("Something Went Wrong");
                            displayalert("Check Your Internet Connect");
                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("userid", UserId);
                            params.put("petid", petid);
                            params.put("day", selectedDay);
                            params.put("breakfast", breakFast);
                            params.put("lunch", Lunch);
                            params.put("dinner", Dinner);
                            params.put("breakfasttime", breakfasttime);
                            params.put("lunchtime", lunchtime);
                            params.put("dinnertime", dinnertime);
                            params.put("totalfoodcal", calers);
                            return params;
                        }
                    };
                    mysingleton.getmInstance(foodplan.this).addtorequestque(stringRequest);

                } catch (Exception e) {

                }
            }
        });


    }

    public void displayalert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------

    // food button clicks here
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.breakfast:
                fd.getbreakfast(foodplan.this, breakfast, mselecteditem, total_calories);
                // double t=fd.gettotal_cal();
                //  foodplantn.setText(String.valueOf(t));
                break;
            case R.id.lunch:

                fd.getlunch(foodplan.this, lunch, mlselecteditem, total_lunch_cal);
                //double ta=fd.gettotal_cal();
                //foodplantn.setText(String.valueOf(ta));
                break;
            case R.id.dinner:
                fd.getdinner(foodplan.this, dinner, mdselecteditem, total_dinner_cal);
                // double tat=fd.gettotal_cal();
                // foodplantn.setText(String.valueOf(tat));
                break;
        }
    }

    public void selecttime(final Button button) {
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(foodplan.this, android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                if (hourOfDay >= 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }
                button.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

            }
        }, currentHour, currentMinute, false);

        timePickerDialog.show();
    }
}
