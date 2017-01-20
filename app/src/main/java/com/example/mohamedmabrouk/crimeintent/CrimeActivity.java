package com.example.mohamedmabrouk.crimeintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
   private  static final String EXTRA_CRIME_ID = "com.example.mohamedmabrouk.crimeintent.crime_id";

    public static Intent newIntent(Context context,UUID uuid){
        Intent intent=new Intent(context,CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,uuid);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID crimeid=(UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newFragment(crimeid);
    }
}
