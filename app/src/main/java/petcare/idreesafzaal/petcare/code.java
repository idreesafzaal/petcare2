package petcare.idreesafzaal.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class code extends AppCompatActivity {
    EditText phoneNumber, verifyCode;
    String phonenumber, verifycode,name,email,password;
    String type="petowner";
    String url = "http://192.168.100.15/andriodfiles/register.php";
    Button btn;
    String mVerificationId;
    int btntype = 0;
    ImageView img;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        img=(ImageView) findViewById(R.id.imageView1) ;
        Intent intent=getIntent();
        phonenumber=intent.getStringExtra("number");


        mAuth = FirebaseAuth.getInstance();

        verifyCode = (EditText) findViewById(R.id.cod);
        btn = (Button) findViewById(R.id.codeBtn);
        img.setVisibility(View.INVISIBLE);
        verifyCode.setVisibility(View.INVISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btntype == 0) {
                    verifyCode.setVisibility(View.INVISIBLE);
                    btn.setEnabled(false);
                    //phonenumber="+923484365579";

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phonenumber,
                            60,
                            TimeUnit.SECONDS,
                            code.this,
                            mCallbacks

                    );
                } else {
                    verifyCode.setVisibility(View.VISIBLE);
                    img.setVisibility(View.VISIBLE);
                    btn.setEnabled(true);
                    verifycode = verifyCode.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verifycode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(code.this, "something is wrong", Toast.LENGTH_SHORT).show();
            }

            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                btntype = 1;
                verifyCode.setVisibility(View.VISIBLE);
                verifyCode.setHint("Enter Given Code");

                btn.setText("verify code");
                btn.setEnabled(true);

                // ...
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                            FirebaseUser user = task.getResult().getUser();
                            Intent intent;
                            intent = new Intent(code.this, home.class);
                            startActivity(intent);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(code.this, "something is wrong", Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


}

