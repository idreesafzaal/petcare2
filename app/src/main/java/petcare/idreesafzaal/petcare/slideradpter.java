package petcare.idreesafzaal.petcare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class slideradpter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public slideradpter(Context context) {

        this.context = context;
    }

    public int[] slider_image = {
            R.drawable.petcaredogandcat,
            R.drawable.petdoc,
            R.drawable.dogshower,

    };
    public String[] silder_heading = {
            "PET CARE",
            "PET DOCTOR",
            "PET SHOWER",
    };

    @Override
    public int getCount() {

        return silder_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);
        ImageView SlideImageView = (ImageView) view.findViewById(R.id.img);
        TextView SlideHeading = (TextView) view.findViewById(R.id.heading);

        SlideImageView.setImageResource(slider_image[position]);
        SlideHeading.setText(silder_heading[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
