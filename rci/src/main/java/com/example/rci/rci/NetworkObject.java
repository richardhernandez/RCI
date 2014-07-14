package com.example.rci.rci;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by richard on 7/13/14.
 */
public class NetworkObject {
    protected Map<String, String> attributes;

    public boolean saveObject(String path) {
        JSONObject json = new JSONObject();
        try {
            for (String key : attributes.keySet()) {
                json.put(key, attributes.get(key));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        HttpManager.post("user", null, new JsonHttpResponseHandler() {

        });

        return true;
    }
}
