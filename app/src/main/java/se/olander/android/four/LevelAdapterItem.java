package se.olander.android.four;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class LevelAdapterItem extends RecyclerView.Adapter<LevelViewHolder> implements LevelViewHolder.OnItemClickListener {

    private List<LevelDto> levels;
    private LevelViewHolder.OnItemClickListener onItemClickListener;

    public LevelAdapterItem(List<LevelDto> levels) {
        this.levels = levels;
    }

    @Override
    public LevelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_level, parent, false);
        LevelViewHolder levelViewHolder = new LevelViewHolder(view);
        levelViewHolder.setOnItemClickListener(this);

        return levelViewHolder;
    }

    @Override
    public void onBindViewHolder(LevelViewHolder holder, int position) {
        LevelDto levelDto = levels.get(position);
        holder.setLevel(levelDto);
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

    @Override
    public void onItemClick(LevelDto level) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(level);
        }
    }

    public void setOnItemClickListener(LevelViewHolder.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
