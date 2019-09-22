package petcare.idreesafzaal.petcare;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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

public class petFoodPlanUpdate extends AppCompatActivity {
    localhost host = new localhost();
    Button button;
    String url = "http://" + host.api + "/andriodfiles/searchFoodPlan.php";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_food_plan_update);
        button = (Button) findViewById(R.id.movetoffodplan);
        button.setVisibility(View.INVISIBLE);
        builder = new AlertDialog.Builder(petFoodPlanUpdate.this);
        final String petid = getIntent().getStringExtra("petid");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dynamicFoodPlan);
        recyclerView.setLayoutManager(linearLayoutManager);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String message = jsonObject.getString("code");
                    if (message.equals("notfound")) {
                        button.setVisibility(View.VISIBLE);
                    }
                    if (message.equals("findpets")) {
                        recyclerView.setAdapter(new petFoodDynamicView(petFoodPlanUpdate.this, jsonArray, petid));
                        recyclerView.scrollToPosition(jsonArray.length()-1);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(petFoodPlanUpdate.this, "Some thing went wrong check your internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("petid", petid);
                return params;
            }
        };
        mysingleton.getmInstance(petFoodPlanUpdate.this).addtorequestque(request);
    }
}
