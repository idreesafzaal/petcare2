package petcare.idreesafzaal.petcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class doctorHome extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(doctorHome.this, doctorHome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:
                    mSlideViewPager.setVisibility(View.INVISIBLE);
                    mDotLayout.setVisibility(View.INVISIBLE);
                    fragment = new doctorProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.docfragment_container, fragment).commit();
                    return true;
                case R.id.navigation_notifications:
                    mSlideViewPager.setVisibility(View.INVISIBLE);
                    mDotLayout.setVisibility(View.INVISIBLE);
                    fragment = new requestedappointment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.docfragment_container, fragment).commit();
                    return true;
                case R.id.suggestion:
                    mSlideViewPager.setVisibility(View.INVISIBLE);
                    mDotLayout.setVisibility(View.INVISIBLE);
                    fragment = new doctorsuggestioncontainer();
                    getSupportFragmentManager().beginTransaction().replace(R.id.docfragment_container, fragment).commit();
                    return true;
            }
            return false;
        }
    };
    Toolbar toolbar;
    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private slideradpter slideradpter;
    private TextView[] mDots;
    private long backPressesTime;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);


        toolbar = (Toolbar) findViewById(R.id.doctorhometoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        mSlideViewPager = (ViewPager) findViewById(R.id.pageview);
        AnimationUtils.loadAnimation(doctorHome.this,R.anim.rotateimage);
        mSlideViewPager.setAnimation(animation);
        mDotLayout = (LinearLayout) findViewById(R.id.dots);
        slideradpter = new slideradpter(doctorHome.this);
        mSlideViewPager.setAdapter(slideradpter);
        addDots(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if (backPressesTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressesTime = System.currentTimeMillis();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
            if(id==R.id.acceptedapp){
                mSlideViewPager.setVisibility(View.INVISIBLE);
                mDotLayout.setVisibility(View.INVISIBLE);
               Fragment fragment = new showacceptappointment();
                getSupportFragmentManager().beginTransaction().replace(R.id.docfragment_container, fragment).commit();
                //return true;
            }
            if (id == R.id.logouts) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(doctorHome.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(doctorHome.this, logandsig.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                // Toast.makeText(doctorHome.this,"logout click here",Toast.LENGTH_LONG).show();
            }


        return super.onOptionsItemSelected(item);
    }


    public void addDots(int position) {
        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(doctorHome.this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorDeepPurple));
            mDotLayout.addView(mDots[i]);

        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            addDots(i);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
