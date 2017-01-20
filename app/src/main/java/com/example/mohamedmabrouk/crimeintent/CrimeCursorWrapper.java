package com.example.mohamedmabrouk.crimeintent;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.mohamedmabrouk.crimeintent.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mohamed Mabrouk on 30/03/2016.
 */
public class CrimeCursorWrapper  extends CursorWrapper{
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Crime getCrime(){
        String uuidString=getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title=getString(getColumnIndex(CrimeTable.Cols.TITLE));
        Long date=getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int Solved=getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect=getString(getColumnIndex(CrimeTable.Cols.SUSPECT));
        Long time=getLong(getColumnIndex(CrimeTable.Cols.TIME));
        Crime crime=new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(Solved != 0);
        crime.setSusbect(suspect);
        crime.setTime(new Date(time));
        return crime;
    }
}
