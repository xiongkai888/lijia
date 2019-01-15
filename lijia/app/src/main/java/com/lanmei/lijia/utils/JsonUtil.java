package com.lanmei.lijia.utils;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.xson.common.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * json 和 实体类之间的相互转换
 *
 * @author 00
 */
public class JsonUtil {

    /**
     * 将一个实体对象  转换成一个json字符串  提示对象中可包含集合
     *
     * @param t 实体类
     * @return
     */
    public static <T> String beanToJson(T t) {
        Gson gson = new Gson();
        String json = gson.toJson(t);
        return json;
    }

    /**
     * 将一个json字符串 转换成一个实体类对象 可包含list
     *
     * @param json
     * @param
     * @return
     */
    public static <T> T jsonToBean(String json, Class<T> class1) throws InstantiationException, IllegalAccessException {
        Gson gson = new Gson();
        T t = class1.newInstance();
        t = gson.fromJson(json, class1);
        return t;
    }

    /**
     * 将json字符串转换成一个json对象
     *
     * @param str
     * @return
     */
    public static JSONObject stringToJson(String str) {
        try {
            return new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getString(InputStream is) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            byte[] byteArray = baos.toByteArray();
            //String str = new String(byteArray);

            return new String(byteArray, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 从assert文件夹中读取json文件，然后转化为json对象
     *
     * @throws Exception
     */
    public static JSONObject getJsonDataFromAssets(Context context, String jsonFileName) throws Exception {
        JSONObject mJsonObj = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = context.getAssets().open(jsonFileName);
        int len = -1;
        byte[] buf = new byte[1024];
        while ((len = is.read(buf)) != -1) {
            sb.append(new String(buf, 0, len, "UTF-8"));
        }
        is.close();
        mJsonObj = new JSONObject(sb.toString());
        return mJsonObj;
    }

    /**
     * 根据JSONArray String获取到List
     *
     * @param <T>
     * @param <T>
     * @param jArrayStr
     * @return
     */
    public static <T> List<T> getListByArray(Class<T> class1, String jArrayStr) {
        List<T> list = new ArrayList<>();
        JSONArray jsonArray = JSONArray.parseArray(jArrayStr);
        if (StringUtils.isEmpty(jsonArray)) {
            return list;//nerver return null
        }
        for (Object object : jsonArray) {
            com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) object;
            T t = com.alibaba.fastjson.JSONObject.toJavaObject(jsonObject, class1);
            list.add(t);
        }
        return list;
    }

    /**
     * 根据List获取到对应的JSONArray
     *
     * @param list
     * @return
     */
    public static JSONArray getJSONArrayByList(List<?> list) {
        JSONArray jsonArray = new JSONArray();
        if (list == null || list.isEmpty()) {
            return jsonArray;//nerver return null
        }

        for (Object object : list) {
            jsonArray.add(object);
        }
        return jsonArray;
    }

}
