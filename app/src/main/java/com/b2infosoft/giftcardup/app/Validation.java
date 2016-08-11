package com.b2infosoft.giftcardup.app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rajesh on 7/14/2016.
 */
public class Validation {

    private static Validation instance = new Validation();

    private Validation() {

    }

    public static Validation getInstance() {
        return instance;
    }

    public boolean isEnrollmentNumber(String enroll) {
        if (enroll.length() >= 5 && enroll.length() <= 8) {
            return true;
        }
        return false;
    }

    public boolean isMobileNumber(String mobile) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", mobile)) {
            if (mobile.length() != 10) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    public boolean isPassword(String password) {
        boolean check = false;
        if (password.length()< 5) {
            check = false;
        } else {
            check = true;
        }
        return check;
    }

    public boolean isPassword1(String password) {
        boolean check = false;
        if (password.length()< 1) {
            check = false;
        } else {
            check = true;
        }
        return check;
    }


    public boolean isPasswordConfirm(String s, String s1) {
        return s.equals(s1);
    }

    public boolean isName(String name) {
        Pattern pattern = Pattern.compile(new String("^[a-zA-Z\\s]*$"));
        Matcher matcher = pattern.matcher(name);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public boolean isSubject(String subject) {
        boolean check = false;
        if (subject.length() == 0) {
            check = false;
        } else {
            check = true;
        }
        return check;
    }

    public boolean isMessage(String message) {
        boolean check = false;
        if (message.length() == 0) {
            check = false;
        } else {
            check = true;
        }
        return check;
    }
    public boolean isInstituteID(String str) {
        if (str.length() != 0)
            return true;
        return false;
    }
    public boolean isSession(String str) {
        if (str.length() != 0)
            return true;
        return false;
    }
    public boolean isScholarID(String str) {
        if (str.length() != 0)
            return true;
        return false;
    }
}
