package se.olander.android.four;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;


public class SelectLevelFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = SelectLevelFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_level, container, false);
        ListView levelListView = view.findViewById(R.id.list_levels);

        List<LevelDto> levels = LevelUtils.getLevels(getContext());
        LevelListAdapter levelListAdapter = new LevelListAdapter(getContext());
        levelListAdapter.addAll(levels);
        levelListView.setAdapter(levelListAdapter);
        levelListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object item = parent.getItemAtPosition(position);
        if (item instanceof LevelDto) {
            LevelDto level = (LevelDto) item;
            getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, LevelFragment.newInstance(level))
                .addToBackStack(null)
                .commit();
        }
    }
}


