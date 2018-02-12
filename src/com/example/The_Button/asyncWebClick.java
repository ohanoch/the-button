package com.example.The_Button;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public abstract class asyncWebClick extends AsyncTask<String, String, String> {
    String username;
    String availableContent;
    final userInfo currUser;
    String[] contentNames={"wikipedia", "xkcd"};
    String[] allContent={"https://en.wikipedia.org/wiki/Special:Random", "https://c.xkcd.com/random/mobile_comic/"};

    @Override
    protected String doInBackground(String... params){
        Log.d("SIZEBACKGROUND", "do in background was summond here ");
        return "";
    }

    public asyncWebClick(String inUsername, Context context) {
        username=inUsername;
        availableContent = "";
        Log.d("BEFORECURRUSER", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa ");
        currUser = ((userInfo)context.getApplicationContext());
        Log.d("AFTERCURRUSER", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa ");
    }

    @Override
    protected void onPreExecute() { //CHANGE THIS TO CHECK FROM currUSer AND NOT FROM DATABASE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        try {
            Log.d("SIZEPRE", "onPreExecute was summond here "+currUser.getName());
//            String[] contentNames={"wikipedia", "xkcd"};
//            String[] allContent={"https://en.wikipedia.org/wiki/Special:Random", "https://c.xkcd.com/random/mobile_comic/"};



//            final Context context = this;

            ContentValues params2 = new ContentValues(); //container to hold parameters
            params2.put("username", username); //add username and password to my parameter container

            asyncHTTPPost asyncHttpPost = new asyncHTTPPost( //send parameters in and get reply
                    "http://lamp.ms.wits.ac.za/~s1501858/getSettings.php", params2) {
                @Override
                protected void onPostExecute(String output) {

                    try {
                        JSONArray all = new JSONArray(output);
                        for (int i=0; i<contentNames.length; i++){

                            JSONObject item=all.getJSONObject(0);
                            String source = item.getString(contentNames[i]);
                            if (source.equals("1")){
                                if (availableContent.isEmpty()) {
                                    availableContent=allContent[i];
                                }else {
                                    availableContent = availableContent + " " + allContent[i];
                                }
                            }

                            int y = availableContent.split(" ").length;
                            Log.d("SIZEPREEXECUTE", "onPreExecute " +y);
//                            Toast toast = Toast.makeText(this,""+ y, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                        }
                        int y = availableContent.split(" ").length;
                        Log.d("SIZEPREEXECUTEDONE", "onPreExecute done " +y+" "+availableContent);
                        currUser.setAvailableContentURLs(availableContent);
                        Log.d("SIZEPREEXECUTEDONE", "after setting currUser done " +y+" "+currUser.getAvailableContentURLs());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("PREEXECUTECATCH", "onPreExecute " +e);
//                        Toast toast = Toast.makeText(context,"Error:\n"+e , Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                    }
                }
            };
            asyncHttpPost.execute(); //executes the post method

        } catch (Exception e) {
            e.printStackTrace();
            StringBuilder builder = new StringBuilder();
            for(String s : allContent) {
                builder.append(s);
                builder.append(" ");
            }
            currUser.setAvailableContentURLs(builder.toString());
        }
    }

    @Override
    protected abstract void onPostExecute(String output);

}