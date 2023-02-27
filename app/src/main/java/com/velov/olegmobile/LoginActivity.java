package com.velov.olegmobile;

import static com.velov.olegmobile.autgorization.utils.TokenUtils.getToken;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.velov.olegmobile.autgorization.utils.AuthorizationType;

import org.json.JSONException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private TextView result;
    private Button loginButton;

    private TextView oleg;
    private EditText name;
    private EditText login;
    private EditText password;
    private Button registerButton;

    AuthorizationType type = AuthorizationType.LOGIN;

    //For testing response from server
    class ReturnTokenTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String response = null;

            try {
                //Returns access token
                switch (type) {
                    case LOGIN:
                        response = getToken(login.getText().toString(), password.getText().toString());
                        break;
                    case REGISTER:
                        response = getToken(name.getText().toString(), login.getText().toString(),
                                password.getText().toString());
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return response;
        }

        //Test func for login will show access token on login page
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
        name = findViewById(R.id.et_name); /////
        login = findViewById(R.id.et_login);
        password = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.bt_login);
        registerButton = findViewById(R.id.bt_register);


        View.OnClickListener onClickLoginListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReturnTokenTask().execute();
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
                }

                else {
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