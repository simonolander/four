package se.olander.android.four;


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
    private static final String COLOR_MAP_KEY = "COLOR_MAP_KEY";

    private long totalTime;
    private LevelDto level;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException("Arguments are null, user newInstance()");
        }
        level = (LevelDto) args.getSerializable(LEVEL_KEY);
        totalTime = args.getLong(TOTAL_TIME_KEY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_victory, container, false);

        TextView totalTimeTextView = view.findViewById(R.id.total_time);
        totalTimeTextView.setText(FormatUtils.formatHHmmssms(totalTime));

        Button nextLevelButton = view.findViewById(R.id.nextLevel);
        final LevelDto nextLevel = LevelUtils.getNextLevel(getContext(), level);
        nextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(LevelFragment.newInstance(nextLevel));
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

    public static VictoryScreenFragment newInstance(LevelDto level, long totalTime) {
        Bundle args = new Bundle();
        args.putSerializable(LEVEL_KEY, level);
        args.putLong(TOTAL_TIME_KEY, totalTime);
        VictoryScreenFragment fragment = new VictoryScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
