package com.notbytes.barcodereader;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;



public class LoginActivity extends AppCompatActivity
{
    //initilization
    private String TAG = "LoginActivity";
    private EditText etusername, etpassword;
    private Button btnlogin;
    private TextView tvreg;
    private final int LoginTask = 1;

    String username, password;

    //on create method
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initilization


        //initilization
        etusername = findViewById(R.id.etusername);
        etpassword = findViewById(R.id.etpassword);
        btnlogin = findViewById(R.id.btn);


        btnlogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //string values

                try
                {
                    // Validate if username, password is filled
                    login();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });


    }
    //on create closed
    private void login()
    {
        username =etusername.getText().toString().trim();
        password = etpassword.getText().toString().trim();


                    // Starting QRScanActivity
                    Intent intent = new Intent(getApplicationContext(), QRScanActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(intent);
                    finish();


    }


    //on back stag

    @Override
    public void onBackPressed()
    {
        // AndyUtils.showSimpleProgressDialog(this,"ok","ok",true);
        AlertDialog.Builder builder =new AlertDialog.Builder(LoginActivity.this);
        builder.setCancelable(true);
        builder.setMessage("DO YOU WANT TO LOGOUT");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //if user pressed "yes", then he is allowed to exit from application
                LoginActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alertDialog =builder.create();
        alertDialog.show();
    }

}


