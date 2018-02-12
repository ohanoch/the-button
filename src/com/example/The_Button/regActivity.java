package com.example.The_Button;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class regActivity extends Activity {

    Button submitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg);
        addListenerOnSubmit();
    }

    @Override
    public void onResume (){
        super.onResume();
        getActionBar().setTitle(Html.fromHtml("<small>THE MIGHTY BUTTON!</small>"));
    }

    public void addListenerOnSubmit() {

        final Context context = this;
        submitButton = (Button) findViewById(R.id.registerSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EditText input = (EditText)findViewById(R.id.regUserNameText); //Read in username
                String name = input.getText().toString();
                input = (EditText)findViewById(R.id.regPassword1Text); //Read in username
                String pass1 = input.getText().toString();
                input = (EditText)findViewById(R.id.regPassword2Text); //Read in username
                String pass2 = input.getText().toString();
                input = (EditText)findViewById(R.id.regEmailText); //Read in username
                String email = input.getText().toString();
                input = (EditText)findViewById(R.id.regSecurityQText); //Read in username
                String securityQ = input.getText().toString();
                input = (EditText)findViewById(R.id.regSecurityAText); //Read in username
                String securityA = input.getText().toString();

                Log.d ("Registration", name + " " + pass1 + " " + pass2 + " " + email + " " + securityQ + " " + securityA);
                Log.d ("Registration", "" + android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());

                if(name.isEmpty()) {  //if the name field is Empty
                    EditText field = (EditText)findViewById(R.id.regUserNameText);
                    field.setText("");
                    field = (EditText)findViewById(R.id.regPassword1Text);
                    field.setText("");
                    field = (EditText)findViewById(R.id.regPassword2Text);
                    field.setText("");
                    Toast toast = Toast.makeText(context, "Username can't be empty", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(pass1.isEmpty()) {  //if the password field is Empty
                    EditText field = (EditText)findViewById(R.id.regPassword1Text);
                    field.setText("");
                    field = (EditText)findViewById(R.id.regPassword2Text);
                    field.setText("");
                    Toast toast = Toast.makeText(context, "Password can't be empty", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(!pass1.equals(pass2)) {  //if password is different from repeated password
                    EditText field = (EditText)findViewById(R.id.regPassword1Text);
                    field.setText("");
                    field = (EditText)findViewById(R.id.regPassword2Text);
                    field.setText("");
                    Toast toast = Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(!email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    EditText field = (EditText)findViewById(R.id.regPassword1Text);
                    field.setText("");
                    field = (EditText)findViewById(R.id.regPassword2Text);
                    field.setText("");
                    Toast toast = Toast.makeText(context, "Email is not in correct format", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(securityQ.isEmpty()) {  //if the security question field is Empty
                    EditText field = (EditText)findViewById(R.id.regPassword1Text);
                    field.setText("");
                    field = (EditText)findViewById(R.id.regPassword2Text);
                    field.setText("");
                    Toast toast = Toast.makeText(context, "Security question can't be empty", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(securityA.isEmpty()) {  //if the security answer field is Empty
                    EditText field = (EditText)findViewById(R.id.regPassword1Text);
                    field.setText("");
                    field = (EditText)findViewById(R.id.regPassword2Text);
                    field.setText("");
                    Toast toast = Toast.makeText(context, "Security answer can't be empty", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else{

                    ContentValues params = new ContentValues(); //container to hold parameters
                    params.put("username", name); //add username and password to my parameter container
                    params.put("password", pass1);
                    params.put("email", email);
                    params.put("securityQ", securityQ);
                    params.put("securityA", securityA);

                    asyncHTTPPost asyncHttpPost = new asyncHTTPPost(
                            "http://lamp.ms.wits.ac.za/~s1501858/addUser.php", params) {//send parameters in and get reply


                        @Override
                        protected void onPostExecute(String output) {

                            if (output.equals("\"0\"")) { //if user is already in use
                                //User already in use message
                                Toast toast = Toast.makeText(context, "username unavailable", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                EditText field = (EditText)findViewById(R.id.regUserNameText);
                                field.setText("");
                                field = (EditText)findViewById(R.id.regPassword1Text);
                                field.setText("");
                                field = (EditText)findViewById(R.id.regPassword2Text);
                                field.setText("");
                            } else if (output.equals("\"1\"")) { //user created
                                //User created
                                Toast toast = Toast.makeText(context, "Thank You For Registering \n\nPlease Login", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                Intent intent = new Intent(context, loginActivity.class);
                                startActivity(intent);
                            } else{
                                Toast toast = Toast.makeText(context, "Error registering!\n Notice: no special characters allowed in any of the fields, only letters and numbers allowed", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }
                    };
                    asyncHttpPost.execute(); //executes the post method
                }

            }
        });
    }



}