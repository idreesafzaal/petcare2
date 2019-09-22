package petcare.idreesafzaal.petcare;

import android.content.Context;
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

public class scheduleHelper {
    Context context;
    localhost host = new localhost();
    String urlmakesech = "http://" + host.api + "/andriodfiles/makeSchedule.php";

    public scheduleHelper(Context context) {
        this.context = context;
    }

    public void makeschedule(final String petname, final String selectedid, final String selectedday, final String stime, final String etime, final String userid, final String type) {

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, urlmakesech, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String message = jsonObject.getString("message");
                    if (message.equals("alreadyHave")) {
                        Toast.makeText(context, "you have already have schedule on this day", Toast.LENGTH_LONG).show();
                    }
                    if (message.equals("add")) {
                        Toast.makeText(context, "add now", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Something went wrong....!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "check your internet", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("petname", petname);
                params.put("petid", selectedid);
                params.put("day", selectedday);
                params.put("stime", stime);
                params.put("etime", etime);
                params.put("userid", userid);
                params.put("type", type);
                return params;
            }
        };
        mysingleton.getmInstance(context).addtorequestque(stringRequest1);
    }
}
