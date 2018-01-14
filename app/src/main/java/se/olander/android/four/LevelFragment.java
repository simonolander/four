package se.olander.android.four;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class LevelFragment extends Fragment {

    private static final String LEVEL_KEY = "LEVEL_KEY";

    private static final String TAG = LevelFragment.class.getSimpleName();

    private PaintingView paintingView;
    private FloatingActionButton colorButton1;
    private FloatingActionButton colorButton2;
    private FloatingActionButton colorButton3;
    private FloatingActionButton colorButton4;
    private FloatingActionButton colorClearButton;
    private Button doneButton;

    private LevelDto level;

    private long timeStart;

    private View currentSelectedColorButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException("Arguments can not be null, use " + TAG + ".newInstance(LevelDto)");
        }
        level = (LevelDto) args.getSerializable(LEVEL_KEY);
        timeStart = System.currentTimeMillis();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level, container, false);

        paintingView = view.findViewById(R.id.painting);
        paintingView.setColors(LevelUtils.getColor1(), LevelUtils.getColor2(), LevelUtils.getColor3(), LevelUtils.getColor4());
        paintingView.setPainting(level.getPainting());
        paintingView.setOnRegionClickListener(new PaintingView.OnRegionClickListener() {
            @Override
            public void onRegionClick(Painting.PaintRegion region) {
                if (currentSelectedColorButton == colorButton1) {
                    paintingView.setRegionColor(region, Colour.COLOUR_1);
                }
                if (currentSelectedColorButton == colorButton2) {
                    paintingView.setRegionColor(region, Colour.COLOUR_2);
                }
                if (currentSelectedColorButton == colorButton3) {
                    paintingView.setRegionColor(region, Colour.COLOUR_3);
                }
                if (currentSelectedColorButton == colorButton4) {
                    paintingView.setRegionColor(region, Colour.COLOUR_4);
                }
                if (currentSelectedColorButton == colorClearButton) {
                    paintingView.setRegionColor(region, null);
                }
            }
        });

        colorButton1 = view.findViewById(R.id.color_1);
        colorButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onColorButtonClick(view);
            }
        });
        colorButton1.setBackgroundTintList(ColorStateList.valueOf(LevelUtils.getColor1()));
        colorButton1.setSize(FloatingActionButton.SIZE_MINI);

        colorButton2 = view.findViewById(R.id.color_2);
        colorButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onColorButtonClick(view);
            }
        });
        colorButton2.setBackgroundTintList(ColorStateList.valueOf(LevelUtils.getColor2()));
        colorButton2.setSize(FloatingActionButton.SIZE_MINI);

        colorButton3 = view.findViewById(R.id.color_3);
        colorButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onColorButtonClick(view);
            }
        });
        colorButton3.setBackgroundTintList(ColorStateList.valueOf(LevelUtils.getColor3()));
        colorButton3.setSize(FloatingActionButton.SIZE_MINI);

        colorButton4 = view.findViewById(R.id.color_4);
        colorButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onColorButtonClick(view);
            }
        });
        colorButton4.setBackgroundTintList(ColorStateList.valueOf(LevelUtils.getColor4()));
        colorButton4.setSize(FloatingActionButton.SIZE_MINI);

        colorClearButton = view.findViewById(R.id.color_clear);
        colorClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onColorButtonClick(view);
            }
        });
        colorClearButton.setSize(FloatingActionButton.SIZE_MINI);

        doneButton = view.findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Painting.PaintRegion> someDiscoloredRegions = paintingView.getPainting().getSomeDiscoloredRegions();
                if (someDiscoloredRegions.isEmpty()) {
                    victory();
                } else {
                    paintingView.transitionToRegions(someDiscoloredRegions);
                }
            }
        });

        onColorButtonClick(colorButton1);

        return view;
    }

    private void onColorButtonClick(View view) {
        colorButton1.setSize(FloatingActionButton.SIZE_MINI);
        colorButton2.setSize(FloatingActionButton.SIZE_MINI);
        colorButton3.setSize(FloatingActionButton.SIZE_MINI);
        colorButton4.setSize(FloatingActionButton.SIZE_MINI);
        colorClearButton.setSize(FloatingActionButton.SIZE_MINI);
        if (view == colorButton1) {
            colorButton1.setSize(FloatingActionButton.SIZE_NORMAL);
            currentSelectedColorButton = colorButton1;
        }
        if (view == colorButton2) {
            colorButton2.setSize(FloatingActionButton.SIZE_NORMAL);
            currentSelectedColorButton = colorButton2;
        }
        if (view == colorButton3) {
            colorButton3.setSize(FloatingActionButton.SIZE_NORMAL);
            currentSelectedColorButton = colorButton3;
        }
        if (view == colorButton4) {
            colorButton4.setSize(FloatingActionButton.SIZE_NORMAL);
            currentSelectedColorButton = colorButton4;
        }
        if (view == colorClearButton) {
            colorClearButton.setSize(FloatingActionButton.SIZE_NORMAL);
            currentSelectedColorButton = colorClearButton;
        }
    }

    private void victory() {
        Toast.makeText(getContext(), "All done :)", Toast.LENGTH_SHORT).show();
        FragmentManager manager = getFragmentManager();
        assert manager != null;
        manager.popBackStack();
        manager
            .beginTransaction()
            .replace(R.id.fragment_container, VictoryScreenFragment.newInstance(
                level,
               System.currentTimeMillis() - timeStart,
                paintingView.getColorArray()))
            .addToBackStack(null)
            .commit();
    }

    public static Fragment newInstance(@NonNull LevelDto level) {
        Bundle args = new Bundle();
        args.putSerializable(LEVEL_KEY, level);
        LevelFragment fragment = new LevelFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
