package com.example.myfirstaidkit.data;

import android.provider.BaseColumns;

public class UserUtilities {

    public static abstract class UserEntry implements BaseColumns{

        public static final String USER_TABLE = "user";
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String BIRTHDATE = "birthday";
        public static final String AVATAR = "avatar";
        public static final String PASSWORD = "password";
        public static final String CONFIRM_PASSWORD = "confirm_password";

    }
}
