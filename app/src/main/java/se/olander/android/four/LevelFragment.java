package se.olander.android.four;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LevelFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level, container, false);

        PaintingView painting = view.findViewById(R.id.painting);
        painting.setPainting(Painting.somePainting());

        FloatingActionButton color1 = view.findViewById(R.id.color_one);
        color1.setOnClickListener(new View.OnClickListener() {
            private final String TAG = LevelFragment.class.getSimpleName();
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + view);
            }
        });


        return view;
    }

    public static Fragment newInstance(Level level) {
        return new LevelFragment();
    }
}
