package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.bookRLS;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;

/**
 * Created by kirichenko on 12.09.2015.
 */
public class RLSItemFragment extends DialogFragment {

    private MainInformationRLS objectWithInformation;

    private TextView tvNameOfProduct, tvIdentifikatorNomenklaturnojPoziciiRls, tvEnglishName, tvActiveComponent,
            tvLife, tvForm, tvDosage, tvFilling, tvPacking, tvManufacture, tvCountryManufacture,
            tvIdRegistration, tvDateRegistration, tvRegistrator, tvCountryRegistrator, tvAgeProduct,
            tvFarmGroup, tvATX;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle(R.string.information_about_product);

        View oView = inflater.inflate(R.layout.information_about_product, null, false);

        getAllViewElements(oView);

        return oView;
    }

    private void getAllViewElements(View oView) {

        tvNameOfProduct = (TextView) oView.findViewById(R.id.tvNameOfProduct);
        tvIdentifikatorNomenklaturnojPoziciiRls = (TextView) oView.findViewById(R.id.tvIdentifikatorNomenklaturnojPoziciiRls);
        tvEnglishName = (TextView) oView.findViewById(R.id.tvEnglishName);
        tvActiveComponent = (TextView) oView.findViewById(R.id.tvActiveComponent);
        tvLife = (TextView) oView.findViewById(R.id.tvLife);
        tvForm = (TextView) oView.findViewById(R.id.tvForm);
        tvDosage = (TextView) oView.findViewById(R.id.tvDosage);

        tvFilling = (TextView) oView.findViewById(R.id.tvFilling);
        tvPacking = (TextView) oView.findViewById(R.id.tvPacking);

        tvManufacture = (TextView) oView.findViewById(R.id.tvManufacture);
        tvCountryManufacture = (TextView) oView.findViewById(R.id.tvCountryManufacture);

        tvIdRegistration = (TextView) oView.findViewById(R.id.tvIdRegistration);
        tvDateRegistration = (TextView) oView.findViewById(R.id.tvDateRegistration);
        tvRegistrator = (TextView) oView.findViewById(R.id.tvRegistrator);
        tvCountryRegistrator = (TextView) oView.findViewById(R.id.tvCountryRegistrator);

        tvAgeProduct = (TextView) oView.findViewById(R.id.tvAgeProduct);
        tvFarmGroup = (TextView) oView.findViewById(R.id.tvFarmGroup);
        tvATX = (TextView) oView.findViewById(R.id.tvATX);

        if (objectWithInformation != null) {
            tvNameOfProduct.setText(isNotNullString(objectWithInformation.getsTradename()));
            tvIdentifikatorNomenklaturnojPoziciiRls.setText(isNotNullString(objectWithInformation.getsCode()));
            tvEnglishName.setText(isNotNullString(objectWithInformation.getsLatname()));
            tvActiveComponent.setText(isNotNullString(objectWithInformation.getsNdv()));

            String sValue = isNotNullString(objectWithInformation.getsLive());

            if (sValue.equals("Y")) {
                sValue = getActivity().getString(R.string.Yes);
            } else if (sValue.equals("N")) {
                sValue = getActivity().getString(R.string.No);
            }

            tvLife.setText(sValue);

            tvForm.setText(isNotNullString(objectWithInformation.getsForm()));
            tvDosage.setText(isNotNullString(objectWithInformation.getsDosage()));

            tvFilling.setText(isNotNullString(objectWithInformation.getsFilling()));
            tvPacking.setText(isNotNullString(objectWithInformation.getsPacking()));

            tvManufacture.setText(isNotNullString(objectWithInformation.getsManufacturer()));
            tvCountryManufacture.setText(isNotNullString(objectWithInformation.getsMcountry()));

            tvIdRegistration.setText(isNotNullString(objectWithInformation.getsRegnum()));
            tvDateRegistration.setText(isNotNullString(objectWithInformation.getsRegdate()));

            tvRegistrator.setText(isNotNullString(objectWithInformation.getsRegistrator()));
            tvCountryRegistrator.setText(isNotNullString(objectWithInformation.getsRcountry()));

            tvAgeProduct.setText(isNotNullString(objectWithInformation.getsAge()));
            tvFarmGroup.setText(isNotNullString(objectWithInformation.getsForm()));
            tvATX.setText(isNotNullString(objectWithInformation.getsAtc()));

        }

    }

    private void setClickListeners() {

//            btnYes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    getDialog().dismiss();
//                }
//            });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    public String isNotNullString(String sValueString) {
        if (sValueString == null) {
            return " - ";
        } else {
            if (sValueString.equals("null")) {
                return " - ";
            } else {
                return sValueString;
            }
        }
    }

    public void setObjectWithInformation(MainInformationRLS objectWithInformation) {
        this.objectWithInformation = objectWithInformation;
    }
}
