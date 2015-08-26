package com.dreamspace.superman.Common;

import com.qiniu.android.storage.UploadManager;

/**
 * Created by Administrator on 2015/8/26 0026.
 */
public class UpLoadUtils {
    private static class UploadManagerHolder{
        private static final UploadManager INSTANCE=new UploadManager();
    }
    private UpLoadUtils(){}
    public static final UploadManager getInstance(){
        return UploadManagerHolder.INSTANCE;
    }
}
