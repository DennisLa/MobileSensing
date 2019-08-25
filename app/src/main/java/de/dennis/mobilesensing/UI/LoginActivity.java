package de.dennis.mobilesensing.UI;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;

public class LoginActivity extends AppCompatActivity {
    private Button btnRegister;
    private Button btnForgotPassword;
    private Button btnLogin;
    private TextView txtEmail;
    private TextView txtPassword;
    private ProgressDialog mProgressDialog;

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
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = txtEmail.getText().toString();
                final String password = txtPassword.getText().toString();
                if(email.equals("") || password.equals(""))
                {
                    Toast.makeText(Application.getContext(),"Bitte füllen Sie alle Felder aus!",Toast.LENGTH_LONG).show();
                }else{
                    //some dumb content for the moment
                    mProgressDialog.show();
                    ParseUser.logInInBackground(email, password, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                // Hooray! The user is logged in.
                                SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("Username", email);
                                editor.putString("Password", password);
                                editor.apply();
                                Intent i = new Intent(Application.getContext(), MapsActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                // Signup failed. Look at the ParseException to see what happened.
                                Toast.makeText(Application.getContext(),"Wrong Email or Password", Toast.LENGTH_LONG);

                            }
                        }
                    });
                    mProgressDialog.dismiss();
//                    mProgressDialog.show();
//                    ParseUser.logInInBackground(email, password, new LogInCallback() {
//                        public void done(ParseUser user, ParseException e) {
//                            if (user != null) {
//                                // Hooray! The user is logged in.
//                                SharedPreferences.Editor editor = prefs.edit();
//                                editor.putString("Username",email);
//                                editor.putString("Password",password);
//                                editor.putString("Session",user.getSessionToken());
//                                editor.apply();
//                                mProgressDialog.dismiss();
//                                Intent i = new Intent(Application.getContext(), WebviewActivity.class);
//                                startActivity(i);
//                                finish();
//                            } else {
//                                // Signup failed. Look at the ParseException to see what happened.
//                                mProgressDialog.dismiss();
//                                Toast.makeText(Application.getContext(),e.getMessage(), Toast.LENGTH_LONG);
//                            }
//                        }
//                    });
                    //Baas Box Login
                    /*
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
                    });*/
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
