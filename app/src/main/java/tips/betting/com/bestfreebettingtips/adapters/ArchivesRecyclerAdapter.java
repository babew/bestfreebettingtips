package tips.betting.com.bestfreebettingtips.adapters;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tips.betting.com.bestfreebettingtips.R;
import tips.betting.com.bestfreebettingtips.models.Tip;

public class ArchivesRecyclerAdapter extends Adapter<ArchivesRecyclerAdapter.ViewHolder> {
    private static final int VIEW_TYPE_TIP          = 0;
    private static final int VIEW_TYPE_DATE         = 1;
    private static final int VIEW_TYPE_PROGRESS     = 2;

    private ArrayList<Tip>      mArchivesArrayList;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private TextView    mAwayTeamTextView;
        private TextView    mCoefficientTextView;
        private TextView    mDateTextView;
        private TextView    mHomeTeamTextView;
        private TextView    mScoreTextView;
        private ImageView   mSuccessImageView;
        private TextView    mTipTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mHomeTeamTextView       = itemView.findViewById(R.id.home_team_text_view);
            mAwayTeamTextView       = itemView.findViewById(R.id.away_team_text_view);
            mScoreTextView          = itemView.findViewById(R.id.score_text_view);
            mTipTextView            = itemView.findViewById(R.id.tip_text_view);
            mCoefficientTextView    = itemView.findViewById(R.id.coefficient_text_view);
            mDateTextView           = itemView.findViewById(R.id.date_text_view);
            mSuccessImageView       = itemView.findViewById(R.id.success_image_view);
        }
    }

    public ArchivesRecyclerAdapter(ArrayList<Tip> archives) {
        this.mArchivesArrayList = archives;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == VIEW_TYPE_TIP)
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_archive, parent, false);
        else if (viewType == VIEW_TYPE_DATE)
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);

        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Tip tip = mArchivesArrayList.get(position);
        if (tip != null) {
            if (tip.getmId() == null) {
                holder.mDateTextView.setText(formatDate(tip.getmDate()));
                return;
            }
            holder.mHomeTeamTextView.setText(tip.getmHomeTeam());
            holder.mAwayTeamTextView.setText(tip.getmAwayTeam());
            holder.mScoreTextView.setText(tip.getmScore());
            holder.mTipTextView.setText("Tip: " + tip.getmTip());
            holder.mCoefficientTextView.setText("Coefficient: " + tip.getmCoefficient());
            holder.mSuccessImageView.setImageResource(tip.getmSuccess().equalsIgnoreCase("1") ? R.drawable.success : R.drawable.failed);
        }
    }

    private String formatDate(String date) {
        return date.substring(8, 10) + "." + date.substring(5, 7) + "." + date.substring(0, 4);
    }

    public int getItemCount() {
        return this.mArchivesArrayList.size();
    }

    public int getItemViewType(int position) {
        if (this.mArchivesArrayList.get(position) == null)
            return VIEW_TYPE_PROGRESS;

        if (mArchivesArrayList.get(position).getmId() == null)
            return VIEW_TYPE_DATE;

        return VIEW_TYPE_TIP;
    }
}