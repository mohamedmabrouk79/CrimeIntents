package com.example.mohamedmabrouk.crimeintent;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.io.InputStream;

/**
 * Created by Mohamed Mabrouk on 28/03/2016.
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks,CrimeFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.details_Fragment_Container)==null){
            Intent intent=CrimePagerActivity.newIntent(this,crime.getID());
            startActivity(intent);
        }else{
            Fragment newDetail = CrimeFragment.newFragment(crime.getID());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_Fragment_Container, newDetail)
                    .commit();
        }
    }
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.Fragment_Container);
        listFragment.UpdateUI();
    }




}
