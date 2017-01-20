package com.example.mohamedmabrouk.crimeintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.text.AndroidCharacter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.text.format.DateFormat;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Mohamed Mabrouk on 26/03/2016.
 */
public class CrimeFragment extends Fragment {
    //************ decleration varibles **********//
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_Time = "DialogTime";
    private static final String DIALOG_PHOTO= "DialogPhoto";
        private static final int REQUEST_Gallery = 3;
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME= 4;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO= 2;
    private  Crime mCrime;
    private EditText mCrimeTitle;
    private Button DateButton;
    private CheckBox Solved_Button;
    private Button TimeButton;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private Callbacks mCallbacks;
    private Button mGallery;



    //************ crate interface **//
    public abstract interface Callbacks {
       abstract void onCrimeUpdated(Crime crime);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID CrimeId=(UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime=CrimrLab.get(getActivity()).getCrime(CrimeId);
        setHasOptionsMenu(true);
    }

    //********* to create menu items ********//
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list_delete, menu);
    }

    //****** to add action for menu items ******//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                Crime crime=new Crime();
                UUID CrimeId=(UUID)getArguments().getSerializable(ARG_CRIME_ID);
                List<Crime> crimes=CrimrLab.get(getActivity()).getCrimes();
                for(int i=0 ; i<crimes.size();i++){
                    if (crimes.get(i).getID().equals(CrimeId)){
                        CrimrLab.get(getActivity()).deleteCrime(i,CrimeId);
                    }
                }
               // Intent intent=new Intent(getActivity(),CrimeListFragment.class);
                    mCallbacks.onCrimeUpdated(crime);
                    getActivity().finish();


                mCallbacks.onCrimeUpdated(crime);
                getActivity().finish();
               return true;
            default:

        return super.onOptionsItemSelected(item);
    }}


    @Override
    public void onPause() {
        super.onPause();
        CrimrLab.get(getActivity())
                .UpdateCrime(mCrime);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks=null;
    }

    //***** return instence from Crime Fragment   *******//
    public static CrimeFragment newFragment(UUID crimeid){
       Bundle bundle=new Bundle();
        bundle.putSerializable(ARG_CRIME_ID, crimeid);
        CrimeFragment fragment=new CrimeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.crime_fragment,container,false);
        //declration buttons - text - Check box
        mCrimeTitle=(EditText)v.findViewById(R.id.Crime_Title);
        mCrimeTitle.setText(mCrime.getTitle());
        DateButton=(Button)v.findViewById(R.id.Date_button);
        TimeButton=(Button)v.findViewById(R.id.time_button);
        Solved_Button=(CheckBox)v.findViewById(R.id.Solved);
        mReportButton=(Button)v.findViewById(R.id.crime_report);
        mSuspectButton=(Button)v.findViewById(R.id.crime_susbect);
        mPhotoButton=(ImageButton)v.findViewById(R.id.image_Button);
        mPhotoView=(ImageView)v.findViewById(R.id.crime_photo);
        mPhotoFile=CrimrLab.get(getActivity()).getPhotoFile(mCrime);
        mGallery=(Button)v.findViewById(R.id.photo_Gallery);
        //******** update date on date Button ********//
        updateDate();
        updateTime();
        updatePhotoView();

        ////******* tranfer date format to day, mon 22, 2015*******/////
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        DateButton.setText(df.format("EEE , dd MMMM ,yyyy", mCrime.getDate()));
        Solved_Button.setChecked(mCrime.isSolved());
        //add action for EditText
        mCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
          //*** action for Date button *****/////
        DateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment fragment = DatePickerFragment.NewInstence(mCrime.getDate());
                fragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                fragment.show(fragmentManager, DIALOG_DATE);
            }
        });

        //********* action for Gallery Button  **************//
        mGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                File ImageFile= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                Uri uri=Uri.parse(ImageFile.getPath());
               
               intent.setDataAndType(uri,"image/*");
                intent=Intent.createChooser(intent,getString(R.string.ChoosePhoto));
                startActivityForResult(intent, REQUEST_Gallery);
            }
        });

        //******* action for Time Button *****//
       TimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                TimePickerFragment pickerFragment =TimePickerFragment.NewInstence(mCrime.getTime());
                pickerFragment.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
                pickerFragment.show(fragmentManager, DIALOG_Time);
            }
        });
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager=getFragmentManager();
                Bitmap bitmap=PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
                if (bitmap!=null ) {
                    ShowPhotoFragment showPhotoFragment = new ShowPhotoFragment();
                    showPhotoFragment.ShowPhotoFragment(bitmap);
                    showPhotoFragment.show(fragmentManager, DIALOG_PHOTO);
                }else{
                    Toast.makeText(getActivity(), " :) No photo for Show :)", Toast.LENGTH_SHORT).show();
                }

            }
        });
           //******* add action for solved check box  ***********//
        Solved_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                updateCrime();
            }
        });

        //*********** add acton for Send Crime Report **************//
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder builder= ShareCompat.IntentBuilder.from(getActivity());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);
            }
        });

        //********** acton for detect suspect ******//
        final Intent intent=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(intent);
          startActivityForResult(intent, REQUEST_CONTACT);
            }
        });
        if (mCrime.getSusbect()!=null){
         mSuspectButton.setText(mCrime.getSusbect());
        }
        //****** to check if contects app us found or not in your device  ************//
       PackageManager manager=getActivity().getPackageManager();
        if(manager.resolveActivity(intent,PackageManager.MATCH_DEFAULT_ONLY)==null){
        mSuspectButton.setEnabled(false);
        }

        //*********** action for Image Button *********//
        final  Intent takePhoto=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto=mPhotoFile!=null&&takePhoto.resolveActivity(manager)!=null;
       mPhotoButton.setEnabled(canTakePhoto);
        if (canTakePhoto){
         Uri uri=Uri.fromFile(mPhotoFile);
            takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        }
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(takePhoto,REQUEST_PHOTO);
            }
        });
        return v;
    }


    //********* override onActivityResult to get Date form DatePicker and put it in Crime Date *******//
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date= (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            DateButton.setText(date.toString());
            updateDate();
            updateCrime();
        }else if(requestCode==REQUEST_CONTACT && data!=null){
            Uri uri=data.getData();
            String[] StringFeiled=new String[]{ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts._ID};

            Cursor cursor=getActivity().getContentResolver().query(uri,StringFeiled,null,null,null);
            try {
                if (cursor.getCount()==0){
                    return;
                }
                cursor.moveToFirst();
                String suspect=cursor.getString(0);
                mCrime.setSusbect(suspect);
                updateCrime();
                mSuspectButton.setText(suspect);
            }finally {
                cursor.close();
            }
        }else if (requestCode==REQUEST_PHOTO){
            updateCrime();
            updatePhotoView();
        }else if(requestCode==REQUEST_Gallery){
            try {
              Uri uri=data.getData();
                InputStream inputStream=getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);

                mPhotoView.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(getActivity(), "A7a Error yad ", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode==REQUEST_TIME){
            Date date=(Date) data.getSerializableExtra(TimePickerFragment.EXTRA_Time);
            mCrime.setTime(date);
            updateTime();
            updateCrime();
        }
    }


    //******* for update Fragment ***********//
    private void updateCrime() {
        CrimrLab.get(getActivity()).UpdateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);

    }
 private void updateTime(){
     android.text.format.DateFormat df = new android.text.format.DateFormat();
     TimeButton.setText(df.format("hh:mm", mCrime.getTime()));
 }
    private void updateDate(){
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        DateButton.setText(df.format("EEE , dd MMMM ,yyyy", mCrime.getDate()));
//        TimeButton.setText(String.valueOf(mCrime.getDate().getHours()) + " : " + String.valueOf(mCrime.getDate().getMinutes()));

    }

    //*********** update image view *********//
    private void updatePhotoView(){
        if(mPhotoFile==null||!mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else {
            Bitmap bitmap=PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    private void updatePhotoView1(){
        if(mPhotoFile==null||!mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else {
            Bitmap bitmap=PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    //********* for sending Crime Report **********//
    public String getCrimeReport(){
        String SolvedString=null;
        if (mCrime.isSolved()){
            SolvedString=getString(R.string.crime_report_solved);
        }else{
            SolvedString=getString(R.string.crime_report_unsolved);
        }
        String dateFormat="EEE , dd MMMM ,yyyy";
        String dateString=DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect=mCrime.getSusbect();
        if(suspect==null){
            suspect=getString(R.string.crime_report_no_suspect);
        }else{
            suspect=getString(R.string.crime_report_suspect,suspect);
        }

        String report=getString(R.string.crime_report,mCrime.getTitle(),dateString,SolvedString,suspect);
        return report;
    }

}
