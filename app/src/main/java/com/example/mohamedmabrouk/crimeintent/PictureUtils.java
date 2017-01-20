package com.example.mohamedmabrouk.crimeintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by Mohamed Mabrouk on 31/03/2016.
 */
public class PictureUtils  {
    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);
        return getScleBitmap(path,size.x,size.y);
    }

    public static Bitmap getScleBitmap(String Path, int desWidth,int desHeight){
       BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(Path,options);

        float srcWidth=options.outWidth;
        float srcHeight=options.outHeight;

        int SampleSize=1;
        if(srcWidth>desWidth||srcHeight>desHeight) {
            if (srcWidth > desWidth) {
                SampleSize = Math.round(srcWidth / desWidth);
            } else {
                SampleSize = Math.round(srcHeight / desHeight);
            }
        }
            options=new BitmapFactory.Options();
            options.inSampleSize=SampleSize;

            return BitmapFactory.decodeFile(Path,options);

    }
}
