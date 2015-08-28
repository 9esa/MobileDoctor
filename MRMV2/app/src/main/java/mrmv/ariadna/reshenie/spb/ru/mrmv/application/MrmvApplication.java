package mrmv.ariadna.reshenie.spb.ru.mrmv.application;

import android.app.Application;
import android.content.res.Configuration;


import org.acra.*;
import org.acra.annotation.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;


/*
    Kirichenko Denis
    Класс который запускается когда загружается приложение
*/

public class MrmvApplication extends Application{

    private static boolean activityVisible;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        ACRA.init(this);
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
