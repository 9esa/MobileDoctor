package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuides.ItemGuides;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.extraFieldsStatTalon.ExtraFieldStatTalon;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.ItemProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.StructureFullFieldProtocols;

/**
 * Created by kirichenko on 27.05.2015.
 */
public class ConstructViewProtocols {

    public static final float SPINNER_WEIGHT = 0.940f;
    public static final float EDITE_TEXT_WEIGHT = 0.050f;
    public static int QUESTION = 0;
    public static int EXTRAFIELD = 1;

    private static String SEPARATOR = "0";

    private static String SINGSTRING = "0";
    private static String MULTYSTRING = "1";
    private static String CHOOSELIST = "2";
    private static String CHOOSEBOOK = "3";
    private static String CHOOSEDIAGNOSE = "4";

    private static String DISABLE = "0";
    private static String ENABLE = "1";

    private static String TYPE_TEXT = "0";
    private static String TYPE_DATE = "1";
    private static String TYPE_TIME = "2";
    private static String TYPE_DATE_TIME = "3";
    private static String TYPE_LOGICAL = "4";
    private static String TYPE_NUMBER = "5";
    private static String TYPE_IDENTIFY = "6";

    private static float fRight = 0.75f;
    private static float fLeft = 0.25f;

    private static HashMap <Spinner, EditText> mapOfConnectionSpAndEt;
    private static HashMap <Spinner, Boolean> mapFirstCallSpinner;

    private static boolean bSetDefaultValue = false;

    public static View constructNewRow(TableLayout tlExtraSpinner, Activity oActivity, ItemProtocols oItemProtocols, DataBaseHelper oDataBaseHelper, boolean bSetDefaultValue) {

        ConstructViewProtocols.bSetDefaultValue = bSetDefaultValue;

        LinearLayout llMain = new LinearLayout(oActivity);
        llMain.setLayoutParams(new LinearLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        llMain.setOrientation(LinearLayout.VERTICAL);

        if(mapOfConnectionSpAndEt == null){
            mapOfConnectionSpAndEt = new HashMap<>();
        }

        if(mapFirstCallSpinner == null){
            mapFirstCallSpinner = new HashMap<>();
        }

        LinearLayout llLeftColumn = new LinearLayout(oActivity);

        if (oItemProtocols.getsTyp().equals(SEPARATOR)) {

            llLeftColumn.setLayoutParams(getParamForExtraRow());
            llLeftColumn.setOrientation(LinearLayout.VERTICAL);
            llLeftColumn.setPadding(0, (int) oActivity.getResources().getDimension(R.dimen.title_of_spinner_outpatien_padding_top), (int) oActivity.getResources().getDimension(R.dimen.left_column_of_row_table_outpatien_padding_right), (int) oActivity.getResources().getDimension(R.dimen.title_of_spinner_outpatien_padding_bottom));
            llMain.addView(getSeparator(oActivity, oItemProtocols, llLeftColumn));

        } else {

            llLeftColumn.setLayoutParams(getParamForExtraRow());
            llLeftColumn.setOrientation(LinearLayout.HORIZONTAL);
            llLeftColumn.setPadding(0, (int) oActivity.getResources().getDimension(R.dimen.title_of_spinner_outpatien_padding_top), (int) oActivity.getResources().getDimension(R.dimen.left_column_of_row_table_outpatien_padding_right), (int) oActivity.getResources().getDimension(R.dimen.title_of_spinner_outpatien_padding_bottom));
            llMain.addView(chooseExtraField(oActivity, oItemProtocols, oDataBaseHelper, llLeftColumn));

        }

        tlExtraSpinner.addView(llMain);

        return llLeftColumn;
    }

    public static View getSeparator(Activity oActivity, ItemProtocols oItemProtocols, LinearLayout llLeftColumn) {

        TextView tvTitleSpinnerLeft = new TextView(oActivity);
        tvTitleSpinnerLeft.setText(oItemProtocols.getsText());
        tvTitleSpinnerLeft.setTextColor(Color.BLACK);
        tvTitleSpinnerLeft.setTextAppearance(oActivity,android.R.style.TextAppearance_Small);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,fRight);
        tvTitleSpinnerLeft.setLayoutParams(params);

        llLeftColumn.addView(tvTitleSpinnerLeft);

        View vSeparator = new View(oActivity);

        params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, fLeft);
        vSeparator.setLayoutParams(params);

