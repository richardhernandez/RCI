package com.example.rci.rci;

import java.util.Map;
import java.util.HashMap;

import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Created by Chase Johnston on 7/20/2014.
 */
public class SmsSender {

    /* Find your sid and token at twilio.com/user/account */
    public static final String ACCOUNT_SID = "ACff32e9d9c349d90831715cbdec51cf66";
    public static final String AUTH_TOKEN = "[AuthToken]";

    public static void main(String[] args) throws TwilioRestException {

        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        Account account = client.getAccount();

        MessageFactory messageFactory = account.getMessageFactory();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("To", "+6789929460")); // Replace with a valid phone number for your account.
        params.add(new BasicNameValuePair("From", "+17706267136")); // Replace with a valid phone number for your account.
        params.add(new BasicNameValuePair("Body", "How do I change my Router?"));
        Message sms = messageFactory.create(params);
    }
}
