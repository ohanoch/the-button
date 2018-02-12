package com.example.The_Button;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 1490527 on 2017/04/25.
 */
public class userInfo extends Application {

    private String username="";
    private String availableContentURLs="";
    private String availableContentNames="";
    //private String[] allContentNames={"Settings_Wikipedia", "Settings_XKCD"};
    //private String[] allContentURLs={"https://en.wikipedia.org/wiki/Special:Random", "https://c.xkcd.com/random/mobile_comic/"};
    private ArrayList<String[]> allContent= new ArrayList<>();
    private String securityQ="";
    private String securityA="";
    private String currURL=""; //curr URL that weblink opens
    private String currName=""; //curr Source that weblink opens
    private String currSourceType = ""; //current SourceName
    public String tempAvailableContentNames=""; //used for settings page
    private String tempAvailableContentUrls=""; //used for settings page


    public void addToAllContent(String Source_Name, String Source_Type, String Source_URL){

        String[] temp = {Source_Name, Source_Type, Source_URL};
        allContent.add(temp);
    }

    public void logALLContent(){

        Log.d("CurrUserLog", "username: "+username);
        Log.d("CurrUserLog", "Available Content URL:" + availableContentURLs);
        Log.d("CurrUserLog", "available Content Names:" + availableContentNames);
        for(int i=0; i<allContent.size(); i++){
            Log.d("CurrUserLog", "all Content position 0,0: " + allContent.get(i)[0] + "  Size: "  + allContent.size());
        }

        Log.d("CurrUserLog", "current Name: " + currName);
        Log.d("CurrUserLog", "current URL: " + currURL);
        Log.d("CurrUserLog", "current Source Type: " + currSourceType);


    }

    public String getName() {
        return username;
    }
    public void setName(String input) {
        this.username = input;
    }

    public String getAvailableContentNames() { return availableContentNames;}

    public void setAvailableContentNames(String input) {
        if (this.availableContentNames.isEmpty()){
            this.availableContentNames=input;
        }else{
            this.availableContentNames = this.availableContentNames+" "+input;
        }
    }

    public String getAvailableContentURLs() { return availableContentURLs;}

    public void setAvailableContentURLs(String input) {
        if (this.availableContentURLs.isEmpty()){
            this.availableContentURLs=input;
        }else{
            this.availableContentURLs = this.availableContentURLs+" "+input;
        }
    }

    public String[] getAllContentNames() {

        String[] temp = new String[allContent.size()];

        for(int i = 0; i<allContent.size(); i++ ){
            temp[i] = allContent.get(i)[0];
        }


        return temp;
    }

    public String[] getAllContentURLs() {
        String[] temp = new String[allContent.size()];

        for(int i = 0; i<allContent.size(); i++ ){
            temp[i] = allContent.get(i)[2];
        }


        return temp;
    }

    public String getSecurityQ(){return securityQ;}
    public void setSecurityQ(String input){securityQ = input;}

    public String getSecurityA(){return securityA;}
    public void setSecurityA(String input){securityA = input;}

    public void setCurrURL(String input){currURL =  input;}
    public String getCurrURL(){return currURL;}

    public void setCurrName(String input){
        currName =  input;

        for (String[] curr : allContent){

            if(curr[0].contains(input)){

                currSourceType = curr[1];
                break;

            }
        }


    }
    public String getCurrName(){return currName;}

    public String getCurrSourceType(){return currSourceType;}

    public void syncTempToAvailableContent() {
        availableContentNames = tempAvailableContentNames;
        availableContentURLs = tempAvailableContentUrls;
    }

    public void syncAvailableContentToTemp() {
//        Log.d("TempRemove -1",":"+ tempAvailableContentNames);
//        Log.d("TempRemove -1",":"+ availableContentNames);
        tempAvailableContentNames = availableContentNames;
        tempAvailableContentUrls = availableContentURLs;
//        Log.d("TempRemove 0", ":"+tempAvailableContentNames);
//        Log.d("TempRemove 0", ":"+availableContentNames);
    }
    public void tempUpdate(String name, String url) {
        if (this.tempAvailableContentNames.isEmpty()){
            this.tempAvailableContentNames=name;
        }else{
            this.tempAvailableContentNames = this.tempAvailableContentNames+" "+name;
        }
        if (this.tempAvailableContentUrls.isEmpty()){
            this.tempAvailableContentUrls=url;
        }else{
            this.tempAvailableContentUrls = this.tempAvailableContentUrls+" "+url;
        }
    }
    public void tempRemove(String name, String url){
//        Log.d("TempRemove 1", ":"+tempAvailableContentNames);
//        Log.d("TempRemove 1", ":"+availableContentNames);

        tempAvailableContentNames = tempAvailableContentNames.replace(name+" ","");
//        Log.d("TempRemove 2.1", ":"+tempAvailableContentNames);
        tempAvailableContentNames = tempAvailableContentNames.replace(" "+name,"");
//        Log.d("TempRemove 2.2", ":"+tempAvailableContentNames);
        tempAvailableContentNames = tempAvailableContentNames.replace(name,"");
//        Log.d("TempRemove 2.3", ":"+tempAvailableContentNames);
        tempAvailableContentNames = tempAvailableContentNames.replace("  "," ");
//        Log.d("TempRemove 2.4", ":"+tempAvailableContentNames);




        tempAvailableContentUrls = tempAvailableContentUrls.replace(url+" ","");
//        Log.d("TempRemove 3.1", ":"+tempAvailableContentUrls);
        tempAvailableContentUrls = tempAvailableContentUrls.replace(" "+url,"");
//        Log.d("TempRemove 3.2", ":"+tempAvailableContentUrls);
        tempAvailableContentUrls = tempAvailableContentUrls.replace(url,"");
//        Log.d("TempRemove 3.3", ":"+tempAvailableContentUrls);
        tempAvailableContentUrls = tempAvailableContentUrls.replace("  "," ");
//        Log.d("TempRemove 3.4", ":"+tempAvailableContentUrls);
        if (tempAvailableContentNames.startsWith(" ")){
            tempAvailableContentNames.replaceFirst(" ", "");

        }
//        Log.d("TempRemove 4", ":"+tempAvailableContentNames);
        if (tempAvailableContentUrls.startsWith(" ")){
            tempAvailableContentUrls.replaceFirst(" ", "");
        }
//        Log.d("TempRemove 5", ":"+tempAvailableContentUrls);
    }

    public void tempReset() {
        tempAvailableContentNames = "";
        tempAvailableContentUrls = "";
    }

    public String getTempAvailableContentNames(){
        return tempAvailableContentNames;
    }

    public void reset() {
        this.username = "";
        this.availableContentURLs="";
        this.availableContentNames="";

         username="";
         availableContentURLs="";
         availableContentNames="";
        allContent.clear();
        // allContentNames={"Settings_Wikipedia", "Settings_XKCD"};
        // allContentURLs={"https://en.wikipedia.org/wiki/Special:Random", "https://c.xkcd.com/random/mobile_comic/"};
         securityQ="";
         securityA="";
         currURL=""; //curr URL that weblink opens
         currName=""; //curr Source that weblink opens
         tempAvailableContentNames=""; //used for settings page
         tempAvailableContentUrls="";
        currSourceType="";
    }
//
//    public void resetContent() {
//        this.availableContentURLs="";
//        this.availableContentNames="";
//    }
}