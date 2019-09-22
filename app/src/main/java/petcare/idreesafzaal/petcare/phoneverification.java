package petcare.idreesafzaal.petcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class phoneverification extends AppCompatActivity {
    EditText etcode;
    ProgressBar progressBar;
    String verifacationid;
    Button verifycode;
    localhost host = new localhost();
    String lat;
    String lng;
    String doc;
    private FirebaseAuth mAuth;
    String petownerurl = "http://" + host.api + "/andriodfiles/register.php";
    String url = "http://" + host.api + "/andriodfiles/reg_doctor.php";
    String name, info, email, country, address, password, med, phonenumber, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneverification);
        mAuth = FirebaseAuth.getInstance();
        verifycode = (Button) findViewById(R.id.vercodeBtn);
        etcode = (EditText) findViewById(R.id.sendcode);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        phonenumber=getIntent().getStringExtra("number");
        name = getIntent().getStringExtra("name");
        info = getIntent().getStringExtra("info");
        email = getIntent().getStringExtra("email");
        country = getIntent().getStringExtra("country");
        address = getIntent().getStringExtra("address");
        password = getIntent().getStringExtra("password");
        med = getIntent().getStringExtra("medical_licence");
        lat = getIntent().getStringExtra("lat");
        lng = getIntent().getStringExtra("lng");

        sendVerficationCode(phonenumber);

        verifycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etcode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    etcode.setError("Enter valid code here");
                    etcode.requestFocus();
                    return;
                } else {
                    verifycode(code);
                }
            }
        });
    }

    private void verifycode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifacationid, code);
        sigInwithCredential(credential);
    }

    private void sigInwithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String message = jsonObject.getString("code");
                                if (message.equals("logged_in")) {
                                    String id = jsonObject.getString("doctor_id");
                                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(phoneverification.this);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putBoolean("doctorlogedin", true);
                                    editor.putString("doctorId", id);
                                    editor.apply();
                                    Intent intent = new Intent(phoneverification.this, doctorHome.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(phoneverification.this, message, Toast.LENGTH_LONG).show();
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
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name", name);
                            params.put("email", email);
                            params.put("info", info);
                            params.put("number", phonenumber);
                            params.put("country", country);
                            params.put("address", address);
                            params.put("medical_licence", med);
                            params.put("password", password);
                            params.put("type", "doctor");
                            params.put("lat", lat);
                            params.put("lng", lng);
                            return params;
                        }
                    };
                    mysingleton.getmInstance(phoneverification.this).addtorequestque(stringRequest);

                } else {
                    Toast.makeText(phoneverification.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendVerficationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                java.util.concurrent.TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verifacationid = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifycode(code);
                etcode.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(phoneverification.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
