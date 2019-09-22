package petcare.idreesafzaal.petcare;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class view_pets extends AppCompatActivity {
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/selectUSERPET.php";
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pets);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view_pets.this);
        userId = sharedPreferences.getString("userId", null);
        final RecyclerView userPets = (RecyclerView) findViewById(R.id.userPets);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);

        // THIS ALSO SETS setStackFromBottom to true
        userPets.setLayoutManager(mLayoutManager);
        userPets.scrollToPosition(0);
        // userPets.setLayoutManager(new LinearLayoutManager(this));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String message = jsonObject.getString("code");
                    if (message.equals("findpets")) {
                        userPets.setAdapter(new petViews(view_pets.this, jsonArray));
                        userPets.scrollToPosition(jsonArray.length() - 1);
                        //Log.d("cvbnm", response);
                    } else {
                        Toast.makeText(view_pets.this, "not found", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view_pets.this, "internet problem", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userId);
                return params;

            }
        };
        mysingleton.getmInstance(view_pets.this).addtorequestque(stringRequest);
    }
}
