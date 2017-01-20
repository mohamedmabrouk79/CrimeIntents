package com.example.mohamedmabrouk.crimeintent;

/**
 * Created by Mohamed Mabrouk on 30/03/2016.
 */
public class CrimeDbSchema {
    public static class CrimeTable{
        public static final String NAME = "crimes";
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT="suspect";
            public static final String TIME="time";
        }
    }
}
