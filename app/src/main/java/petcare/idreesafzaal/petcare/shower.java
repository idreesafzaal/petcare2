package petcare.idreesafzaal.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class shower extends AppCompatActivity {
    static final int TIME_DIALOG_ID = 1111;
    private EditText view;
    public Button btnClick;
    private int hr;
    private int min;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.hm:
                    Intent intent = new Intent(shower.this, home.class);
                    startActivity(intent);
                    return true;
                case R.id.cc:
                    intent = new Intent(shower.this, profile.class);
                    startActivity(intent);
                    return true;
                case R.id.chat:
                    intent = new Intent(shower.this, chat.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shower);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bot);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Spinner spiner = findViewById(R.id.food);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(adapter);

    }

    public void onclick(View view) {
        Toast.makeText(shower.this, "Your schedule is successfully create", Toast.LENGTH_SHORT).show();

    }
}
