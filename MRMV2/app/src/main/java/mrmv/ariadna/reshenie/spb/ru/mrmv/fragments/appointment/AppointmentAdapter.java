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

    private String sIdPacient;

    public AppointmentAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }


    public static String BLOCK_IN_REG = "0";

    public static String ACTIVE = "1";

    public static String ISSUED = "2";

    public static String RESERVED = "3";

    private String STATUS_BLOCK = mContext.getString(R.string.disable_numb);
    private String STATUS_UNBLOCK = mContext.getString(R.string.enable_numb);
    private String STATUS_BLOCK_ME = mContext.getString(R.string.pacient_book_on_this_time);

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

        String sTime = "", sStatus = "", sIdNumber = "", sIdPacientNumberOnNumb = "";

        MenuHolder menuHolder = (MenuHolder) oCurrentView.getTag();

        if (menuHolder != null) {

            sTime = cursor.getString(cursor.getColumnIndex(Numbers.TIME));
            sStatus = cursor.getString(cursor.getColumnIndex(Numbers.STATUS));
            sIdNumber = cursor.getString(cursor.getColumnIndex(Numbers.IDNUMBER));
            sIdPacientNumberOnNumb = cursor.getString(cursor.getColumnIndex(Numbers.PATIENTID));

            menuHolder.sStatus = sStatus;
            menuHolder.sIdNumber = sIdNumber;
            menuHolder.sIdPacientNumberOnNumb = sIdPacientNumberOnNumb;
            menuHolder.tvTimeNumbers.setText(isNotNullString(sTime));

            if(sIdPacient.equals(sIdPacientNumberOnNumb)){
                setColorForItemNumb(menuHolder, sStatus, context, true);
            }else{
                setColorForItemNumb(menuHolder, sStatus, context, false);
            }
        }

    }

    private void setColorForItemNumb(MenuHolder menuHolder, String sStatus, Context mContext, boolean bControlMeNumb){
        if(sStatus.equals(BLOCK_IN_REG)){
            menuHolder.tvTimeNumbers.setBackground(mContext.getResources().getDrawable(R.drawable.background_numb_block));
            if(bControlMeNumb){
                menuHolder.tvStatusNumber.setText(STATUS_BLOCK_ME);
            }else{
                menuHolder.tvStatusNumber.setText(STATUS_BLOCK);
            }
        }else if(sStatus.equals(ACTIVE)){
            menuHolder.tvTimeNumbers.setBackground(mContext.getResources().getDrawable(R.drawable.background_numb_enable));
            menuHolder.tvStatusNumber.setText(STATUS_UNBLOCK);
        }else if(sStatus.equals(ISSUED)){
            menuHolder.tvTimeNumbers.setBackground(mContext.getResources().getDrawable(R.drawable.background_numb_block));
            if(bControlMeNumb){
                menuHolder.tvStatusNumber.setText(STATUS_BLOCK_ME);
            }else{
                menuHolder.tvStatusNumber.setText(STATUS_BLOCK);
            }

        }else if(sStatus.equals(RESERVED)){
            menuHolder.tvTimeNumbers.setBackground(mContext.getResources().getDrawable(R.drawable.background_numb_block));
            if(bControlMeNumb){
                menuHolder.tvStatusNumber.setText(STATUS_BLOCK_ME);
            }else{
                menuHolder.tvStatusNumber.setText(STATUS_BLOCK);
            }

        }
    }

    public void setsIdPacient(String sIdPacient) {
        this.sIdPacient = sIdPacient;
    }


    public static class MenuHolder {

        String sStatus,sIdNumber, sIdPacientNumberOnNumb;

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
