package se.olander.android.four;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class LevelFragment extends Fragment {

    private static final String LEVEL_KEY = "LEVEL_KEY";

    private static final String TAG = LevelFragment.class.getSimpleName();

    private FloatingActionButton colorButton1;
    private FloatingActionButton colorButton2;
    private FloatingActionButton colorButton3;
    private FloatingActionButton colorButton4;
    private FloatingActionButton colorClearButton;
    private Button doneButton;

    private int color1 = 0xffff867c;
    private int color2 = 0xff99d066;
    private int color3 = 0xfffff263;
    private int color4 = 0xff5eb8ff;

    private LevelDto level;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException("Arguments can not be null, use " + TAG + ".newInstance(LevelDto)");
        }
        this.level = (LevelDto) args.getSerializable(LEVEL_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level, container, false);

        final PaintingView painting = view.findViewById(R.id.painting);
        painting.setPainting(level.getPainting());
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

        doneButton = view.findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Painting.PaintRegion> someDiscoloredRegions = painting.getPainting().getSomeDiscoloredRegions();
                if (!someDiscoloredRegions.isEmpty()) {
                    painting.transitionToRegions(someDiscoloredRegions);
                }
                else {
                    Toast.makeText(getContext(), "All done :)", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    public static Fragment newInstance(@NonNull LevelDto level) {
        Bundle args = new Bundle();
        args.putSerializable(LEVEL_KEY, level);
        LevelFragment fragment = new LevelFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
