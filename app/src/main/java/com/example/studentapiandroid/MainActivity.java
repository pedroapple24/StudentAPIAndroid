package com.example.studentapiandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    Button getIdButton;
    TextView studentIdField;
    String studentId = null;
    TextView setStudentId, setStudentName, setStudentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        super.onCreate(savedInstanceState);
        //Load XML File
        setContentView(R.layout.activity_main);
        //Intilization
        getIdButton = (Button) findViewById(R.id.button2);
        studentIdField = (TextView) findViewById(R.id.editText4);
        setStudentId = (TextView) findViewById(R.id.editText5);
        setStudentName = (TextView) findViewById(R.id.editText6);
        setStudentCourse = (TextView) findViewById(R.id.editText7);
        getIdButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        studentId = studentIdField.getText().toString();
        try {
            if (studentId.matches( "[0-9]+") && studentId.length() >= 1){
                Toast.makeText(MainActivity.this, "Searching data for studentId: " + studentId, Toast.LENGTH_LONG).show();
                getCallStudent();
            }
            else {
                Toast.makeText(MainActivity.this, "Please provide a valid entry for studentId", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCallStudent() throws Exception{

        String targetUrl = "http://10.0.2.2:8080/students/"+studentId;
        URL obj = new URL(targetUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setReadTimeout(10000); // time in milliseconds
        con.setConnectTimeout(15000); //
        con.connect(); // calling the web address
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + targetUrl);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        JSONObject myResponse = new JSONObject(response.toString());
        setStudentId.setText(myResponse.getString("id"));
        setStudentName.setText(myResponse.getString("name"));
        setStudentCourse.setText(myResponse.getString("course"));
    }

}
