package com.jaredco.regrann.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.jaredco.regrann.util.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader {

    static String fname;
    static boolean isSocial = false;
    private static final String DOWNLOAD_FOLDER = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

    public static void downloadFile(Context context, String url, String f, boolean fromSocial) {
        fname = f;
        Util.setTempVideoFileName(fname);
        isSocial = fromSocial;
        new DownloadFileTask(context).execute(url);
    }

    private static class DownloadFileTask extends AsyncTask<String, Void, Boolean> {
        private final Context context;

        public DownloadFileTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context, "Downloading video...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            String fileUrl = urls[0];
            try {
                URL url = new URL(fileUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // Get the file name from the URL
                String fileName = fname;

                File outputFile = new File(DOWNLOAD_FOLDER + "/Regrann/", fileName);

                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                FileOutputStream outputStream = new FileOutputStream(outputFile);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

                return true;
            } catch (Exception e) {

                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            RegrannApp.sendEvent("filedownload_" + result);

            ShareActivity._this.videoDownloadComplete(result, isSocial);


            //      Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show();
            // Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show();
            ShareActivity._this.videoDownloadComplete(result, isSocial);
        }
    }
}
