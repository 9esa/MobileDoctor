package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.doctorCards;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.SelectDialogWithReturnTag;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuidesMkb10.MedicalGuidesMKB10;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.EnableProtocols;

/**
 * Created by kirichenko on 11.05.2015.
 */
public class NewDiagnosesAdapter extends CursorAdapter {

    private int iMode;

    public NewDiagnosesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View oCurrentView = LayoutInflater.from(context).inflate(R.layout.element_of_list_diagnoses, parent, false);

        MenuHolder menuHolder = new MenuHolder();

        menuHolder.tvTitleDiagnoses = (TextView) oCurrentView.findViewById(R.id.tvTitleDiagnoses);
        menuHolder.tvTags = (TextView) oCurrentView.findViewById(R.id.tvTags);


        oCurrentView.setTag(menuHolder);

        return oCurrentView;
    }

    @Override
    public void bindView(View oCurrentView, Context context, Cursor cursor) {

        String sTitleDiagnoses = null, sTags = null;

        MenuHolder menuHolder = (MenuHolder) oCurrentView.getTag();

        if (menuHolder != null) {

            if(iMode == SelectDialogWithReturnTag.SELECT_DIAGNOSIS){
                sTitleDiagnoses = cursor.getString(cursor.getColumnIndex(MedicalGuidesMKB10.TEXT));
                sTags = cursor.getString(cursor.getColumnIndex(MedicalGuidesMKB10.CODE));
            }else if(iMode == SelectDialogWithReturnTag.SELECT_PROTOCOLS){
                sTitleDiagnoses = cursor.getString(cursor.getColumnIndex(EnableProtocols.TEXT));
                sTags = cursor.getString(cursor.getColumnIndex(EnableProtocols.CODE));
            }


        }

        menuHolder.tvTitleDiagnoses.setText(sTitleDiagnoses);
        menuHolder.tvTags.setText(sTags);
    }

    public void setMode(int mode) {
        this.iMode = mode;
    }


    /**
     * Элементы для каждой строки
     */
    public static class MenuHolder {

        //Время
        TextView tvTitleDiagnoses;

        //Тег
        TextView tvTags;


    }
}
