package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private int port = 53212;
    private String server = "se2-isys.aau.at";
    protected PrintWriter output;
    protected BufferedReader input;

    TextView textView = (TextView) findViewById(R.id.textView);
    Button button = (Button) findViewById(R.id.button);
    Button berechnen = (Button) findViewById(R.id.button2);
    EditText textMatrikelnummer = (EditText) findViewById(R.id.textFieldMatrikelnummer);
    EditText textOutput = (EditText) findViewById(R.id.textFieldOutput);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = new Socket(server, port);
                            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                            String text = textMatrikelnummer.getText().toString();
                            output.println(text);
                            String inputText = input.readLine();
                            textOutput.setText(inputText);
                            input.close();
                            output.close();
                            socket.close();
                        } catch (Exception exception) {
                            exception.getStackTrace();
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });

        berechnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = new Socket(server, port);
                            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                            String text = textMatrikelnummer.getText().toString().trim();
                            int temp = Integer.parseInt(text);
                            int result = temp % 7;
                            System.out.println(result);
                            Log.d("Result", String.valueOf(result));
                            if (alternierendeQuersumme(text).equals("Ihr Wert ist gerade")) {
                                output.println("Ihr Wert ist gerade");
                            } else {
                                output.println("Ihr Wert ist ungerade");
                            }
                            String inputText = input.readLine();
                            textOutput.setText(inputText);
                            input.close();
                            output.close();
                            socket.close();
                        } catch (Exception exception) {
                            exception.getStackTrace();
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });
    }

    protected String alternierendeQuersumme(String text) {
        text = textMatrikelnummer.getText().toString().trim();
        int number = Integer.parseInt(text);
        int altSum = 0;
        for (int i = 0; i > number; i++) {
            if (i % 2 == 0) {
                altSum = altSum + number % 10;
            } else {
                altSum = altSum - number % 10;
            }
            number = number / 10;
        }
        if (altSum % 2 == 0) {
            return "Ihr Wert ist gerade";
        } else {
            return "Ihr Wert ist ungerade";
        }
    }
}