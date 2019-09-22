package petcare.idreesafzaal.petcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class schedules extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);
    }

    public void shower1(View view) {
        Intent intent;
        intent = new Intent(schedules.this, shower.class);
        startActivity(intent);

    }

    public void play1(View view) {
        Intent intent;
        intent = new Intent(schedules.this, shower.class);
        startActivity(intent);

    }

    public void walk1(View view) {
        Intent intent;
        intent = new Intent(schedules.this, shower.class);
        startActivity(intent);

    }
}
