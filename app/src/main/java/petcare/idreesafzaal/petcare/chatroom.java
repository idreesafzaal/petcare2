package petcare.idreesafzaal.petcare;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

public class chatroom extends AppCompatActivity {
    private Toolbar mtoolbar;
    ViewPager mviewPager;
    TabLayout tabLayout;
    sectionAdpterPager msectionAdpterPager;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
//        mtoolbar=(Toolbar) findViewById(R.id.main_page_toolbar);

        mviewPager = (ViewPager) findViewById(R.id.mainPager);
        tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        msectionAdpterPager = new sectionAdpterPager(getSupportFragmentManager());
        mviewPager.setAdapter(msectionAdpterPager);
        tabLayout.setupWithViewPager(mviewPager);
    }
}
