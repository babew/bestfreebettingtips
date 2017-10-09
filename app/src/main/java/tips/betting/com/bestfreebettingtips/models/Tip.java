package tips.betting.com.bestfreebettingtips.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Tip implements Parcelable {
    private String mAwayTeam;
    private String mCoefficient;
    private String mDate;
    private String mHomeTeam;
    private String mId;
    private String mLeague;
    private String mScore;
    private String mSuccess;
    private String mTip;

    public Tip(String id, String mHomeTeam, String mAwayTeam, String mDate, String mScore, String mLeague, String mCoefficient, String mTip, String mSuccess) {
        this.mId            = id;
        this.mHomeTeam      = mHomeTeam;
        this.mAwayTeam      = mAwayTeam;
        this.mDate          = mDate;
        this.mScore         = mScore;
        this.mLeague        = mLeague;
        this.mCoefficient   = mCoefficient;
        this.mTip           = mTip;
        this.mSuccess       = mSuccess;
    }

    public String getmId() {
        return this.mId;
    }

    public String getmHomeTeam() {
        return this.mHomeTeam;
    }

    public String getmAwayTeam() {
        return this.mAwayTeam;
    }

    public String getmDate() {
        return this.mDate;
    }

    public String getmScore() {
        return this.mScore;
    }

    public String getmLeague() {
        return this.mLeague;
    }

    public String getmCoefficient() {
        return this.mCoefficient;
    }

    public String getmTip() {
        return this.mTip;
    }

    public String getmSuccess() {
        return this.mSuccess;
    }

    public int describeContents() {
        return 0;
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            return new Tip(parcel);
        }

        @Override
        public Object[] newArray(int i) {
            return new Tip[i];
        }
    };

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mId);
        parcel.writeString(this.mHomeTeam);
        parcel.writeString(this.mAwayTeam);
        parcel.writeString(this.mDate);
        parcel.writeString(this.mScore);
        parcel.writeString(this.mLeague);
        parcel.writeString(this.mCoefficient);
        parcel.writeString(this.mTip);
        parcel.writeString(this.mSuccess);
    }

    public Tip(Parcel parcel) {
        this.mId = parcel.readString();
        this.mHomeTeam = parcel.readString();
        this.mAwayTeam = parcel.readString();
        this.mDate = parcel.readString();
        this.mScore = parcel.readString();
        this.mLeague = parcel.readString();
        this.mCoefficient = parcel.readString();
        this.mTip = parcel.readString();
        this.mSuccess = parcel.readString();
    }
}