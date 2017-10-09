package tips.betting.com.bestfreebettingtips.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;

import tips.betting.com.bestfreebettingtips.fragments.ArchiveFragment;
import tips.betting.com.bestfreebettingtips.fragments.TipsFragment;
import tips.betting.com.bestfreebettingtips.fragments.VipFragment;
import tips.betting.com.bestfreebettingtips.models.Tip;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    public static final int VIP_TIPS_INDEX  = 0;
    public static final int TIPS_INDEX      = 0;
    public static final int ARCHIVE_INDEX   = 1;
    public static final int TABS_COUNT      = 2;

    private VipFragment         mVipFragment;
    private TipsFragment        mTipsFragment;
    private ArchiveFragment     mArchiveFragment;
    private ArrayList<Tip>      mArchivesArrayList;
    private ArrayList<Tip>      mTipsArrayList;

    public MainPagerAdapter(FragmentManager fm, ArrayList<Tip> tips, ArrayList<Tip> archives) {
        super(fm);
        mTipsArrayList      = tips;
        mArchivesArrayList  = archives;
    }

    public Fragment getItem(int position) {
        switch (position) {
/*            case VIP_TIPS_INDEX:
                if (mVipFragment == null)
                    mVipFragment = new VipFragment();

                Bundle vipsBundle = new Bundle();
                vipsBundle.putParcelableArrayList("tips", this.mTipsArrayList);
                mVipFragment.setArguments(vipsBundle);
                return mVipFragment;*/
            case TIPS_INDEX:
                if (mTipsFragment == null)
                    mTipsFragment = new TipsFragment();

                Bundle tipsBundle = new Bundle();
                tipsBundle.putParcelableArrayList("tips", this.mTipsArrayList);
                mTipsFragment.setArguments(tipsBundle);
                return mTipsFragment;
            case ARCHIVE_INDEX:
                if (this.mArchiveFragment == null)
                    this.mArchiveFragment = new ArchiveFragment();

                Bundle archiveBundle = new Bundle();
                archiveBundle.putParcelableArrayList("archives", this.mArchivesArrayList);
                this.mArchiveFragment.setArguments(archiveBundle);
                return this.mArchiveFragment;
            default:
                if (this.mTipsFragment == null)
                    this.mTipsFragment = new TipsFragment();

                return this.mTipsFragment;
        }
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
/*            case VIP_TIPS_INDEX:
                return "VIP";*/
            case TIPS_INDEX:
                return "FREE";
            case ARCHIVE_INDEX:
                return "ARCHIVES";
            default:
                return "VIP";
        }
    }

    public int getCount() {
        return TABS_COUNT;
    }
}