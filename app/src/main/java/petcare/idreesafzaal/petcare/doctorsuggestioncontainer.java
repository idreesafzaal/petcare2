package petcare.idreesafzaal.petcare;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class doctorsuggestioncontainer extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;
    doctorsectionpageradpter pager;

    public doctorsuggestioncontainer() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctorsuggestioncontainer, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.mainPager);
        tabLayout = (TabLayout) view.findViewById(R.id.main_tabs);
        pager = new doctorsectionpageradpter(getFragmentManager());
        viewPager.setAdapter(pager);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
