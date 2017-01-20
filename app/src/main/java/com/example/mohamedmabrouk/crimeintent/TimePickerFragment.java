package com.example.mohamedmabrouk.crimeintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Mohamed Mabrouk on 30/03/2016.
 */
public class TimePickerFragment extends DialogFragment {
    private static final String ARGS_TIME="time";
    public static final String EXTRA_Time = "com.example.mohamedmabrouk.crimeintent.time";
    private TimePicker mTimePicker;


    //******** for return DatePicker Fragment *******//
    public static TimePickerFragment NewInstence(Date date){
        Bundle bundle=new Bundle();
        bundle.putSerializable(ARGS_TIME, date);
        TimePickerFragment fragment=new TimePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    //***  for sending result from date  picker ***//
    public void SendResult(int requestCode, Date date){
        if (getTargetFragment()==null){
            return ;
        }
        Intent intent=new Intent();
        intent.putExtra(EXTRA_Time, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), requestCode, intent);

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date=(Date)getArguments().getSerializable(ARGS_TIME);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int Hour=calendar.get(Calendar.HOUR);
        int Minute=calendar.get(Calendar.MINUTE);
        View v= LayoutInflater.from(getActivity()).inflate(R.layout.dailog_time,null);
        mTimePicker=(TimePicker)v.findViewById(R.id.dailog_time_time_picker);
        return new AlertDialog.Builder(getActivity()).
                setView(v).
                setTitle("Date of crime:").
                setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int Hour=mTimePicker.getHour();
                                int Minute=mTimePicker.getMinute();
                                Date date=new Date();
                                date.setHours(Hour);
                                date.setMinutes(Minute);
                                //  Toast.makeText(getActivity(),date.getHours()+":"+date.getMinutes(), Toast.LENGTH_SHORT).show();
                                SendResult(Activity.RESULT_OK, date);

                            }
                        }


                ).create();
    }
}
