/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tangpo.lianfu.config.WeiBo;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ������Ϣ�ṹ�塣
 *
 * @author SINA
 * @since 2013-11-24
 */
public class Geo {

    /** �������� */
    public String longitude;
    /** ά������ */
    public String latitude;
    /** ���ڳ��еĳ��д��� */
    public String city;
    /** ����ʡ�ݵ�ʡ�ݴ��� */
    public String province;
    /** ���ڳ��еĳ������� */
    public String city_name;
    /** ����ʡ�ݵ�ʡ������ */
    public String province_name;
    /** ���ڵ�ʵ�ʵ�ַ������Ϊ�� */
    public String address;
    /** ��ַ�ĺ���ƴ������������������᷵�ظ��ֶ� */
    public String pinyin;
    /** ������Ϣ����������������᷵�ظ��ֶ� */
    public String more;
    
    public static Geo parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        Geo geo = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            geo = parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return geo;
    }

    public static Geo parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        
        Geo geo = new Geo();
        geo.longitude       = jsonObject.optString("longitude");
        geo.latitude        = jsonObject.optString("latitude");
        geo.city            = jsonObject.optString("city");
        geo.province        = jsonObject.optString("province");
        geo.city_name       = jsonObject.optString("city_name");
        geo.province_name   = jsonObject.optString("province_name");
        geo.address         = jsonObject.optString("address");
        geo.pinyin          = jsonObject.optString("pinyin");
        geo.more            = jsonObject.optString("more");
        
        return geo;
    }
}
