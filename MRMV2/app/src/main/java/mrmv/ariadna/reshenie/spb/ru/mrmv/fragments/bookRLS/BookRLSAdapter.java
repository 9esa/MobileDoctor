package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.bookRLS;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.rlsAction.RLSItem;

/**
 * Created by kirichenko on 12.09.2015.
 */
public class BookRLSAdapter extends CursorAdapter {

    public BookRLSAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View oCurrentView = LayoutInflater.from(context).inflate(R.layout.item_rls, parent, false);

        MenuHolder menuHolder = new MenuHolder();

        menuHolder.tvNameOfProduct = (TextView) oCurrentView.findViewById(R.id.tvNameOfProduct);
        menuHolder.tvLatName = (TextView) oCurrentView.findViewById(R.id.tvLatName);
        menuHolder.tvActiveObject = (TextView) oCurrentView.findViewById(R.id.tvActiveObject);
        menuHolder.tvFormExite = (TextView) oCurrentView.findViewById(R.id.tvFormExite);
        menuHolder.tvDosage = (TextView) oCurrentView.findViewById(R.id.tvDosage);
        menuHolder.tvManufacture = (TextView) oCurrentView.findViewById(R.id.tvManufacture);
        menuHolder.tvCountry = (TextView) oCurrentView.findViewById(R.id.tvCountry);

        oCurrentView.setTag(menuHolder);

        return oCurrentView;
    }

    @Override
    public void bindView(View oCurrentView, Context context, Cursor cursor) {

        String sName = "", sLatName = "", sActiveObj = "", sFormExite = "",
                sDosage = "", sManufacture = "", sCountry = "";

        MenuHolder menuHolder = (MenuHolder) oCurrentView.getTag();

        if (menuHolder != null) {

            sName = isNotNullString(cursor.getString(cursor.getColumnIndex(RLSItem.TRADENAME)));

            sLatName = isNotNullString(cursor.getString(cursor.getColumnIndex(RLSItem.LATNAME)));
            sActiveObj = isNotNullString(cursor.getString(cursor.getColumnIndex(RLSItem.NDV)));
            sFormExite = isNotNullString(cursor.getString(cursor.getColumnIndex(RLSItem.FORM)));
            sDosage = isNotNullString(cursor.getString(cursor.getColumnIndex(RLSItem.DOSAGE)));
            sManufacture = isNotNullString(cursor.getString(cursor.getColumnIndex(RLSItem.MANUFACTURER)));
            sCountry = isNotNullString(cursor.getString(cursor.getColumnIndex(RLSItem.MCOUNTRY)));

            menuHolder.tvNameOfProduct.setText(sName);

            menuHolder.tvLatName.setText(sLatName);
            menuHolder.tvActiveObject.setText(sActiveObj);
            menuHolder.tvFormExite.setText(sFormExite);
            menuHolder.tvDosage.setText(sDosage);
            menuHolder.tvManufacture.setText(sManufacture);
            menuHolder.tvCountry.setText(sCountry);
        }

    }

    public static class MenuHolder {
        TextView tvNameOfProduct;
        TextView tvLatName;
        TextView tvActiveObject;
        TextView tvFormExite;
        TextView tvDosage;
        TextView tvManufacture;
        TextView tvCountry;
    }

    public String isNotNullString(String sValueString) {
        if (sValueString == null) {
            return "";
        } else {
            if (sValueString.equals("null")) {
                return "";
            } else {
                return sValueString;
            }
        }
    }

}
