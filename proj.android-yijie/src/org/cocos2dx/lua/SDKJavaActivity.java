/****************************************************************************
    类名：SDKJavaActivity 
    作用：《你来嘛英雄》游戏平台SDK Android接入类
    说明：1.所有Protected 和 Public修饰的成员变量和成员方法 不可以删除 必须复写 
         Private修饰的成员函数可以删除
         2.没有相应SDK方法的 对应方法可以为空实现
    时间：2017-01-20
    码农：tzl
    平台：UC(阿里)
****************************************************************************/
package org.cocos2dx.lua;
import org.cocos2dx.lib.Cocos2dxLuaJavaBridge;
import org.cocos2dx.lib.Cocos2dxActivity;
import android.content.Intent;
import android.widget.Toast;

// ucSDK(aliSDK)头文件 开始

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.telephony.TelephonyManager;


//微信
import java.net.URL;
import java.lang.Exception;
import cc.aw.nlmyx.zdhy.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import android.content.res.AssetManager;
import java.io.InputStream;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

//易接
import com.snowfish.cn.ganga.base.SFActionCallback;
import com.snowfish.cn.ganga.base.SFLuaAdaper;
import com.snowfish.cn.ganga.helper.SFOnlineHelper;
import org.cocos2dx.lib.Cocos2dxLuaJavaBridge;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager; 
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
// ucSDK(aliSDK)头文件 结束


public class SDKJavaActivity extends Cocos2dxActivity{
    /*
     platname       platform      usertype
     体验服（官网）     1             1
     体验服（小米）     1             2
     体验服（360）     1             3
     体验服（UC）      1             4
     小米服            2             0
     360服            3             0
     UC服             4             0
     */

    protected static SDKJavaActivity sdkJavaActivity = null;
    
    private static final int PLATFROM = 5;
    private static final int USERTYPE = 8;
    protected static final boolean isDebug = false;//是否是测试模式
    
    //小米登陆参数
    private static String info;
    private long uid;

//得到sdkJavaActivity

     public static SDKJavaActivity getSDKJavaActivity(){

        return sdkJavaActivity;
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdkJavaActivity = this;
        //ucSDK(aliSDK) onCreate 开始
        //ucSDK(aliSDK) onCreate 结束
        /*初始化Lua组件*/
        SFLuaAdaper.init(sdkJavaActivity, new SFActionCallback() {
            @Override
            public void callback(Runnable run) {
                sdkJavaActivity.runOnGLThread(run);
            }
        });



        try{ 
            

            ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            final String msg=appInfo.metaData.getString("usertype");

            final String phoneName = android.os.Build.MODEL ;
            TelephonyManager mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            final String imei = mTm.getDeviceId();

            String[] msgS = msg.split("\\$");
            info=msgS[1]+","+"5"+","+phoneName+","+imei;


           // Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("yijieInfo",msg+","+"5"+","+phoneName+","+imei);

                    }
        catch(Exception e){
            
                    }  


        // ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        // String msg=appInfo.metaData.getString("com.snowfish.appid");
        // AppActivity.sdkToastLog(msg);
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*易接 onResume*/
        SFOnlineHelper.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*易接 onPause*/
        SFOnlineHelper.onPause(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        /*易接 onStop*/
        SFOnlineHelper.onStop(this);
    }
    @Override
    protected void onRestart() {
        super.onRestart ();
        /*易接 onRestart*/
        SFOnlineHelper.onRestart(this);
    }


    @Override
    protected void onDestroy(){
        //ucSDK(aliSDK) onDestroy 开始
        //ucSDK(aliSDK) onDestroy 结束
        super.onDestroy();
        /*易接 onDestroy*/
        SFOnlineHelper.onDestroy(this);
    }

    //登录游戏 必须调用
    public static void sdkJavaInit() {
        // 调用SDK执行登陆操作
        // AppActivity.sdkToastLog("初始化了sdk");
            sdkJavaActivity.runOnUiThread(new Runnable() {
                     @Override
                    public void run() {
               //此时已在主线程中，可以更新UI了
              

                        //此时已在主线程中，可以更新UI了
                                  sdkJavaActivity.runOnGLThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // SDKJavaActivity sj=new SDKJavaActivity(); 
                                            // Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("yijieInfo",sdkJavaActivity.info);
                                            // AppActivity.sdkToastLog(sdkJavaActivity.info);
                                            Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("yijieInfo",info);
                                            // AppActivity.sdkToastLog(info);

                                    }
                                  });

                    }
                });

    }


@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
            AppActivity.sdkToastLog("调用返回键=====dispatchKeyEvent");

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {            
            //具体的操作代码

                sdkJavaActivity.runOnUiThread(new Runnable() {
                     @Override
                    public void run() {
               //此时已在主线程中，可以更新UI了
              

                        
                        //此时已在主线程中，可以更新UI了
                                  SDKJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
                                    @Override
                                    public void run() {
                                            Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("yijieExit","");

                                    }
                                  });



                    }
                });
             
        }
        return super.dispatchKeyEvent(event);
    }


    //提交游戏角色信息 必须调用
    // zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip
    public static void sdkJavaSubmitData(final String submitDatas) {

    }

    //切换账号 必须调用
    public static void sdkJavaLogout() {

    }

    //退出游戏 必须调用
    public static void sdkJavaExit() {

    }



    //支付 必须调用
    public static void sdkJavaPay(final String payDatas) {
        // userID.."$"..payid.."$"..costMoney.."$"..url
        String[] payData = payDatas.split("\\$");
        String userId=payData[0];
        String payid = payData[1];
        String costMoney=payData[2];
        String payUrl=payData[3];
        String ip = payData[4];
        String port = payData[5];
        // Toast.makeText(sdkJavaActivity, userId, Toast.LENGTH_LONG).show();
//        doSdkPay(null,true,ProtocolConfigs.FUNC_CODE_WEIXIN_PAY,userId,payid,costMoney,payUrl,ip,port);
//AppActivity.sdkToastLog(""+payUrl);


    }




