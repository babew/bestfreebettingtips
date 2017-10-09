package tips.betting.com.bestfreebettingtips.globalClasses;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tips.betting.com.bestfreebettingtips.models.Tip;

public class DownloadDataRunnable implements Runnable {
    public static final int COMMAND_GET_TIPS            = 0;
    public static final int COMMAND_GET_ARCHIVES        = 1;
    public static final int COMMAND_GET_MORE_ARCHIVES   = 2;

    private static final String TAG_AWAY_TEAM               = "AwayTeam";
    private static final String TAG_COEFFICIENT             = "Coefficent";
    private static final String TAG_DATE                    = "Date";
    private static final String TAG_FORECAST                = "ForeCast";
    private static final String TAG_FORECASTS               = "Forecasts";
    private static final String TAG_HOME_TEAM               = "HomeTeam";
    private static final String TAG_ID                      = "ID";
    private static final String TAG_LEFT_FORECASTS_COUNT    = "leftForecastsCount";
    private static final String TAG_RESULT                  = "Result";
    private static final String TAG_SUCCESS                 = "Success";
    private static final String TAG_TOURNAMENT              = "Tournament";

    private int                 mCommand;
    private Context             mContext;
    private DownloadListener    mListener;
    private String              mDate               = "";
    private String              mUrl                = "http://besttipscontrol.com/api/forecast/getforecasts?showOldForecasts=";

    public interface DownloadListener {
        void onDownloadCompleted(int i, ArrayList<Tip> arrayList, int i2);
    }

    public DownloadDataRunnable(Context context, String url, int command, String date, DownloadListener listener) {
        mContext    = context;
        mUrl        += url;
        mCommand    = command;
        mListener   = listener;
        mDate       = date;
    }

    public void run() {
        BufferedReader bufferedReader;
        MalformedURLException e;
        IOException e2;
        if (IsNetworkAvailable()) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(mUrl).openConnection();
                connection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                try {
                    StringBuffer buffer = new StringBuffer();
                    String str = "";
                    while (true) {
                        str = reader.readLine();
                        if (str != null) {
                            buffer.append(str + "\n");
                        } else {
                            getData(buffer.toString());
                            bufferedReader = reader;
                            return;
                        }
                    }
                } catch (MalformedURLException e3) {
                    e = e3;
                    bufferedReader = reader;
                    showError();
                    e.printStackTrace();
                    return;
                } catch (IOException e4) {
                    e2 = e4;
                    bufferedReader = reader;
                    showError();
                    e2.printStackTrace();
                    return;
                }
            } catch (MalformedURLException e5) {
                e = e5;
                showError();
                e.printStackTrace();
                return;
            } catch (IOException e6) {
                e2 = e6;
                showError();
                e2.printStackTrace();
                return;
            }
        }
        showError();
    }

    public void getData(String result) {
        int leftDataCount = 0;
        try {
            ArrayList<Tip>  dataArrayList   = new ArrayList();
            JSONObject      jsonObject      = new JSONObject(result);
            JSONArray       jsonArray       = jsonObject.getJSONArray(TAG_FORECASTS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tipJSON = jsonArray.getJSONObject(i);
                if (!mDate.equalsIgnoreCase(tipJSON.getString(TAG_DATE).substring(0, 10))) {
                    mDate = tipJSON.getString(TAG_DATE).substring(0, 10);
                    dataArrayList.add(new Tip(null, null, null, tipJSON.getString(TAG_DATE), null, null, null, null, null));
                }
                dataArrayList.add(new Tip(tipJSON.getString(TAG_ID), tipJSON.getString(TAG_HOME_TEAM), tipJSON.getString(TAG_AWAY_TEAM), tipJSON.getString(TAG_DATE), tipJSON.getString(TAG_RESULT), tipJSON.getString(TAG_TOURNAMENT), tipJSON.getString(TAG_COEFFICIENT), tipJSON.getString(TAG_FORECAST), tipJSON.getString(TAG_SUCCESS)));
            }
            if (mCommand != 0) {
                leftDataCount = jsonObject.getInt(TAG_LEFT_FORECASTS_COUNT);
            }
            if (mListener != null) {
                mListener.onDownloadCompleted(mCommand, dataArrayList, leftDataCount);
            }
        } catch (JSONException e) {
            try {
                Toast.makeText(mContext, "No internet connection", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (Exception ex) {}
        }
    }

    private boolean IsNetworkAvailable() {
        NetworkInfo netInfo = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showError() {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Toast.makeText(mContext, "No internet connection", Toast.LENGTH_LONG).show();
                } catch (Exception e) {}
            }
        });
    }
}