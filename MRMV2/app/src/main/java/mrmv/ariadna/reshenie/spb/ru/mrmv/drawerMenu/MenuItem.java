package mrmv.ariadna.reshenie.spb.ru.mrmv.drawerMenu;

/**
 * Created by kirichenko on 23.04.2015.
 */
public class MenuItem {
    public int icon;
    public String title;

    public MenuItem() {
        super();
    }

    public MenuItem(int icon, String title) {
        super();
        this.icon = icon;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
