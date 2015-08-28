package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.findEmk;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.informationAboutPatient.Patients;

/**
 * Created by kirichenko on 27.08.2015.
 */
public class EmkAdapter extends CursorAdapter {

    public EmkAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View oCurrentView = LayoutInflater.from(context).inflate(R.layout.element_of_list_patients, parent, false);
        MenuHolder menuHolder = new MenuHolder();

        menuHolder.tvNumber = (TextView) oCurrentView.findViewById(R.id.tvNumber);
        menuHolder.tvFIO = (TextView) oCurrentView.findViewById(R.id.tvFIO);
        menuHolder.tvDateBorn = (TextView) oCurrentView.findViewById(R.id.tvDateBorn);
        menuHolder.tvUserPhone = (TextView) oCurrentView.findViewById(R.id.tvUserPhone);
        menuHolder.tvAddress = (TextView) oCurrentView.findViewById(R.id.tvAddress);

        oCurrentView.setTag(menuHolder);

        return oCurrentView;
    }

    @Override
    public void bindView(View oCurrentView, Context context, Cursor cursor) {

        String sId = "", sName = "", sSecondName = "",
                sThirdName = "", sNumberCard = "", sPhone = "", sAddress =  "";

        MenuHolder menuHolder = (MenuHolder) oCurrentView.getTag();

        if (menuHolder != null) {

            sId = cursor.getString(cursor.getColumnIndex(Patients.IDPATIENT));
            sName = cursor.getString(cursor.getColumnIndex(Patients.PATIENT_NAME));
            sSecondName = cursor.getString(cursor.getColumnIndex(Patients.PATIENT_SECONDNAME));
            sThirdName = cursor.getString(cursor.getColumnIndex(Patients.PATIENT_THIRD_NAME));
            sNumberCard = cursor.getString(cursor.getColumnIndex(Patients.NUMBER_CARD));
            sPhone = cursor.getString(cursor.getColumnIndex(Patients.PHONE));
            sAddress = cursor.getString(cursor.getColumnIndex(Patients.ADDRESS));

            menuHolder.sIdPatient = sId;
            menuHolder.tvNumber.setText(isNotNullString(sId));
            menuHolder.tvFIO.setText(isNotNullString(sSecondName) + " " + isNotNullString(sName) + " " + isNotNullString(sThirdName));
            //menuHolder.tvNumber.setText(isNotNullString(sNumberCard));
            menuHolder.tvUserPhone.setText(isNotNullString(sPhone));
            menuHolder.tvAddress.setText(isNotNullString(sAddress));

        }
    }

    /**
     * Ёлементы дл€ каждой строки
     */
    public static class MenuHolder {

        String sIdPatient;

        TextView tvNumber;
        TextView tvFIO;
        TextView tvDateBorn;
        TextView tvUserPhone;
        TextView tvAddress;

    }

    public String isNotNullString(String sValueString){
            if(sValueString == null){
                return "";
            }else{
                if(sValueString.equals("null")){
                    return "";
                }else{
                    return sValueString;
                }
            }
    }


}
