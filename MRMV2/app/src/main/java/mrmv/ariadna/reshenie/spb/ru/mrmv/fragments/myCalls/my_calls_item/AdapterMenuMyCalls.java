package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls.my_calls_item;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.callsAction.Calls;


/**
 * Created by kirichenko on 27.04.2015.
 */
public class AdapterMenuMyCalls extends CursorAdapter {

    public AdapterMenuMyCalls(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View oCurrentView = LayoutInflater.from(context).inflate(R.layout.element_of_list_my_calls, parent, false);
        MenuHolder menuHolder = new MenuHolder();

        menuHolder.imgStatusComment = (ImageView) oCurrentView.findViewById(R.id.imageStatusComment);
        menuHolder.imgStatusReady = (ImageView) oCurrentView.findViewById(R.id.imageStatusReady);
        menuHolder.tvNumberPhone = (TextView) oCurrentView.findViewById(R.id.tv_number_phone);
        menuHolder.tvNumberMobilePhone = (TextView) oCurrentView.findViewById(R.id.tv_number_mobile_phone);
        menuHolder.textTime = (TextView) oCurrentView.findViewById(R.id.textTime);
        menuHolder.textNamePacient = (TextView) oCurrentView.findViewById(R.id.textNamePacient);
        menuHolder.textAgePacient = (TextView) oCurrentView.findViewById(R.id.textAgePacient);
        menuHolder.textAddress = (TextView) oCurrentView.findViewById(R.id.textAddress);
        menuHolder.textReason = (TextView) oCurrentView.findViewById(R.id.textReason);
        menuHolder.textViewIdCalls = (TextView) oCurrentView.findViewById(R.id.textViewIdCalls);

        oCurrentView.setTag(menuHolder);

        return oCurrentView;
    }

