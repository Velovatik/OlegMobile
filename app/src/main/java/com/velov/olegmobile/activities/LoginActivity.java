package com.velov.olegmobile.activities;


import static com.velov.olegmobile.authorization.utils.token.TokenUtils.OLEG_LOGIN_URL;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.OLEG_REGISTER_URL;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.buildRequest;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.generateURL;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.getStatus;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.getToken;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.getUserData;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.makeRequest;
import static com.velov.olegmobile.authorization.utils.token.TokenUtils.parseJSON;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.velov.olegmobile.R;
import com.velov.olegmobile.authorization.utils.AuthorizationType;
import com.velov.olegmobile.authorization.utils.User;
import com.velov.olegmobile.authorization.utils.token.Status;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton, registerButton;
    private TextView result;
    private TextInputEditText name, login, password;

    AuthorizationType type = AuthorizationType.LOGIN;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());


    class ReturnToken implements Runnable {
        private String token = null;
        private Status status = Status.DEFAULT;

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
            status = getStatus(response);

            handler.post(new Runnable() { //UI Thread instead of onPostExecute()
                public void showError(String message) {
                    login.setError(message);
                    password.setError(message);
                    if (name.getVisibility() == View.VISIBLE) name.setError(message);
                }
                public void goToCalendar() {
                    Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                    startActivity(intent);
                }
                @Override
                public void run() {
                    //Realization of validation form
                    if (login.getText().toString().isEmpty()) {
                        login.setError("Поле не может быть пустым");
                    } else if (password.getText().toString().isEmpty()) {
                        password.setError("Поле не может быть пустым");
                    } else if (name.getVisibility() == View.VISIBLE && name.getText()
                            .toString().isEmpty()) {
                        name.setError("Поле не может быть пустым");
                    } else {
                        try {
                            switch (status) {
                                case OK: {
                                    token = getToken(response);
                                    result.setText(token); //test mode
                                    break;
                                }
                                case ERROR: {
                                    showError("Неверный логин или пароль");
                                    break;
                                }
                                case CONNECTION_ERROR: {
                                    showError("Не удается установить соединение с сервером");
                                    break;
                                }
                            }
                        } finally {
                            SharedPreferences sharedPreferences = getSharedPreferences("tokenSharedPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("token", token);
                            myEdit.commit(); //fix

                            if (getActStatus() == Status.OK) {
                                goToCalendar();
                            }
                        }
                    }
                }

            });
        }

        public Status getActStatus() {
            return status;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        result = findViewById(R.id.tv_result);
        name = findViewById(R.id.et_name);
        login = findViewById(R.id.et_login);
        password = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.bt_login);
        registerButton = findViewById(R.id.bt_register);

        View.OnClickListener onClickLoginListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReturnToken returnToken = new ReturnToken();
                executor.execute(returnToken);
                //Create condition for switching to calendar activity
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