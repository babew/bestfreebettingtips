package tips.betting.com.bestfreebettingtips.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import java.util.ArrayList;
import tips.betting.com.bestfreebettingtips.R;
import tips.betting.com.bestfreebettingtips.globalClasses.DownloadDataRunnable;
import tips.betting.com.bestfreebettingtips.globalClasses.DownloadDataRunnable.DownloadListener;
import tips.betting.com.bestfreebettingtips.models.Tip;

public class SplashActivity extends AppCompatActivity implements DownloadListener {
    private ArrayList<Tip> mArchiveArrayList = new ArrayList();
    private ImageView mBallImageView;
    private int mLeftDataCount;
    private long mTime;
    private ArrayList<Tip> mTipsArrayList = new ArrayList();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.mTime = System.currentTimeMillis();
        startBallAnimation();
        getTipsAndArchives(0);
    }

    private void startBallAnimation() {
        this.mBallImageView = (ImageView) findViewById(R.id.ball_image_view);
        TranslateAnimation translation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (getResources().getDisplayMetrics().heightPixels / 3));
        translation.setStartOffset(300);
        translation.setDuration(1500);
        translation.setFillAfter(true);
        translation.setInterpolator(new BounceInterpolator());
        this.mBallImageView.startAnimation(translation);
    }

    private void getTipsAndArchives(int command) {
        new Thread(new DownloadDataRunnable(this, command == 0 ? "false" : "true", command, "", this)).start();
    }

    private void checkDelay() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                if (SplashActivity.this.mTime + 2000 > currentTime) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SplashActivity.this.goToMainActivity();
                        }
                    }, (SplashActivity.this.mTime + 2000) - currentTime);
                } else {
                    SplashActivity.this.goToMainActivity();
                }
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putParcelableArrayListExtra("tips", this.mTipsArrayList);
        intent.putParcelableArrayListExtra("archives", this.mArchiveArrayList);
        intent.putExtra("leftDataCount", this.mLeftDataCount);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDownloadCompleted(int command, ArrayList<Tip> data, int leftDataCount) {
        if (command == 1) {
            this.mArchiveArrayList = data;
            this.mLeftDataCount = leftDataCount;
            checkDelay();
        } else if (command == 0) {
            this.mTipsArrayList = data;
            getTipsAndArchives(1);
        }
    }
}