package petcare.idreesafzaal.petcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class registerusers {
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/register.php";

    public void addusres(final String name, final String email, final String contact, final String password, final String type, final Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String message = jsonObject.getString("code").trim();
                    String userid = jsonObject.getString("userid");

                    if (message.equals("success")) {

                        Toast.makeText(context, "sigup now", Toast.LENGTH_SHORT).show();
                        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("userlogedin", true);
                        editor.putString("userId", userid);
                        editor.apply();
                        Intent intent = new Intent(context, code.class);
                        intent.putExtra("number", contact);
                        context.startActivity(intent);
                        //
                    } else {
                        Intent intent = new Intent(context, logandsig.class);
                        context.startActivity(intent);
                        Toast.makeText(context, "you have already account", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("number", contact);
                params.put("type", type);
                params.put("password", password);
                return params;
            }
        };
        mysingleton.getmInstance(context).addtorequestque(stringRequest);
    }
}
