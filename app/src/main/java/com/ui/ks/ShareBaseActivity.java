package com.ui.ks;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.base.BaseActivity;
import com.cocosw.bottomsheet.BottomSheet;
import com.ui.global.Global;
import com.ui.util.BitmapUtils;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.tencent.open.utils.SystemUtils;

public class ShareBaseActivity extends BaseActivity {
    public Handler __mHandler;

    public String shareTitle = "";
    public String shareResume = "";
    public String sharePicUrl = "";
    public String shareUrl = "";

    public Tencent mTencent;

    public IWXAPI api;

    public Context ctx;
//    private boolean wxIsTimeLine = false;
    private static final int THUMB_SIZE = 150;
    private Bitmap bmp;
    private static final int MAX_SIZE = 99;
    public int wxScene = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState, int loginType, boolean setTheme) {
        super.onCreate(savedInstanceState, loginType, setTheme);
        __mHandler = new Handler();
    }

    //初始化qq
    private boolean hasInitQQ = false;
    private void initQQ() {
        if(!hasInitQQ) {
            mTencent = Tencent.createInstance(Global.QQ_APP_ID, this.getApplicationContext());

            hasInitQQ = true;
        }
    }

    //初始化微信
    private boolean hasInitWx = false;
    public void initWx() {
        if(!hasInitWx) {
            String wx_app_id = Global.isDebug ? Global.WX_DEBUG_APP_ID : Global.WX_APP_ID;

            api = WXAPIFactory.createWXAPI(this, wx_app_id);
            api.registerApp(wx_app_id);

            hasInitWx = true;
        }
    }

    public void doShare(final Context ctx) {
        this.ctx = ctx;

        BottomSheet.Builder a = new BottomSheet.Builder(this).title("分享").sheet(R.menu.menu_share)
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.share_wx_friends:
                                //微信好友
                                wxScene = SendMessageToWX.Req.WXSceneSession;
                                doShareToWeixin();
                                break;
                            case R.id.share_wx_timeline:
                                //微信朋友圈
                                wxScene = SendMessageToWX.Req.WXSceneTimeline;
                                doShareToWeixin();
                                break;
//                            case R.id.share_wx_collect:
//                                //微信收藏
//                                wxScene = SendMessageToWX.Req.WXSceneFavorite;
//                                doShareToWeixin();
//                                break;
//                            case R.id.share_sina:
//                                //新浪微博
//                                doShareToSina();
//                                break;
                            case R.id.share_qq:
                                //qq
                                doShareToQQ();
                                break;

                            case R.id.share_qzone:
                                //qzone
                                doShareToQzone();
                                break;

                            case R.id.share_sms:
                                //发送短信
                                doShareSms();
                                break;

                            case R.id.share_copy:
                                //复制链接
                                doShareCopy();
                                break;

                            case R.id.share_content:
                                //更多
                                doShareMore();
                                break;
                        }
                    }
                });


        a.grid().show();
    }

    public void doShareMore() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "分享"));
    }

    public void doShareToSina() {
        if(shareTitle.length() <= 0) {
            SysUtils.showError("分享内容不存在");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("shareTitle", shareTitle);
        bundle.putString("shareResume", shareResume);
        bundle.putString("shareUrl", shareUrl);
        bundle.putString("sharePicUrl", sharePicUrl);

//        SysUtils.startAct(ctx, new ShareSinaActivity(), bundle);
    }

    /*
    public class BaseUiListener implements IUiListener {
        public void onComplete(JSONObject response) {
            doComplete(response);
        }

        protected void doComplete(JSONObject values) {
        }

        public void onError(UiError e) {
            SysUtils.showError("onError: " + e.errorDetail);
        }

        public void onCancel() {
//            SysUtils.showError(getString(R.string.weibosdk_demo_toast_auth_canceled));
        }
    }
    */

    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
        }
        @Override
        public void onComplete(Object response) {
        }
        @Override
        public void onError(UiError e) {
        }
    };

    /**
     * 分享到qq
     */
    public void doShareToQQ() {
        if(!SystemUtils.checkMobileQQ(this)) {
            SysUtils.showError("QQ未安装");
            return;
        }
        initQQ();

        if(shareTitle.length() <= 0) {
            SysUtils.showError("分享内容不存在");
            return;
        }

        if(!StringUtils.isEmpty(shareUrl)) {
            //有链接，分享图文
            __doshareWebpageQQ();
        } else if(!StringUtils.isEmpty(sharePicUrl)) {
            //否则看是否有图片
            __dosharePicQQ();
        } else {
            SysUtils.showError("分享内容不存在");
        }
    }

    public void doShareToQzone() {
        if(!SystemUtils.checkMobileQQ(this)) {
            SysUtils.showError("QQ未安装");
            return;
        }
        initQQ();

        if(shareTitle.length() <= 0) {
            SysUtils.showError("分享内容不存在");
            return;
        }

        if(!StringUtils.isEmpty(shareUrl)) {
            //有链接，分享图文
            __doshareWebpageQzone();
        }
    }

    private void __doshareWebpageQzone() {
        Bundle params = new Bundle();
//        params.putString(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT );
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareTitle);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareResume);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareUrl);//必填

        if (!StringUtils.isEmpty(sharePicUrl)) {
            ArrayList<String> picList = new ArrayList<String>();
            picList.add(sharePicUrl);
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, picList);
        }

        mTencent.shareToQzone(this, params, qqShareListener);

    }

    /**
     * 分享网页到qq
     */
    private void __doshareWebpageQQ() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareResume);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  shareUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, sharePicUrl);
        mTencent.shareToQQ(this, params, qqShareListener);
    }

    /**
     * 分享图片到qq
     */
    private void __dosharePicQQ() {
        final String saveFile = SysUtils.getPicSavePath(ctx, "");
        ImageRequest f = new ImageRequest(
                sharePicUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        try {
                            File image = new File(saveFile);

                            if(image.exists()) {
                                image.delete();
                            }

                            //写到此文件
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            response.compress(Bitmap.CompressFormat.PNG, 0, bos);
                            byte[] bitmapdata = bos.toByteArray();

                            FileOutputStream out = new FileOutputStream(image);
                            out.write(bitmapdata);
                            out.close();

                            __okSharePicToQQ(saveFile);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                0,
                0,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        SysUtils.showError("分享失败");
                    }
                }
        );
        executeRequest(f);
    }

    private void __okSharePicToQQ(String saveFile) {
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, saveFile);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ(this, params, qqShareListener);
    }

    public boolean wxIsInstalled() {
        return api.isWXAppInstalled();
    }

    public void doShareToWeixin() {
        initWx();

        if(!wxIsInstalled()) {
            SysUtils.showError("微信未安装");
            return;
        }

        if(!StringUtils.isEmpty(sharePicUrl)) {
            //有图片，首先尝试解析出图片
            wx_loadBitmap();
        } else {
            //没有图片
            __doShareToWeixin();
        }
    }

    private void __doShareToWeixin() {
        if(!StringUtils.isEmpty(shareUrl)) {
            //有超链接
            if(!StringUtils.isEmpty(shareTitle)) {
                //有标题，当成超链接
                __doShareWebpageToWeixin();
            } else {
                //没有标题
                if(bmp != null) {
                    //有图片，分享图片
                    __doShareWeixinPicToWeixin();
                } else {
                    //都不存在
                    SysUtils.showError("分享内容不存在");
                }
            }
        } else {
            //没有超链接
            if(bmp != null) {
                //分享图片
                __doShareWeixinPicToWeixin();
            } else {
                //图片也不存在，那么尝试分享纯文字
                if(!StringUtils.isEmpty(shareTitle)) {
                    __doShareWeixinTextToWeixin();
                } else {
                    //都不存在
                    SysUtils.showError("分享内容不存在");
                }
            }
        }
    }

    /**
     * 分享微信网页格式
     */
    private void __doShareWebpageToWeixin() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareTitle;
        msg.description = shareResume;

        //缩略图，需要压缩，不能超过32k
        if(bmp != null) {
            msg.thumbData = BitmapUtils.bmpToByteArray(BitmapUtils.getResizedBitmap(bmp, MAX_SIZE), true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = wxScene;
        api.sendReq(req);
    }

    /**
     * 分享微信图片
     */
    private void __doShareWeixinPicToWeixin() {
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = BitmapUtils.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = wxScene;
        api.sendReq(req);
    }

    /**
     * 分享文字到微信
     */
    private void __doShareWeixinTextToWeixin() {
        WXTextObject textObj = new WXTextObject();
        textObj.text = shareTitle;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = shareResume;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = wxScene;

        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void doShareSms() {
        String smsContent = shareTitle + ": " + shareUrl;
        Uri uri = Uri.parse("smsto:");
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsContent);
        ctx.startActivity(it);
    }

    public void doShareCopy() {
        ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(shareUrl);

        SysUtils.showSuccess("网址已复制");
    }

    private void wx_loadBitmap() {
        ImageRequest f = new ImageRequest(
                sharePicUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        bmp = response;
                        __doShareToWeixin();
                    }
                },
                0,
                0,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        bmp = null;
                        __doShareToWeixin();
                    }
                }
        );
        executeRequest(f);
    }

    public void setShareResume(String resume) {
        shareResume = resume;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            mTencent.onActivityResult(requestCode, resultCode, data);
    }
}
