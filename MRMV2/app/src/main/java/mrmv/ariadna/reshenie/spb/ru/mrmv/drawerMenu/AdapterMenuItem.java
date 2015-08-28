package mrmv.ariadna.reshenie.spb.ru.mrmv.drawerMenu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;

/**
 * Created by kirichenko on 23.04.2015.
 * Класс повзоялющий создать адаптер из наших элементов
 */
public class AdapterMenuItem extends ArrayAdapter<MenuItem> {

    private Context context;
    private int layoutResouresId;
    private MenuItem[] menuItems;

    public AdapterMenuItem(Context context, int resource, MenuItem[] menuItems) {
        super(context, resource, menuItems);
        this.context = context;
        this.layoutResouresId = resource;
        this.menuItems = menuItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View oCurrentView = convertView;

        MenuHolder menuHolder = null;

        if (oCurrentView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            /**
             * Получаем view элемента
             */
            oCurrentView = inflater.inflate(layoutResouresId, parent, false);

            menuHolder = new MenuHolder();
            menuHolder.imgIcon = (ImageView) oCurrentView.findViewById(R.id.imgIcon);
            menuHolder.txtTitle = (TextView) oCurrentView.findViewById(R.id.txtTitle);

            /**
             * Асоциируем элемент View с нашим созданным элементом
             */
            oCurrentView.setTag(menuHolder);
        } else {
            menuHolder = (MenuHolder) oCurrentView.getTag();
        }

        MenuItem menuItem = menuItems[position];
        menuHolder.txtTitle.setText(menuItem.title);
        menuHolder.imgIcon.setImageResource(menuItem.icon);

        return oCurrentView;
    }

    /**
     * Элементы для каждой строки
     */
    static class MenuHolder {
        ImageView imgIcon;
        TextView txtTitle;
    }

}
