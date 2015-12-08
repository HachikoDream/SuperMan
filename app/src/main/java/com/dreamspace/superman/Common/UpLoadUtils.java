package com.dreamspace.superman.Common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2015/8/26 0026.
 */
public class UpLoadUtils {
    private static class UploadManagerHolder {
        static Configuration config = new Configuration.Builder()
                .putThreshhold(10 * 1024 * 1024)  // 启用分片上传阀值。默认 512K
                .build();
        private static final UploadManager INSTANCE = new UploadManager(config);
    }

    public static void upLoadImage(String path, String key, String token, UpCompletionHandler completionHandler, UploadOptions options) {
        byte[] data = compressImage(path);
        getInstance().put(data, key, token, completionHandler, options);
    }

    private UpLoadUtils() {
    }

    public static final UploadManager getInstance() {
        return UploadManagerHolder.INSTANCE;
    }

    private static byte[] compressImage(String imagePath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //设置为true获取图片的初始大小
        opts.inJustDecodeBounds = true;
        Bitmap image = BitmapFactory.decodeFile(imagePath, opts);
        int imageHeight = opts.outHeight;
        int imageWidth = opts.outWidth;

        opts.inJustDecodeBounds = false;

        //控制图片高宽中较低的一个在500像素左右
        if (Math.min(imageHeight, imageWidth) > 500) {
            float ratio = Math.max(imageHeight, imageWidth) / 500;
            opts.inSampleSize = Math.round(ratio);
        }
        Bitmap finalImage = BitmapFactory.decodeFile(imagePath, opts);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //降低画质
        finalImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }
}
