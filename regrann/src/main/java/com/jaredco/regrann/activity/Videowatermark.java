package com.jaredco.regrann.activity;

/**
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

import com.daasuu.mp4compose.composer.Mp4Composer;
import com.daasuu.mp4compose.composer.Mp4Composer.Listener;


public class Videowatermark {
    private Mp4Composer mp4Composer;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

public OnvideowatermarkListener onvideowatermarkListener;

    public Bitmap watermarkBitmap;

    public interface OnvideowatermarkListener {
        void onComplete();

        void onError(String str);

        void onProgressUpdate(int i);
    }

    public Videowatermark(Context context, String str, String str2, String str3, OnvideowatermarkListener onvideowatermarkListener2) {
        this.onvideowatermarkListener = onvideowatermarkListener2;
        if (str == null || str2 == null || str3 == null) {
            this.onvideowatermarkListener.onError( "There was a problem with the video.");
        } else {
            startApplyingWatermark(context, str, str2, str3);
        }
    }

    private void startApplyingWatermark(final Context context, String str, String str2, String str3) {
        this.watermarkBitmap = BitmapFactory.decodeFile(str3);
        this.mp4Composer = new Mp4Composer(str, str2).filter(new GlWatermarkFilter(this.watermarkBitmap)).videoBitrate(getVideoBitrate(str)).listener(new Listener() {
            public void onProgress(double d) {
                if (Videowatermark.this.onvideowatermarkListener != null) {
                    Videowatermark.this.onvideowatermarkListener.onProgressUpdate((int) Math.round(d * 100.0d));
                }
            }

            public void onCompleted() {
                if (Videowatermark.this.watermarkBitmap != null) {
                    Videowatermark.this.watermarkBitmap.recycle();
                }
                if (Videowatermark.this.onvideowatermarkListener != null) {
                    Videowatermark.this.onvideowatermarkListener.onComplete();
                }
            }

            public void onCanceled() {
                if (Videowatermark.this.watermarkBitmap != null) {
                    Videowatermark.this.watermarkBitmap.recycle();
                }
            }

            public void onFailed(Exception exc) {
                if (Videowatermark.this.watermarkBitmap != null) {
                    Videowatermark.this.watermarkBitmap.recycle();
                }
                if (Videowatermark.this.onvideowatermarkListener == null || exc.getMessage() == null || exc.getMessage().contains("InterruptedException")) {
                    Videowatermark.this.onvideowatermarkListener.onError("Error processing video for watermark");
                } else {
                    Videowatermark.this.onvideowatermarkListener.onError(exc.getMessage());
                }
            }
        }).start();
    }

    public void cancel() {
        Mp4Composer mp4Composer2 = this.mp4Composer;
        if (mp4Composer2 != null) {
            mp4Composer2.cancel();
        }
    }


    private int getVideoBitrate(String str) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(str);
            int parseInt = Integer.parseInt(mediaMetadataRetriever.extractMetadata(20));
            mediaMetadataRetriever.release();
            return parseInt;
        } catch (Exception unused) {
            mediaMetadataRetriever.release();
            return 0;
        } catch (Throwable th) {
            mediaMetadataRetriever.release();
            throw th;
        }
    }

}
**/
