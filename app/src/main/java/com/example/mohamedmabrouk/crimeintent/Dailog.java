package com.example.mohamedmabrouk.crimeintent;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Mohamed Mabrouk on 05/04/2016.
 */
public class Dailog extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public  void dailog(int id){
        showDialog(0);
    }
    public Dialog onCreateDialog(int id) {
        View view= LayoutInflater.from(this).inflate(R.layout.dailog_photo, null);
        switch (id){
            case 0 :
                return new android.app.AlertDialog.Builder(this).setView(view).
                        setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Dailog.this, "nnnnnnnnnnnn", Toast.LENGTH_SHORT).show();
                            }
                        }).create();
        }
        return null;
    }
}
