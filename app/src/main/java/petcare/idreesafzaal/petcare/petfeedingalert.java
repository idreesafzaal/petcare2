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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class petfeedingalert extends Service {
    private NotificationManager mNM;
    String message, petname, imagebites;

    String d, ltim, dtim, btim;
    int splashtime = 60000;
    boolean bt, lt, dt;
    String userid;
    localhost host = new localhost();
    Calendar calendar;
    String url = "http://" + host.api + "/andriodfiles/selectfeedtime.php";
    int hour;
    int mint;
    int notificationId;
    String amPm;
    String currentTime;
    List<String> lunchtime;
    String[] dinnertime;
    List<String> breakfasttime;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {

        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        lunchtime = new ArrayList<>();
        breakfasttime = new ArrayList<>();
        super.onCreate();

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        onTaskRemoved(intent);
        //Toast.makeText(this,"breakfast",Toast.LENGTH_LONG).show();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userid = sharedPreferences.getString("userId", null);
        bt = sharedPreferences.getBoolean("bt", false);
        lt = sharedPreferences.getBoolean("lt", false);
        dt = sharedPreferences.getBoolean("dt", false);
        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        mint = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        if (hour >= 12) {
            amPm = "PM";
        } else {
            amPm = "AM";
        }
        String h = String.valueOf(hour);
        String m = String.valueOf(mint);
        String s = String.valueOf(second);

        d = String.format("%02d:%02d", hour, mint) + amPm;
        //Toast.makeText(this,"active",Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    dinnertime = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        lunchtime.add(jsonObject.getString("lt"));
                        dtim = (jsonArray.getJSONObject(i).getString("dt"));
                        btim = (jsonArray.getJSONObject(i).getString("bt"));
                        ltim = (jsonArray.getJSONObject(i).getString("lt"));

                        if (dtim.equals(d) && dt == true) {
                            String petname = (jsonArray.getJSONObject(i).getString("petname"));
                            String imagebites = (jsonArray.getJSONObject(i).getString("image"));
                            message = "Your pet" + " " + petname + "" + "has dinner time";
                            final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(petfeedingalert.this);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("dt", false);
                            editor.apply();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(petfeedingalert.this);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putBoolean("dt", true);
                                    editor.apply();

                                }
                            }, splashtime);
                            showNotification(message, imagebites);
                        }
                        if (btim.equals(d) && bt == true) {
                            petname = (jsonArray.getJSONObject(i).getString("petname"));
                            imagebites = (jsonArray.getJSONObject(i).getString("image"));
                            message = "Your pet" + " " + petname + "" + "has breakfast time";
                            final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(petfeedingalert.this);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("bt", false);
                            editor.apply();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(petfeedingalert.this);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putBoolean("bt", true);
                                    editor.apply();

                                }
                            }, splashtime);
                            showNotification(message, imagebites);
                        }

                        if (ltim.equals(d) && lt == true) {
                            petname = (jsonArray.getJSONObject(i).getString("petname"));
                            imagebites = (jsonArray.getJSONObject(i).getString("image"));
                            message = "Your pet" + " " + petname + "" + "has lunch time";

                            final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(petfeedingalert.this);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("lt", false);
                            editor.apply();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(petfeedingalert.this);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putBoolean("lt", true);
                                    editor.apply();

                                }
                            }, splashtime);

                            showNotification(message, imagebites);


                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(petfeedingalert.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                return params;
            }
        };
        mysingleton.getmInstance(this).addtorequestque(stringRequest);

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Intent intent = new Intent(getApplicationContext(), this.getClass());
        intent.setPackage(getPackageName());
        startService(intent);


        super.onTaskRemoved(rootIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification(final String message, String imagebites) {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        //CharSequence text = getText(R.string.local_service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, home.class), 0);
        final Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sound);
        // Set the info for the views that show in the notification panel.

        /**put your code inside this
         * this method will get exceuted after 1 min without stopping thread
         * same thing can be recursive based on your requirement*/
        Notification notification = new Notification.Builder(petfeedingalert.this)
                .setSmallIcon(R.drawable.ic_pets_black_24dp) // the status icon
                .setTicker("pet lunach")  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("check pet food plan").setSound(sound)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentText(message).setAutoCancel(true)  // the contents of the entry
                .setContentIntent(contentIntent).build()  // The intent to send when the entry is clicked
                ;


        // Send the notification.
        mNM.notify(notificationId, notification);


    }
}
