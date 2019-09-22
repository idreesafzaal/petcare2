package petcare.idreesafzaal.petcare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class signup extends AppCompatActivity {
    EditText Name, Contact, Email, Password;
    String name, contact, email, password;
    String type = "owner";
    CheckBox checkBox1, checkBox2;
    AlertDialog.Builder builder;
    CountryCodePicker ccp;
    registerusers register;
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        register = new registerusers();
        Name = (EditText) findViewById(R.id.name);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        Contact = (EditText) findViewById(R.id.number);
        ccp.registerCarrierNumberEditText(Contact);
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        builder = new AlertDialog.Builder(signup.this);
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        Button signupbtn = (Button) findViewById(R.id.signupBtn);

        signupbtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                name = Name.getText().toString().toLowerCase().trim();
                final String contact = ccp.getFullNumberWithPlus();
                email = Email.getText().toString().toLowerCase().trim();
                password = Password.getText().toString().toLowerCase();
                if (name.isEmpty()) {
                    Name.setError("Enter name");
                    Name.requestFocus();
                    return;
                }
                if (contact.isEmpty()) {
                    Contact.setError("Enter phone number");
                    Contact.requestFocus();
                    return;
                }
                if (email.isEmpty() || !email.matches(emailPattern)) {
                    Email.setError("Enter valid email");
                    Email.requestFocus();
                    return;
                }
                if (password.isEmpty() || password.length() <6 || password.length() > 12) {
                    Password.setError("Enter valid password length");
                    Password.requestFocus();
                    return;
                }

                Intent intent = new Intent(signup.this, userphoneverification.class);
                intent.putExtra("ownername", name);
                intent.putExtra("owneremail", email);
                intent.putExtra("number", contact);
                intent.putExtra("ownerpassword", password);
                intent.putExtra("ownertype", type);
                intent.putExtra("petowner", "1");
                startActivity(intent);
            }


        });


    }


    public void displayalert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Password.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    ;


    public void cod() {


        Toast.makeText(signup.this, "YOU HAVE CANT CHECK BOTH CHECKBOXES", Toast.LENGTH_SHORT).show();


    }
}
