package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.mesFragments;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuides.MedicalGuides;

/**
 * Created by kirichenko on 28.06.2015.
 */
public class MesTypeClosedAdapter  extends CursorAdapter {

    public MesTypeClosedAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {


        View oCurrentView = LayoutInflater.from(context).inflate(R.layout.mes_layout, parent, false);
        MenuHolder menuHolder = new MenuHolder();

        menuHolder.tvCode = (TextView) oCurrentView.findViewById(R.id.text_view_code_mes_item);
        menuHolder.tvText = (TextView) oCurrentView.findViewById(R.id.text_view_text_mes);


        oCurrentView.setTag(menuHolder);

        return oCurrentView;
    }

    @Override
    public void bindView(View oCurrentView, Context context, Cursor cursor) {

        String sCode, sText;

        MenuHolder menuHolder = (MenuHolder) oCurrentView.getTag();

        if (menuHolder != null) {
            sCode = cursor.getString(cursor.getColumnIndex(MedicalGuides.CODE));
            sText = cursor.getString(cursor.getColumnIndex(MedicalGuides.TEXT));

            menuHolder.tvCode.setText(sCode);
            menuHolder.tvText.setText(sText);
        }
    }


    /**
     * Элементы для каждой строки
     */
    public static class MenuHolder {

        TextView tvCode;
        TextView tvText;
    }

}
