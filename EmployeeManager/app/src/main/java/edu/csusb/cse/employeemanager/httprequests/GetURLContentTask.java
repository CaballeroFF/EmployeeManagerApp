package edu.csusb.cse.employeemanager.httprequests;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetURLContentTask extends AsyncTask<String, Integer, String> {
    private static final String TAG = "DEBUG";

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder temp = new StringBuilder();
        int responseCode = -1;

        String getUrl = Uri.parse(strings[0])
                .buildUpon()
                .appendPath("employee")
                .build().toString();

        try{
            URL url = new URL(getUrl);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("GET");
            request.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;

            while((line = reader.readLine()) != null){
                temp.append(line);
            }

            responseCode = request.getResponseCode();

            request.disconnect();

        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return temp.toString();
    }
}
