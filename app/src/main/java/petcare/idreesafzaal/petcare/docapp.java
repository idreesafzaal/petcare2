package petcare.idreesafzaal.petcare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class docapp extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docapp);
        final RatingBar rb;
        rb=(RatingBar) findViewById(R.id.rating_bar);
        final TextView tv;
        tv=(TextView) findViewById(R.id.txt);
        final Button bt;
        bt=(Button) findViewById(R.id.btn);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("rating is "+rb.getRating());
                Toast.makeText(docapp.this,"YOUR REQUEST IS SEND TO DOCTOR",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void request(View view){


    }
}
