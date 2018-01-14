package se.olander.android.four;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;


public class SelectLevelFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = SelectLevelFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_level, container, false);

        List<LevelDto> levels = LevelUtils.getLevels(getContext());
        adapter = new LevelAdapter(levels);

        layoutManager = new LinearLayoutManager(getContext());

        recyclerView = view.findViewById(R.id.list_levels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

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

    public static SelectLevelFragment newInstance() {
        return new SelectLevelFragment();
    }
}


