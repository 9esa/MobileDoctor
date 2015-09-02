package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.appointment;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.specials.Special;

/**
 * Created by kirichenko on 01.09.2015.
 */
public class ChooseAdapter extends CursorAdapter {

    public ChooseAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View oCurrentView = LayoutInflater.from(context).inflate(R.layout.another_doctor_selected, parent, false);
        MenuHolder menuHolder = new MenuHolder();

        menuHolder.tvText = (TextView) oCurrentView.findViewById(R.id.tvItemSelected);
        oCurrentView.setTag(menuHolder);

        return oCurrentView;

    }

    @Override
    public void bindView(View oCurrentView, Context context, Cursor cursor) {
        String sId = null, sText = null;

        MenuHolder menuHolder = (MenuHolder) oCurrentView.getTag();

        if (menuHolder != null) {
            sId = cursor.getString(cursor.getColumnIndex(Special.IDNUMBER));
            sText = cursor.getString(cursor.getColumnIndex(Special.TEXT));

            menuHolder.sIdItem = sId;
            menuHolder.tvText.setText(sText);

        }
    }

    /**
     * Ёлементы дл€ каждой строки
     */
    public static class MenuHolder {

        String sIdItem;

        TextView tvText;


    }

}
