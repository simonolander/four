package se.olander.android.four;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class LevelListAdapter extends ArrayAdapter<LevelDto> {
    public LevelListAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item_level, parent, false);
        }

        TextView title = view.findViewById(R.id.title);
        LevelDto level = getItem(position);
        if (level != null) {
            title.setText(level.name);
        }

        return view;
    }
}
