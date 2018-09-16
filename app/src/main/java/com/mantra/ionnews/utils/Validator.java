package com.mantra.ionnews.utils;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rajat on 17/03/17.
 */

public class Validator {

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

    public static Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static Matcher matcher;

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        if (password.length() >= 5) {
            return true;
        }
        return false;
    }

    public static boolean isValidMobileNumber(String mobile_no) {
        return android.util.Patterns.PHONE.matcher(mobile_no).matches();
    }

    public static boolean isValidName(String name) {
        if (name.length() > 1)
            return true;
        return false;
    }

    public static boolean hasText(EditText editText, String errMsg) {
        if (editText.getEditableText().toString().length() > 0) {
            return true;
        }
        editText.requestFocus();
        editText.setError(errMsg);
        return false;
    }

    public static boolean validate(final String password) {
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
