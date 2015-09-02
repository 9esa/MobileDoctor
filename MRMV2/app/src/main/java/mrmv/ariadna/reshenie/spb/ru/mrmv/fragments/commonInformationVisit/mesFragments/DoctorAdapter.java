package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.mesFragments;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.listOfDoctorsAction.EnableDoctors;

/**
 * Created by kirichenko on 23.06.2015.
 */
public class DoctorAdapter  extends CursorAdapter {


    public DoctorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View oCurrentView = LayoutInflater.from(context).inflate(R.layout.enable_doctors, parent, false);
        MenuHolder menuHolder = new MenuHolder();

        menuHolder.tvCode = (TextView) oCurrentView.findViewById(R.id.text_View_code_doctor_on_dep);
        menuHolder.tvFIO = (TextView) oCurrentView.findViewById(R.id.text_View_fio_doctor);
        menuHolder.nameOfSpeciality = (TextView) oCurrentView.findViewById(R.id.text_View_special_doctor);
//        menuHolder.nameOfCategory = (TextView) oCurrentView.findViewById(R.id.text_View_name_of_category);
//
//        menuHolder.depOfName = (TextView) oCurrentView.findViewById(R.id.text_View_dep_of_name);
//        menuHolder.note = (TextView) oCurrentView.findViewById(R.id.text_View_note);


        oCurrentView.setTag(menuHolder);

        return oCurrentView;
    }

    @Override
    public void bindView(View oCurrentView, Context context, Cursor cursor) {
        String sId, sCode, sFIO, sSpecName, sCategName, sDepId, sDepName, sNote;


        MenuHolder menuHolder = (MenuHolder) oCurrentView.getTag();

        if (menuHolder != null) {
            sCode = cursor.getString(cursor.getColumnIndex(EnableDoctors.CODE));
            sFIO = cursor.getString(cursor.getColumnIndex(EnableDoctors.NAME));
            sSpecName = cursor.getString(cursor.getColumnIndex(EnableDoctors.SPECNAME));
//            sCategName = cursor.getString(cursor.getColumnIndex(EnableDoctors.CATEGNAME));
//            sDepName = cursor.getString(cursor.getColumnIndex(EnableDoctors.DEPNAME));
//            sNote = cursor.getString(cursor.getColumnIndex(EnableDoctors.NOTE));

            menuHolder.tvCode.setText(sCode);
            menuHolder.tvFIO.setText(sFIO);
            menuHolder.nameOfSpeciality.setText(sSpecName);
//            menuHolder.nameOfCategory.setText(sCategName);
//            menuHolder.depOfName.setText(sDepName);
//            menuHolder.note.setText(sNote);

        }
    }

    /**
     * Элементы для каждой строки
     */
    public static class MenuHolder {

        TextView tvCode;
        TextView tvFIO;
        TextView nameOfSpeciality;
//        TextView nameOfCategory;
//        TextView depOfName;
//        TextView note;

    }
}
