package se.olander.android.four;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class VictoryScreenFragment extends Fragment {
    private static final String LEVEL_KEY = "LEVEL_KEY";
    private static final String TOTAL_TIME_KEY = "TOTAL_TIME_KEY";
    private static final String COLOR_ARRAY_KEY = "COLOR_ARRAY_KEY";

    private long totalTime;
    private LevelDto level;
    private Colour[] colorArray;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException("Arguments are null, user newInstance()");
        }
        level = (LevelDto) args.getSerializable(LEVEL_KEY);
        totalTime = args.getLong(TOTAL_TIME_KEY);
        colorArray = (Colour[]) args.getSerializable(COLOR_ARRAY_KEY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_victory, container, false);
        Context context = getContext();
        assert context != null;

        TextView totalTimeTextView = view.findViewById(R.id.total_time);
        totalTimeTextView.setText(FormatUtils.formatHHmmssms(totalTime));

        TextView numberOfRegionsTextView = view.findViewById(R.id.number_of_regions);
        numberOfRegionsTextView.setText(FormatUtils.formatNumber(colorArray.length));

        int numberOfColor1 = MathUtils.countOccurrences(colorArray, Colour.COLOUR_1);
        TextView numberOfColor1TextView = view.findViewById(R.id.number_of_color_1);
        numberOfColor1TextView.setText(FormatUtils.formatNumber(numberOfColor1));

        int numberOfColor2 = MathUtils.countOccurrences(colorArray, Colour.COLOUR_2);
        TextView numberOfColor2TextView = view.findViewById(R.id.number_of_color_2);
        numberOfColor2TextView.setText(FormatUtils.formatNumber(numberOfColor2));

        int numberOfColor3 = MathUtils.countOccurrences(colorArray, Colour.COLOUR_3);
        TextView numberOfColor3TextView = view.findViewById(R.id.number_of_color_3);
        numberOfColor3TextView.setText(FormatUtils.formatNumber(numberOfColor3));

        int numberOfColor4 = MathUtils.countOccurrences(colorArray, Colour.COLOUR_4);
        TextView numberOfColor4TextView = view.findViewById(R.id.number_of_color_4);
        numberOfColor4TextView.setText(FormatUtils.formatNumber(numberOfColor4));

        Button nextLevelButton = view.findViewById(R.id.nextLevel);
        final LevelDto nextLevel = LevelUtils.getNextLevel(context, level);
        nextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(nextLevel != null ? LevelFragment.newInstance(nextLevel) : SelectLevelFragment.newInstance());
            }
        });
        if (nextLevel == null) {
            nextLevelButton.setEnabled(false);
        }

        Button replayButton = view.findViewById(R.id.replay);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(LevelFragment.newInstance(level));
            }
        });

        Button selectLevelButton = view.findViewById(R.id.select_levels);
        selectLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(SelectLevelFragment.newInstance());
            }
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        manager.popBackStack();
        manager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit();
    }

    public static VictoryScreenFragment newInstance(LevelDto level, long totalTime, Colour[] colorArray) {
        Bundle args = new Bundle();
        args.putSerializable(LEVEL_KEY, level);
        args.putLong(TOTAL_TIME_KEY, totalTime);
        args.putSerializable(COLOR_ARRAY_KEY, colorArray);
        VictoryScreenFragment fragment = new VictoryScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
