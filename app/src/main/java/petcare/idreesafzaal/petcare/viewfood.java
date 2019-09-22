package petcare.idreesafzaal.petcare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class viewfood extends AppCompatActivity {

    String[] breakfast = {"mikk", "biscut"};
    String[] luch = {"padagaricfood", "milk"};
    String[] dinner = {"beef ", "/", "chicken"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewfood);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, breakfast);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, luch);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dinner);
        ListView lv = (ListView) findViewById(R.id.b);
        ListView lv1 = (ListView) findViewById(R.id.l);
        ListView lv2 = (ListView) findViewById(R.id.d);
        lv.setAdapter(adapter);
        lv1.setAdapter(adapter1);
        lv2.setAdapter(adapter2);
    }
}
