package com.example.The_Button;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.test.suitebuilder.TestMethod;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.*;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * Created by 1490527 on 2017/04/26.
 */
public class webActivity extends MyActivity{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weblayout);

        final userInfo currUser = ((userInfo)this.getApplication());

        webViewClick(currUser);

    }

    @Override
    public void onResume (){
        super.onResume();
        getActionBar().setTitle(Html.fromHtml("<small>THE MIGHTY BUTTON!</small>"));
    }

    public void webViewClick(userInfo currUser) {

        WebView webview = (WebView)findViewById(R.id.randomWeb);
        webview.setWebViewClient(new myWebViewClient());

        //make webview faster tweeks
        if (Build.VERSION.SDK_INT >= 19) {
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

//        currUser.logALLContent();

        // Load all other urls normally.
        webview.loadUrl(currUser.getCurrURL());


        TextView likeView = (TextView)findViewById(R.id.likeNum);
        TextView dislikeView = (TextView)findViewById(R.id.dislikeNum);

        ContentValues paramsShowLikes = new ContentValues(); //container to hold parameters
        paramsShowLikes.put("url",webview.getUrl());
        asyncHTTPPost asyncHttpPostShowLikes = new asyncHTTPPost( //send parameters in and get reply
                "http://lamp.ms.wits.ac.za/~s1501858/showLikes.php", paramsShowLikes) {
            @Override
            protected void onPostExecute(String output) {
                output = output.replace("[", "");
                output = output.replace("]", "");
                output = output.replace("\"", "");
                String likeArr[]=output.split(",");
                likeView.setText(likeArr[0]);
                dislikeView.setText(likeArr[1]);
            }
        };
        asyncHttpPostShowLikes.execute();


//                String webURL = webview.getUrl();
        ImageButton likeButton = (ImageButton)findViewById(R.id.likeImageButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues paramsLikeURL = new ContentValues(); //container to hold parameters
                paramsLikeURL.put("username", currUser.getName()); //add username and password to my parameter container
                paramsLikeURL.put("url", webview.getUrl());
                String source = currUser.getCurrName();
                paramsLikeURL.put("source", source);
                paramsLikeURL.put("title", webview.getTitle());
                paramsLikeURL.put("sourceType","webLink");

                asyncHTTPPost asyncHttpPostLikeURL = new asyncHTTPPost( //send parameters in and get reply
                        "http://lamp.ms.wits.ac.za/~s1501858/likeURL.php", paramsLikeURL) {
                    @Override
                    protected void onPostExecute(String output) {
                        output = output.replace("[", "");
                        output = output.replace("]", "");
                        output = output.replace("\"", "");
                        String likeArr[]=output.split(",");
                        likeView.setText(likeArr[0]);
                        dislikeView.setText(likeArr[1]);

                    }
                };
                asyncHttpPostLikeURL.execute();
            }

        });
        ImageButton dislikeButton = (ImageButton)findViewById(R.id.dislikeImageButton);
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues paramsDislikeURL = new ContentValues(); //container to hold parameters
                paramsDislikeURL.put("username", currUser.getName()); //add username and password to my parameter container
                paramsDislikeURL.put("url", webview.getUrl());
                String source = currUser.getCurrName();
                paramsDislikeURL.put("source", source);
                paramsDislikeURL.put("title", webview.getTitle());
                paramsDislikeURL.put("sourceType",currUser.getCurrSourceType());

                asyncHTTPPost asyncHttpPostDislikeURL = new asyncHTTPPost( //send parameters in and get reply
                        "http://lamp.ms.wits.ac.za/~s1501858/dislikeURL.php", paramsDislikeURL) {
                    @Override
                    protected void onPostExecute(String output) {
                        output = output.replace("[", "");
                        output = output.replace("]", "");
                        output = output.replace("\"", "");
                        String likeArr[]=output.split(",");
                        likeView.setText(likeArr[0]);
                        dislikeView.setText(likeArr[1]);

                    }
                };
                asyncHttpPostDislikeURL.execute();
            }

        });
//            }
//        };
//        asyncWC.execute();


    }






}
