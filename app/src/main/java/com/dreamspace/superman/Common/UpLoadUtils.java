package com.dreamspace.superman.Common;

import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;

/**
 * Created by Administrator on 2015/8/26 0026.
 */
public class UpLoadUtils {
    private static class UploadManagerHolder{
        static Configuration config = new Configuration.Builder()
                .putThreshhold(10*1024 * 1024)  // 启用分片上传阀值。默认 512K
                .build();
        private static final UploadManager INSTANCE=new UploadManager(config);
    }
    private UpLoadUtils(){}
    public static final UploadManager getInstance(){
        return UploadManagerHolder.INSTANCE;
    }
}
