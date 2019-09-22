package petcare.idreesafzaal.petcare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

public class getappointmentdata extends AppCompatActivity {
    RecyclerView recyclerView;
    localhost host = new localhost();
    Toolbar toolbar;
    public static final int REQUEST_CALL = 1;
    String url = "http://" + host.api + "/andriodfiles/searchalldoctor.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_getappointmentdata);
        recyclerView = (RecyclerView) findViewById(R.id.appreycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    recyclerView.setAdapter(new docappoitment(jsonArray, getappointmentdata.this));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getappointmentdata.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

        };
        mysingleton.getmInstance(getappointmentdata.this).addtorequestque(stringRequest);
    }

    public void makecall(String phone) {
        if (ContextCompat.checkSelfPermission(getappointmentdata.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getappointmentdata.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }
}
