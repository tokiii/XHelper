package com.lost.cuthair.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串和list的转换
 * Created by lost on 2016/5/11.
 */
public class StringUtils {

    /**
     * list转换字符串
     * @param list
     * @return
     */
    public static String listToString(List<String> list) {

        if (list.size() != 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i)).append(",");
            }
            return sb.toString().substring(0, sb.toString().length() - 1);
        }
         return "";

    }


    /**
     * 字符串转换成list
     * @param s
     * @return
     */
    public static List<String>  stringToList(String s) {
        String[] strs = s.split(",");
        Log.i("info", "转换成的字符串-数组--->" + s);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            list.add(strs[i]);
        }

        return list;
    }
}
