package com.example.mohamedmabrouk.crimeintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks{
    public static final String EXTRA_CRIME_ID = "com.example.mohamedmabrouk.crimeintent.crime_id";
    public static ViewPager mViewPager;
    private List<Crime> mCrimes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        UUID crimeid=(UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager=(ViewPager)findViewById(R.id.activity_crime_pager_view_pager);
        FragmentManager fragmentManager=getSupportFragmentManager();
        mCrimes=CrimrLab.get(this).getCrimes();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                 return CrimeFragment.newFragment(crime.getID());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getID().equals(crimeid)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    //******* for return intent ********////////
    public static Intent newIntent(Context context,UUID uuid){
        Intent intent=new Intent(context,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, uuid);
        return intent;

    }


    @Override
    public void onCrimeUpdated(Crime crime) {

    }
}
