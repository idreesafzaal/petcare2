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
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

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

public class petschedulealert extends Service {
    int ONGOING_NOTIFICATION_ID;
    int notificationId;
    int hour;
    int mint;
    int splashier = 60000;
    String amPm;
    petfeedingalert petfed;
    Calendar calendar;
    localhost host = new localhost();
    boolean startTime, endTime;
    NotificationManager manager;
    String url = "http://" + host.api + "/andriodfiles/serachUserPetSchedule.php";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        super.onCreate();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);

        calendar = Calendar.getInstance();
        petfed = new petfeedingalert();

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        mint = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        if (hour >= 12) {
            amPm = "PM";
        } else {
            amPm = "AM";
        }

        final String currentTime;
        currentTime = String.format("%02d:%02d", hour, mint) + amPm;

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(petschedulealert.this);
        final String userid = sharedPref.getString("userId", null);
        startTime = sharedPref.getBoolean("starttime", false);


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String starttime = jsonObject.getString("st").trim();
                        String endtime = jsonObject.getString("et").trim();
                        if (starttime.equals(currentTime.trim()) && startTime == true) {
                            final String petname = (jsonArray.getJSONObject(i).getString("ppp"));
                            final String type = jsonObject.getString("stype");
                            String message = "your pet " + " : " + petname + " " + "has" + " : " + type + " " + "time";
                            SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(petschedulealert.this);
                            SharedPreferences.Editor editor = sharedPreferences1.edit();
                            editor.putBoolean("starttime", false);
                            editor.apply();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(petschedulealert.this);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putBoolean("starttime", true);
                                    editor.apply();

                                }
                            }, splashier);
                            shownotification(message);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(petschedulealert.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                return params;
            }
        };
        mysingleton.getmInstance(petschedulealert.this).addtorequestque(request);

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(getApplicationContext(), this.getClass());
        intent.setPackage(getPackageName());
        startService(intent);
        super.onTaskRemoved(rootIntent);
    }

    public void shownotification(String message) {
        try {


            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, home.class), 0);
            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sound);
            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_pets_black_24dp)
                    .setContentTitle("pet schedule")
                    .setContentText("please check this schedule")
                    .setSound(sound)
                    .setContentIntent(contentIntent).build();
            manager.notify(notificationId, notification);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
