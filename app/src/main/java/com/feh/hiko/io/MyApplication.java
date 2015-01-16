package com.feh.hiko.io;

/**
 * Created by theoz on 1/12/15.
 */
import android.app.Application;

import java.net.URISyntaxException;

public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.
        try {
            initSingletons();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    protected void initSingletons() throws URISyntaxException {
        // Initialize the instance of MySingleton
        MySingleton.initInstance();
    }

    public void customAppMethod()
    {
        // Custom application method
    }
}