        vSeparator.setLayoutParams(new LinearLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,1, fLeft));
        vSeparator.setBackgroundColor(oActivity.getResources().getColor(R.color.common_dark_overlay));

        llLeftColumn.addView(vSeparator);

        return llLeftColumn;
    }

    public static View chooseExtraField(Activity oActivity, ItemProtocols oItemProtocols, DataBaseHelper oDataBaseHelper, LinearLayout llLeftColumn) {
        if(oItemProtocols != null){

            TextView tvTitleSpinnerLeft = new TextView(oActivity);
            tvTitleSpinnerLeft.setText(oItemProtocols.getsText());

            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, fRight);
            tvTitleSpinnerLeft.setLayoutParams(params);

            tvTitleSpinnerLeft.setTextColor(Color.BLACK);
            llLeftColumn.addView(tvTitleSpinnerLeft);

            String sColor  = oItemProtocols.getsColor();

            if (oItemProtocols.getsValueType().equals(SINGSTRING) || oItemProtocols.getsValueType().equals(MULTYSTRING)) {

                View vTempView = setDataType(oActivity, oItemProtocols);
                    params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, fLeft);
                vTempView.setLayoutParams(params);

                try{
                    vTempView.setBackgroundColor(Color.parseColor("#" +sColor));
                }catch (Exception ex){
                }

                llLeftColumn.addView(setParamForView(oActivity, vTempView, oItemProtocols,true));

            } else if (oItemProtocols.getsValueType().equals(CHOOSELIST) || oItemProtocols.getsValueType().equals(CHOOSEBOOK)) {

                setSelectionList(oActivity, oItemProtocols, oDataBaseHelper, llLeftColumn, params, sColor);

            } else if (oItemProtocols.getsValueType().equals(CHOOSEDIAGNOSE)) {
    //            Spinner spLeft = new Spinner(oActivity);
    //            loadSpinnersData(spLeft, MedicalCommonConstants.TYPE_VISIT, oDataBaseHelper,oActivity);
    //            spLeft.setBackgroundColor(oActivity.getResources().getColor(R.color.spinner_background));
    //            llLeftColumn.addView(spLeft);

    //            spResultHealthCare = (Spinner) oView.findViewById(R.id.spinner_result_healthcare_result_of_request);
    //            loadSpinnersData(spResultHealthCare, MedicalCommonConstants.RESULT_CARE, oDataBaseHelper );

            } else {

                TextView tvUnsupported = new TextView(oActivity);
                tvUnsupported.setText("Данный тип вопроса не поддерживается");
                llLeftColumn.addView(setParamForView(oActivity, tvUnsupported, oItemProtocols,true));

            }

        }
        return llLeftColumn;
    }

    static void setSelectionList(Activity oActivity, ItemProtocols oItemProtocols, DataBaseHelper oDataBaseHelper, LinearLayout llLeftColumn, TableRow.LayoutParams params, String sColor){

        LinearLayout llMain = new LinearLayout(oActivity);
        llMain.setLayoutParams(new LinearLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, fLeft));
        llMain.setOrientation(LinearLayout.HORIZONTAL);

        /**
         * ------------------------------------
         */
        EditText editText = new EditText(oActivity);
        params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, EDITE_TEXT_WEIGHT);

        params.setMargins((int) oActivity.getResources().getDimension(R.dimen.left_padding_edit_text),0,0,0);
        //editText.setPadding((int) oActivity.getResources().getDimension(R.dimen.left_padding_edit_text),0,0,0);

        editText.setLayoutParams(params);
        //    llLeftColumn.addView(setParamForView(oActivity, editText, oItemProtocols));

        editText.setTextColor(oActivity.getResources().getColor(R.color.color_for_lists));
        /**
         * ------------------------------------
         */

        Spinner spLeft = new Spinner(oActivity);
        params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, SPINNER_WEIGHT);

        spLeft.setLayoutParams(params);

        loadSpinnersData(spLeft, oItemProtocols, oDataBaseHelper, oActivity, editText);

        try {
           // spLeft.setBackgroundColor(Color.parseColor("#" + sColor));
        }catch (Exception ex){}

        //spLeft.setPadding(0,0,(int) oActivity.getResources().getDimension(R.dimen.left_padding_edit_text),0);

//        ImageButton btnClear = new ImageButton(oActivity);
//        params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, EDITE_TEXT_WEIGHT);
//        btnClear.setLayoutParams(params);

