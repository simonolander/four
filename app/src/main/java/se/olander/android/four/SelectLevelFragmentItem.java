package se.olander.android.four;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class SelectLevelFragmentItem extends Fragment implements LevelViewHolder.OnItemClickListener {

    private static final String TAG = SelectLevelFragmentItem.class.getSimpleName();

    private RecyclerView recyclerView;
    private LevelAdapterItem adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_level, container, false);

        List<LevelDto> levels = LevelUtils.getLevels(getContext());
        adapter = new LevelAdapterItem(levels);
        adapter.setOnItemClickListener(this);

        layoutManager = new LinearLayoutManager(getContext());

        recyclerView = view.findViewById(R.id.list_levels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    public static SelectLevelFragmentItem newInstance() {
        return new SelectLevelFragmentItem();
    }

    @Override
    public void onItemClick(LevelDto level) {
        getFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, LevelFragment.newInstance(level))
            .addToBackStack(null)
            .commit();
    }
}


