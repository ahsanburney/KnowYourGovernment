package com.example.kulsum.knowyourgovernment;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AsyncLoaderTask extends AsyncTask<String, Void, String> {
    private MainActivity mainActivity;
    private String ValueFromMain;
    private String topLocation;
    String city;
    String stateValue;
    String zipValue;

    private final String webUrl = "https://www.googleapis.com/civicinfo/v2/representatives?";
    private final String yourAPIKey = "AIzaSyDlbbX06Ixcyl_ekvUmVZAOogI9RBR1j3o";
    private static final String TAG = "AsynLoaderTask";

    public AsyncLoaderTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {
        ValueFromMain = params[0];

        Uri.Builder buildURL = Uri.parse(webUrl).buildUpon();
        buildURL.appendQueryParameter("key", yourAPIKey);
        buildURL.appendQueryParameter("address", params[0]);
        String urlValue = buildURL.build().toString();
        Log.d(TAG, "doInBackground: " + urlValue);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlValue);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int checkStatus = connection.getResponseCode();
            if(checkStatus!=200)
            {return null;}
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
            String extractValues;
            while ((extractValues = bufferedReader.readLine()) != null) {
                stringBuilder.append(extractValues).append('\n');
            }
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
        parseJSON(stringBuilder.toString().trim());
        return stringBuilder.toString();
    }
    private ArrayList<Official> parseJSON(String s) {
        if(s==null) return null;
        ArrayList<Official> officialArrayList = new ArrayList<>();
        officialArrayList.clear();
        try {
            JSONObject officialOffice = new JSONObject(s);
            JSONArray officialOfficeJSONArray = officialOffice.getJSONArray("offices");
            JSONArray officialsNames = officialOffice.getJSONArray("officials");
            String inputValues = officialOffice.getString("normalizedInput");
            JSONObject jsonObject = new JSONObject(inputValues);
            if(jsonObject.has("city"))
            {city = jsonObject.getString("city");}
            else
            { city = "";}

            if(jsonObject.has("state"))
            {stateValue = jsonObject.getString("state");}
            else
            { stateValue = "";}

            if(jsonObject.has("zip"))
            {zipValue = jsonObject.getString("zip");}
            else
            { zipValue = "";}
            topLocation = city+","+stateValue+" "+zipValue;

            int count = 0;
            while (count < officialOfficeJSONArray.length()) {
                JSONObject officeObject = officialOfficeJSONArray.getJSONObject(count);
                count++;
                String officialDetails = officeObject.getString("name");
                String officeObjectString = officeObject.getString("officialIndices");
                String changesStart = officeObjectString.replace("[","");
                String changesEnd = changesStart.replace("]","");
                List<String> stringArrayList = new ArrayList<String>(Arrays.asList(changesEnd.split(",")));
                List<Integer> integerArrayList = new ArrayList<Integer>();
                for(String note:stringArrayList)
                {
                    integerArrayList.add(Integer.parseInt(note.trim()));
                }
                for( int i=0;i<integerArrayList.size();i++)
                {
                    JSONObject officialsNamesJSONObject = officialsNames.getJSONObject(integerArrayList.get(i));
                    String officialName = officialsNamesJSONObject.getString("name");
                    JSONArray add =null,
                            note0 =null,
                            note1=null,
                            note2 =null;
                    JSONObject jsonObject1 = null;
                    String line1 = "",
                            line2= "",
                            line3="",
                            city = "",
                            state = "",
                            zip =  "",
                            side = "",
                            phone="",
                            url = "",
                            imageUrl ="",
                            email="",
                            youtube = "",
                            twitter ="",
                            googleplus ="",
                            facebook="";

                    if(officialsNamesJSONObject.has("address"))
                    {add = officialsNamesJSONObject.getJSONArray("address");}
                    else
                    { add = null;}
                    if(add!=null&&add.length()>0){
                        jsonObject1 = add.getJSONObject(0);

                        if(jsonObject1.has("line1"))
                        {line1 = jsonObject1.getString("line1");}
                        else
                        { line1 = "";}

                        if(jsonObject1.has("line2"))
                        {line2 = jsonObject1.getString("line2");}
                        else
                        { line2 = "";}

                        if(jsonObject1.has("line3"))
                        {line3 = jsonObject1.getString("line3");}
                        else
                        { line3 = "";}

                        if(jsonObject1.has("city"))
                        {city = jsonObject1.getString("city");}
                        else
                        { city = "";}


                        if(jsonObject1.has("state"))
                        {state = jsonObject1.getString("state");}
                        else
                        { state = "";}

                        if(jsonObject1.has("zip"))
                        {zip = jsonObject1.getString("zip");}
                        else
                        { zip = "";}
                    }

                    if(officialsNamesJSONObject.has("party"))
                    {side = officialsNamesJSONObject.getString("party");}
                    else
                    { side = "";}

                    if(officialsNamesJSONObject.has("channels"))
                    {note2 = officialsNamesJSONObject.getJSONArray("channels");}
                    else
                    { note2 = null;}
                    if(note2!=null&&note2.length()>0) {
                        for (int i1 = 0; i1 < note2.length(); i1++) {
                            JSONObject social = note2.getJSONObject(i1);
                            if (social.has("type"))
                                if(social.getString("type").compareToIgnoreCase("GooglePlus")==0)
                                    googleplus = social.has("id")? social.getString("id"):"";
                                else if (social.has("type"))
                                    if(social.getString("type").compareToIgnoreCase("Facebook")==0)
                                        facebook = social.has("id")?social.getString("id"):"";
                                    else if (social.has("type"))
                                        if(social.getString("type").compareToIgnoreCase("Twitter")==0)
                                            twitter = social.has("id")?social.getString("id"):"";
                                        else if (social.has("type"))
                                            if(social.getString("type").compareToIgnoreCase("YouTube")==0)
                                                youtube = social.has("id")?social.getString("id"):"";

                        }
                    }


                    if(officialsNamesJSONObject.has("phones"))
                    {note0 = officialsNamesJSONObject.getJSONArray("phones");}
                    else
                    { note0 = null;}
                    if(note0!=null&&note0.length()>0) {
                        phone = note0.getString(0);
                    }

                    if(officialsNamesJSONObject.has("urls"))
                    {note1 = officialsNamesJSONObject.getJSONArray("urls");}
                    else
                    { note1 = null;}
                    if(note1!=null&&note1.length()>0) {
                        url = note1.getString(0);
                    }

                    if(officialsNamesJSONObject.has("emails"))
                    {note1 = officialsNamesJSONObject.getJSONArray("emails");}
                    else
                    { note1 = null;}

                    if(note1!=null&&note1.length()>0) {
                        email = note1.getString(0).equals("")?"":note1.getString(0);
                    }

                    if(officialsNamesJSONObject.has("photoUrl"))
                    {imageUrl = officialsNamesJSONObject.getString("photoUrl");}
                    else
                    { imageUrl = "";}


                    officialArrayList.add(new Official(officialDetails, officialName,line1,line2,line3,city,state,
                            zip,side,phone,email,url,imageUrl, topLocation,googleplus,facebook,twitter,youtube));
                    Log.d(TAG, "AsyncSide: " + side);

                }
            }
            return officialArrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {

        ArrayList<Official> officialArrayList = parseJSON(s);
        if(officialArrayList!=null) {
            ((TextView) mainActivity.findViewById(R.id.zipandstate1)).setText(topLocation);
            mainActivity.receiveData(officialArrayList);
        }


    }
}
