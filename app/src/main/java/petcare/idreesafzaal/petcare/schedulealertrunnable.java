package petcare.idreesafzaal.petcare;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

public class schedulealertrunnable implements Runnable {
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/serachUserPetSchedule.php";
    String user_id, dbstarttime, currenttime, dbendtime, amPm;
    String pet_name, schedule_type, day;
    Boolean starttime, endtime;
    String Message;
    Calendar calendar;
    int hour, mint, id;
    NotificationManager manager;
    Context context;
    Handler handler = new Handler();

    public schedulealertrunnable(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        try {


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            user_id = sharedPreferences.getString("userId", null);
            starttime = sharedPreferences.getBoolean("starttime", false);
            endtime = sharedPreferences.getBoolean("endtime", false);
            calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            mint = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            final String dayLongName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
            if (hour >= 12) {
                amPm = "PM";
            } else {
                amPm = "AM";
            }


            currenttime = String.format("%02d:%02d", hour, mint) + amPm;


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            dbstarttime = jsonArray.getJSONObject(i).getString("st").trim();
                            dbendtime = jsonArray.getJSONObject(i).getString("et").trim();
                            day = jsonArray.getJSONObject(i).getString("day").trim();
                            if (dbstarttime.equals(currenttime) && starttime == true && day.equals(dayLongName)) {
                                pet_name = (jsonArray.getJSONObject(i).getString("ppp"));
                                schedule_type = (jsonArray.getJSONObject(i).getString("stype"));
                                Message = "your pet " + " : " + pet_name + " " + "has" + " : " + schedule_type + " " + "time";
                                final SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);
                                final SharedPreferences.Editor editor = sharedPreferences1.edit();
                                editor.putBoolean("starttime", false);
                                editor.apply();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferences sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(context);
                                        SharedPreferences.Editor editor1 = sharedPreferences2.edit();
                                        editor.putBoolean("starttime", true);
                                        editor.apply();
                                    }
                                }, 60000);
                                showNotification(Message);

                            }
                            if (dbendtime.equals(currenttime) && endtime == true && day.equals(dayLongName)) {
                                pet_name = (jsonArray.getJSONObject(i).getString("ppp"));
                                schedule_type = (jsonArray.getJSONObject(i).getString("stype"));
                                Message = "your pet " + " : " + pet_name + " " + "has" + " : " + schedule_type + " " + "end time";
                                final SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);
                                final SharedPreferences.Editor editor = sharedPreferences1.edit();
                                editor.putBoolean("endtime", false);
                                editor.apply();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferences sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(context);
                                        SharedPreferences.Editor editor1 = sharedPreferences2.edit();
                                        editor.putBoolean("endtime", true);
                                        editor.apply();
                                    }
                                }, 60000);
                                showNotification(Message);
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
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userid", user_id);
                    return params;
                }
            };
            mysingleton.getmInstance(context).addtorequestque(stringRequest);
            handler.postDelayed(this, 2000);
        } catch (Exception e) {

        }
    }


    private void showNotification(String message) {
        try {

            final PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, home.class), 0);
            final Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.sound);
            Notification notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_pets_black_24dp) // the status icon
                    .setTicker("pet scdehule")  // the status text
                    .setWhen(System.currentTimeMillis())  // the time stamp
                    .setContentTitle("check pet schedule").setSound(sound)  // the label of the entry
                    .setContentText(message).setAutoCancel(true)  // the contents of the entry
                    .setContentIntent(contentIntent).build()  // The intent to send when the entry is clicked
                    ;


            // Send the notification.
            manager.notify(id, notification);

        } catch (Exception e) {
        }
    }
}
