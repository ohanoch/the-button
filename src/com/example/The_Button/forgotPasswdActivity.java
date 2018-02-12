package com.example.The_Button;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class forgotPasswdActivity extends Activity {


    TextView mainView;
    EditText input;
    final Context context = this;
    String currActivity;
    String user;
    String newPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpasswd);


        mainView = (TextView)findViewById(R.id.mainText);
        input = (EditText)findViewById(R.id.resetInput);

        setUName();
        onclickSubmit();
    }

    @Override
    public void onResume (){
        super.onResume();
        getActionBar().setTitle(Html.fromHtml("<small>THE MIGHTY BUTTON!</small>"));
    }

    public void onclickSubmit(){

        Button submit = (Button) findViewById(R.id.resetSubmit);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String readIn = input.getText().toString();

                if (currActivity.equals("getUName")){

                    getUName(readIn);

                }else if(currActivity.equals("securityQ")){

                    askSecurityQ(readIn);

                }else if(currActivity.equals("password1")){

                    checkPassword1(readIn);

                }else if(currActivity.equals("password2")){

                    checkPassword2(readIn);

                }



            }

        });

    }

    public void setUName(){
        resetFields();

        currActivity = "getUName";

        mainView.setText("Username");

    }

    public void getUName(String uName){

        if(uName.equals("")){
            Toast toast = Toast.makeText(context, "Enter A UserName", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            setUName();
        }else{

            ContentValues params = new ContentValues(); //container to hold parameters
            params.put("username", uName); //add username and password to my parameter container
            params.put("password", "");

            asyncHTTPPost asyncHttpPost = new asyncHTTPPost( //send parameters in and get reply
                    "http://lamp.ms.wits.ac.za/~s1501858/checkLogin.php", params) {
                @Override
                protected void onPostExecute(String output) {


                   if(output.equals("\"1\"")) {//if valid username not valid password

                       user = uName;
                       setSecurityQ();


                    }else if(output.equals("\"0\"")){//if both username and password are invalid

                       Toast toast = Toast.makeText(context, "Invalid Username", Toast.LENGTH_LONG);
                       toast.setGravity(Gravity.CENTER, 0, 0);
                       toast.show();

                        setUName();


                    }
                }
            };
            asyncHttpPost.execute(); //executes the post method


        }

    }

    public void setSecurityQ(){

        resetFields();

        currActivity = "securityQ";

        ContentValues params = new ContentValues(); //container to hold parameters
        params.put("username", user); //add username and password to my parameter container

        asyncHTTPPost asyncHttpPost = new asyncHTTPPost( //send parameters in and get reply
                "http://lamp.ms.wits.ac.za/~s1501858/getSecurityQ.php", params) {
            @Override
            protected void onPostExecute(String output) {

                output=output.replace("\"", "");
                mainView.setText(output);
            }
        };
        asyncHttpPost.execute(); //executes the post method


    }

    public void askSecurityQ(String answer){

        if(answer.equals("")){
            Toast toast = Toast.makeText(context, "Answer the bloody question!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            setSecurityQ();
        }else{

            ContentValues params = new ContentValues(); //container to hold parameters
            params.put("username", user);
            params.put("answer", answer);


            asyncHTTPPost asyncHttpPost = new asyncHTTPPost( //send parameters in and get reply
                    "http://lamp.ms.wits.ac.za/~s1501858/checkAnswer.php", params) {
                @Override
                protected void onPostExecute(String output) {

                    if(output.equals("\"1\"")) {//if valid username not valid password

                        setPasswordFirst();


                    }else if(output.equals("\"0\"")){//if both username and password are invalid

                        Toast toast = Toast.makeText(context, "Incorrect Answer", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        setSecurityQ();


                    }
                }
            };
            asyncHttpPost.execute(); //executes the post method


        }

    }

    public void setPasswordFirst(){

        resetFields();
        mainView.setText("Please Enter Password");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // sets field to accept a password
        currActivity = "password1";
    }

    public void setPasswordSecond(){


        resetFields();
        mainView.setText("Please Re-enter Password");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // sets field to accept a password
        currActivity = "password2";
    }

    public void checkPassword1(String password){

        if(password.equals("")){
            Toast toast = Toast.makeText(context, "Enter a Password", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            setPasswordFirst();
        }else{

            newPassword = password;
            setPasswordSecond();

        }

    }

    public void checkPassword2(String password){

        if(password.equals("")){
            Toast toast = Toast.makeText(context, "Enter a Password", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            setPasswordSecond();
        }else if(newPassword.equals(password)){

            Toast toast = Toast.makeText(context, "Password Changed", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            ContentValues params = new ContentValues(); //container to hold parameters
            params.put("username", user);
            params.put("password", password);


            asyncHTTPPost asyncHttpPost = new asyncHTTPPost( //send parameters in and get reply
                    "http://lamp.ms.wits.ac.za/~s1501858/changePassword.php", params) {
                @Override
                protected void onPostExecute(String output) {

                    Intent intent = new Intent(context, loginActivity.class);
                    startActivity(intent);

                }
            };

            asyncHttpPost.execute(); //executes the post method


        }else{

            Toast toast = Toast.makeText(context, "Passwords Do Not Match", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            password = "";
            setPasswordFirst();
        }




    }

    public void resetFields(){
        //resets all fields
        mainView.setText("");
        input.setText("");
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);//sets the input type of input to normal text
        currActivity = "";
    }


}