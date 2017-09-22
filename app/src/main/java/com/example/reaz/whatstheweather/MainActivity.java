package com.example.reaz.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText cityname;
    TextView resulttextview;
    public void findWeather(View view){
        Log.i("cityname",cityname.getText().toString());
        InputMethodManager mgr =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityname.getWindowToken(),0);

        DownloadTask task = new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+cityname.getText().toString());


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityname = (EditText)findViewById(R.id.search);
        resulttextview = (TextView)findViewById(R.id.resulttextview);

    }
public class  DownloadTask extends AsyncTask<String,Void,String>{
    @Override
    protected String doInBackground(String... urls) {
        String result="";
        URL url;
        HttpURLConnection urlConnection=null;

        try {
            url = new URL(urls[0]);
            urlConnection=(HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data =reader.read();
            while (data !=-1){
                char current = (char) data;
                result+=current;
                data=reader.read();

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }



        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            String message ="";
            JSONObject jsonObject = new JSONObject(result);
            String weatherinfo = jsonObject.getString("weather");
            Log.i("name", weatherinfo);
            JSONArray arr = new JSONArray(weatherinfo);
            for (int i=2;i<arr.length();i++){
                JSONObject jsonPart = arr.getJSONObject(i);
                Log.i("main",jsonPart.getString("main"));
                Log.i("description",jsonPart.getString("description"));
                String main = "";
                String description ="";
                main = jsonPart.getString("main");
                description= jsonPart.getString("description");
                if(main != "" && description !=""){
                    message += main+": "+description + "\r\n";

                }
if(message != ""){
    resulttextview.setText(message);
}
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

}