/**
 * 分享文本到朋友圈
 */
public static void wxSdkJavaShareTextToMoments(final String textContent) {
AppActivity.sdkToastLog("这是一段------------测试文本");
    // 实例化
    String APP_ID = "wx9d31c3a4640b546f";
    IWXAPI wxApi = WXAPIFactory.createWXAPI(sdkJavaActivity, APP_ID);
    wxApi.registerApp(APP_ID);

    String text = "这是一段测试文本";
    // 初始化一个WXTextObject对象
    WXTextObject textObj = new WXTextObject();
    textObj.text = text;

    // 用WXTextObject对象初始化一个WXMediaMessage对象
    WXMediaMessage msg = new WXMediaMessage();
    msg.mediaObject = textObj;
    msg.description = text;

    // 构造一个Req
    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
    req.message = msg;
    req.scene = SendMessageToWX.Req.WXSceneTimeline;

//Req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
    //        req.scene = SendMessageToWX.Req.WXSceneSession;//发送给微信好友
//Req.scene = SendMessageToWX.Req.WXSceneTimeline;//发送给微信朋友圈
//Req.scene = SendMessageToWX.Req.WXSceneSession;//发送给微信好友

AppActivity.sdkToastLog("这是一段测试文本");
    // 调用api接口发送数据到微信
    wxApi.sendReq(req);
}



/**
 * 分享图片到微信朋友圈
 */
public static void wxSdkJavaShareImageToMoments() {
//    Bitmap bmp = null
//    try {
//        URL url = new URL("http://b.hiphotos.baidu.com/image/pic/item/fd039245d688d43f76b17dd4781ed21b0ef43bf8.jpg");
//        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        if (bmp.getByteCount() > 4096000) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = bmp.getByteCount() / 4096000;//缩放比例
//            options.inJustDecodeBounds = false;
//            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//        bmp = null;
//    }

    Bitmap bmp = getUrlBitmap();
    if (bmp != null) {
        int THUMB_SIZE = 100;
        // 实例化
        String APP_ID = "wx9d31c3a4640b546f";
        IWXAPI wxApi = WXAPIFactory.createWXAPI(sdkJavaActivity, APP_ID);
        wxApi.registerApp(APP_ID);

        WXImageObject imgObj = new WXImageObject(bmp);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.thumbData = bmpToByteArray(thumbBmp, true);  // 设置缩略图

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;//发送到朋友圈
        //            req.scene = SendMessageToWX.Req.WXSceneSession;//发送给微信好友
        wxApi.sendReq(req);
    }
}



//private static Resources getResources() {
//    // TODO Auto-generated method stub
//    Resources mResources = null;
//    mResources = getResources();
//    return mResources;
//}
/*
 * 分享链接到微信朋友圈
 */
public static void wxSdkJavaShareWebToMoments() {
AppActivity.sdkToastLog("图片不能为空2222");
    int THUMB_SIZE = 100;
    // 实例化
    String APP_ID = "wx9d31c3a4640b546f";
    IWXAPI wxApi = WXAPIFactory.createWXAPI(sdkJavaActivity, APP_ID);
    wxApi.registerApp(APP_ID);

    WXWebpageObject webpage = new WXWebpageObject();
    webpage.webpageUrl = "https://www.baidu.com/";
    WXMediaMessage msg = new WXMediaMessage(webpage);
    msg.title = "你来嘛英雄官网";
    msg.description = "欢迎下载";

//    Bitmap bmp = getUrlBitmap();
//    if (bmp != null) {


//        BufferedInputStream bis=new BufferedInputStream(getAssets().open("res.Word.zhuangbei.png"));
//        Bitmap bmp = BitmapFactory.decodeStream(bis);





//    AssetManager am = AppActivity.getContext().getResources().getAssets();
////   assets/res/Word/zhuangbei.png
////        AssetManager am = getResources().getAssets();
////        InputStream is = am.open("assets/res/Word/zhuangbei.png");
//        try {
//            InputStream is = am.open("assets/res/Word/zhuangbei.png");
//        } catch (Exception e) {
//            e.printStackTrace();
//            AppActivity.sdkToastLog("图片不能为空333");
//        }
//
//        Bitmap bmp = BitmapFactory.decodeStream(is);
//        is.close();

        Bitmap bmp = BitmapFactory.decodeResource(AppActivity.getContext().getResources(), R.drawable.icon);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();

        msg.thumbData = bmpToByteArray(thumbBmp, true);
//    }else{
//        AppActivity.sdkToastLog("图片不能为空");
//    }

    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = buildTransaction("webpage");
    req.message = msg;
    req.scene = SendMessageToWX.Req.WXSceneTimeline;//发送到朋友圈
    wxApi.sendReq(req);
}

/**
 * 获取图片
 *
 * @return
 */
private static Bitmap getUrlBitmap() {
    try {
        URL url = new URL("http://b.hiphotos.baidu.com/image/pic/item/fd039245d688d43f76b17dd4781ed21b0ef43bf8.jpg");
        Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        if (bitmap.getByteCount() > 4096000) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = bitmap.getByteCount() / 4096000;//缩放比例
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
        }
        return bitmap;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

private static String buildTransaction(final String type) {
    return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
}

private static byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
    if (needRecycle) {
        bmp.recycle();
    }
    byte[] result = output.toByteArray();
    try {
        output.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}







}
