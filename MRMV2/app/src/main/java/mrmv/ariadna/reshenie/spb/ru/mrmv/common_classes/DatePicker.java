package mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls.IDateChanged;

/**
 * Created by kirichenko on 03.05.2015.
 */
public class DatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private IDateChanged callBackForNotify;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Dialog picker = new DatePickerDialog(getActivity(), this,
                year, month, day);

        picker.setTitle("Выберете дату");

        return picker;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    public void setServiceObject(IDateChanged callBackForNotify){
        this.callBackForNotify = callBackForNotify;
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year,
                          int month, int day) {

        if(callBackForNotify!= null){
            callBackForNotify.dateChanged(year, month,day);
        }

    }
}