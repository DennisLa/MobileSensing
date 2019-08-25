package de.dennis.mobilesensing.UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;

public class RegisterActivity extends AppCompatActivity  {
    private Spinner spinnerGender;
    private Button btnRegister;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtPasswordRepeat;
    private EditText txtSurname;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDisplayDate = (TextView) findViewById(R.id.txtBirthdate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("d", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        //txtEmail
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        //txtPassword
        txtPassword =(EditText) findViewById(R.id.txtPassword);
        //txtPasswordRepeat
        txtPasswordRepeat = (EditText) findViewById(R.id.txtPasswordRepeat);
        //txtSurname
        txtSurname = (EditText) findViewById(R.id.txtSurname);
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //btnRegister
        btnRegister = (Button) findViewById(R.id.btnCommitRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = txtEmail.getText().toString();
                final String password = txtPassword.getText().toString();
                String passwortRepeat = txtPasswordRepeat.getText().toString();
                String surname = txtSurname.getText().toString();
                String gender = spinnerGender.getSelectedItem().toString();
                int flag = 0;

                if (email.equals("") || email.contains("@") == false) {
                    flag = 1;
                    txtEmail.setError("Please insert a valid email");
                }
                if (password.equals("")) {
                    txtPassword.setError("Please insert a password");
                    flag = 1;
                }
                if (passwortRepeat.equals("")) {
                    txtPasswordRepeat.setError("Please repeat your password");
                    flag = 1;
                }
                if (surname.equals("")) {
                    txtSurname.setError("Please insert a name");
                    flag = 1;
                }
                if (mDisplayDate.getText().equals("")) {
                    mDisplayDate.setError("Please insert a valid date");
                    flag = 1;
                }
                if (flag == 1) return;

                if (!password.equals(passwortRepeat)) {
                    txtPassword.setError("Die Passwörter stimmen nicht überein!");
                    txtPassword.setText("");
                    txtPasswordRepeat.setText("");
                    return;
                }

                Date date = null;
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/DD/yyyy");
                    String s = mDisplayDate.getText().toString();
                    date = formatter.parse(mDisplayDate.getText().toString());
                    System.out.println(date);
                    System.out.println(formatter.format(date));

                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                ParseObject user = new ParseObject("Users");
//                user.put("gender", gender);
//                user.put("password", password);
//                user.put("email", email);
//                user.put("username", surname);
//                user.put("DoB", date);
//                user.saveInBackground();


                ParseUser user = new ParseUser();
                user.setUsername(surname);
                user.setPassword(password);
                user.setEmail(email);
                user.put("DoB", date);
                user.put("gender", gender);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // Hooray! Let them use the app now.
//                            SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = prefs.edit();
//                            editor.putString("Username", email);
//                            editor.putString("Password", password);
//                            editor.apply();
                            Intent i = new Intent(Application.getContext(), MapsActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                            e.printStackTrace();
                        }
                    }
                });
//                Intent i = new Intent(Application.getContext(), LoginActivity.class);
//                startActivity(i);
//                finish();
//                mProgressDialog.show();
//                user.signUpInBackground(new SignUpCallback() {
//                    public void done(ParseException e) {
//                        if (e == null) {
//                            Toast.makeText(Application.getContext(), "Registiert!", Toast.LENGTH_LONG);
//                            SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = prefs.edit();
//                            editor.putString("Username", email);
//                            editor.putString("Password", password);
//                            editor.apply();
//                            mProgressDialog.dismiss();
//                            Intent i = new Intent(Application.getContext(), LoginActivity.class);
//                            startActivity(i);
//                            finish();
//                        } else {
//                            mProgressDialog.dismiss();
//                            Toast.makeText(Application.getContext(), e.getMessage(), Toast.LENGTH_LONG);
//                            // Sign up didn't succeed. Look at the ParseException
//                            // to figure out what went wrong
//                        }
//                    }
//                });
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
//                }
            }
        });
    }
}
