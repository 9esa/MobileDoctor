package mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by kirichenko on 28.04.2015.
 * Клас которые позволяет передавать информацию между intent.
 * Создается после авторизациич
 */
public class LoginAccount implements Parcelable {

    private Boolean bStatus;
    private String sDoctorId;

    private String sValueName;
    private String sValueDepName;
    private String sToken;

    private String sErrorCode;
    private String sErrorMessage;

    final static String LOG_TAG = "LoginAccountParcel";

    public LoginAccount(String sErrorCode, String sErrorMessage, boolean bStatus) {
        this.sErrorCode = sErrorCode;
        this.sErrorMessage = sErrorMessage;
        this.bStatus = bStatus;
    }

    public LoginAccount(boolean bStatus, String sDoctorId, String sValueName, String sValueDepName, String sToken) {
        this.bStatus = bStatus;
        this.sDoctorId = sDoctorId;
        this.sValueName = sValueName;
        this.sValueDepName = sValueDepName;
        this.sToken = sToken;
    }

    public LoginAccount(Parcel newParceble) {
        this.bStatus = newParceble.readByte() != 0;
        this.sDoctorId = newParceble.readString();
        this.sValueName = newParceble.readString();
        this.sValueDepName = newParceble.readString();
        this.sToken = newParceble.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeByte((byte) (bStatus ? 1 : 0));
        parcel.writeString(sDoctorId);
        parcel.writeString(sValueName);
        parcel.writeString(sValueDepName);
        parcel.writeString(sToken);

    }

    public static final Parcelable.Creator<LoginAccount> CREATOR = new Parcelable.Creator<LoginAccount>() {

        public LoginAccount createFromParcel(Parcel newParceble) {
            return new LoginAccount(newParceble);
        }

        public LoginAccount[] newArray(int size) {
            return new LoginAccount[size];
        }
    };

    public boolean getbStatus() {
        return bStatus;
    }

    public void setbStatus(boolean bStatus) {
        this.bStatus = bStatus;
    }

    public String getssDoctorId() {
        return sDoctorId;
    }

    public void setssDoctorId(String sDoctorId) {
        this.sDoctorId = sDoctorId;
    }

    public String getsValueName() {
        return sValueName;
    }

    public void setsValueName(String sValueName) {
        this.sValueName = sValueName;
    }

    public String getsValueDepName() {
        return sValueDepName;
    }

    public void setsValueDepName(String sValueDepName) {
        this.sValueDepName = sValueDepName;
    }

    public String getsToken() {
        return sToken;
    }

    public void setsToken(String sToken) {
        this.sToken = sToken;
    }

    public String getsErrorCode() {
        return sErrorCode;
    }

    public void setsErrorCode(String sErrorCode) {
        this.sErrorCode = sErrorCode;
    }

    public String getsErrorMessage() {
        return sErrorMessage;
    }

    public void setsErrorMessage(String sErrorMessage) {
        this.sErrorMessage = sErrorMessage;
    }
}
