package petcare.idreesafzaal.petcare;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class getPetId {
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/select_pet.php";
    String petId;

    public void getpetInfo(final String userid, final String petName, Cache cache, Network network, final Context context) {
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String message = jsonObject.getString("message");
                        if (message.equals("found")) {
                            petId = jsonObject.getString("petid");
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("petids", petId);
                            editor.apply();
                        } else {
                            Toast.makeText(context, "not found", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Internet problem ", Toast.LENGTH_LONG).show();
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userid", userid);
                    params.put("petname", petName);
                    return params;
                }
            };
            RequestQueue requestQueue = new RequestQueue(cache, network);
            requestQueue.start();
            requestQueue.add(stringRequest);
        } catch (Exception e) {

        }
    }


    public String get_pet_id() {
        return petId;
    }

}
