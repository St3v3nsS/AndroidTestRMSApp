package com.st3v3nss.TestRMS;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView (R.id.login)
    Button loginButton;

    @SuppressLint("NonConstantResourceId")
    @BindView (R.id.key)
    EditText textView;

    @SuppressLint("NonConstantResourceId")
    @BindView (R.id.password)
    EditText passwordText;

    @SuppressLint("NonConstantResourceId")
    @BindView (R.id.hidden_key)
    TextView goodKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SharedPreferences preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("password", "16bY9G/cf10kWIanYdb9Ng==");
        editor.apply();

        DetectRoot detector = new DetectRoot();

        if(detector.checkForBusyBoxBinary() || detector.checkForSuBinary() || detector.isEmulator()){
            new AlertDialog.Builder(this)
                    .setTitle("Rooted/emulated Device")
                    .setMessage("You are using a rooted/emulated Device!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setNeutralButton(
                            "Quit",
                            (dialog, id) -> System.exit(1))
                    .show();
        }

        loginButton.setOnClickListener(v -> {
            String finalKey = textView.getText().toString();

            if (TextUtils.isEmpty(finalKey)){
                textView.setError("Key should not be empty!");
                return;
            }
            if (!finalKey.equals(getKey())){
                textView.setError("Wrong key used!");
                return;
            }

            String password1 = passwordText.getText().toString();

            if (TextUtils.isEmpty(password1)){
                passwordText.setError("Password should not be empty!");
                return;
            }

            SharedPreferences preferences1 = getSharedPreferences("pref", Context.MODE_PRIVATE);
            String passwordEnc = preferences1.getString("password", "");
            String passwordDec = Crypto.decrypt(finalKey, passwordEnc);

            if(password1.equals(passwordDec)){
                Toast.makeText(this, "Congrats! Key is: " + finalKey + "; Password is: " + passwordDec, Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Password/key not valid! Try again!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getKey(){
        return goodKey.getText().toString();
    }
}