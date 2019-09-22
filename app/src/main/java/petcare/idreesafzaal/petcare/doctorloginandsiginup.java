package petcare.idreesafzaal.petcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class doctorloginandsiginup extends AppCompatActivity {
    localhost host = new localhost();
    Button signup, login;
    EditText email, password;
    String stremail, strpassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String url = "http://" + host.api + "/andriodfiles/doctorlogin.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorloginandsiginup);
        signup = (Button) findViewById(R.id.doctoraccount);
        login = (Button) findViewById(R.id.doctorloginBtn);
        email = (EditText) findViewById(R.id.doctoremail);
        password = (EditText) findViewById(R.id.doctorpassword);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stremail = email.getText().toString().toLowerCase();
                strpassword = password.getText().toString().toLowerCase();
                if (stremail.isEmpty() || !stremail.matches(emailPattern)) {
                    email.setError("Enter email");
                    email.requestFocus();
                    return;
                }
                if (strpassword.isEmpty()) {
                    password.setError("Enter password");
                    password.requestFocus();
                    return;
                }
                loginuser(stremail, strpassword);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doctorloginandsiginup.this, doctor_registertion.class);
                startActivity(intent);
            }
        });

    }

    public void loginuser(String emil, String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String message = jsonObject.getString("message");
                    // Toast.makeText(getBaseContext(),"user not found",Toast.LENGTH_LONG).show();
                    if (message.equals("find")) {
                        String doctorId = jsonObject.getString("doctorid");
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(doctorloginandsiginup.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("doctorlogedin", true);
                        editor.putString("doctorId", doctorId);
                        editor.apply();
                        Intent intent = new Intent(doctorloginandsiginup.this, doctorHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    if (message.equals("login_failed")) {
                        Toast.makeText(getBaseContext(), "user not found", Toast.LENGTH_LONG).show();
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", stremail);
                params.put("password", strpassword);
                params.put("type", "doctor");
                return params;
            }
        };
        mysingleton.getmInstance(doctorloginandsiginup.this).addtorequestque(stringRequest);
    }
}
