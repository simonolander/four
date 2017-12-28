package se.olander.android.four;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LevelFragment extends Fragment {

    private FloatingActionButton colorButton1;
    private FloatingActionButton colorButton2;
    private FloatingActionButton colorButton3;
    private FloatingActionButton colorButton4;
    private FloatingActionButton colorClearButton;

    private int color1 = 0xffff867c;
    private int color2 = 0xff99d066;
    private int color3 = 0xfffff263;
    private int color4 = 0xff5eb8ff;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level, container, false);

        final PaintingView painting = view.findViewById(R.id.painting);
        painting.setPainting(Painting.somePainting());

        painting.setColors(color1, color2, color3, color4);

        colorButton1 = view.findViewById(R.id.color_1);
        colorButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                painting.setSelectedRegionColor(Colour.COLOUR_1);
            }
        });
        colorButton1.setBackgroundTintList(ColorStateList.valueOf(color1));

        colorButton2 = view.findViewById(R.id.color_2);
        colorButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                painting.setSelectedRegionColor(Colour.COLOUR_2);
            }
        });
        colorButton2.setBackgroundTintList(ColorStateList.valueOf(color2));

        colorButton3 = view.findViewById(R.id.color_3);
        colorButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                painting.setSelectedRegionColor(Colour.COLOUR_3);
            }
        });
        colorButton3.setBackgroundTintList(ColorStateList.valueOf(color3));

        colorButton4 = view.findViewById(R.id.color_4);
        colorButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                painting.setSelectedRegionColor(Colour.COLOUR_4);
            }
        });
        colorButton4.setBackgroundTintList(ColorStateList.valueOf(color4));

        colorClearButton = view.findViewById(R.id.color_clear);
        colorClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                painting.setSelectedRegionColor(null);
            }
        });


        return view;
    }

    public static Fragment newInstance(Level level) {
        return new LevelFragment();
    }
}
