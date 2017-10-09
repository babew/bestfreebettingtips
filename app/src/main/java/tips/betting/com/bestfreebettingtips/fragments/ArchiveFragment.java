package tips.betting.com.bestfreebettingtips.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import tips.betting.com.bestfreebettingtips.R;
import tips.betting.com.bestfreebettingtips.adapters.ArchivesRecyclerAdapter;
import tips.betting.com.bestfreebettingtips.globalClasses.DownloadDataRunnable;
import tips.betting.com.bestfreebettingtips.globalClasses.DownloadDataRunnable.DownloadListener;
import tips.betting.com.bestfreebettingtips.models.Tip;

public class ArchiveFragment extends Fragment implements DownloadListener {
    private TextView                    mNoArchivesTextView;
    private RecyclerView                mRecyclerView;
    private SwipeRefreshLayout          mSwipeRefreshLayout;
    private ArchivesRecyclerAdapter     mAdapter;
    private ArrayList<Tip>              mArchivesArrayList;
    private LinearLayoutManager         mLayoutManager;

    private int         mPastVisibleItems;
    private int         mTotalItemsCount;
    private int         mVisibleItemsCount;
    private int         mLeftDataCount          = 20;
    private boolean     mLoading                = false;

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
        mNoArchivesTextView     = view.findViewById(R.id.no_data_text_view);
        mRecyclerView           = view.findViewById(R.id.recycler_view);
        mLayoutManager          = new LinearLayoutManager(getContext());
        mAdapter                = new ArchivesRecyclerAdapter(mArchivesArrayList);

        if (mArchivesArrayList.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mNoArchivesTextView.setVisibility(View.VISIBLE);
            mNoArchivesTextView.setText("No archives at this moment.");
        }

        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getArchives();
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
        mRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    mVisibleItemsCount  = mLayoutManager.getChildCount();
                    mTotalItemsCount    = mLayoutManager.getItemCount();
                    mPastVisibleItems   = mLayoutManager.findFirstVisibleItemPosition();
                    if (mVisibleItemsCount + mPastVisibleItems >= mTotalItemsCount && !mLoading && mLeftDataCount > 0) {
                        mLoading = true;
                        getMoreArchives();
                    }
                }
            }
        });
    }

    private void getData() {
        this.mArchivesArrayList = getArguments().getParcelableArrayList("archives");
    }

    private void getArchives() {
        new Thread(new DownloadDataRunnable(getActivity(), "true", DownloadDataRunnable.COMMAND_GET_ARCHIVES, "", this)).start();
    }

    private void getMoreArchives() {
        Tip lastArchive = mArchivesArrayList.get(mArchivesArrayList.size() - 1);
        new Thread(new DownloadDataRunnable(getActivity(), "true&id=" + lastArchive.getmId(), DownloadDataRunnable.COMMAND_GET_MORE_ARCHIVES, lastArchive.getmDate().substring(0, 10), this)).start();
        mArchivesArrayList.add(null);
        mAdapter.notifyDataSetChanged();
    }

    public void onDownloadCompleted(final int command, final ArrayList<Tip> data, final int leftDataCount) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        mLeftDataCount = leftDataCount;
                        if (command == DownloadDataRunnable.COMMAND_GET_MORE_ARCHIVES) {
                            mArchivesArrayList.remove(mArchivesArrayList.size() - 1);
                            mArchivesArrayList.addAll(data);
                            mAdapter.notifyDataSetChanged();
                            mLoading = false;
                        } else if (command == DownloadDataRunnable.COMMAND_GET_ARCHIVES) {
                            mArchivesArrayList.clear();
                            mArchivesArrayList.addAll(data);
                            mAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    } catch (Exception e) {}
                }
            });
        } catch (Exception e) {}
    }
}