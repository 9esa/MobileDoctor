package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls.my_calls_item;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kirichenko on 27.04.2015.
 * Элемент меню мои вызовы
 */
public class MenuItemMyCalls implements Parcelable {
    ///////////Элементы отображаемые на экране

    //Статус
    private int imgStatusComment;  //protocolStatus
    private int imgStatusReady; //status

    //Время
    private String textTime; //callDate

    //Пациент
    private String textNamePacient;  //firstName + lastName
    private String textAgePacient; //sex + age

    //Адрес
    private String textAddress;  //regAddress

    //Причина вызова
    private String textReason;  //note
    //Причина вызова диагноз
    private String textDiagnosis;

    ////////////

    //Номер вызова
    private String sId;

    public MenuItemMyCalls(Parcel newParceble) {
        super();

        this.imgStatusComment = newParceble.readInt();
        this.imgStatusReady = newParceble.readInt();

        this.textTime = newParceble.readString();

        this.textNamePacient = newParceble.readString();
        this.textAgePacient = newParceble.readString();

        this.textAddress = newParceble.readString();

        this.textReason = newParceble.readString();
        this.textDiagnosis = newParceble.readString();

    }

    public MenuItemMyCalls(int imgStatusComment, int imgStatusReady, String textTime, String textNamePacient, String textAgePacient, String textAddress, String textReason, String textDiagnosis) {

        super();

        this.imgStatusComment = imgStatusComment;
        this.imgStatusReady = imgStatusReady;
        this.textTime = textTime;
        this.textNamePacient = textNamePacient;
        this.textAgePacient = textAgePacient;
        this.textAddress = textAddress;
        this.textReason = textReason;
        this.textDiagnosis = textDiagnosis;
    }

    public int getImgStatusComment() {
        return imgStatusComment;
    }

    public void setImgStatusComment(int imgStatusComment) {
        this.imgStatusComment = imgStatusComment;
    }

    public int getImgStatusReady() {
        return imgStatusReady;
    }

    public void setImgStatusReady(int imgStatusReady) {
        this.imgStatusReady = imgStatusReady;
    }

    public String getTextTime() {
        return textTime;
    }

    public void setTextTime(String textTime) {
        this.textTime = textTime;
    }

    public String getTextNamePacient() {
        return textNamePacient;
    }

    public void setTextNamePacient(String textNamePacient) {
        this.textNamePacient = textNamePacient;
    }

    public String getTextAgePacient() {
        return textAgePacient;
    }

    public void setTextAgePacient(String textAgePacient) {
        this.textAgePacient = textAgePacient;
    }

    public String getTextAddress() {
        return textAddress;
    }

    public void setTextAddress(String textAddress) {
        this.textAddress = textAddress;
    }

    public String getTextReason() {
        return textReason;
    }

    public void setTextReason(String textReason) {
        this.textReason = textReason;
    }

    public String getTextDiagnosis() {
        return textDiagnosis;
    }

    public void setTextDiagnosis(String textDiagnosis) {
        this.textDiagnosis = textDiagnosis;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(imgStatusComment);
        parcel.writeInt(imgStatusReady);
        parcel.writeString(textTime);
        parcel.writeString(textNamePacient);
        parcel.writeString(textAgePacient);
        parcel.writeString(textAddress);
        parcel.writeString(textReason);
        parcel.writeString(textDiagnosis);

    }

    public static final Parcelable.Creator<MenuItemMyCalls> CREATOR = new Parcelable.Creator<MenuItemMyCalls>() {

        public MenuItemMyCalls createFromParcel(Parcel newParceble) {
            return new MenuItemMyCalls(newParceble);
        }

        public MenuItemMyCalls[] newArray(int size) {
            return new MenuItemMyCalls[size];
        }
    };

}

