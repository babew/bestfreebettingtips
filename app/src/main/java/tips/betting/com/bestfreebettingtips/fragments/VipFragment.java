package tips.betting.com.bestfreebettingtips.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import tips.betting.com.bestfreebettingtips.R;
import tips.betting.com.bestfreebettingtips.adapters.TipsRecyclerAdapter;
import tips.betting.com.bestfreebettingtips.globalClasses.DownloadDataRunnable;
import tips.betting.com.bestfreebettingtips.models.Tip;

/**
 * Created by lyubomir.babev on 04/10/2017.
 */

public class VipFragment extends Fragment implements DownloadDataRunnable.DownloadListener {
    private TextView                    mNoTipsTextView;
    private RecyclerView                mRecyclerView;
    private SwipeRefreshLayout          mSwipeRefreshLayout;
    private TipsRecyclerAdapter         mAdapter;
    private RecyclerView.LayoutManager  mLayoutManager;
    private ArrayList<Tip>              mTipsArrayList;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
        init(view);
    }

    private void init(View view) {
        mSwipeRefreshLayout     = view.findViewById(R.id.swipe_refresh_layout);
        mNoTipsTextView         = view.findViewById(R.id.no_data_text_view);
        mRecyclerView           = view.findViewById(R.id.recycler_view);
        mLayoutManager          = new LinearLayoutManager(getContext());
        mAdapter                = new TipsRecyclerAdapter(mTipsArrayList);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTips();
            }
        });

        try {
            Field f = mSwipeRefreshLayout.getClass().getDeclaredField("mCircleView");
            f.setAccessible(true);
            ((ImageView) f.get(mSwipeRefreshLayout)).setImageResource(R.drawable.ball);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            mSwipeRefreshLayout.setColorSchemeResources(R.color.black);
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            mSwipeRefreshLayout.setColorSchemeResources(R.color.black);
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        if (mTipsArrayList.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mNoTipsTextView.setVisibility(View.VISIBLE);
            mNoTipsTextView.setText("No tips at this moment.");
        }
    }

    private void getData() {
        mTipsArrayList = getArguments().getParcelableArrayList("tips");
    }

    private void getTips() {
        new Thread(new DownloadDataRunnable(getActivity(), "false", DownloadDataRunnable.COMMAND_GET_TIPS, "", this)).start();
    }

    public void onDownloadCompleted(final int command, final ArrayList<Tip> data, int leftDataCount) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        if (command == DownloadDataRunnable.COMMAND_GET_TIPS) {
                            mTipsArrayList.clear();
                            mTipsArrayList.addAll(data);
                            mAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    } catch (Exception e) {}
                }
            });
        } catch (Exception e) {}
    }
}