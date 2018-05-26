package edu.csusb.cse.employeemanager.httprequests;

import android.util.Log;

import java.util.concurrent.ExecutionException;

public class HttpRequests {

    private static final String TAG = "DEBUG";

    public HttpRequests(){

    }

    public String httpGetRequest(String SERVER){
        String employees = null;
        GetURLContentTask getURLContentTask = new GetURLContentTask();
        getURLContentTask.execute(SERVER);
        try {
            employees = getURLContentTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public String httpPostRequest(String json, String SERVER){
        Log.d(TAG, "httpPOSTRequest: line 159 " + json);

        String returnMessage = null;
        PostURLContentTask postURLContentTask = new PostURLContentTask();
        postURLContentTask.execute(SERVER, json);
        try {
            returnMessage = postURLContentTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return returnMessage;
    }

    public String httpDeleteRequest(String json, String SERVER){
        Log.d(TAG, "httpDeleteRequest: line 173 " + json);

        String returnMessage = null;
        DeleteURLContentTask deleteURLContentTask = new DeleteURLContentTask();
        deleteURLContentTask.execute(SERVER, json);
        try {
            returnMessage = deleteURLContentTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return returnMessage;
    }

    public String httpPutRequest(String json, String SERVER){
        Log.d(TAG, "httpPutRequest: " + json);

        String returnMessage = null;
        PutURLContentTask putURLContentTask = new PutURLContentTask();
        putURLContentTask.execute(SERVER, json);
        try {
            returnMessage = putURLContentTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return returnMessage;
    }
}
