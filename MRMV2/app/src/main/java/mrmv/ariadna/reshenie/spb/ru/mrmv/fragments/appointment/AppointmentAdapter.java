package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.appointment;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.numbers.Numbers;

/**
 * Created by kirichenko on 30.08.2015.
 */
public class AppointmentAdapter extends CursorAdapter {

    public AppointmentAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    /**
     * Блокирован для выдачи в регистратуре
     */
    private String BLOCK_IN_REG = "0";
    /**
     * Активный
     */
    private String ACTIVE = "1";
    /**
     * Выдан пациенту
     */
    private String ISSUED = "2";
    /**
     * Зарезервирован
     */
    private String RESERVED = "3";

    private String STATUS_BLOCK = mContext.getString(R.string.disable_numb);
    private String STATUS_UNBLOCK = mContext.getString(R.string.enable_numb);


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View oCurrentView = LayoutInflater.from(context).inflate(R.layout.number_time, parent, false);
        MenuHolder menuHolder = new MenuHolder();

        menuHolder.tvTimeNumbers = (TextView) oCurrentView.findViewById(R.id.tvTimeNumbers);
        menuHolder.tvStatusNumber = (TextView) oCurrentView.findViewById(R.id.tvStatusNumber);

        oCurrentView.setTag(menuHolder);

        return oCurrentView;

    }

    @Override
    public void bindView(View oCurrentView, Context context, Cursor cursor) {

        String sTime = "", sStatus = "", sIdNumber = "";

        MenuHolder menuHolder = (MenuHolder) oCurrentView.getTag();

        if (menuHolder != null) {

            sTime = cursor.getString(cursor.getColumnIndex(Numbers.TIME));
            sStatus = cursor.getString(cursor.getColumnIndex(Numbers.STATUS));
            sIdNumber = cursor.getString(cursor.getColumnIndex(Numbers.IDNUMBER));

            menuHolder.sStatus = sStatus;
            menuHolder.sIdNumber = sIdNumber;
            menuHolder.tvTimeNumbers.setText(isNotNullString(sTime));

            setColorForItemNumb(menuHolder, sStatus, context);

        }

    }

    private void setColorForItemNumb(MenuHolder menuHolder, String sStatus, Context mContext){
        if(sStatus.equals(BLOCK_IN_REG)){
            menuHolder.tvTimeNumbers.setBackground(mContext.getResources().getDrawable(R.drawable.background_numb_block));
            menuHolder.tvStatusNumber.setText(STATUS_BLOCK);
        }else if(sStatus.equals(ACTIVE)){
            menuHolder.tvTimeNumbers.setBackground(mContext.getResources().getDrawable(R.drawable.background_numb_enable));
            menuHolder.tvStatusNumber.setText(STATUS_UNBLOCK);
        }else if(sStatus.equals(ISSUED)){
            menuHolder.tvTimeNumbers.setBackground(mContext.getResources().getDrawable(R.drawable.background_numb_block));
            menuHolder.tvStatusNumber.setText(STATUS_BLOCK);
        }else if(sStatus.equals(RESERVED)){
            menuHolder.tvTimeNumbers.setBackground(mContext.getResources().getDrawable(R.drawable.background_numb_block));
            menuHolder.tvStatusNumber.setText(STATUS_BLOCK);
        }
    }

    /**
     * Элементы для каждой строки
     */
    public static class MenuHolder {

        String sStatus,sIdNumber;

        TextView tvTimeNumbers;
        TextView tvStatusNumber;
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
