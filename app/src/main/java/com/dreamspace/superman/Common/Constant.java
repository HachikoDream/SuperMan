package com.dreamspace.superman.Common;

import java.util.Map;

/**
 * Created by Administrator on 2015/8/20 0020.
 */
public class Constant {
    public static String BASE_URL="http://api2.hloli.me:9777/v1.0";
    public static final String FEMALE="female";
    public static final String MALE="male";
    public static final class COME_SOURCE{
        public static final String SOURCE="source_of_verify_phone";
        public static final int THIRD_PLAT=0;
        public static final int MODIFY_PWD=1;
        public static final int MODIFY_PHOME=2;
    }
    public static final class USER_APPLY_STATE{
        //todo 需要检测一下每一个的正确性
        public static final String NOT_APPLY="not apply";
        public static final String NORMAL="normal";
        public static final String PENDING="pending";
        public static final String STOP="Stop";
    }
    public static final class LESSON_STATE{
        public static final String ON="on";
        public static final String OFF="off";
    }
}