//        btnClear.setBackground(R.drawable.ic_clear_black_24dp);
//        btnClear.setBackgroundResource(R.drawable.ic_clear_black_24dp);
//        btnClear.setImageResource();
        //btnClear.setBackground();

        llMain.addView(setParamForView(oActivity, spLeft, oItemProtocols, false));

        llMain.addView(setParamForView(oActivity, editText, oItemProtocols,true));

     //   llMain.addView(btnClear);

        llLeftColumn.addView(setParamForView(oActivity, llMain, oItemProtocols,false));

        mapOfConnectionSpAndEt.put(spLeft, editText);
        mapFirstCallSpinner.put(spLeft,false);
        // tvTitleSpinnerLeft.setTextColor(oActivity.getResources().getColor(R.color.color_for_lists));

    }

    public static void setSpinnerListeners(){

        for(final Spinner itemSpiiner : mapOfConnectionSpAndEt.keySet()){
            itemSpiiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (adapterView != null) {
                            if(adapterView instanceof Spinner){

                                Spinner listItemGuides = (Spinner)adapterView;

                                if(mapFirstCallSpinner != null){

                                    if(mapFirstCallSpinner.get(listItemGuides)){
                                        if(listItemGuides.getCount() > 0) {

                                            Object object = listItemGuides.getItemAtPosition(0);

                                            if (object instanceof ItemGuides) {

                                                ItemGuides oItemGuides = (ItemGuides) object;

                                                if (oItemGuides.getsMultiValue().equals("1")) {

                                                    String sValue = mapOfConnectionSpAndEt.get(itemSpiiner).getText().toString();

                                                    if(sValue.isEmpty()){
                                                        sValue += adapterView.getSelectedItem().toString();
                                                    }else{
                                                        sValue += ", " + adapterView.getSelectedItem().toString();
                                                    }

                                                    mapOfConnectionSpAndEt.get(itemSpiiner).setText(sValue);

                                                }else{
                                                    String sValue = adapterView.getSelectedItem().toString();
                                                    mapOfConnectionSpAndEt.get(itemSpiiner).setText(sValue);
                                                }
                                            }
                                        }
                                    }

                                    mapFirstCallSpinner.put(listItemGuides,true);
                                }
                            }
                        }
                    }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }


    }

    static View setDataType(final Activity oActivity, ItemProtocols oItemProtocols) {

        if (oItemProtocols == null) {
            return new TextView(oActivity);
        } else {

            if (oItemProtocols.getsDataType() != null) {

                if (oItemProtocols.getsDataType().equals(TYPE_TEXT)) {

                    EditText etNewInputValue = new EditText(oActivity);

                    etNewInputValue.setText(controlStringOnNull(oItemProtocols.getsCtext()));

                    return etNewInputValue;

                } else if (oItemProtocols.getsDataType().equals(TYPE_DATE)) {

                    final Button btnDate = new Button(oActivity);

                    btnDate.setText(oItemProtocols.getsCtext());

                    btnDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Calendar c = Calendar.getInstance();
                            int mYear = c.get(Calendar.YEAR);
                            int mMonth = c.get(Calendar.MONTH);
                            int mDay = c.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog dpd = new DatePickerDialog(oActivity,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {

                                            String sValueForServer = "";

                                            if (dayOfMonth <= 8) {
                                                sValueForServer += "0" + (dayOfMonth);
                                            } else {
                                                sValueForServer += dayOfMonth;
                                            }

                                            if (monthOfYear <= 8) {
                                                sValueForServer += ".0" + (monthOfYear + 1);
                                            } else {
                                                sValueForServer += "." + (monthOfYear + 1);
                                            }

                                            btnDate.setText( sValueForServer + "." + year);

                                        }
                                    }, mYear, mMonth, mDay);
                            dpd.show();
                        }
                    });

                    return btnDate;

                } else if (oItemProtocols.getsDataType().equals(TYPE_TIME)) {

                    final Button btnTime = new Button(oActivity);

                    btnTime.setText(oItemProtocols.getsCtext());

                    btnTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Calendar c = Calendar.getInstance();
                            int mHour = c.get(Calendar.HOUR_OF_DAY);
                            int mMinute = c.get(Calendar.MINUTE);

                            TimePickerDialog tpd = new TimePickerDialog(oActivity,
                                    new TimePickerDialog.OnTimeSetListener() {

                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay,
                                                              int minute) {
                                            btnTime.setText(hourOfDay + ":" + minute);
                                        }
                                    }, mHour, mMinute, true);
                            tpd.show();
                        }

                        ;
                    });

                    return btnTime;

                } else if (oItemProtocols.getsDataType().equals(TYPE_DATE_TIME)) {
                    final Button btnDate = new Button(oActivity);
                    btnDate.setText(oItemProtocols.getsCtext());
                    btnDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Calendar c = Calendar.getInstance();
                            int mYear = c.get(Calendar.YEAR);
                            int mMonth = c.get(Calendar.MONTH);
                            int mDay = c.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog dpd = new DatePickerDialog(oActivity,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {

                                            String sValueForServer = "";

                                            if (dayOfMonth <= 8) {
                                                sValueForServer += "0" + (dayOfMonth);
                                            } else {
                                                sValueForServer += dayOfMonth;
                                            }

                                            if (monthOfYear <= 8) {
                                                sValueForServer += ".0" + (monthOfYear + 1);
                                            } else {
                                                sValueForServer += "." + (monthOfYear + 1);
                                            }

                                            btnDate.setText( sValueForServer + "." + year);

                                        }
                                    }, mYear, mMonth, mDay);
                            dpd.show();
                        }
                    });

                    return btnDate;
                } else if (oItemProtocols.getsDataType().equals(TYPE_LOGICAL)) {
                    return new Switch(oActivity);
                } else if (oItemProtocols.getsDataType().equals(TYPE_NUMBER)) {
                    EditText tvNumber = new EditText(oActivity);
                    tvNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                    tvNumber.setText(controlStringOnNull(oItemProtocols.getsCtext()));
                    return tvNumber;
                } else if (oItemProtocols.getsDataType().equals(TYPE_IDENTIFY)) {
                    EditText tvIdentify = new EditText(oActivity);
                    tvIdentify.setText(controlStringOnNull(oItemProtocols.getsCtext()));
                    return tvIdentify;
                } else {
                    return new TextView(oActivity);
                }
            } else {
                return new TextView(oActivity);
            }
        }
    }

    public static void parceValueFromView(View vForParce, ItemProtocols oItemProtocols, DataBaseHelper oDataBaseHelper, int iCurrentMode) {

        if (vForParce != null) {

            if (vForParce instanceof LinearLayout) {

                LinearLayout llViewForParce = (LinearLayout) vForParce;

                if (llViewForParce.getChildCount() > 0) {
                    View vSourceView = llViewForParce.getChildAt(1);

                    if (oItemProtocols != null) {
                        String sValueType = oItemProtocols.getsValueType();
                        if (oItemProtocols.getsTyp().equals(SEPARATOR)) {
                            return;
                        } else {
                            if(sValueType.equals(CHOOSELIST) || sValueType.equals(CHOOSEBOOK)){
                                if(vSourceView instanceof  LinearLayout){
                                        LinearLayout llList = (LinearLayout) vSourceView;

                                        if(llViewForParce.getChildCount() >= 1){
                                            View vSourceViewText = llList.getChildAt(0);

                                            if(vSourceViewText instanceof Spinner){

                                                Spinner spSourceView = (Spinner) vSourceViewText;

                                                if(spSourceView != null){
                                                    if(mapOfConnectionSpAndEt != null){
                                                        if(mapOfConnectionSpAndEt.get(spSourceView) != null){
                                                            oItemProtocols.setsCtext(mapOfConnectionSpAndEt.get(spSourceView).getText().toString());
                                                        }
                                                            // oItemProtocols.setsCtext(spSourceView.getSelectedItem().toString());
                                                    }
                                                }
                                            }
                                        }
                                }


                            }else if(sValueType.equals(SINGSTRING) || sValueType.equals(MULTYSTRING)){

                                if(oItemProtocols.getsDataType().equals(TYPE_TEXT) || sValueType.equals(TYPE_NUMBER) || sValueType.equals(TYPE_IDENTIFY) ){
                                    if(vSourceView instanceof EditText){
                                        oItemProtocols.setsCtext(((EditText) vSourceView).getText().toString());
                                    }
                                }else if(oItemProtocols.getsDataType().equals(TYPE_DATE) || sValueType.equals(TYPE_DATE_TIME) || sValueType.equals(TYPE_TIME)|| sValueType.equals(CHOOSEDIAGNOSE) ){
                                    if(vSourceView instanceof Button){
                                        oItemProtocols.setsCtext(((Button) vSourceView).getText().toString());
                                    }
                                }
                            }

                            if(iCurrentMode == QUESTION){
                                StructureFullFieldProtocols.updateFieldProtocol(oDataBaseHelper,oItemProtocols);
                            }else if (iCurrentMode == EXTRAFIELD){
                                StructureFullFieldProtocols.updateExtraFieldStattalon(oDataBaseHelper,oItemProtocols);
                            }

                        }
                    }
                }
            }
        }
    }

    static View setParamForView(Activity oActivity, View vNewElements, ItemProtocols oItemProtocols, boolean bUseColor) {

        if (oItemProtocols == null) {
            return vNewElements;
        }

        if (oItemProtocols.getsStatus() != null) {
            if (oItemProtocols.getsStatus().equals(DISABLE)) {
                vNewElements.setEnabled(false);
            } else if (oItemProtocols.getsStatus().equals(ENABLE)) {
                vNewElements.setEnabled(true);
            } else {
                vNewElements.setEnabled(false);
            }
        }

        String sColor = oItemProtocols.getsColor();

        if (sColor != null) {

            try {

                String color = "#" + sColor;

                if(bUseColor){
                    vNewElements.setBackgroundColor(Color.parseColor(color));
                }

            } catch (Exception ex) {

                if(bUseColor){
                    vNewElements.setBackgroundColor(oActivity.getResources().getColor(R.color.spinner_background));
                }

            }

        }

        return vNewElements;
    }

    //Загружаем не по курсору все справочники
    static void loadSpinnersData(Spinner spPlaceService, ItemProtocols oItemProtocols, DataBaseHelper oDataBaseHelper, Activity oActivity) {

        String sFormitemId = oItemProtocols.getsFormitemId();

        //Получаем список элементов из базы данных
        List<ItemGuides> listItemGuides = ExtraFieldStatTalon.getLabelExtraFieldStatTalon(oDataBaseHelper, sFormitemId, oItemProtocols);

        //Прикручиваем адаптер
        ArrayAdapter<ItemGuides> dataAdapter = new ArrayAdapter<ItemGuides>(oActivity,
                android.R.layout.simple_spinner_item, listItemGuides);

        //Определям в каком стиле у нас будут выпадать менюшки
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Прикручиваем адаптер к Spinner
        spPlaceService.setAdapter(dataAdapter);

        spPlaceService.setSelection(setValueForEachSpinner(spPlaceService, oItemProtocols.getsCtext()));
    }

    private static void loadSpinnersData(Spinner spPlaceService, ItemProtocols oItemProtocols, DataBaseHelper oDataBaseHelper, Activity oActivity, EditText editText) {
        String sFormitemId = oItemProtocols.getsFormitemId();

        //Получаем список элементов из базы данных
        List<ItemGuides> listItemGuides = ExtraFieldStatTalon.getLabelExtraFieldStatTalon(oDataBaseHelper, sFormitemId, oItemProtocols);

        //Прикручиваем адаптер
        ArrayAdapter<ItemGuides> dataAdapter = new ArrayAdapter<ItemGuides>(oActivity,
                android.R.layout.simple_spinner_item, listItemGuides);

        //Определям в каком стиле у нас будут выпадать менюшки
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Прикручиваем адаптер к Spinner
        spPlaceService.setAdapter(dataAdapter);

        if(bSetDefaultValue){
            editText.setText(controlStringOnNull(oItemProtocols.getsDefaultValue()));
        }else{
            editText.setText(controlStringOnNull(oItemProtocols.getsCtext()));
        }



        //setValueForEachSpinner(spPlaceService, oItemProtocols.getsCtext());
        //spPlaceService.setSelection());
    }

    static int setValueForEachSpinner(Spinner spSource, String sValueIdForSelect) {

        for (int iCount = 0; iCount < spSource.getAdapter().getCount(); iCount++) {

            Object oPrepareCast = spSource.getAdapter().getItem(iCount);

            if (oPrepareCast != null) {
                if (oPrepareCast instanceof ItemGuides) {
                    ItemGuides itemGuides = (ItemGuides) oPrepareCast;

                    if (itemGuides.getsText().equals(sValueIdForSelect)) {
                        return iCount;
                    }
                }
            }
        }
        return 0;
    }

    private static String controlStringOnNull(String sInputValue){
        if(sInputValue != null){
            if(sInputValue.equals("null")){
                return "";
            }else{
                return sInputValue;
            }
        }else{
            return "";
        }
    }


    private static LinearLayout.LayoutParams getParamForExtraRow() {
        return new LinearLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
    }

    public static void cleanAllElements() {
        mapOfConnectionSpAndEt.clear();
    }

}
