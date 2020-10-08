package com.jaredco.regrann.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jaredco.regrann.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Util

{


    public static void showOkDialog(String title, String msg, Context ctx) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);

        // set dialog message
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.ic_launcher);

        alertDialogBuilder.setMessage(msg)
                .setCancelable(false).setPositiveButton("Ok", null);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }



    static public boolean isKeepCaption (Context ctx)
    {

        SharedPreferences preferences;
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
     //  return preferences.getBoolean(IS_KEEP_CAPTION, false) ;
        return false ;
    }

    public static String prepareCaption(String title, String author, String caption_suffix,  Context ctx) {
        return prepareCaption( title,  author,  ctx, caption_suffix, false);
    }

    public static String prepareCaption(String title, String author, String caption_suffix,  Context ctx, boolean isTikTok) {
        return prepareCaption( title,  author,  ctx, caption_suffix, isTikTok);
    }

    public static String prepareCaption(String title, String author, Context ctx, String caption_suffix, boolean isTikTok) {
        String caption = "";
        String newCaption;

        try {

            caption =  title ;


            Log.d("regrann","caption 1 : " + caption);
            SharedPreferences preferences;
            preferences = PreferenceManager.getDefaultSharedPreferences(ctx);

            boolean signatureactive = false;

            String sigPref = preferences.getString("signature_type_list", "");


            if (sigPref.equals("2") || sigPref.equals("3"))
                signatureactive = true;


            if (signatureactive) {
                String signatureText = preferences.getString("signature_text", "");
                caption = title + signatureText;

            }

            if (sigPref.equalsIgnoreCase("3")) { // replace signature
                caption = preferences.getString("signature_text", "");

            }
            Log.d("regrann","caption 2 : " + caption);
            // set title to caption which now may include signature

            newCaption = caption ;



                //count how many hashtags
                if (title != null) {
                    int count = newCaption.length() - newCaption.replace("#", "").length();




                    String caption_prefix = preferences.getString("caption_prefix", "Reposted");

                    if (isTikTok)
                        caption = caption_prefix + " from TikTok/@" + author + " " + newCaption;
                        else

                        caption = caption_prefix + " from @" + author + " " + newCaption;





                    if (count > 30 && signatureactive)

                    {
                        caption = caption_prefix + " from @" + author + "  " + title;
                    }


                }



            newCaption = caption + "  "  ;
            Log.d("regrann","newcaption 1 : " + newCaption);






        } catch (Exception e) {
            return "";
        }



        return newCaption;
    }



    // Decodes image and scales it to reduce memory consumption
    public static Bitmap decodeFile(File f) {
        try {
            int IMAGE_MAX_SIZE = 1000 ;
            Bitmap b = null;

            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();

            return b;
        } catch (OutOfMemoryError e) {} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
