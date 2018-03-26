package de.dennis.mobilesensing.UI;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import de.dennis.mobilesensing.R;

import static android.os.Build.VERSION_CODES.M;

public class PermissionActivity extends AppCompatActivity {
    public ArrayList<String> permissionsList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        Intent intent = getIntent();
        permissionsList = intent.getStringArrayListExtra("PermissionList");
        checkPermissions();
        finish();
    }

    public void checkPermissions(){
        if (Build.VERSION.SDK_INT >= M) {
            String[] permissions = new String[permissionsList.size()];
            for (int i=0 ;i<permissionsList.size(); i++){
                permissions[i] = permissionsList.get(i);
            }
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
