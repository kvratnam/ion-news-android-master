package com.mantra.ionnews.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mantra.ionnews.R;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.mantra.ionnews.utils.ConstantClass.DEFAULT_DATE_FORMAT;

/**
 * Created by TaNMay on 21/02/17.
 */

public class Util {

    private static ConnectivityManager mCM;


    public static void hideSoftKeyboard(Activity activity) {
        if (activity!=null){
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = activity.getCurrentFocus();
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static boolean hasInternetAccess(Context context) {
        if (mCM == null)
            mCM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mCM.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }


    private static Bitmap decodeUri(Context context, Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public static Bitmap getSquareBitmap(Bitmap srcBmp) {
        Bitmap dstBmp = null;

        if (srcBmp.getWidth() >= srcBmp.getHeight()) {

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth() / 2 - srcBmp.getHeight() / 2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );

        } else {

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }
        return dstBmp;
    }

    public static int getTimeInMinFromDate(Date date) {
        int timeInMin = 0;
        return timeInMin;
    }

    public static int getTimeInHoursFromDate(Date date) {
        int timeInHours = 0;
        return timeInHours;
    }

    public static int getTimeInDaysFromDate(Date date) {
        int timeInDays = 0;
        return timeInDays;
    }

    public static int getTimeInMonthsFromDate(Date date) {
        int timeInMonths = 0;
        return timeInMonths;
    }

    public static int getTimeInYearsFromDate(Date date) {
        int timeInYears = 0;
        return timeInYears;
    }

    public static int getCurrentYear() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return currentYear;
    }

    public static int getCurrentMonth() {
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        return currentMonth;
    }

    public static Date getCurrentDate() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        Date date = null;
        try {
            String formattedDate = sdf.format(c.getTime());
            date = sdf.parse(formattedDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDateFromIst(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
            return date;
//            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long differenceInDates(Date newDate, Date oldDate) {
        long difference = newDate.getTime() - oldDate.getTime();
        return difference;
    }


    public static String getFormattedHashtags(String tags, String defaultTag) {
        String formattedTags = "";
        tags = tags.replace(",", " ");
        if (tags.trim().length() > 0) {
            String[] splitTags = tags.split("#");
            for (int i = 0; i < splitTags.length; i++) {
                if (splitTags[i].trim().length() > 0) {
                    if (formattedTags.length() > 0) formattedTags = formattedTags + ", ";
                    formattedTags = formattedTags + "#" + splitTags[i].trim();
                }
            }
        }

        if (formattedTags.length() < 1) {
            formattedTags = "#" + defaultTag;
        }

        return formattedTags.toUpperCase();
    }

    public static String getSingleFormattedHashtag(String tags, String defaultTag) {
        String formattedTags = "";
        tags = tags.replace(",", " ");
        if (tags.trim().length() > 0) {
            String[] splitTags = tags.split("#");
            for (int i = 0; i < splitTags.length; i++) {
                if (splitTags[i].trim().length() > 0) {
                    formattedTags = formattedTags + "#" + splitTags[i].trim();
                    break;
                }
            }
        }

        if (formattedTags.length() < 1) {
            return null;
        } else {
            return formattedTags.toUpperCase();
        }
    }
}
