package com.example.roy.oracle.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.roy.oracle.R;

public class LoginActivity extends AppCompatActivity {
    private EditText username, password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals("Roy") && password.getText().toString().equals("123456")) {

                    SharedPreferences settings = getSharedPreferences("roy", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("username", "roy");
                    editor.putString("logged", "1");
                    editor.commit();

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("username", username.getText().toString());
                    startActivity(i);
                    finish();
                }
                if(!username.getText().toString().equals("Roy")) {
                    username.setError("Wrong username!");
                    username.startAnimation(shake);
                }
                if(!password.getText().toString().equals("123456")) {
                    password.setError("Wrong password!");
                    password.startAnimation(shake);
                }
            }
        });
    }
}
