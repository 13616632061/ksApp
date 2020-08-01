package com.ui.ks.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ui.global.Global;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
 
public class WXEntryActivity extends Activity implements IWXAPIEventHandler
{
    private IWXAPI api;
 
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String wx_app_id = Global.isDebug ? Global.WX_DEBUG_APP_ID : Global.WX_APP_ID;
        api = WXAPIFactory.createWXAPI(this, wx_app_id, false);
        api.registerApp(wx_app_id);
        api.handleIntent(getIntent(), this);
    }
 
    public void onReq(BaseReq req)
    {
    }
 
    public void onResp(BaseResp resp)
    {
//        int result = 0;
 
        switch (resp.errCode)
        {
        case BaseResp.ErrCode.ERR_OK:
//            result = R.string.errcode_success;
            if(resp instanceof SendAuth.Resp) {
                //登录返回
                String code = ((SendAuth.Resp) resp).code;
                //发送登录广播
                sendBroadcast(new Intent(Global.BROADCAST_WEIXIN_LOGIN_ACTION).putExtra("code", code));
            } else if(resp instanceof SendMessageToWX.Resp) {
                //分享返回
            }
            break;
        case BaseResp.ErrCode.ERR_USER_CANCEL:
//            result = R.string.errcode_cancel;
            break;
        case BaseResp.ErrCode.ERR_AUTH_DENIED:
//            result = R.string.errcode_deny;
            break;
        default:
//            result = R.string.errcode_unknown;
            break;
        }

//        Log.v("ks", resp.toString());
 
//        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();
//        overridePendingTransition(R.anim.change_in, R.anim.change_out);
    }
}

