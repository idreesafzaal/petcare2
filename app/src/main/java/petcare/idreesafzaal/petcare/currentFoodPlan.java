package petcare.idreesafzaal.petcare;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class currentFoodPlan extends AppCompatActivity {

    TextView breakFast, Lunch, Diner, breakFasttime, lunchTime, dinnerTime, Day;
    Calendar calendar;
    AlertDialog.Builder builder;
    String url = "http://172.20.10.9/andriodfiles/currentFoodPlan.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_food_plan);

        builder = new AlertDialog.Builder(currentFoodPlan.this);
        //in onStart()


        breakFast = (TextView) findViewById(R.id.breakfast);
        Day = (TextView) findViewById(R.id.day);
        Lunch = (TextView) findViewById(R.id.lunch);
        Diner = (TextView) findViewById(R.id.Dinner);

        breakFasttime = (TextView) findViewById(R.id.breakfastTime);
        lunchTime = (TextView) findViewById(R.id.lunchTime);
        dinnerTime = (TextView) findViewById(R.id.dinnerTime);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    String breakfast = jsonObject.getString("breakfast");
                    String lunch = jsonObject.getString("lunch");
                    String dinner = jsonObject.getString("dinner");
                    String breakfastTime = jsonObject.getString("breakfastTime");
                    String lunchtime = jsonObject.getString("lunchtime");
                    String dinnertime = jsonObject.getString("dinnertime");
                    String day = jsonObject.getString("day");
                    if (code.equals("notfound")) {
                        Toast.makeText(currentFoodPlan.this, "didnt find food plan", Toast.LENGTH_SHORT).show();
                    } else {

                        breakFast.setText(breakfast);
                        Lunch.setText(lunch);
                        Diner.setText(dinner);
                        breakFasttime.setText(breakfastTime);
                        lunchTime.setText(lunchtime);
                        dinnerTime.setText(dinnertime);
                        Day.setText(day);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(currentFoodPlan.this, "internet", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                calendar = Calendar.getInstance();
//date format is:  "Date-Month-Year Hour:Minutes am/pm"
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm a"); //Date and time
                String currentDate = sdf.format(calendar.getTime());

//Day of Name in full form like,"Saturday", or if you need the first three characters you have to put "EEE" in the date format and your result will be "Sat".
                SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
                Date date = new Date();
                String dayName = sdf_.format(date);
                params.put("day", dayName);
                return params;
            }
        };
        mysingleton.getmInstance(currentFoodPlan.this).addtorequestque(stringRequest);

    }

    public void displayalert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

