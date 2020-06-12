package com.kleverowl.staffapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.File;

public class Utils {

    public static String APP_FOLDER = Environment.getExternalStorageDirectory() + "/viahyshree";

    public static void createDirectory() {
        File file = new File(APP_FOLDER);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public static boolean isValidPhoneNumber(String phoneNumber) {
        boolean isValid = false;
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber indianNumber = phoneUtil.parse(phoneNumber, "IN");
            isValid = phoneUtil.isValidNumber(indianNumber);
        } catch (NumberParseException e) {
            isValid = false;
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        System.out.println("Phone Number Validity Flag : " + isValid);
        return isValid;
    }

    public static int dpToPx(float dp, Context context) {
        return dpToPx(dp, context.getResources());
    }

    private static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static int getStaffType(Context context) {
        return PreferenceConnector.readInteger(context, PreferenceConnector.TYPE, StaffType.STAFF);
    }
}
