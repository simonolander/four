package se.olander.android.four;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


        return view;
    }

    public static Fragment newInstance(Level level) {
        return new LevelFragment();
    }
}
