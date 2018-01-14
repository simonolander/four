package se.olander.android.four;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {

    private List<LevelDto> levels;

    public LevelAdapter(List<LevelDto> levels) {
        this.levels = levels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_level, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LevelDto levelDto = levels.get(position);
        holder.levelName.setText(levelDto.getName());
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView levelName;

        ViewHolder(View levelName) {
            super(levelName);
            this.levelName = levelName.findViewById(R.id.level_name);
        }
    }
}
