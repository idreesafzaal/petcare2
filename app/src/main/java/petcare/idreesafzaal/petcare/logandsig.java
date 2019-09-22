package petcare.idreesafzaal.petcare;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class logandsig extends AppCompatActivity {
    localhost host = new localhost();
    EditText Username, Password;
    String username, password;
    String login_url = "http://" + host.api + "/andriodfiles/log.php";

    Button login_btn, doctorcontinuebtn;
    AlertDialog.Builder builder;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Boolean Registered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logandsig);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Registered = sharedPref.getBoolean("userlogedin", false);
//


        doctorcontinuebtn = (Button) findViewById(R.id.doctorcontinue);
        doctorcontinuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logandsig.this, doctorloginandsiginup.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });
        Username = (EditText) findViewById(R.id.name);
        Password = (EditText) findViewById(R.id.password);

        login_btn = (Button) findViewById(R.id.loginBtn);


        builder = new AlertDialog.Builder(logandsig.this);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = Username.getText().toString().toLowerCase();
                if(username.isEmpty() || !username.matches(emailPattern)){
                    Username.setError("Enter valid email");
                    Username.requestFocus();
                    return;
                }
                password = Password.getText().toString().toLowerCase();
                if(password.isEmpty() || password.length()<6 || password.length()>12){
                    Password.setError("Enter valid password");
                    Password.requestFocus();
                    return;
                }
                try {


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");


                                        if (code.equals("login_failed")) {
                                            // Toast.makeText(logandsig.this,"not",Toast.LENGTH_SHORT).show();
                                            builder.setTitle("Login failed");
                                            displayalert(jsonObject.getString("message"));
                                        } else {
                                            String usrname = jsonObject.getString("user_name");
                                            String user_id = jsonObject.getString("userid");
                                            String userphone = jsonObject.getString("phone");
                                            String useremail = jsonObject.getString("email");
                                            final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(logandsig.this);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putBoolean("userlogedin", true);
                                            editor.putString("userId", user_id);
                                            editor.putString("userName", usrname);
                                            editor.putString("userEmail", useremail);
                                            editor.putString("userPhone", userphone);
                                            editor.apply();
                                            Intent intent = new Intent(logandsig.this, home.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.putExtra("userid", user_id);
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(logandsig.this, "YOU DONT HAVE INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name", username);
                            params.put("password", password);
                            return params;
                        }
                    };
                    mysingleton.getmInstance(logandsig.this).addtorequestque(stringRequest);
                }catch (Exception e){}
                }


        });


    }

    public void displayalert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Username.setText("");
                Password.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void cod(View view) {

        Intent intent;
        intent = new Intent(this, signup.class);
        startActivity(intent);
    }


}
