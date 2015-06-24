package com.android.porta.pk.responses;

/**
 * Created by Talha on 6/21/15.
 */
public abstract class Response {

    public String message;
    public int status;

    public boolean isSuccessful() {
        return status == 200;
    }
}
