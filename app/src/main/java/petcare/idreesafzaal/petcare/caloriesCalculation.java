package petcare.idreesafzaal.petcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class caloriesCalculation extends AppCompatActivity {
    EditText weight;
    RadioButton pound, kg;
    String Weight;
    double calories;
    Button next;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories_calculation);
        weight = (EditText) findViewById(R.id.petweight);
        pound = (RadioButton) findViewById(R.id.pound);
        kg = (RadioButton) findViewById(R.id.kg);
        toolbar = (Toolbar) findViewById(R.id.meassge_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(caloriesCalculation.this, petRegistration.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        Intent intent = getIntent();
        final String petname = intent.getStringExtra("petname");

        next = (Button) findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weight = weight.getText().toString();
                if (pound.isChecked() && kg.isChecked() || Weight.equals("")) {
                    Toast.makeText(caloriesCalculation.this, "you cant check both checkboxex", Toast.LENGTH_LONG).show();
                    pound.setChecked(false);
                    kg.setChecked(false);
                } else {
                    double w = Double.valueOf(Weight);
                    if (pound.isChecked()) {
                        calories = w * 30;
                        Toast.makeText(caloriesCalculation.this, "your pet calories are" + calories, Toast.LENGTH_LONG).show();
                    }
                    if (kg.isChecked()) {
                        double pd = w * 2.2;
                        calories = pd * 30;
                        Toast.makeText(caloriesCalculation.this, "your pet calories are" + calories, Toast.LENGTH_LONG).show();
                    }
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(caloriesCalculation.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("weight", String.valueOf(calories));
                    editor.apply();
                    Intent intent = new Intent(caloriesCalculation.this, foodplan.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("calories", String.valueOf(calories));
                    intent.putExtra("pname", petname);
                    startActivity(intent);
                }

            }
        });
    }

}
