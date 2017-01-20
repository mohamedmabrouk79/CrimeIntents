package com.example.mohamedmabrouk.crimeintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Mohamed Mabrouk on 16/04/2016.
 */
public class ShowPhotoFragment extends DialogFragment {
    private ImageView imageView;
    private static Bitmap bitmap;


    public ShowPhotoFragment(){}
    public static void ShowPhotoFragment(Bitmap bitmap1){
       bitmap=bitmap1;
   }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dailog_photo,null);
         imageView=(ImageView)view.findViewById(R.id.Photo_show);
         imageView.setImageBitmap(bitmap);
        return new AlertDialog.Builder(getActivity()).setTitle("Show Crime Photo :").
                setView(view).create();
    }
}
