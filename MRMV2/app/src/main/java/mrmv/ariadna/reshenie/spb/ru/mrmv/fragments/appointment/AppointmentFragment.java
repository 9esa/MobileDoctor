package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;

/**
 * Created by kirichenko on 25.08.2015.
 */
public class AppointmentFragment extends Fragment {

    private CalendarView getAllViewNumbers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View oView = inflater.inflate(R.layout.choose_free_number, null, false);

        getAllViewElements(oView);

        initializeCalendar();

        return oView;
    }


    private void getAllViewElements(View oView){
        getAllViewNumbers = (CalendarView) oView.findViewById(R.id.cwChooseDate);
    }

    private void initializeCalendar(){

        getAllViewNumbers.setShowWeekNumber(false);

        getAllViewNumbers.setFirstDayOfWeek(2);

        getAllViewNumbers.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Toast.makeText(getActivity(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
            }

        });


    }

}
