package mrmv.ariadna.reshenie.spb.ru.mrmv.activites;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.AsyncLoadingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.ICallbackForLoadingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.callsAction.Calls;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by kirichenko on 07.07.2015.
 */
public class MapActivity extends Activity implements ICallbackForLoadingMainInformation, OnBalloonListener, LoaderManager.LoaderCallbacks<Cursor> {

    private MapController mMapController;
    private OverlayManager mOverlayManager;

    private DataBaseHelper oDataBaseHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        oDataBaseHelper = DataBaseHelper.getInstance(this);

        MapView mapView = (MapView) findViewById(R.id.map);

        mMapController = mapView.getMapController();

        if(mMapController != null){

            mMapController.setPositionAnimationTo(new GeoPoint(59.939131, 30.314832));

            mMapController.setZoomCurrent(10);

            mOverlayManager = mMapController.getOverlayManager();
            if(mOverlayManager != null){
                mOverlayManager.getMyLocation().setEnabled(true);

                startLoadFromDataBase();
            }
        }

    }

    private void startLoadFromDataBase(){

        LoaderManager.LoaderCallbacks<Cursor> interfaceForCallBack = this;


        getLoaderManager().initLoader(0, new Bundle(), interfaceForCallBack);

    }

    private void startLoadNewCoordinateByAddress(String sAddressForSearch){

        String sValueForSave = sAddressForSearch;

        if(sAddressForSearch.equals("-")){
            return;
        }
//todo Исправить выход за приделы

        if(sAddressForSearch.indexOf(" кв.") > 0){
            sAddressForSearch = sAddressForSearch.substring(0,sAddressForSearch.indexOf(" кв."));
        }

        String sValueAddressForRequest = "http://maps.google.com/maps/api/geocode/json?address=" +
                sAddressForSearch + "&sensor=false";

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, sValueForSave);
        loadingInfromation.setsRequest(sValueAddressForRequest);
        loadingInfromation.setDaemon(true);
        loadingInfromation.start();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void showLocation(double iLatitude, double iLongitude, String sValueAddress) {
        // Load required resources
        Resources res = getResources();

        if(res != null){
            if(mMapController != null){
                Overlay overlay = new Overlay(mMapController);

                OverlayItem oOverlayItem = new OverlayItem(new GeoPoint(iLatitude + 0.0002, iLongitude), res.getDrawable(R.drawable.red_mark));

                BalloonItem oBalloonItem = new BalloonItem(this, oOverlayItem.getGeoPoint());
                oBalloonItem.setText(sValueAddress);
                oBalloonItem.setOnBalloonListener(this);

                oOverlayItem.setBalloonItem(oBalloonItem);

                overlay.addOverlayItem(oOverlayItem);

                if(mOverlayManager != null){
                    mOverlayManager.addOverlay(overlay);
                }
            }
        }

    }

    @Override
    public void getLoadedInformation(JSONObject jsonObject, final Object identificatieNummer) {

        if(jsonObject != null){
            try {

                final double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                final double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                Log.d("Test", " sRegAddress " + lng +" . " + lat);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        showLocation(lat, lng, String.valueOf(identificatieNummer));
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificatieNummer) {

    }

    @Override
    public void onBalloonViewClick(BalloonItem balloonItem, View view) {
//        OverlayItem item = balloonItem.getOverlayItem();
//
//        Intent intent = new Intent().setClass(this, InfoActivity.class);
//        if (balloonItem.getText() != null){
//            intent.putExtra(InfoActivity.ID_INFO, balloonItem.getText());
//        }else{
//            intent.putExtra(InfoActivity.ID_INFO, "Яндекс");
//        }
//        startActivity(intent);
    }

    @Override
    public void onBalloonShow(BalloonItem balloonItem) {

    }

    @Override
    public void onBalloonHide(BalloonItem balloonItem) {

    }

    @Override
    public void onBalloonAnimationStart(BalloonItem balloonItem) {

    }

    @Override
    public void onBalloonAnimationEnd(BalloonItem balloonItem) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoaderForMes(this, oDataBaseHelper);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursorOfData) {

        if (cursorOfData != null) {

            if (cursorOfData.moveToFirst()) {
                do {

                    String sRegAddress = cursorOfData.getString(cursorOfData.getColumnIndex(Calls.REGADDRESSFULL));
                    String sLocAddress = cursorOfData.getString(cursorOfData.getColumnIndex(Calls.LOCADDRESSFULL));

                    if(sLocAddress.equals("null")){
                        startLoadNewCoordinateByAddress(sRegAddress);
                    }else{
                        startLoadNewCoordinateByAddress(sLocAddress);
                    }


                } while (cursorOfData.moveToNext());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        getLoaderManager().destroyLoader(0);
    }

    static class CursorLoaderForMes extends CursorLoader {

        private DataBaseHelper oDataBaseHelper;

        public CursorLoaderForMes(Activity context, DataBaseHelper oDataBaseHelper) {
            super(context);
            this.oDataBaseHelper = oDataBaseHelper;
        }

        @Override
        public Cursor loadInBackground() {
            return Calls.getMyCalls(oDataBaseHelper, 0);
        }
    }


}
