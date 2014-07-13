package com.example.rci.rci;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by richard on 7/10/14.
 */
public class HttpManager implements AsyncTaskResponse {
    private HttpExecuter executer;

    public enum FORM_TYPE {
        SIGNUP,
        LOGIN,
        SSID_PUT,
        SSID_GET,
        USERLIST
    }

    public HttpManager() {
        this.executer = new HttpExecuter();
        executer.delegate = this;
    }

    public int post(FORM_TYPE form, Map<String, ? extends Object> entries) {
        try {
            String url = "http://54.210.12.229/";
            switch (form) {
                case SIGNUP:
                    url += "signup.php";
                    break;
                case LOGIN:
                    url += "login.php";
                    break;
                case SSID_PUT:
                    // TODO this
                    break;
                default:
                    return -1;
            }

            HttpPost post = new HttpPost(url);
            List<NameValuePair> n = new ArrayList<NameValuePair>(entries.size());
            for (String e : entries.keySet()) {
                n.add(new BasicNameValuePair(e, entries.get(e).toString()));
            }

            post.setEntity(new UrlEncodedFormEntity(n));
            return executer.execute(post).get().getStatusLine().getStatusCode();
        }
        catch (Exception e) {
            Log.e("HttpManager.Post", e.getClass().getName() + ": " + e.getLocalizedMessage());
            return -1;
        }
    }

    @Override
    public void processFinish(HttpResponse response) {

    }

    private class HttpExecuter extends AsyncTask<HttpRequestBase, Void, HttpResponse> {
        public AsyncTaskResponse delegate;

        @Override
        protected HttpResponse doInBackground(HttpRequestBase... requests) {
            try {
                HttpClient client = new DefaultHttpClient();
                return client.execute(requests[0]);
            } catch (IOException e) {
                Log.i("HttpExecuter", "Error happened here");
                return null;
            }
        }

        @Override
        protected void onPostExecute(HttpResponse httpResponse) {
            super.onPostExecute(httpResponse);
            delegate.processFinish(httpResponse);
        }
    }
}
