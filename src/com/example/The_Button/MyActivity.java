package com.example.The_Button;

//import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
//import android.text.Layout;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Random;


public class MyActivity extends Activity {

    //#########################################################################################################################################
    // http://stackoverflow.com/questions/3270206/same-option-menu-in-all-activities-in-android
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final userInfo currUser = ((userInfo) this.getApplication());
        Context context = this;
        switch (item.getItemId()) {
            // action with ID action_logout was selected
            case R.id.action_logout:
                LayoutInflater inflater = getLayoutInflater();
                View toastView = inflater.inflate(R.layout.unicorn_toast,
                        (ViewGroup) findViewById(R.id.relativeLayout1));
                TextView unicornText = (TextView) toastView.findViewById(R.id.unicornText);
                unicornText.setText("Why do you leave me??\nPlease come back and be my friend..");
                ImageView imageView = (ImageView)toastView.findViewById(R.id.unicornImg);
                imageView.setImageResource(R.drawable.sad_unicorn);

                Toast toast = new Toast(context);
                toast.setView(toastView);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
//                Toast.makeText(this, "Logging out - We hope you enjoyed your stay", Toast.LENGTH_SHORT).show();

                ContentValues settingsParams = new ContentValues(); //container to hold parameters
                settingsParams.put("username", currUser.getName());
//                Log.d("LOGOUTPRESSED","currUser.getAvailableContentNames() is: "+currUser.getAvailableContentNames());
                String newSettings="";
                for (int i = 0; i < currUser.getAllContentNames().length; i++) {
                    if (currUser.getAvailableContentNames().contains(currUser.getAllContentNames()[i])){
                        if (i == currUser.getAllContentNames().length -1){

                            newSettings+=currUser.getAllContentNames()[i]+ "=1";

                        }else{
                            newSettings+=currUser.getAllContentNames()[i]+ "=1, ";
                        }

                    }else{
                        if (i == currUser.getAllContentNames().length -1){

                            newSettings+=currUser.getAllContentNames()[i]+ "=0";

                        }else{
                            newSettings+=currUser.getAllContentNames()[i]+ "=0, ";
                        }
                    }
                }
//                Log.d("ResetSettings", newSettings);
                settingsParams.put("newSettings", newSettings);


                asyncHTTPPost asyncHttpPostSettings = new asyncHTTPPost( //send parameters in and get reply
                        "http://lamp.ms.wits.ac.za/~s1501858/setSettings2.php", settingsParams) {
                    @Override
                    protected void onPostExecute(String output) {/*no need for this function*/}
                };
                asyncHttpPostSettings.execute();

                ContentValues securityParams = new ContentValues(); //container to hold parameters
                securityParams.put("username", currUser.getName());
                securityParams.put("securityQ", currUser.getSecurityQ());
                securityParams.put("securityA", currUser.getSecurityA());
                asyncHTTPPost asyncHttpPostSecurity = new asyncHTTPPost( //send parameters in and get reply
                        "http://lamp.ms.wits.ac.za/~s1501858/setSecurityQuestions.php", securityParams) {
                    @Override
                    protected void onPostExecute(String output) {/*no need for this function*/}
                };
                asyncHttpPostSecurity.execute();

                currUser.reset();

                Intent intent = new Intent(this, loginActivity.class);
                startActivity(intent);
                break;

            // action with ID action_favorites was selected
            case R.id.action_favorites:
                getActionBar().setTitle(Html.fromHtml("<small>The Button - Favorites</small>"));

                LayoutInflater favoritesInflater = (LayoutInflater) MyActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View favoriteLayout = favoritesInflater.inflate(R.layout.favoriteslayout,
                        (ViewGroup) findViewById(R.id.favoriteslayout));
                PopupWindow favoriteWindow = new PopupWindow(favoriteLayout, 700, 1100, true);

                ContentValues favoriteParams = new ContentValues(); //container to hold parameters
                favoriteParams.put("username", currUser.getName());

                asyncHTTPPost asyncHttpPostFavorites = new asyncHTTPPost( //send parameters in and get reply
                        "http://lamp.ms.wits.ac.za/~s1501858/getFavorites.php", favoriteParams) {
                    @Override
                    protected void onPostExecute(String output) {



                        try {
                            JSONArray all = new JSONArray(output);

                            String[] favURLList = new String[all.length()];
                            String[] favNameList = new String[all.length()];
                            String[] favSourceList = new String[all.length()];
                            for (int i=0; i<all.length(); i++){
                                JSONObject item=all.getJSONObject(i);
                                favURLList[i] = item.getString("Link_URL");
                                favNameList[i] = item.getString("Link_Title");
                                favSourceList[i] = item.getString("Source_Name");
                            }

                            favoriteWindow.showAtLocation(favoriteLayout, Gravity.CENTER, 0, 0);
                            LinearLayout l = (LinearLayout)favoriteLayout.findViewById(R.id.favoriteslayout);

                            if (favURLList.length > 0){

                                for (int i = 0; i < favURLList.length; i++) {
                                    Button currFavorite = new Button(context);
                                    currFavorite.setTransformationMethod(null);
                                    currFavorite.setGravity(Gravity.LEFT);
                                    currFavorite.setPadding(50,50,50,50);
                                    currFavorite.setText(i+1+") " + favNameList[i]);
                                    l.addView(currFavorite);
                                    final String currURL = favURLList[i];
                                    final String currSource = favSourceList[i];
                                    currFavorite.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            currUser.setCurrURL(currURL);
                                            currUser.setCurrName(currSource);
                                            favoriteWindow.dismiss();
                                            Intent intent = new Intent(context, webActivity.class);
                                            startActivity(intent);

                                        }
                                    });
                                }

                            }else{

                                TextView noFavorites = new TextView(context);
                                noFavorites.setTextColor(Color.BLACK);
                                noFavorites.setText(
                                        "No Favorites! \n\nGo like Somthing! \n\nUntill Then..." +
                                        "How much wood, would a woodchuck chuck if a woodchuck could chuck wood?" +
                                        "\n\nCompared to beavers, groundhogs/woodchucks are not adept at moving timber, although some will chew" +
                                        " wood. A wildlife biologist once measured the inside volume of a typical woodchuck burrow and estimated " +
                                        "that -- if wood filled the hole instead of dirt -- the industrious animal would have chucked about 700 pounds' worth.");
                                l.addView(noFavorites);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("GETYFAVORITES", "Error: " +e);
                        }

                    }
                };
                asyncHttpPostFavorites.execute();

                favoriteWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        getActionBar().setTitle(Html.fromHtml("<small>THE MIGHTY BUTTON!</small>"));
                    }
                });
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                getActionBar().setTitle(Html.fromHtml("<small>The Button - Settings</small>"));

                try {

//###############################################################################################################################################

                    LayoutInflater settingsInflater = (LayoutInflater) MyActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View settingsLayout = settingsInflater.inflate(R.layout.settingslayout2,
                            (ViewGroup) findViewById(R.id.settingsLinearLayout));
                    PopupWindow settingsWindow = new PopupWindow(settingsLayout, 700, 1100, true);

                    settingsWindow.showAtLocation(settingsLayout, Gravity.CENTER, 0, 0);
                    LinearLayout l = (LinearLayout)settingsLayout.findViewById(R.id.settingsLinearLayout);



//                    currUser.syncAllContentToTemp();

//                    currUser.syncAvailableContentToTemp();
                    currUser.tempReset();
                    int sourceIndex=0;
                    for (String currSource : currUser.getAllContentNames()){
//                        Log.d("Settings","currSource1 "+currSource);
                        CheckBox currCB = new CheckBox(this);
//                        Log.d("Settings","currSource2 "+currSource + " sourceIndex "+ sourceIndex);
                        final String currURL = currUser.getAllContentURLs()[sourceIndex];
//                        Log.d("Settings","currSource3 "+currSource + " currURL: "+currURL);
                        currCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                                Log.d("TempRemove -2", ":"+currUser.tempAvailableContentNames);
                                if (currCB.isChecked()){
                                    currUser.tempUpdate(currSource,currURL);
                                }else{
                                    currUser.tempRemove(currSource,currURL);

                                }
                            }
                        });
