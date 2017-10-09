package tips.betting.com.bestfreebettingtips.adapters;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tips.betting.com.bestfreebettingtips.R;
import tips.betting.com.bestfreebettingtips.models.Tip;

public class TipsRecyclerAdapter extends Adapter<TipsRecyclerAdapter.ViewHolder> {
    private static final int VIEW_TYPE_DATE     = 0;
    private static final int VIEW_TYPE_TIP      = 1;

    private ArrayList<Tip>      mTipsArrayList;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private TextView mAwayTeamTextView;
        private TextView mCoefficientTextView;
        private TextView mDateTextView;
        private TextView mHomeTeamTextView;
        private TextView mLeagueTextView;
        private TextView mTimeTextView;
        private TextView mTipTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mLeagueTextView         = itemView.findViewById(R.id.league_text_view);
            mDateTextView           = itemView.findViewById(R.id.date_text_view);
            mHomeTeamTextView       = itemView.findViewById(R.id.home_team_text_view);
            mAwayTeamTextView       = itemView.findViewById(R.id.away_team_text_view);
            mTipTextView            = itemView.findViewById(R.id.tip_text_view);
            mTimeTextView           = itemView.findViewById(R.id.time_text_view);
            mCoefficientTextView    = itemView.findViewById(R.id.coefficient_text_view);
        }
    }

    public TipsRecyclerAdapter(ArrayList<Tip> tips) {
        mTipsArrayList = tips;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == VIEW_TYPE_DATE)
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tip, parent, false);

        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Tip tip = mTipsArrayList.get(position);
        if (tip.getmId() == null) {
            holder.mDateTextView.setText(formatDate(tip.getmDate()));
            return;
        }
        holder.mLeagueTextView.setText(tip.getmLeague());
        holder.mHomeTeamTextView.setText(tip.getmHomeTeam());
        holder.mAwayTeamTextView.setText(tip.getmAwayTeam());
        holder.mTipTextView.setText("Tip: " + tip.getmTip());
        holder.mTimeTextView.setText(formatTime(tip.getmDate()));
        holder.mCoefficientTextView.setText("Coefficient: " + tip.getmCoefficient());
    }

    private String formatDate(String date) {
        return date.substring(8, 10) + "." + date.substring(5, 7) + "." + date.substring(0, 4);
    }

    private String formatTime(String date) {
        return date.substring(date.indexOf("T") + 1, date.length() - 3);
    }

    public int getItemViewType(int position) {
        if ((mTipsArrayList.get(position)).getmId() == null)
            return VIEW_TYPE_DATE;

        return VIEW_TYPE_TIP;
    }

    public int getItemCount() {
        return mTipsArrayList.size();
    }
}