    @Override
    public void bindView(View oCurrentView, Context context, Cursor cursor) {

        String sId, sStatus, sProtocolStatus, sCallDate, sLastName, sFirstName, sSecondName, sSex,
                sBirthDate, sAge, sRegAddress, sLocAddress, sNote, sPhone, sMobilePhone,sPacientId;

        MenuHolder menuHolder = (MenuHolder) oCurrentView.getTag();

        if (menuHolder != null) {

            sId = cursor.getString(cursor.getColumnIndex(Calls.IDCALLS));
            sStatus = cursor.getString(cursor.getColumnIndex(Calls.STATUS));
            sProtocolStatus = cursor.getString(cursor.getColumnIndex(Calls.PROTOCOL_STATUS));
            sCallDate = cursor.getString(cursor.getColumnIndex(Calls.CALL_DATE));
            sLastName = cursor.getString(cursor.getColumnIndex(Calls.LAST_NAME));
            sFirstName = cursor.getString(cursor.getColumnIndex(Calls.FIRST_NAME));
            sSecondName = cursor.getString(cursor.getColumnIndex(Calls.SECOND_NAME));
            sAge = cursor.getString(cursor.getColumnIndex(Calls.AGE));
            sSex = cursor.getString(cursor.getColumnIndex(Calls.SEX));
            sRegAddress = cursor.getString(cursor.getColumnIndex(Calls.REGADDRESS));
            sLocAddress = cursor.getString(cursor.getColumnIndex(Calls.LOCADDRESS));
            sNote = cursor.getString(cursor.getColumnIndex(Calls.NOTE));
            sPhone = cursor.getString(cursor.getColumnIndex(Calls.PHONE));
            sMobilePhone = cursor.getString(cursor.getColumnIndex(Calls.CELLULAR));

            menuHolder.sPacientId = cursor.getString(cursor.getColumnIndex(Calls.PATIENTID));

            if(sPhone.equals("null")){
                sPhone = " - ";
            }

            if(sMobilePhone.equals("null")){
                sMobilePhone = " - ";
            }

            if (sSex.equals("1")) {
                sSex = "ж";
            } else {
                sSex = "м";
            }

            if(sStatus.equals("0")){
                menuHolder.imgStatusReady.setVisibility(View.INVISIBLE);
            }else {
                menuHolder.imgStatusReady.setVisibility(View.VISIBLE);
            }

            menuHolder.setsStatus(sStatus);

            if(sProtocolStatus.equals("0")){
                menuHolder.imgStatusComment.setVisibility(View.INVISIBLE);
            }else {
                menuHolder.imgStatusComment.setVisibility(View.VISIBLE);
            }


            try{
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy kk:mm", Locale.ENGLISH);
                Date result = null;
                try {
                    result = df.parse(sCallDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                df = new SimpleDateFormat("HH:mm");

                String reportDate = df.format(result);

                menuHolder.textTime.setText(reportDate);
            }catch (Exception ex){
                menuHolder.textTime.setText(sCallDate);
            }

            menuHolder.tvNumberPhone.setText(sPhone);
            menuHolder.tvNumberMobilePhone.setText(sMobilePhone);

            menuHolder.textNamePacient.setText(sLastName + " " + sFirstName + " " + sSecondName);
            menuHolder.textAgePacient.setText(sSex + " / " + sAge);


            if(sLocAddress.equals("null")){
                menuHolder.textAddress.setText(sRegAddress);
            }else{
                menuHolder.textAddress.setText(sLocAddress);
            }


            menuHolder.textReason.setText(sNote);
            menuHolder.textViewIdCalls.setText(sId);

        }

    }

    /**
     * Элементы для каждой строки
     */
    public static class MenuHolder {

        String sStatus;
        String sPacientId;

        //Статус
        ImageView imgStatusComment;
        ImageView imgStatusReady;

        //Время
        TextView textTime;

        //Пациент
        TextView textNamePacient;
        TextView textAgePacient;

        //Адрес
        TextView textAddress;

        TextView tvNumberPhone;
        TextView tvNumberMobilePhone;


        //Причина вызова
        TextView textReason;
        //Причина вызова диагноз
        TextView textViewIdCalls;

        public String getsStatus() {
            return sStatus;
        }

        public void setsStatus(String sStatus) {
            this.sStatus = sStatus;
        }

        public String getsPacientId() {
            return sPacientId;
        }

        public void setsPacientId(String sPacientId) {
            this.sPacientId = sPacientId;
        }

        public ImageView getImgStatusComment() {
            return imgStatusComment;
        }

        public void setImgStatusComment(ImageView imgStatusComment) {
            this.imgStatusComment = imgStatusComment;
        }

        public ImageView getImgStatusReady() {
            return imgStatusReady;
        }

        public void setImgStatusReady(ImageView imgStatusReady) {
            this.imgStatusReady = imgStatusReady;
        }

        public TextView getTextTime() {
            return textTime;
        }

        public void setTextTime(TextView textTime) {
            this.textTime = textTime;
        }

        public TextView getTextNamePacient() {
            return textNamePacient;
        }

        public void setTextNamePacient(TextView textNamePacient) {
            this.textNamePacient = textNamePacient;
        }

        public TextView getTextAgePacient() {
            return textAgePacient;
        }

        public void setTextAgePacient(TextView textAgePacient) {
            this.textAgePacient = textAgePacient;
        }

        public TextView getTextAddress() {
            return textAddress;
        }

        public void setTextAddress(TextView textAddress) {
            this.textAddress = textAddress;
        }

        public TextView getTextReason() {
            return textReason;
        }

        public void setTextReason(TextView textReason) {
            this.textReason = textReason;
        }

        public TextView getTextIdCalls() {
            return textViewIdCalls;
        }

        public void setTextIdCalls(TextView textViewIdCalls) {
            this.textViewIdCalls = textViewIdCalls;
        }
    }
// Паттерн !!!!
//https://xjaphx.wordpress.com/2011/06/16/viewholder-pattern-caching-view-efficiently/
}
