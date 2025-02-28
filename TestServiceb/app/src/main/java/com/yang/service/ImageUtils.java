package com.yang.service;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by yubo on 2015/8/31.
 * 图像处理的工具类
 */
public class ImageUtils {

    /** 将yuv数据转换为jpeg */
    public static byte[] yuv2Jpeg(byte[] yuvBytes, int width, int height) {
        YuvImage yuvImage = new YuvImage(yuvBytes, ImageFormat.NV21, width, height, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 100, baos);

        return baos.toByteArray();
    }

    /** 旋转图像 */
    public static Bitmap rotateBitmap(Bitmap sourceBitmap, int degree) {
        Matrix matrix = new Matrix();
        //旋转90度，并做镜面翻转
        matrix.setRotate(degree);
        matrix.postScale(-1, 1);
        return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
    }

    /** 保存bitmap到文件 */
    public static void saveBitmap(Bitmap bitmap, String path) {
        if(bitmap != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(path)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
