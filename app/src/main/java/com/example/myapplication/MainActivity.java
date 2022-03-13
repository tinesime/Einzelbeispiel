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
                        sendListener();
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
                        computeListener();
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });
    }

    private void sendListener() {
        EditText textMatrikelnummer = (EditText) findViewById(R.id.textFieldMatrikelnummer);
        EditText textOutput = (EditText) findViewById(R.id.textFieldOutput);

        try {
            // define properties
            Socket socket = new Socket(server, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            // Sende Nachricht
            String text = textMatrikelnummer.getText().toString();
            output.println(text);

            // Empfange Nachricht
            String inputText = input.readLine();
            textOutput.setText(inputText);

            // Close all handles
            input.close();
            output.close();
            socket.close();
        } catch (Exception exception) {
            exception.getStackTrace();
        }
    }

    private void computeListener() {
        EditText textMatrikelnummer = (EditText) findViewById(R.id.textFieldMatrikelnummer);
        EditText textOutput = (EditText) findViewById(R.id.textFieldOutput);

        // get text from the text field
        String text = textMatrikelnummer.getText().toString();

        // Berechne Matrikelnummer % 7
        int num = Integer.parseInt(text);
        int result = num % 7;
        Log.d("Result", String.valueOf(result)); // 0

        char[] numbers = text.toCharArray();
        int[] array = new int[numbers.length];

        // fill te array
        for (int i = 0; i < array.length; i++) {
            array[i] = Character.getNumericValue(numbers[i]);
        }

        // compute alternating sum of digits
        int altSum = 0;
        for (int i = 0; i < array.length; i++) {
            if (i % 2 == 0) {
                altSum += array[i];
            } else {
                altSum -= array[i];
            }
        }

        // convert the alternating sum into a string for the output
        String res = Integer.toString(altSum);

        // print result in the text field
        textOutput.setText(res);
    }
}