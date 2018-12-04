package com.fxyan.validationcodeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ValidationCodeView view = findViewById(R.id.codeView);
        view.setOnInputCompletedListener(new ValidationCodeView.OnInputCompletedListener() {
            @Override
            public void onInputCompleted(String input) {
                Toast.makeText(MainActivity.this, input, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
