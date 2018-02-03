package se.olander.android.four;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class LevelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView levelName;

    private LevelDto level;
    private OnItemClickListener onItemClickListener;

    LevelViewHolder(View view) {
        super(view);
        levelName = view.findViewById(R.id.level_name);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(getLevel());
        }
    }

    public void setLevel(LevelDto level) {
        this.level = level;
        this.levelName.setText(level.getName());
    }

    public LevelDto getLevel() {
        return level;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(LevelDto level);
    }
}
