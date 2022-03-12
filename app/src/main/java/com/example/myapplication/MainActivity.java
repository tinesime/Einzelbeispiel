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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textView);
        Button button = (Button) findViewById(R.id.button);
        Button berechnen = (Button) findViewById(R.id.button2);
        EditText textMatrikelnummer = (EditText) findViewById(R.id.textFieldMatrikelnummer);
        EditText textOutput = (EditText) findViewById(R.id.textFieldOutput);

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

                            // Sende Nachricht
                            String text = textMatrikelnummer.getText().toString();
                            output.println(text);

                            // Empfange Nachricht
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

                            // Sende Nachricht
                            String text = textMatrikelnummer.getText().toString().trim();

                            // Berechne Matrikelnummer % 7
                            int temp = Integer.parseInt(text);
                            int result = temp % 7;
                            Log.d("Result", String.valueOf(result));

                            int number = Integer.parseInt(text);
                            int altSum = 0;
                            int i = 0;
                            while (number != 0) {
                                if (i == 0) {
                                    altSum = number;
                                } else {
                                    if (i % 2 == 0) {
                                        altSum = altSum + number;
                                    } else {
                                        altSum = altSum - number;
                                    }
                                }
                                i++;
                            }
                            Log.d("Temp", String.valueOf(altSum));
                            output.println(altSum);

                            /*
                            Log.d("Number", String.valueOf(number));
                            int altSum = 0;
                            for (int i = 0; i > number; i++) {
                                if (i % 2 == 0) {
                                    altSum = altSum + number % 10;
                                } else {
                                    altSum = altSum - number % 10;
                                }
                                number = number / 10;
                            }
                            Log.d("AltSum", String.valueOf(altSum));
                            output.println(altSum);
                             */

                            // Empfange Nachricht
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
}