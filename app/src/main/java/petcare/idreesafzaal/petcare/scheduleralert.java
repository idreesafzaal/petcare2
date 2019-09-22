package petcare.idreesafzaal.petcare;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

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
import java.util.Map;

public class scheduleralert extends Service {
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/serachUserPetSchedule.php";
    String user_id, dbstarttime, currenttime, dbendtime, amPm;
    String pet_name, schedule_type;
    Boolean starttime, endtime;
    String Message;
    Calendar calendar;
    int hour, mint, id;
    NotificationManager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);
        try {


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            user_id = sharedPreferences.getString("userId", null);
            starttime = sharedPreferences.getBoolean("starttime", false);
            endtime = sharedPreferences.getBoolean("endtime", false);
            calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            mint = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            if (hour >= 12) {
                amPm = "PM";
            } else {
                amPm = "AM";
            }


            currenttime = String.format("%02d:%02d", hour, mint) + amPm;


            Log.d("current", currenttime);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            dbstarttime = jsonArray.getJSONObject(i).getString("st").trim();
                            dbendtime = jsonArray.getJSONObject(i).getString("et").trim();
                            if (dbstarttime.equals(currenttime) && starttime == true) {
                                pet_name = (jsonArray.getJSONObject(i).getString("ppp"));
                                schedule_type = (jsonArray.getJSONObject(i).getString("stype"));
                                Message = "your pet " + " : " + pet_name + " " + "has" + " : " + schedule_type + " " + "time";
                                final SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(scheduleralert.this);
                                final SharedPreferences.Editor editor = sharedPreferences1.edit();
                                editor.putBoolean("starttime", false);
                                editor.apply();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferences sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(scheduleralert.this);
                                        SharedPreferences.Editor editor1 = sharedPreferences2.edit();
                                        editor.putBoolean("starttime", true);
                                        editor.apply();
                                    }
                                }, 60000);
                                showNotification(Message);

                            }
                            if (dbendtime.equals(currenttime) && endtime == true) {
                                pet_name = (jsonArray.getJSONObject(i).getString("ppp"));
                                schedule_type = (jsonArray.getJSONObject(i).getString("stype"));
                                Message = "your pet " + " : " + pet_name + " " + "has" + " : " + schedule_type + " " + "end time";
                                final SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(scheduleralert.this);
                                final SharedPreferences.Editor editor = sharedPreferences1.edit();
                                editor.putBoolean("endtime", false);
                                editor.apply();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferences sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(scheduleralert.this);
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
            mysingleton.getmInstance(this).addtorequestque(stringRequest);
        } catch (Exception e) {

        }
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(getApplicationContext(), this.getClass());
        intent.setPackage(getPackageName());
        startService(intent);
        super.onTaskRemoved(rootIntent);
    }

    private void showNotification(String message) {
        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, home.class), 0);
        final Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sound);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_pets_black_24dp) // the status icon
                .setTicker("pet lunach")  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("check pet schedule").setSound(sound)  // the label of the entry
                .setContentText(message).setAutoCancel(true)  // the contents of the entry
                .setContentIntent(contentIntent).build()  // The intent to send when the entry is clicked
                ;


        // Send the notification.
        manager.notify(id, notification);
    }
}
