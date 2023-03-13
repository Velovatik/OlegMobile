package com.velov.olegmobile.activities;


import static com.velov.olegmobile.httputils.authorization.TokenUtils.OLEG_LOGIN_URL;
import static com.velov.olegmobile.httputils.authorization.TokenUtils.OLEG_REGISTER_URL;
import static com.velov.olegmobile.httputils.authorization.TokenUtils.buildRequest;
import static com.velov.olegmobile.httputils.HttpUtils.generateURL;
import static com.velov.olegmobile.httputils.authorization.TokenUtils.getStatus;
import static com.velov.olegmobile.httputils.authorization.TokenUtils.getToken;
import static com.velov.olegmobile.httputils.authorization.TokenUtils.getUserData;
import static com.velov.olegmobile.httputils.authorization.TokenUtils.makeRequest;
import static com.velov.olegmobile.httputils.authorization.TokenUtils.parseJSON;

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
import com.velov.olegmobile.httputils.authorization.AuthorizationType;
import com.velov.olegmobile.httputils.authorization.User;
import com.velov.olegmobile.httputils.authorization.Status;

import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton, registerButton;
    private TextView result;
    private TextInputEditText name, login, password;
    private TextView tokenResult;
    AuthorizationType type = AuthorizationType.LOGIN;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    SharedPreferences tokenPreferences;

    private String token = null; //Variable for writing and reading access_token param

    class ReturnToken implements Runnable {
        private Status status = Status.DEFAULT;

        public Status getRequestStatus() {
            return status;
        }

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
            Request request = buildRequest(client, url, json);
            Response response = makeRequest(client, request);
            status = getStatus(response);

            handler.post(new Runnable() { //UI Thread instead of onPostExecute()
                /**
                 * Method shows err message in all editText fields
                 * @param message set String error message
                 */
                public void showError(String message) { //Rewrite just to show message in textView under EditText fields
                    login.setError(message);
                    password.setError(message);
                    if (name.getVisibility() == View.VISIBLE) name.setError(message);
                }

                /**
                 * Method invoke redirection on calendar activity
                 */
                public void goToCalendar() {
                    Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                    startActivity(intent);
                }

                @Override
                public void run() {
                    //Conditions for checking if information is correct
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
                                    result.setText(token); //Field for test response from server
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
                            tokenPreferences = getSharedPreferences("access_token", MODE_PRIVATE); //access_token is name of prefs file
                            SharedPreferences.Editor tokenEditor = tokenPreferences.edit();
                            tokenEditor.putString("token", token);
                            tokenEditor.apply();

                            if (getRequestStatus() == Status.OK) {
                                goToCalendar();
                            }
                        }
                    }
                }

            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Connection for layouts
        result = findViewById(R.id.tv_result);
        name = findViewById(R.id.et_name);
        login = findViewById(R.id.et_login);
        password = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.bt_login);
        registerButton = findViewById(R.id.bt_register);
        tokenResult = findViewById(R.id.tv_token);

        tokenPreferences = getSharedPreferences("access_token", MODE_PRIVATE);
        String access_token = tokenPreferences.getString("token", "undefined");
        if (!Objects.equals(access_token, "undefined")) {
            Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
            startActivity(intent);
        }

        View.OnClickListener onClickLoginListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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