//                        Log.d("Settings","currSource4 "+currSource);
                        currCB.setText(currSource.replace("Settings_",""));
                        currCB.setTextColor(Color.MAGENTA);
                        if (currUser.getAvailableContentNames().contains(currSource)){
                            currCB.setChecked(true);
                        }else{
                            currCB.setChecked(false);
                        }
//                        Log.d("Settings","currSource5 "+currSource);

                        l.addView(currCB);
                        sourceIndex++;
                    }

//                    Log.d("SETTINGSPOPUP", "adding security question");
                    EditText newSecurityQ = (EditText)settingsLayout.findViewById(R.id.settingsSecurityQText);
                    EditText newSecurityA = (EditText)settingsLayout.findViewById(R.id.settingsSecurityAText);
//                    Log.d("SETTINGSPOPUP", "user security question: "+currUser.getSecurityQ() + " security answer: " +currUser.getSecurityA());
                    newSecurityQ.setText(currUser.getSecurityQ(),TextView.BufferType.EDITABLE);
                    newSecurityA.setText(currUser.getSecurityA(),TextView.BufferType.EDITABLE);
//                    Log.d("SETTINGSPOPUP", "added security question: "+newSecurityQ.getText().toString() + " security answer: " +newSecurityA.getText().toString());

                    Button SaveChangesButton = new Button(this);// = (Button) settingsLayout.findViewById(R.id.settingsSaveChangesButton);
                    SaveChangesButton.setText("Save Changes");
                    SaveChangesButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {

                            currUser.syncTempToAvailableContent();

                            if (currUser.getTempAvailableContentNames().isEmpty()){
                                Toast toast = Toast.makeText(context, "At least one source must be selected", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }else{
                                if (!newSecurityQ.getText().toString().equals(currUser.getSecurityQ())){
                                    currUser.setSecurityQ(newSecurityQ.getText().toString());
                                }
                                if (!newSecurityA.getText().toString().equals(currUser.getSecurityA())){
                                    currUser.setSecurityA(newSecurityA.getText().toString());
                                }
                                settingsWindow.dismiss();
                            }
                        }
                    });
                    l.addView(SaveChangesButton);

                    settingsWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            getActionBar().setTitle(Html.fromHtml("<small>THE MIGHTY BUTTON!</small>"));
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("POPUPCATCH", "got to catch in popup try with error " + e);
                }


                break;
            default:
                return super.onOptionsItemSelected(item);
//                break;
        }

        return true;
    }

}