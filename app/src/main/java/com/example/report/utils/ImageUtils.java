package com.example.report.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtils {
    private static final int MAX_IMAGE_DIMENSION = 1024;
    private static final int JPEG_QUALITY = 80;

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) return null;
        
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, stream);
        return stream.toByteArray();
    }

    public static Bitmap byteArrayToBitmap(byte[] data) {
        if (data == null) return null;
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) return null;
        
        byte[] bytes = bitmapToByteArray(bitmap);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static Bitmap base64ToBitmap(String base64) {
        if (base64 == null) return null;
        
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap resizeBitmap(Bitmap bitmap) {
        if (bitmap == null) return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= MAX_IMAGE_DIMENSION && height <= MAX_IMAGE_DIMENSION) {
            return bitmap;
        }

        float scale;
        if (width > height) {
            scale = (float) MAX_IMAGE_DIMENSION / width;
        } else {
            scale = (float) MAX_IMAGE_DIMENSION / height;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        if (bitmap == null) return null;
        
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
