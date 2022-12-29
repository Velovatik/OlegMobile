package com.velov.olegmobile;

import static com.velov.olegmobile.utils.LoginUtils.generateLoginURL;
import static com.velov.olegmobile.utils.LoginUtils.getToken;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private TextView result;
    private Button loginButton;

    private TextView oleg;
    private EditText login;
    private EditText password;
    private Button registerButton;

    //For testing response from server
    class ReturnTokenTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String response = null;
            try {
                response = getToken(login.getText().toString(), password.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            result.setText(response);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        result = findViewById(R.id.tv_result);
        oleg = findViewById(R.id.tv_oleg);
        login = findViewById(R.id.et_login);
        password = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.bt_login);
        registerButton = findViewById(R.id.bt_register);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReturnTokenTask().execute();
            }
        };

        loginButton.setOnClickListener(onClickListener);
    }
}