package com.example.rci.rci;

import org.apache.http.HttpResponse;

/**
 * Created by richard on 7/11/14.
 */
public interface AsyncTaskResponse {
    public void processFinish(HttpResponse response);
}
