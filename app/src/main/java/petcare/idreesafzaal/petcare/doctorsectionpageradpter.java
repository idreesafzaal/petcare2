package petcare.idreesafzaal.petcare;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class doctorsectionpageradpter extends FragmentPagerAdapter {
    public doctorsectionpageradpter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                makesuggestion suggestion = new makesuggestion();
                return suggestion;
            case 1:
                viewaddsuggestion view = new viewaddsuggestion();
                return view;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Suggest something";
            case 1:
                return "View your suggestions";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
