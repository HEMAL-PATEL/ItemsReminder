package com.example.oluwagbenga.reminder.views.utils;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtil {

    public static void add(Fragment fragment, @IdRes int viewId, FragmentActivity activity, boolean isBackstack){
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(viewId, fragment);
        if (isBackstack){
            ft.addToBackStack(null);
        }
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.commit();
    }

    public static void replace(Fragment fragment, @IdRes int viewId, FragmentActivity activity){
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(viewId, fragment);
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.commit();
    }
}
