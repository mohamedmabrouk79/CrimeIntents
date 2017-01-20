package com.example.mohamedmabrouk.crimeintent;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mohamed Mabrouk on 28/03/2016.
 */
public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private Callbacks mCallbacks;


    //**** for create callback interface  ***//
    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ShowSubttile();
    }

    //**** for create views layouts **********//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_crime_list,container,false);
        mCrimeRecyclerView=(RecyclerView)view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        UpdateUI();
        return view;
    }

    //****** override onResume methoid for update Crime ******//
    @Override
    public void onResume() {
        super.onResume();
        UpdateUI();
    }

 //************** for save subttitle values after rotation *********//
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    //*******    **********//

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks=null;
    }

    //**** create Menu *****//
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem menuItem=menu.findItem(R.id.menu_item_show_subtitle);
      /*  SearchView searchView=(SearchView)menu.findItem(R.id.menu_item_search).getActionView();
        SearchManager searchManager=(SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
      //  searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));*/
        if(mSubtitleVisible){
            menuItem.setTitle(R.string.hide_subtitle);
        }else{
      menuItem.setTitle(R.string.show_subtitle);
        }
    }

    //*** Add action to menu items ******//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimrLab.get(getActivity()).addCrime(crime);
                UpdateUI();
                mCallbacks.onCrimeSelected(crime);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible=!mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
             ShowSubttile();
            default:
                return super.onOptionsItemSelected(item);
        }
        }

    //***  to show number of Crimes **//
    private void ShowSubttile(){
        CrimrLab crimrLab=CrimrLab.get(getActivity());
        int num=crimrLab.getCrimes().size();
        String subttile=getString(R.string.subtitle_format,num);
        if (!mSubtitleVisible) {
            subttile = null;
        }
        AppCompatActivity activity= (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subttile);

    }

    //***** for Update  Crime Adapter on recycle view *****//
    public void UpdateUI(){
        CrimrLab crimrLab=CrimrLab.get(getActivity());
        List<Crime> crimeList=crimrLab.getCrimes();
        if (mAdapter==null) {
            mAdapter = new CrimeAdapter(crimeList);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setCrime(crimeList);
            mAdapter.notifyDataSetChanged();
        }
        ShowSubttile();
    }

    //******** create  View Holder class for hold view ****//
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Crime mCrime;
        private TextView  mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        //**** create View ***//
        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_solved_check_box);
        }

        //*** for put Date come form (Crime) Class at view ****//
        public void bindCrime(Crime crime){
            mCrime=crime;
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(df.format("EEE , dd MMMM ,yyyy", mCrime.getDate())+"   "+df.format("hh:mm", mCrime.getTime()));
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {
          mCallbacks.onCrimeSelected(mCrime);
        }
    }

    //******** create Crime Adepter Class for put Date on Crime Holder *******//
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> mCrimes) {
            this.mCrimes = mCrimes;
        }

        //******* return  date to  view holder ******//
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
            View view=layoutInflater.inflate(R.layout.list_item_crime,parent,false);
            return new CrimeHolder(view);
        }

        //****** for select   view position for put Date on it  *****//
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime=mCrimes.get(position);
            holder.bindCrime(crime);
        }

        // ******** return number of view that recycleview will recycle it ********//
        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrime(List<Crime> Crimes){
            mCrimes=Crimes;
        }
    }


}
