package com.dreamspace.superman.Common;

import com.dreamspace.superman.model.OrderClassify;

import java.util.Map;

/**
 * Created by Administrator on 2015/8/20 0020.
 */
public class Constant {
    public static String BASE_URL="http://api2.hloli.me:9777/v1.0";
    public static final String FEMALE="female";
    public static final String MALE="male";
    //// TODO: 2015/10/27 增加客服电话
    public static final String self_phone="15651630827";
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
    public static final class ORDER_CLASSIFY{
        /**
         *     <string-array name="order_list_item">
         <item>预约</item>
         <item>待付款</item>
         <item>待见面</item>
         <item>已完成</item>
         <item>取消/退款</item>
         </string-array>
         */
        //todo 增加状态 是否评价
                                                //  1       2        3       4       0   -1
        public static final String[] ORDER_NAME={"预约","待付款","待见面","已完成","取消/退款"};
        public static final OrderClassify[] orderClassifys={
                new OrderClassify(1,ORDER_NAME[0]),
                new OrderClassify(2,ORDER_NAME[1]),
                new OrderClassify(3,ORDER_NAME[2]),
                new OrderClassify(4,ORDER_NAME[3]),
                new OrderClassify(0,ORDER_NAME[4])
        };
        public static final int SUSCRIBE=1;
        public static final int PRE_COST=2;
        public static final int PRE_MEET=3;
        public static final int FINISH=4;
        public static final int CANCEL=0;
        public static final int BACK_COST=-1;

    }
}
