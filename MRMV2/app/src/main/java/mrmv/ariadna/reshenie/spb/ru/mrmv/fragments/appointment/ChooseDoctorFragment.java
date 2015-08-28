package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;

/**
 * Created by kirichenko on 28.08.2015.
 */
public class ChooseDoctorFragment extends Fragment {

    private Button btnSaveMe, btnSaveAnother;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View oView = inflater.inflate(R.layout.change_doctor_to_save, null, false);
        getAllViewElements(oView);

        addLIstenersForButton();

        return oView;
    }

    private void getAllViewElements(View oView){

        btnSaveMe = (Button) oView.findViewById(R.id.btnCurrentDoctor);
        btnSaveAnother = (Button) oView.findViewById(R.id.btAnotherDoctor);

    }

    private void addLIstenersForButton(){

        btnSaveAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        btnSaveMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppointmentFragment oAppointmentFragment = new AppointmentFragment();

                FragmentManager oFragmentManager = getFragmentManager();
                FragmentTransaction oFragmentTransaction =  oFragmentManager.beginTransaction();
                oFragmentTransaction.replace(R.id.main_active_layout, oAppointmentFragment);
                oFragmentTransaction.addToBackStack("fragmentStack");
                oFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                oFragmentTransaction.commit();

            }
        });

    }


}
