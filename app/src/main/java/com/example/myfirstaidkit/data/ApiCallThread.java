package com.example.myfirstaidkit.data;

import android.os.AsyncTask;
import android.view.View;


public class ApiCallThread<T> extends AsyncTask<Object, Void, T> {

    public AsyncResponse<T> delegate;
    private View v;

    public ApiCallThread(AsyncResponse delegate){
        this.delegate = delegate;
    }

    protected T doInBackground(Object... params) {
        v = (View) params[0];
        return delegate.apiCall(params);
    }

    protected void onPostExecute(T result) {
        delegate.processFinish(v, result);
    }
}
