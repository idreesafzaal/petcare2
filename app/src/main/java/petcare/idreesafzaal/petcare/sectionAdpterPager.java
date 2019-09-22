package petcare.idreesafzaal.petcare;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class sectionAdpterPager extends FragmentPagerAdapter {
    public sectionAdpterPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                userschatfragment muserschatfragment = new userschatfragment();
                return muserschatfragment;
            case 1:
                doctorsuggestionshowtouserfragment mdoctorchatfragment = new doctorsuggestionshowtouserfragment();
                return mdoctorchatfragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "User Chat";
            case 1:
                return "Doctor,s Suggestions";
            default:
                return null;
        }

    }
}
