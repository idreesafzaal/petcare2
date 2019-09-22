package petcare.idreesafzaal.petcare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import static android.support.design.widget.BottomNavigationView.*;

public class first extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.hm:
                    Intent intent = new Intent(first.this, first.class);
                    startActivity(intent);
                    return true;
                case R.id.cc:
                    intent = new Intent(first.this, profile.class);
                    startActivity(intent);
                    return true;
                case R.id.chat:
                    intent = new Intent(first.this, chat.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bot);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

}
