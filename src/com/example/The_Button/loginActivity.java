package com.example.The_Button;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 1490527 on 2017/05/04.
 */
public class loginActivity extends Activity {
    Button regButton;
    Button loginButton;
    Button forgotPasswd;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }
    @Override
    public void onResume(){
        super.onResume();
        getActionBar().setTitle(Html.fromHtml("<small>THE MIGHTY BUTTON!</small>"));
        //        currUser.setName("or");
        final userInfo currUser = ((userInfo)this.getApplication());
        if(!currUser.getName().isEmpty()){
            Toast.makeText(this, "Cannot go back to login page - Please logout first", Toast.LENGTH_SHORT).show();
            //Change Activities
            final Context context = this;
            Intent intent = new Intent(context, buttonActivity.class);
            startActivity(intent);
        }
        //Listens for click
        addListenerOnReg();
        addListenerOnForgotPasswd();
        addListenerOnLogin();
    }


    public void innitializeAllContent(){

        final userInfo currUser = ((userInfo)getApplication());

        ContentValues params2 = new ContentValues(); //container to hold parameters
        params2.put("username", "none");

        asyncHTTPPost asyncHttpPost = new asyncHTTPPost( //send parameters in and get reply
                "http://lamp.ms.wits.ac.za/~s1501858/getSources.php", params2) {
            @Override
            protected void onPostExecute(String output) {

                try {
                    JSONArray all = new JSONArray(output);
                    for (int i=0; i<all.length(); i++){

                        JSONObject item=all.getJSONObject(i);
                        currUser.addToAllContent("Settings_"+item.getString("Source_Name"), item.getString("Source_Type"), item.getString("Source_URL"));
//                        Log.d("Initialize ALl Content:", item.getString("Source_Name")+ " " +  item.getString("Source_Type") + " " + item.getString("Source_URL"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("GETSOURCESCATCH", "on login went to catch when setting sources, error: " +e);
                }
            }
        };
        asyncHttpPost.execute(); //executes the post method




    };

    public void resetFields(){
        EditText field;

        field = (EditText)findViewById(R.id.uName);
        field.setText("");

        field = (EditText)findViewById(R.id.uPass);
        field.setText("");
    }


    public void addListenerOnLogin() {

        final Context context = this;
        loginButton = (Button) findViewById(R.id.Login);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                EditText input1 = (EditText)findViewById(R.id.uName); //Read in username
                String name = input1.getText().toString();

                EditText input2 = (EditText)findViewById(R.id.uPass);  //Read in password
                String password = input2.getText().toString();

                if(name.isEmpty()){  //if the name field is Empty

                    resetFields();
                    TextView invalid = (TextView)findViewById(R.id.invalidEntree);
                    invalid.setText("Please enter a Username");

                }else if(password.isEmpty()){ //if the password field is empty

                    TextView invalid = (TextView)findViewById(R.id.invalidEntree);
                    invalid.setText("Please enter a Password");

                    EditText field;
                    field = (EditText)findViewById(R.id.uPass);
                    field.setText("");
                }else{

                    ContentValues params = new ContentValues(); //container to hold parameters
                    params.put("username", name); //add username and password to my parameter container
                    params.put("password", password);

                    asyncHTTPPost asyncHttpPost = new asyncHTTPPost( //send parameters in and get reply
                            "http://lamp.ms.wits.ac.za/~s1501858/checkLogin.php", params) {
                        @Override
                        protected void onPostExecute(String output) {


                            if (output.equals("\"2\"")){ //if its a valid username and password

                                //Thank you message
                                LayoutInflater inflater = getLayoutInflater();
                                View toastView = inflater.inflate(R.layout.unicorn_toast,
                                        (ViewGroup) findViewById(R.id.relativeLayout1));
                                TextView unicornText = (TextView) toastView.findViewById(R.id.unicornText);
                                unicornText.setText("LETS HAVE FUN!\nWEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                                ImageView imageView = (ImageView)toastView.findViewById(R.id.unicornImg);
                                imageView.setImageResource(R.drawable.unicorn);

                                Toast toast = new Toast(context);
                                toast.setView(toastView);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();

//                                Toast toast = Toast.makeText(context, "Thank You \nEnjoy", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();

                                resetFields();

                                innitializeAllContent();

                                //set Global userName
                                final userInfo currUser = ((userInfo)getApplication());
                                currUser.setName(name);

//INSERT asyncWebClick here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                ContentValues params2 = new ContentValues(); //container to hold parameters
                                params2.put("username", name); //add username and password to my parameter container

                                asyncHTTPPost asyncHttpPost = new asyncHTTPPost( //send parameters in and get reply
                                        "http://lamp.ms.wits.ac.za/~s1501858/getSettings.php", params2) {
                                    @Override
                                    protected void onPostExecute(String output) {

                                        try {
                                            JSONArray all = new JSONArray(output);
                                            for (int i=0; i<currUser.getAllContentNames().length; i++){

                                                JSONObject item=all.getJSONObject(0);
                                                String source = item.getString(currUser.getAllContentNames()[i]);
//                                                Log.d("Set Available Content", "All Content Names["+i+"]: "+currUser.getAllContentNames()[i]);
//                                                Log.d("Set Available Content", "Source: "+source);
                                                if (source.equals("1")){
                                                    currUser.setAvailableContentNames(currUser.getAllContentNames()[i]);
                                                    currUser.setAvailableContentURLs(currUser.getAllContentURLs()[i]);
                                                }

//                                                int y = availableContent.split(" ").length;
//                                                Log.d("SIZEPREEXECUTE", "onPreExecute " +y);

                                            }
//                                            int y = availableContent.split(" ").length;
//                                            Log.d("SIZEPREEXECUTEDONE", "onPreExecute done " +y+" "+availableContent);
//                                            currUser.setContent(availableContent);
//                                            Log.d("SIZEPREEXECUTEDONE", "after setting currUser done " +y+" "+currUser.getContent());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.d("GETSETTINGSCATCH", "on login went to catch when setting settings, error: " +e);
                                        }
                                    }
                                };
                                asyncHttpPost.execute(); //executes the post method

                                asyncHTTPPost asyncHttpPostSecurityQ = new asyncHTTPPost( //send parameters in and get reply
                                        "http://lamp.ms.wits.ac.za/~s1501858/getSecurityQ.php", params2) {
                                    @Override
                                    protected void onPostExecute(String output) {

                                        currUser.setSecurityQ(output.replace("\"", ""));
                                    }
                                };
                                asyncHttpPostSecurityQ.execute(); //executes the post method

                                asyncHTTPPost asyncHttpPostSecurityA = new asyncHTTPPost( //send parameters in and get reply
                                        "http://lamp.ms.wits.ac.za/~s1501858/getSecurityA.php", params2) {
                                    @Override
                                    protected void onPostExecute(String output) {

                                        currUser.setSecurityA(output.replace("\"", ""));
                                    }
                                };
                                asyncHttpPostSecurityA.execute(); //executes the post method

//####################################################################################################################################################

//                                currUser.logALLContent();

                                //Change Activities
                                Intent intent = new Intent(context, buttonActivity.class);
                                startActivity(intent);

                            }else if(output.equals("\"1\"")) {//if valid username not valid password

                                TextView invalid = (TextView)findViewById(R.id.invalidEntree);
                                invalid.setText("Invalid Password");

                                EditText field;
                                field = (EditText)findViewById(R.id.uPass);
                                field.setText("");

                            }else if(output.equals("\"0\"")){//if both username and password are invalid

                                resetFields();
                                TextView invalid = (TextView)findViewById(R.id.invalidEntree);
                                invalid.setText("Invalid Username");

                            }
                        }
                    };
                    asyncHttpPost.execute(); //executes the post method
                }

            }
        });
    }

    public void addListenerOnReg() {

        final Context context = this;
        regButton = (Button) findViewById(R.id.reg);
        regButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, regActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addListenerOnForgotPasswd() {

        final Context context = this;
        forgotPasswd = (Button) findViewById(R.id.forgotPasswd);
        forgotPasswd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, forgotPasswdActivity.class);
                startActivity(intent);
            }
        });
    }
}
