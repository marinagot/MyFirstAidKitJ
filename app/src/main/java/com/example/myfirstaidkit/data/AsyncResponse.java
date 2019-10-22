package com.example.myfirstaidkit.data;

import android.view.View;

// you may separate this or combined to caller class.
public interface AsyncResponse<T> {
    void processFinish(View v, T output);

    T apiCall(Object... params);
}