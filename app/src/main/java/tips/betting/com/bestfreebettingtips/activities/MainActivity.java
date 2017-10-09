package tips.betting.com.bestfreebettingtips.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import tips.betting.com.bestfreebettingtips.R;
import tips.betting.com.bestfreebettingtips.adapters.MainPagerAdapter;
import tips.betting.com.bestfreebettingtips.models.Tip;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private TabLayout           mTabLayout;
    private ViewPager           mViewPager;
    private MainPagerAdapter    mAdapter;
    private ArrayList<Tip>      mArchivesArrayList;
    private ArrayList<Tip>      mTipsArrayList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();
        init();
    }

    private void getData() {
        Intent intent = getIntent();
        mTipsArrayList      = intent.getParcelableArrayListExtra("tips");
        mArchivesArrayList  = intent.getParcelableArrayListExtra("archives");
    }

    private void init() {
        mTabLayout  = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager  = (ViewPager) findViewById(R.id.view_pager);
        mAdapter    = new MainPagerAdapter(getSupportFragmentManager(), this.mTipsArrayList, this.mArchivesArrayList);

        mViewPager.setAdapter(this.mAdapter);
        mTabLayout.setupWithViewPager(this.mViewPager);
        findViewById(R.id.rate_us_layout).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (Exception e) {
            try {
                Toast.makeText(this, "Page cannot be loaded", Toast.LENGTH_LONG).show();
            } catch (Exception ex) {}
        }
    }
}