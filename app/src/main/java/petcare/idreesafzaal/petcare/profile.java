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

public class profile extends AppCompatActivity {


    EditText Name, Contact, Email;

    String name, contact, email, userid;

    String uname, ucantact, uemail;
    AlertDialog.Builder builder;
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/updateprofile.php";
    String schUrl = "http://" + host.api + "/andriodfiles/searchUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Name = (EditText) findViewById(R.id.name);
        Contact = (EditText) findViewById(R.id.number);
        Email = (EditText) findViewById(R.id.email);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        userid = sharedPref.getString("userId", null);

        builder = new AlertDialog.Builder(profile.this);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, schUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String messag = jsonObject.getString("code");
                    name = jsonObject.getString("user_name");
                    email = jsonObject.getString("email");
                    contact = jsonObject.getString("phone");
                    if (messag.equals("userfound")) {
                        Name.setText(name);
                        Contact.setText(contact);
                        Email.setText(email);
                    } else {
                        Toast.makeText(profile.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                builder.setTitle("Something Went Wrong!!");
                displayalert("Check Your Ineternet Connection!!");

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                return params;
            }
        };
        mysingleton.getmInstance(profile.this).addtorequestque(stringRequest);


        Button updatePro = (Button) findViewById(R.id.updateproBtn);

        updatePro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                uname = Name.getText().toString();
                uemail = Email.getText().toString();
                ucantact = Contact.getText().toString();
                if (uname.equals("") || ucantact.equals("") || uemail.equals("")) {
                    builder.setTitle("Something went wrong!");
                    displayalert("fill all given fields");

                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String messag = jsonObject.getString("message");
                                if (messag.equals("upadateprofile")) {
                                    Toast.makeText(profile.this, "Your profile is updated now", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(profile.this, home.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(profile.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            builder.setTitle("Something Went Wrong!!");
                            displayalert("Check Your Ineternet Connection!!");

                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("userid", userid);
                            params.put("name", uname);
                            params.put("email", uemail);
                            params.put("number", ucantact);
                            ;


                            return params;
                        }
                    };
                    mysingleton.getmInstance(profile.this).addtorequestque(stringRequest);
                }

            }
        });
    }


    public void displayalert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    ;

}
