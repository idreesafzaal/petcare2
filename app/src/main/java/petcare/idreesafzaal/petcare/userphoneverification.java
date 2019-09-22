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

public class userphoneverification extends AppCompatActivity {
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
    String name, info, email, country, address, password, med, phonenumber, ownername, owneremail, ownerpassword, type, petowner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userphoneverification);
        mAuth = FirebaseAuth.getInstance();
        verifycode = (Button) findViewById(R.id.vercodeBtn);
        etcode = (EditText) findViewById(R.id.sendcode);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        ownername = getIntent().getStringExtra("ownername");
        owneremail = getIntent().getStringExtra("owneremail");
        ownerpassword = getIntent().getStringExtra("ownerpassword");
        type = getIntent().getStringExtra("ownertype");
        petowner = getIntent().getStringExtra("petowner");
        doc = getIntent().getStringExtra("doctor");
        phonenumber = getIntent().getStringExtra("number");
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


                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, petownerurl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String message = jsonObject.getString("code");
                                if (message.equals("success")) {
                                    String id = jsonObject.getString("userid");
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(userphoneverification.this);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("userlogedin", true);
                                    editor.putString("userId", id);
                                    editor.apply();
                                    Intent intent = new Intent(userphoneverification.this, home.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else if (message.equals("you_have_already_account")) {
                                    Toast.makeText(userphoneverification.this, message, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(userphoneverification.this, error.getMessage() + "in pet owner adding function", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("name", ownername);
                            params.put("email", owneremail);
                            params.put("number", phonenumber);
                            params.put("type", type);
                            params.put("password", ownerpassword);
                            return params;
                        }
                    };
                    mysingleton.getmInstance(userphoneverification.this).addtorequestque(stringRequest1);

                } else {
                    Toast.makeText(userphoneverification.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(userphoneverification.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}






