package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.doctorCards;

import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuides.ItemGuides;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuides.MedicalGuides;

/**
 * Created by kirichenko on 11.05.2015.
 */
public class CommonFragment extends Fragment {

    private static String IS_DEFAULT = "1";

    protected int setValueForEachSpinner(Spinner spSource, String sValueIdForSelect){

        for(int iCount = 0; iCount < spSource.getAdapter().getCount(); iCount++ ){

            Object oPrepareCast = spSource.getAdapter().getItem(iCount);

            if(oPrepareCast != null){
                if(oPrepareCast instanceof ItemGuides){
                    ItemGuides itemGuides = (ItemGuides) oPrepareCast;

                    if(itemGuides.getsIdGuides().equals(sValueIdForSelect)){
                        return iCount;
                    }
                }
            }
        }
        return 0;
    }

    protected int getDefaultValueForSpinner(Spinner spSource){

        for(int iCount = 0; iCount < spSource.getAdapter().getCount(); iCount++ ){

            Object oPrepareCast = spSource.getAdapter().getItem(iCount);

            if(oPrepareCast != null){
                if(oPrepareCast instanceof ItemGuides){
                    ItemGuides itemGuides = (ItemGuides) oPrepareCast;

                    if(itemGuides.getsIsDefault().equals(IS_DEFAULT)){
                        return iCount;
                    }
                }
            }
        }
        return 0;
    }


    //Загружаем не по курсору все справочники
    protected void loadSpinnersData(Spinner spPlaceService, int iTag,DataBaseHelper oDataBaseHelper) {

        //Получаем список элементов из базы данных
        List<ItemGuides> listItemGuides = MedicalGuides.getLabelMedicalGuide(oDataBaseHelper, iTag);

        //Прикручиваем адаптер
        ArrayAdapter<ItemGuides> dataAdapter = new ArrayAdapter<ItemGuides>(getActivity(),
                android.R.layout.simple_spinner_item, listItemGuides);

        //Определям в каком стиле у нас будут выпадать менюшки
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Прикручиваем адаптер к Spinner
        spPlaceService.setAdapter(dataAdapter);
    }

    //Загружаем не по курсору все справочники
    protected  List<ItemGuides> loadRadioButtonData(int iTag,DataBaseHelper oDataBaseHelper) {

        //Получаем список элементов из базы данных
        return MedicalGuides.getLabelMedicalGuideForRadioButton(oDataBaseHelper, iTag);

    }

    /**
     * Функция помогающая вытащиить id из выбранного элемента спинера
     *
     * @param oItemSpinner Объект спинера
     * @return idGuides
     */
    protected String getIdFromSpinner(Object oItemSpinner) {
        if (oItemSpinner == null) {
            return "";
        } else {
            if (oItemSpinner instanceof ItemGuides) {
                String sValueIdGuides = ((ItemGuides) oItemSpinner).getsIdGuides();
                if (sValueIdGuides.equals("-")) {
                    return "";
                } else {
                    return sValueIdGuides;
                }

            } else {
                return "";
            }
        }
    }

}
