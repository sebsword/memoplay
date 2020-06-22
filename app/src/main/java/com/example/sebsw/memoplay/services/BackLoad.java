package com.example.sebsw.memoplay.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.sebsw.memoplay.activities.MainActivity;
import com.example.sebsw.memoplay.model.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by sebsw on 21/01/2017.
 */
public class BackLoad extends AsyncTask<String,Void,String> {
    Context context = MainActivity.getMainActivity();
    String jsonString;
    // the one below is the final value after everything has been collected
    String json_String;
    JSONObject jsonObject;
    JSONArray jsonArray;
    MyDBHandler myDBHandler = new MyDBHandler(MainActivity.getMainActivity().getApplicationContext(),null,null,1);
    String TAG = "tag";
    String share_url;
    String import_url;


    public BackLoad() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private boolean executed;

    private Boolean JsonExecuted(Boolean executed){
        if (executed){
            return true;
        }else {
            return false;
        }
    }


    @Override
    //uses varArgs to pass in a variable amount of parameters to the background task
    protected String doInBackground(String... params) {
        executed = false;
        share_url = "http://allenchrology.org/memoplay/insertword.php";
        /* when do in background is called the method e.g. the type of background task needed
        is passed in as the first parameter
         */
        String method = params[0];
        if(method.equals("share")){
            // assigning the other variables from the inputted parameters
            String wordTitle = params[1];
            String wordDefinition = params[2];
            String wordCategory = params[3];
            Log.d("TAG", "doInBackground: " + params[1] + params[2] + params[3]);
            try {
                URL url = new URL(share_url);
                //Attempts to open a connection to the specified url
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                //post request
                httpURLConnection.setRequestMethod("POST");
                //setting output to true indicates that we will be outputting data in our request
                httpURLConnection.setDoOutput(true);
                /**
                 * Once the word has been uploaded, It is stored in the database at a specific
                 * auto-generated wordID I will now need to return the auto-generated ID so it can be shared
                 * with a real person, for this reason I will also need to create an input connection
                 **/
                //Indicates we will also be retrieving datat as a response from the server
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                //creates a buffered writer to output the data and send it to the server
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                //encoding data to UTF-8
                String data = URLEncoder.encode("wordTitle", "UTF-8") +
                        "=" + URLEncoder.encode(wordTitle,"UTF-8") +
                        "&" + URLEncoder.encode("wordDefinition", "UTF-8") +
                        "=" + URLEncoder.encode(wordDefinition, "UTF-8") +
                        "&" + URLEncoder.encode("wordCategory", "UTF-8") +
                        "=" + URLEncoder.encode(wordCategory, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.flush();
                //closes output stream
                OS.close();

                /*code to retrieve servers response, which is just the ID of the last inputted word
                that can then be used to generate the URL needed to retrieve that word from the database
                 */
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS,"iso-8859-1"));
                String response = "";
                String line = "";

                //stores the response from the server by reading the retrieved data line by line
                while ((line = bufferedReader.readLine())!=null){
                    Log.d(TAG, "was not null and value returned was" + line);
                    response+= line;
                }
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                // the ID returned by the server is now used to create an import url
                //the url we must access to download a word with a specific wordID
                import_url = "http://allenchrology.org/memoplay/retrieveword.php"
                        + "?id=" + response;
                Log.d("TAG", import_url);
                return "Upload Successful...";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (method.equals("import")){
            try {
                String wordID = params[1];
                import_url = "http://allenchrology.org/memoplay/retrieveword.php"
                        + "?id=" + wordID;
                Log.d("TAG", "loading from" + import_url);
                URL url = new URL(import_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                //Builds a string from each individual line of json returned by the server
                StringBuilder stringBuilder = new StringBuilder();
                while ((jsonString = bufferedReader.readLine())!=null){
                    stringBuilder.append(jsonString+"\n");
                }
                Log.d("tag", stringBuilder.toString());
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.d("tag", "stringbuilder length: " + Integer.toString(stringBuilder.length()));
                //23 is the default size of the servers response
                /*if the response is greater than 23 characters, data was retrieved otherwise
                there was an error such as an invalid url being entered */
                if (stringBuilder.length()>23){
                    executed = true;
                }else{
                    executed = false;
                }
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        // if the upload was successful
        if (result.equals("Upload Successful...")){
            //make a toast to alert user
            Toast.makeText(context,result,Toast.LENGTH_LONG).show();

            //share the newly generated ID with a share intent
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Test your knowledge by downloading a new word for your MemoPlay app at:\n"+ import_url);
            shareIntent.setType("text/plain");
            MainActivity.getMainActivity().loadShareActivity(shareIntent,"Share With Friend");

        }else if (JsonExecuted(executed)){
            json_String = result;
            try {
                //converts the string jsonString into a java jsonObject that can be processed
                jsonObject = new JSONObject(json_String);
                /*the JsonObject contains an array called server_response
                this array contains objects storing data about a specific word*/
                jsonArray = jsonObject.getJSONArray("server_response");
                Log.d(TAG, jsonArray.toString());

                int rowCount = 0;
                String wordTitle, wordDefinition, wordCategory;
                Log.d(TAG, "onPostExecute: " + jsonArray.length());
                while (rowCount<jsonArray.length()){
                    //converts each object in the jsonArray to a java JSON object that can be processed
                    JSONObject tempJO = jsonArray.getJSONObject(rowCount);

                    //retrieves useful information from key:value paring in the object
                    wordTitle = tempJO.getString("word_title");
                    Log.d(TAG, wordTitle);
                    wordDefinition = tempJO.getString("word_definition");
                    Log.d(TAG, wordDefinition);
                    wordCategory = tempJO.getString("word_category");
                    //stores retrieved information as a word
                    Word word = new Word(wordTitle,wordCategory,wordDefinition);
                    //adds retrieved word to the database
                    myDBHandler.addWord(word);
                    myDBHandler.addCategory();
                    rowCount++;
                }
                Toast.makeText(context,"Import Succesful...",Toast.LENGTH_LONG).show();
                MainActivity.getMainActivity().loadCategoriesFragment();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else{
            Toast.makeText(context,"An error has occurred",Toast.LENGTH_LONG).show();
            MainActivity.getMainActivity().loadCategoriesFragment();
        }
    }
}
