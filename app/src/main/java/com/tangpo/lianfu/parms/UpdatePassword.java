package com.tangpo.lianfu.parms;


import com.tangpo.lianfu.config.Configs;
import com.tangpo.lianfu.utils.Escape;
import com.tangpo.lianfu.utils.GetMD5Vec;
import com.tangpo.lianfu.utils.GetTime;
import com.tangpo.lianfu.utils.RandomNum;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shengshoubo on 2015/11/4.
 */
public class UpdatePassword {
    public static final String packagingParam(String... kvs) {
        JSONObject jsonObject = new JSONObject();
        String action = "28";
        String time = GetTime.getTime();
        String rannum = RandomNum.randomString(32);
        String key = Configs.KEY_APPJSONKEY;
        String md5vec = GetMD5Vec.getMD5Vec(action, rannum, time, key);
        try {
            jsonObject.put("action", Escape.escape(action));
            jsonObject.put("time", Escape.escape(time));
            jsonObject.put("rannum", Escape.escape(rannum));
            jsonObject.put("md5ver", Escape.escape(md5vec));

            JSONObject paramJsonObject = new JSONObject();
            paramJsonObject.put("user_id", Escape.escape(kvs[0]));
            paramJsonObject.put("old_pw", Escape.escape(kvs[1]));
            paramJsonObject.put("new_pw", Escape.escape(kvs[2]));

            jsonObject.put("param", paramJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
