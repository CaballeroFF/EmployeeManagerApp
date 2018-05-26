package edu.csusb.cse.employeemanager.httprequests;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteURLContentTask extends AsyncTask<String, Integer, String> {

        private static final String TAG = "DEBUG";

@Override
protected String doInBackground(String... strings) {

        String post;
        int code = -1;

        StringBuilder temp = new StringBuilder();

        try{
                post = strings[1];
                String addurl = Uri.parse(strings[0])
                        .buildUpon()
                        .appendPath("employee")
                        .build().toString();
                URL url = new URL(addurl);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();

                request.setRequestMethod("DELETE");
                request.addRequestProperty("Content-Length", Integer.toString(post.length()));
                request.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                request.setDoOutput(true);
                request.connect();

                OutputStreamWriter writer = new OutputStreamWriter(request.getOutputStream());
                writer.write(post);
                writer.flush();
                writer.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                String line;

                while((line = reader.readLine()) != null){
                        temp.append(line);
                }

                code = request.getResponseCode();

                request.disconnect();
        } catch (IOException e){
                e.printStackTrace();
        }
        return temp.toString();
        }
}
