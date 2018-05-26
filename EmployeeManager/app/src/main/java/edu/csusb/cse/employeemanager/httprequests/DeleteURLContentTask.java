package edu.csusb.cse.employeemanager.httprequests;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteURLContentTask extends AsyncTask<String, Integer, String> {

        private static final String TAG = "DEBUG";

@Override
protected String doInBackground(String... strings) {

        String id = strings[1];

        StringBuilder temp = new StringBuilder();
        HttpURLConnection connection = null;

        String delUrl = Uri.parse(strings[0])
                .buildUpon()
                .appendPath("employee")
                .appendPath(id)
                .build().toString();
        Log.d(TAG, "doInBackground: " + delUrl);

        try {
        URL url = new URL(delUrl);
        connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("DELETE");

        InputStream in = new BufferedInputStream(connection.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine())!= null){
        temp.append(line).append('\n');
        }
        } catch (IOException e){
        e.printStackTrace();
        }
        finally {
        if (connection != null) {
        connection.disconnect();
        }
        }
        return temp.toString();
        }
}
