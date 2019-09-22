package petcare.idreesafzaal.petcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class slpashpetcare extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slpashpetcare);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(slpashpetcare.this);
                Boolean Registered = sharedPref.getBoolean("userlogedin", false);
                Boolean doctorreg=sharedPref.getBoolean("doctorlogedin",false);

                if (Registered == true) {
                    Intent intent = new Intent(slpashpetcare.this, home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else if(doctorreg==true){
                    Intent intent = new Intent(slpashpetcare.this, doctorHome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent mainIntent = new Intent(slpashpetcare.this, logandsig.class);
                    startActivity(mainIntent);
                    finish();
                }
//                /* Create an Intent that will start the Menu-Activity. */
//                Intent mainIntent = new Intent(slpashpetcare.this, logandsig.class);
//                startActivity(mainIntent);
//                finish();

            }
        }, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
