package com.velov.olegmobile.activities;


import static com.velov.olegmobile.authorization.utils.token.TokenUtils.OLEG_LOGIN_URL;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.OLEG_REGISTER_URL;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.buildRequest;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.generateURL;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.getResponse;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.getUserData;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.makeRequest;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.parseJSON;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.velov.olegmobile.R;
import com.velov.olegmobile.authorization.utils.AuthorizationType;
import com.velov.olegmobile.authorization.utils.User;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView result;
    private Button loginButton;
    private TextView oleg;
    private EditText name;
    private EditText login;
    private EditText password;
    private Button registerButton;

    AuthorizationType type = AuthorizationType.LOGIN;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    class ReturnToken implements Runnable {
        private String token = null;

        @Override
        public void run() { //Background work instead of doInBackground()
            URL url = null;
            User user = null;
            switch (type) {
                case REGISTER: {
                    url = generateURL(OLEG_REGISTER_URL);
                    user = getUserData(name.getText().toString(), login.getText().toString(),
                            password.getText().toString());
                    break;
                }
                case LOGIN: {
                    url = generateURL(OLEG_LOGIN_URL);
                    user = getUserData(login.getText().toString(), password.getText().toString());
                    break;
                }
            }

            String json = parseJSON(user);
            OkHttpClient client = new OkHttpClient();
            Request request = buildRequest(client, json, url);
            Response response = makeRequest(client, request);
            token = getResponse(response);

            handler.post(new Runnable() { //UI Thread instead of onPostExecute()
                @Override
                public void run() {
                    result.setText(token);
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        result = findViewById(R.id.tv_result);
        oleg = findViewById(R.id.tv_oleg);
        name = findViewById(R.id.et_name);
        login = findViewById(R.id.et_login);
        password = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.bt_login);
        registerButton = findViewById(R.id.bt_register);


        View.OnClickListener onClickLoginListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ReturnTokenTask task = new ReturnTokenTask();
//                task.execute();

                ReturnToken returnToken = new ReturnToken();
                executor.execute(returnToken);
            }
        };

        loginButton.setOnClickListener(onClickLoginListener);

        View.OnClickListener onClickRegisterListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type == AuthorizationType.LOGIN) {
                    type =  AuthorizationType.REGISTER;
                    name.setVisibility(View.VISIBLE);

                    loginButton.setText("Зарегистрироваться");
                    registerButton.setText("Войти");
                } else {
                    type = AuthorizationType.LOGIN;
                    name.setVisibility(View.INVISIBLE);

                    loginButton.setText("Войти");
                    registerButton.setText("Зарегистрироваться");
                }

            }
        };

        registerButton.setOnClickListener(onClickRegisterListener);
    }
}