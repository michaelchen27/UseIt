package umn.useit.prehome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import umn.useit.R;

public class SliderAdapter extends PagerAdapter {

    //Arrays
    public int[] slide_images = {
            R.drawable.it_support,
            R.drawable.clock,
            R.drawable.phone
    };
    public String[] slide_headings = {
            "Welcome to UseIt",
            "Chat Platform",
            "Easy access"
    };
    public String[] slide_descs = {
            "Solve your IT related problems with the help of experts",
            "Chat with experts from around the world",
            "Get support using only your smartphone"
    };
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageVIew = view.findViewById(R.id.slide_image);
        TextView slideHeading = view.findViewById(R.id.slide_heading);
        TextView slideDescription = view.findViewById(R.id.slide_desc);

        slideImageVIew.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }

}
