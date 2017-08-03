package de.dennis.mobilesensing;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baasbox.android.BaasException;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;

public class LoginActivity extends AppCompatActivity {
    private Button btnRegister;
    private Button btnForgotPassword;
    private Button btnLogin;
    private TextView txtEmail;
    private TextView txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.checkPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtPassword = (TextView) findViewById(R.id.txtPassword);
        final SharedPreferences prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        txtEmail.setText(prefs.getString("Username",""));
        txtPassword.setText(prefs.getString("Password",""));
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Application.getContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = txtEmail.getText().toString();
                final String password = txtPassword.getText().toString();
                if(email.equals("") || password.equals(""))
                {
                    Toast.makeText(Application.getContext(),"Bitte füllen Sie alle Felder aus!",Toast.LENGTH_LONG);
                }else{
                    BaasUser user = BaasUser.withUserName(email)
                            .setPassword(password);
                    user.login(new BaasHandler<BaasUser>() {
                        @Override
                        public void handle(BaasResult<BaasUser> result) {
                            if(result.isSuccess()) {
                                Log.d("LOG", "The user is currently logged in: " + result.value());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("Username",email);
                                editor.putString("Password",password);
                                try {
                                    editor.putString("Session",result.get().getToken());
                                    Log.d("LOGIN_TOKEN",result.get().getToken());
                                } catch (BaasException e) {
                                    e.printStackTrace();
                                }
                                editor.apply();
                                Intent i = new Intent(Application.getContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Log.e("LOG","Show error",result.error());
                                Toast.makeText(Application.getContext(),"Email oder Benutzername sind falsch!", Toast.LENGTH_LONG);
                            }
                        }
                    });
                }
            }
        });

    }
    public void checkPermissions(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String[] permissions = new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.READ_CALL_LOG,Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION};
            boolean flag = false;
            for (int i = 0; i < permissions.length; i++) {
                if (checkSelfPermission(permissions[i]) == PackageManager.PERMISSION_DENIED) {
                    flag = true;
                    break;
                }
            }

            if (flag) {
                requestPermissions(permissions, 1);
            }

        }
    }
}
