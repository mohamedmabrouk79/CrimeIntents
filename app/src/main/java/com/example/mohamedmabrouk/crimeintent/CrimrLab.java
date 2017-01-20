package com.example.mohamedmabrouk.crimeintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.mohamedmabrouk.crimeintent.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mohamed Mabrouk on 27/03/2016.
 */
public class CrimrLab {
    private static CrimrLab sCrimrLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    //******* for adding Crime ******//
    public void addCrime(Crime crime){
    ContentValues contentValues=getContentValues(crime);
     mDatabase.insert(CrimeTable.NAME,null,contentValues);
    }

    //****** for delete Crime *****///
    public void deleteCrime(int i , UUID uuid){
        List<Crime> crimes=getCrimes();
        mDatabase.delete(CrimeTable.NAME,CrimeTable.Cols.UUID+" = ?",new String[]{uuid.toString()});

    }
    ///********* for get Instence from Crime Lab Class ***********//////////
    public static CrimrLab get(Context context ) {
    if(sCrimrLab==null){
       sCrimrLab=new CrimrLab(context);
    }
        return sCrimrLab;
    }
       /////////********private constrector  *************//////
    private CrimrLab(Context context) {
        mContext=context.getApplicationContext();
        mDatabase=new CrimeBaseHelper(mContext).getWritableDatabase();
      /*  for (int i=0 ; i<100 ; i++){
         Crime crime=new Crime();
            crime.setTitle("Crime #"+i);
            crime.setSolved(i%2==0);
            mcrimes.add(crime);
        }*/
    }

    //////******* fr get list of all Crimes ******////
    public List<Crime> getCrimes(){
     List<Crime> crimes=new ArrayList<>();
        CrimeCursorWrapper  wrapper=queryCrimes(null,null);
        try {
          wrapper.moveToFirst();
            while (!wrapper.isAfterLast()){
                crimes.add(wrapper.getCrime());
                wrapper.moveToNext();
            }
        }finally {
            wrapper.close();
        }
        return crimes;
    }
    ///***** retern one crime using id ***************/////
    public Crime getCrime(UUID id){
     CrimeCursorWrapper wrapper=queryCrimes(CrimeTable.Cols.UUID +" =?" ,new String[]{id.toString()});
        try {
            if (wrapper.getCount()==0){
                return null;
            }
            wrapper.moveToFirst();
            return  wrapper.getCrime();
        }finally {
            wrapper.close();
        }
    }

    //********** for insert date in database ***********//
    private static ContentValues getContentValues(Crime crime){
        ContentValues values=new ContentValues();
        values.put(CrimeTable.Cols.UUID,crime.getID().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT,crime.getSusbect());
        values.put(CrimeTable.Cols.TIME,crime.getTime().getTime());
        return values;
    }

    ///******m******* update Crime date ***********m********//
    public void UpdateCrime(Crime crime){
        String uuid=crime.getID().toString();
        ContentValues values=getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuid});
    }

    //******************* delete Crime **************//
    private void DeltetCrime(Crime crime){
        String uuid=crime.getID().toString();
        ContentValues values=getContentValues(crime);
        

    }

    //**************        query             *************//
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }

    //*********** for determine location for save photo *********//
    public File getPhotoFile(Crime crime){
        File externalFile=mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFile==null){
            return null;
        } else
            return new File(externalFile, crime.getPhotoFilename());


    }
}
