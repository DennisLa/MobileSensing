package de.dennis.mobilesensing.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;

public class RegisterActivity extends AppCompatActivity {
    private Spinner spinnerGender;
    private TextView txtGender;
    private Button btnRegister;
    private TextView txtEmail;
    private TextView txtPassword;
    private TextView txtPasswordRepeat;
    private TextView txtSurname;
    private TextView txtName;
    private TextView txtDate;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //txtEmail
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        //txtPassword
        txtPassword =(TextView) findViewById(R.id.txtPassword);
        //txtPasswordRepeat
        txtPasswordRepeat = (TextView) findViewById(R.id.txtPasswordRepeat);
        //txtSurname
        txtSurname = (TextView) findViewById(R.id.txtSurname);
        //txtName
        txtName = (TextView) findViewById(R.id.txtName);
        //txtDate
        txtDate = (TextView) findViewById(R.id.txtBirthdate);
        //txtGender
        txtGender = (TextView) findViewById(R.id.txtGender);
        txtGender.setVisibility(View.GONE);
        //SpinnerGender
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id == 2) {
                    txtGender.setVisibility(View.VISIBLE);
                } else {
                    txtGender.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //btnRegister
        btnRegister = (Button) findViewById(R.id.btnCommitRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = txtEmail.getText().toString();
                final String password = txtPassword.getText().toString();
                String passwortRepeat = txtPasswordRepeat.getText().toString();
                String surname = txtSurname.getText().toString();
                String name = txtName.getText().toString();
                String date = txtDate.getText().toString();
                String gender;
                if(spinnerGender.getSelectedItemId() == 2)
                {
                    gender = txtGender.getText().toString();
                }else{
                    gender = spinnerGender.getSelectedItem().toString();
                }
                if(email.equals("") || password.equals("")||surname.equals("")||name.equals("")||date.equals(""))
                {
                    Toast.makeText(Application.getContext(),"Bitte füllen Sie alle Felder aus!",Toast.LENGTH_LONG );
                }else if(!password.equals(passwortRepeat)){
                    Toast.makeText(Application.getContext(),"Die Passwörter stimmen nicht überein!",Toast.LENGTH_LONG );
                    txtPassword.setText("");
                    txtPasswordRepeat.setText("");
                }else{
                    ParseUser user = new ParseUser();
                    user.setUsername(email);
                    user.setPassword(password);
                    user.setEmail(email);
                    // other fields can be set just like with ParseObject
                    user.put("birthdate", date);
                    user.put("surname",surname);
                    user.put("name",name);
                    user.put("gender",gender);

                    mProgressDialog.show();
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(Application.getContext(), "Registiert!", Toast.LENGTH_LONG);
                                SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("Username",email);
                                editor.putString("Password",password);
                                editor.apply();
                                mProgressDialog.dismiss();
                                Intent i = new Intent(Application.getContext(), LoginActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                mProgressDialog.dismiss();
                                Toast.makeText(Application.getContext(),e.getMessage(),Toast.LENGTH_LONG );
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                            }
                        }
                    });
                    //Baas Box Login
                   /* BaasUser user = BaasUser.withUserName(email)
                            .setPassword(password);
                    user.getScope(BaasUser.Scope.PRIVATE)
                            .put("birthdate", date)
                            .put("surname",surname)
                            .put("name",name)
                            .put("gender",gender)
                            .put("email",email);

                    user.signup(new BaasHandler<BaasUser>() {
                        @Override
                        public void handle(BaasResult<BaasUser> result) {
                            if (result.isSuccess()) {
                                Log.d("LOG", "Current user is: " + result.value());
                                Toast.makeText(Application.getContext(), "Registiert!", Toast.LENGTH_LONG);
                                SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("Username",email);
                                editor.putString("Password",password);
                                editor.apply();
                                finish();
                            } else {
                                Log.e("LOG", "Show error", result.error());
                            }
                        }
                    });*/
                }
            }
        });
    }
}
