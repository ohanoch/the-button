package com.example.The_Button;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Random;

public class buttonActivity extends MyActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button);
//
//        final userInfo currUser = ((userInfo)this.getApplication());
////        currUser.setContent();
//        Log.d("BEFOREBUTTONCLICK", "before onbuttonclick ");



    }

    @Override
    public void onResume(){
        super.onResume();

        getActionBar().setTitle(Html.fromHtml("<small>THE MIGHTY BUTTON!</small>"));
        final userInfo currUser = ((userInfo)this.getApplication());
        if(currUser.getName().isEmpty()){
            Toast.makeText(this, "Cannot go back to Button - Please login first", Toast.LENGTH_SHORT).show();
            //Change Activities
            final Context context = this;
            Intent intent = new Intent(context, buttonActivity.class);
            startActivity(intent);
        }

        onButtonClick();

    }


    public void onButtonClick(){

        final Context context = this;
        ImageButton theButton = (ImageButton)findViewById(R.id.BRB);
        final userInfo currUser = ((userInfo)this.getApplication());

//        currUser.logALLContent();

        theButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String outputArr[] = currUser.getAvailableContentURLs().split(" ");
                Random rand = new Random();
                int num=rand.nextInt(outputArr.length);
                currUser.setCurrURL(outputArr[num]);
                currUser.setCurrName(currUser.getAvailableContentNames().split(" ")[num].replace("Settings_", ""));
//                Log.d("CURRENTURL", currUser.getAllContentNames().toString());
//               Log.d("CURRENTURL", currUser.getAvailableContentNames());
                Log.d("CURRENTURL", "The Source URL is: " + outputArr[num]);
                Log.d("CURRENTURL", "The Source Name is: " + currUser.getAvailableContentNames().split(" ")[num].replace("Settings_", ""));


                LayoutInflater inflater = getLayoutInflater();
                View toastView = inflater.inflate(R.layout.unicorn_toast,
                        (ViewGroup) findViewById(R.id.relativeLayout1));
                TextView unicornText = (TextView) toastView.findViewById(R.id.unicornText);
                unicornText.setText("Here, have some cool stuff!\nWEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                ImageView imageView = (ImageView)toastView.findViewById(R.id.unicornImg);
                imageView.setImageResource(R.drawable.puking_unicorn);

                Toast toast = new Toast(context);
                toast.setView(toastView);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();


                Intent intent = new Intent(context, webActivity.class);
                startActivity(intent);

            }
        });

    }

}