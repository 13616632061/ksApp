package com.ui.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ui.global.Global;
import com.ui.ks.LoginActivity;
import com.MyApplication.KsApplication;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONObject;

public class LoginUtils {

    public static void toLogin(Context ctx, int loginType) {
        //未登录
//        showError("请先登录");
        Bundle b = new Bundle();
        b.putInt("loginType", loginType);
        System.out.println("token  未登录 ="+loginType );
        SysUtils.startAct(ctx, new LoginActivity(), b);
    }

    public static boolean hasLogin() {
        boolean hasLogin = false;
        int login_type = KsApplication.getInt("login_type", 0);
        String token = KsApplication.getString("token", "");
        if (!StringUtils.isEmpty(token)) {
            if (login_type == 1) {
                //店铺
                int seller_id = KsApplication.getInt("seller_id", 0);
                if (seller_id > 0) {
                    hasLogin = true;
                }
            } else if (login_type == 2) {
                //业务员
                hasLogin = true;
            }else if (login_type == 3) {
                //业务员
                hasLogin = true;
            } else if (login_type == 4) {
                //业务员
                hasLogin = true;
            }

        }

        return hasLogin;
    }

    /**
     * 是否店家
     * @return
     */
    public static boolean isSeller() {
        boolean isSeller = false;

        int login_type = KsApplication.getInt("login_type", 0);
        if (login_type == 1) {
            String token = KsApplication.getString("token", "");
            if (!StringUtils.isEmpty(token)) {
                int seller_id = KsApplication.getInt("seller_id", 0);
                if (seller_id > 0) {
                    isSeller = true;
                }
            }
        }

        return isSeller;
    }
    /**
     * 是否总店
     * @return
     */
    public static boolean isMainStore() {
        boolean isMainStore = false;

        int login_type = KsApplication.getInt("login_type", 0);
        if (login_type == 3) {
            String token = KsApplication.getString("token", "");
            if (!StringUtils.isEmpty(token)) {
                int seller_id = KsApplication.getInt("seller_id", 0);
                if (seller_id > 0) {
                    isMainStore = true;
                }
            }
        }

        return isMainStore;
    }

    /**
     * 是否业务员
     * @return
     */
    public static boolean isMember() {
        boolean isMember = false;

        int login_type = KsApplication.getInt("login_type", 0);
        if (login_type == 2) {
            String token = KsApplication.getString("token", "");
            if (!StringUtils.isEmpty(token)) {
                isMember = true;
            }
        }

        return isMember;
    }
    /**
     * 是否营业员
     * @return
     */
    public static boolean isShopper() {
        boolean isShopper = false;
        int login_type = KsApplication.getInt("login_type", 0);
        if (login_type == 4) {
            String token = KsApplication.getString("token", "");
            if (!StringUtils.isEmpty(token)) {
                int seller_id = KsApplication.getInt("seller_id", 0);
                if (seller_id > 0) {
                    isShopper = true;
                }
            }
        }
        return isShopper;
    }

    //是否受限
    public static boolean jurisdiction() {
        return KsApplication.getInt("jurisdiction", 0) == 1;
//        return true;
    }

    public static String ssoTypeStr() {
        int sso_type = KsApplication.getInt("sso_type", 0);

        if(sso_type == 1) {
            return "新浪";
        } else if(sso_type == 2) {
            return "QQ";
        } else if(sso_type == 3) {
            return "微信";
        }

        return "";
    }

    public static void afterLogin(Context ctx, JSONObject jsonObject) {
        LoginUtils.afterLogin(ctx, jsonObject, true, 0);
    }

    public static void afterLogin(Context ctx, JSONObject jsonObject, int loginType) {
        LoginUtils.afterLogin(ctx, jsonObject, true, loginType);
    }

    public static void afterLogin(Context ctx, JSONObject jsonObject, boolean finish, int loginType) {
        try {
            KsApplication.putInt("login_type", loginType);
            if (loginType > 0) {
                KsApplication.putInt("type", jsonObject.getInt("type"));
                KsApplication.putString("token", jsonObject.getString("token"));
                if (loginType == 1||loginType == 3||loginType == 4) {
                    //店铺
                    KsApplication.putInt("seller_id", SysUtils.getFinalInt("id", jsonObject));
                    KsApplication.putInt("jurisdiction", jsonObject.optInt("jurisdiction"));
                    KsApplication.putString("seller_name", jsonObject.getString("seller_name"));
                }
            }

            //发送登录广播
            ctx.sendBroadcast(new Intent(Global.BROADCAST_LOGIN_ACTION));
            KsApplication.putString("login_mod", "");

            //注册推送tag
            LoginUtils.setTag(ctx);

            if(finish) {
                ((Activity) ctx).finish();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTag(Context ctx) {
        String account = "seller_" + KsApplication.getInt("seller_id", 0);
        if (LoginUtils.isSeller()||LoginUtils.isMainStore()||LoginUtils.isShopper()) {
            //商家，设置tag
            MiPushClient.setUserAccount(ctx, account, null);
        } else{
            MiPushClient.unsetUserAccount(ctx, account, null);
        }
    }

    public static void logout(final Context ctx, final int type) {

        KsApplication.putInt("login_type", 0);

        //删除tag
        LoginUtils.setTag(ctx);
        //发出登录广播
        ctx.sendBroadcast(new Intent(Global.BROADCAST_LOGIN_ACTION));
    }
}
