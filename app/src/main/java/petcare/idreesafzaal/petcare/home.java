package petcare.idreesafzaal.petcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.hm:

                    Intent intent = new Intent(home.this, home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;
                case R.id.cc:
//                    intent = new Intent(home.this, profile.class);
//                    startActivity(intent);
                    marquee.setVisibility(View.INVISIBLE);
                    mSlideViewPager.setVisibility(View.INVISIBLE);
                    mDotLayout.setVisibility(View.INVISIBLE);
                    cardView.setVisibility(View.INVISIBLE);
                    fragment = new profileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    return true;
                case R.id.chat:
                    marquee.setVisibility(View.INVISIBLE);
                    mSlideViewPager.setVisibility(View.INVISIBLE);
                    mDotLayout.setVisibility(View.INVISIBLE);
                    cardView.setVisibility(View.INVISIBLE);
                    fragment = new sechudleFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    return true;
                case R.id.not:
                    marquee.setVisibility(View.INVISIBLE);
                    mSlideViewPager.setVisibility(View.INVISIBLE);
                    mDotLayout.setVisibility(View.INVISIBLE);
                    cardView.setVisibility(View.INVISIBLE);
                    fragment = new recycleviewscheduleFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    return true;

            }
            return false;
        }

    };
    TextView tv, marquee;
    String welcome, imagedata;
    localhost host = new localhost();
    String userId;
    String url = "http://" + host.api + "/andriodfiles/saveId.php";
    String url1 = "http://" + host.api + "/andriodfiles/saveuserimage.php";
    String url2 = "http://" + host.api + "/andriodfiles/selectuserimage.php";
    Button viewTodayFoodPlan, addpet;
    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private slideradpter slideradpter;
    private TextView[] mDots;
    CardView cardView;
    byte[] imageBytes;
    Bitmap bitmap;
    ImageView userpic;
    private long backPressesTime;
    int IMG_REQUEST = 1;
    alert lt;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mSlideViewPager = (ViewPager) findViewById(R.id.pageview);
        mDotLayout = (LinearLayout) findViewById(R.id.dots);
        cardView = (CardView) findViewById(R.id.cardview);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor1 = sharedPref.edit();
        editor1.putBoolean("bt", true);
        editor1.putBoolean("lt", true);
        editor1.putBoolean("dt", true);
        editor1.apply();
        new alert(home.this).run();
//        Intent intent = new Intent(getApplicationContext(), petfeedingalert.class);
//        startService(intent);

        slideradpter = new slideradpter(home.this);
        mSlideViewPager.setAdapter(slideradpter);
        animation= AnimationUtils.loadAnimation(home.this,R.anim.rotateimage);
        mSlideViewPager.setAnimation(animation);
        addDots(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
        marquee = (TextView) findViewById(R.id.marqueeText);
        marquee.setSelected(true);

        final SharedPreferences sharedPre = PreferenceManager.getDefaultSharedPreferences(home.this);
        SharedPreferences.Editor editor = sharedPre.edit();
        editor.putBoolean("starttime", true);
        editor.putBoolean("endtime", true);
        editor.apply();
        new schedulealertrunnable(home.this).run();



        //mSlideViewPager.addOnPageChangeListener(viewlistner);

        //addpet=(Button) findViewById(R.id.addpet);
//        addpet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(home.this,petRegistration.class);
//                startActivity(intent);
//            }
//        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(home.this);
        userId = sharedPreferences.getString("userId", null);

        // tv=(TextView) findViewById(R.id.wlc);
//        viewTodayFoodPlan = (Button) findViewById(R.id.viewFood);
//        viewTodayFoodPlan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(home.this, currentFoodPlan.class);
//                startActivity(intent);
//            }
//        });
        // Str   ing welcomeName = getIntent().getExtras().getString("uname");
        // tv.setText(welcomeName);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bot);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        userpic = (ImageView) header.findViewById(R.id.userPic);
        setimage(userpic);
        userpic.setClickable(true);
        userpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectimge();
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setimage(final ImageView userpic) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    imagedata = jsonObject.getString("image");
                    if (imagedata != null) {
                        imageBytes = Base64.decode(imagedata, Base64.DEFAULT);
                        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        userpic.setImageBitmap(decodedImage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Toast.makeText(home.this, error.getMessage() + "in getting image function", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userId);
                return params;
            }
        };
        mysingleton.getmInstance(home.this).addtorequestque(stringRequest);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (backPressesTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressesTime = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

            if (id == R.id.logouts) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(home.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(home.this, logandsig.class);

               // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            Intent intent = new Intent(home.this, petRegistration.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(home.this, view_pets.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(home.this, map.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(home.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            Intent i = new Intent(home.this, logandsig.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        } else if (id == R.id.chatroom) {
            Intent intent = new Intent(home.this, chatroom.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addDots(int position) {
        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(home.this);
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
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        @Override
        public void onPageSelected(int i) {
            addDots(i);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void selectimge() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                saveimage(userId);
                userpic.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveimage(final String userid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String mesaage = jsonObject.getString("message");
                    if (mesaage.equals("saveimage")) {
                        Toast.makeText(home.this, "your image is updated now", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(home.this, error.getMessage() + "in image save function", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("imagename", userid);
                params.put("image", imagetostring(bitmap));
                return params;
            }
        };
        mysingleton.getmInstance(home.this).addtorequestque(stringRequest);
    }

    private String imagetostring(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

        byte[] imagByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imagByte, Base64.DEFAULT);
    }
}
