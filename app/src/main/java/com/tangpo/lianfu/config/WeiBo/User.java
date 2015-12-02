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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * �û���Ϣ�ṹ�塣
 *
 * @author SINA
 * @since 2013-11-24
 */
public class User {

    /** �û�UID��int64�� */
    public String id;
    /** �ַ����͵��û� UID */
    public String idstr;
    /** �û��ǳ� */
    public String screen_name;
    /** �Ѻ���ʾ���� */
    public String name;
    /** �û�����ʡ��ID */
    public int province;
    /** �û����ڳ���ID */
    public int city;
    /** �û����ڵ� */
    public String location;
    /** �û��������� */
    public String description;
    /** �û����͵�ַ */
    public String url;
    /** �û�ͷ���ַ��50��50���� */
    public String profile_image_url;
    /** �û���΢��ͳһURL��ַ */
    public String profile_url;
    /** �û��ĸ��Ի����� */
    public String domain;
    /** �û���΢�� */
    public String weihao;
    /** �Ա�m���С�f��Ů��n��δ֪ */
    public String gender;
    /** ��˿�� */
    public int followers_count;
    /** ��ע�� */
    public int friends_count;
    /** ΢���� */
    public int statuses_count;
    /** �ղ��� */
    public int favourites_count;
    /** �û�������ע�ᣩʱ�� */
    public String created_at;
    /** ��δ֧�� */
    public boolean following;
    /** �Ƿ����������˸��ҷ�˽�ţ�true���ǣ�false���� */
    public boolean allow_all_act_msg;
    /** �Ƿ������ʶ�û��ĵ���λ�ã�true���ǣ�false���� */
    public boolean geo_enabled;
    /** �Ƿ���΢����֤�û�������V�û���true���ǣ�false���� */
    public boolean verified;
    /** ��δ֧�� */
    public int verified_type;
    /** �û���ע��Ϣ��ֻ���ڲ�ѯ�û���ϵʱ�ŷ��ش��ֶ� */
    public String remark;
    /** �û������һ��΢����Ϣ�ֶ� */
    public Status status;
    /** �Ƿ����������˶��ҵ�΢���������ۣ�true���ǣ�false���� */
    public boolean allow_all_comment;
    /** �û���ͷ���ַ */
    public String avatar_large;
    /** �û������ͷ���ַ */
    public String avatar_hd;
    /** ��֤ԭ�� */
    public String verified_reason;
    /** ���û��Ƿ��ע��ǰ��¼�û���true���ǣ�false���� */
    public boolean follow_me;
    /** �û�������״̬��0�������ߡ�1������ */
    public int online_status;
    /** �û��Ļ����� */
    public int bi_followers_count;
    /** �û���ǰ�����԰汾��zh-cn���������ģ�zh-tw���������ģ�en��Ӣ�� */
    public String lang;

    /** ע�⣺�����ֶ���ʱ��������庬�壬OpenAPI ˵���ĵ���ʱû��ͬ�����¶�Ӧ�ֶ� */
    public String star;
    public String mbtype;
    public String mbrank;
    public String block_word;

    public static User parse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return User.parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static User parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }

        User user = new User();
        user.id                 = jsonObject.optString("id", "");
        user.idstr              = jsonObject.optString("idstr", "");
        user.screen_name        = jsonObject.optString("screen_name", "");
        user.name               = jsonObject.optString("name", "");
        user.province           = jsonObject.optInt("province", -1);
        user.city               = jsonObject.optInt("city", -1);
        user.location           = jsonObject.optString("location", "");
        user.description        = jsonObject.optString("description", "");
        user.url                = jsonObject.optString("url", "");
        user.profile_image_url  = jsonObject.optString("profile_image_url", "");
        user.profile_url        = jsonObject.optString("profile_url", "");
        user.domain             = jsonObject.optString("domain", "");
        user.weihao             = jsonObject.optString("weihao", "");
        user.gender             = jsonObject.optString("gender", "");
        user.followers_count    = jsonObject.optInt("followers_count", 0);
        user.friends_count      = jsonObject.optInt("friends_count", 0);
        user.statuses_count     = jsonObject.optInt("statuses_count", 0);
        user.favourites_count   = jsonObject.optInt("favourites_count", 0);
        user.created_at         = jsonObject.optString("created_at", "");
        user.following          = jsonObject.optBoolean("following", false);
        user.allow_all_act_msg  = jsonObject.optBoolean("allow_all_act_msg", false);
        user.geo_enabled        = jsonObject.optBoolean("geo_enabled", false);
        user.verified           = jsonObject.optBoolean("verified", false);
        user.verified_type      = jsonObject.optInt("verified_type", -1);
        user.remark             = jsonObject.optString("remark", "");
        //user.status             = jsonObject.optString("status", ""); // XXX: NO Need ?
        user.allow_all_comment  = jsonObject.optBoolean("allow_all_comment", true);
        user.avatar_large       = jsonObject.optString("avatar_large", "");
        user.avatar_hd          = jsonObject.optString("avatar_hd", "");
        user.verified_reason    = jsonObject.optString("verified_reason", "");
        user.follow_me          = jsonObject.optBoolean("follow_me", false);
        user.online_status      = jsonObject.optInt("online_status", 0);
        user.bi_followers_count = jsonObject.optInt("bi_followers_count", 0);
        user.lang               = jsonObject.optString("lang", "");

        // ע�⣺�����ֶ���ʱ��������庬�壬OpenAPI ˵���ĵ���ʱû��ͬ�����¶�Ӧ�ֶκ���
        user.star               = jsonObject.optString("star", "");
        user.mbtype             = jsonObject.optString("mbtype", "");
        user.mbrank             = jsonObject.optString("mbrank", "");
        user.block_word         = jsonObject.optString("block_word", "");
        
        return user;
    }
}
