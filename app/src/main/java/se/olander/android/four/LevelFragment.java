package se.olander.android.four;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.List;
import java.util.Stack;

public class LevelFragment extends Fragment {

    private static final String LEVEL_KEY = "LEVEL_KEY";

    private static final String TAG = LevelFragment.class.getSimpleName();

    private PaintingView paintingView;
    private FloatingActionButton colorButton1;
    private FloatingActionButton colorButton2;
    private FloatingActionButton colorButton3;
    private FloatingActionButton colorButton4;
    private FloatingActionButton colorClearButton;
    private FloatingActionButton doneButton;
    private FloatingActionButton undoButton;

    private LevelDto level;

    private long timeStart;

    private View currentSelectedColorButton;

    private Stack<Colour[]> colorHistory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException("Arguments can not be null, use " + TAG + ".newInstance(LevelDto)");
        }
        level = (LevelDto) args.getSerializable(LEVEL_KEY);
        timeStart = System.currentTimeMillis();
        colorHistory = new Stack<>();
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
                Colour currentColor = paintingView.getPainting().getColor(region), nextColor;
                if (currentSelectedColorButton == colorButton1) {
                    nextColor = Colour.COLOUR_1;
                }
                else if (currentSelectedColorButton == colorButton2) {
                    nextColor = Colour.COLOUR_2;
                }
                else if (currentSelectedColorButton == colorButton3) {
                    nextColor = Colour.COLOUR_3;
                }
                else if (currentSelectedColorButton == colorButton4) {
                    nextColor = Colour.COLOUR_4;
                }
                else if (currentSelectedColorButton == colorClearButton) {
                    nextColor = null;
                }
                else {
                    return;
                }

                if (currentColor != nextColor) {
                    colorHistory.push(paintingView.getColorArray());
                    paintingView.setRegionColor(region, nextColor);
                    updateDoneButton();
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

        undoButton = view.findViewById(R.id.undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!colorHistory.isEmpty()) {
                    Colour[] colors = colorHistory.pop();
                    paintingView.setColorArray(colors);
                    updateDoneButton();
                }
            }
        });

        onColorButtonClick(colorButton1);
        updateDoneButton();

        return view;
    }

    private void updateDoneButton() {
        if (paintingView.getPainting().hasDiscoloredRegions()) {
            doneButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_orange_dark)));
        }
        else {
            doneButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_green_dark)));
        }
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
        SavedGame savedGame = new SavedGame(
            level,
            System.currentTimeMillis() - timeStart,
            paintingView.getColorArray()
        );
        saveGame(savedGame);
        FragmentManager manager = getFragmentManager();
        assert manager != null;
        manager.popBackStack();
        manager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                VictoryScreenFragment.newInstance(savedGame))
            .addToBackStack(null)
            .commit();
    }

    private void saveGame(SavedGame game) {
        SaveGameUtils.saveGame(getContext(), game);
    }

    public static Fragment newInstance(@NonNull LevelDto level) {
        Bundle args = new Bundle();
        args.putSerializable(LEVEL_KEY, level);
        LevelFragment fragment = new LevelFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
