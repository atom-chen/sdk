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

import com.xiaomi.gamecenter.sdk.MiCommplatform;
import com.xiaomi.gamecenter.sdk.MiErrorCode;
import com.xiaomi.gamecenter.sdk.OnExitListner;
import com.xiaomi.gamecenter.sdk.OnLoginProcessListener;
import com.xiaomi.gamecenter.sdk.entry.MiAccountInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppType;
import com.xiaomi.gamecenter.sdk.entry.ScreenOrientation;

import com.xiaomi.gamecenter.sdk.OnPayProcessListener;
import com.xiaomi.gamecenter.sdk.GameInfoField;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfoOnline;

import android.telephony.TelephonyManager;


//微信
import java.net.URL;
import java.lang.Exception;
import com.aw.dc.nlmyx.mi.R;
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

// ucSDK(aliSDK)头文件 结束


public class SDKJavaActivity extends Cocos2dxActivity implements OnLoginProcessListener,OnPayProcessListener{
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
    private static final int USERTYPE = 2;
    protected static final boolean isDebug = false;//是否是测试模式
    
    //小米登陆参数
    private MiAccountInfo accountInfo;
    private String session;
    private long uid;

    private Handler handler= new Handler()
        {
            public void handleMessage( Message msg )
                {
                    // AppActivity.sdkToastLog(""+msg.what);

//    TelephonyManager mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//    String imei = mTm.getDeviceId();
//    String phoneName = android.os.Build.MODEL ;
//    AppActivity.sdkToastLog(imei);
    
                    switch( msg.what )
                    {
                    case MiErrorCode.MI_XIAOMI_GAMECENTER_SUCCESS:
                        // String phoneName = android.os.Build.MODEL ;
                        // TelephonyManager mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                        // String imei = mTm.getDeviceId();
                        // AppActivity.sdkToastLog(imei);
                        AppActivity.sdkLoginCallBack(""+uid, USERTYPE, PLATFROM,"");
                        AppActivity.sdkToastLog("登陆成功");
                    break;
                    case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_LOGIN_FAIL:
                        // 登陆失败
//                        sdkJavaInit();
                        AppActivity.sdkToastLog("登陆失败");
                        break;
                    case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_CANCEL:
                        // 取消登录
                        AppActivity.sdkToastLog("取消登录");
                        break;
                    case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED:
                        // 登录操作正在进行中
                        AppActivity.sdkToastLog("登录操作正在进行中");
                        break;
                    default :
                        // 登录失败
                        AppActivity.sdkToastLog("登陆失败");
                        break;
                    }
                };
            };


    private Handler payHandler= new Handler()
    {
        public void handleMessage( Message msg )
        {
            switch( msg.what ) {
                case MiErrorCode.MI_XIAOMI_GAMECENTER_SUCCESS:
                AppActivity.sdkToastLog("购买成功");
                // 购买成功
            break;
                case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_CANCEL:
                AppActivity.sdkToastLog("取消购买");
                // 取消购买
            break;
                case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_FAILURE:
                AppActivity.sdkToastLog("购买失败");
                // 购买失败
            break;
                case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED:
                AppActivity.sdkToastLog("操作正在进行中");
                //操作正在进行中
            break;
            default :
                AppActivity.sdkToastLog("购买失败");
                // 购买失败
            break;
            }
        };
    };

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

        //初始化
        final MiAppInfo appInfo = new MiAppInfo();
        appInfo.setAppId("2882303761517616028");
        appInfo.setAppKey("5891761630028");
        appInfo.setAppType(MiAppType.online); // 网游
        appInfo.setOrientation(ScreenOrientation.horizontal);//设置sdk横竖屏

        sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了
                MiCommplatform.Init( sdkJavaActivity, appInfo );                          

            }
        });
    }

    @Override
    protected void onDestroy(){
        //ucSDK(aliSDK) onDestroy 开始
        //ucSDK(aliSDK) onDestroy 结束
        super.onDestroy();
    }

    //游戏初始化 必须调用
    public static void sdkJavaInit() {
        sdkJavaLogin();
    }

    //登录游戏 必须调用
    public static void sdkJavaLogin() {
        // 调用SDK执行登陆操作

        sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了
                MiCommplatform.getInstance().miLogin( sdkJavaActivity, sdkJavaActivity );                       

            }
        });
        
    }


    //小米登陆回调
    @Override
    public void finishLoginProcess(int code,MiAccountInfo arg1) {

        if ( MiErrorCode.MI_XIAOMI_PAYMENT_SUCCESS == code )
        {
            accountInfo = arg1;
            //获取用户的登陆后的 UID(即用户唯一标识)
            uid = arg1.getUid();
            //获取用户的登陆的 Session(请参考 3.3用户session验证接口)
            session = arg1.getSessionId();
            //请开发者完成将uid和session提交给开发者自己服务器进行session验证
            handler.sendEmptyMessage( code );
        }
        else if ( MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_ACTION_EXECUTED == code )
        {
            handler.sendEmptyMessage( code );
        }
        else
        {
            handler.sendEmptyMessage( code );
        }

    }


   private static String roleLevel="20";
   private static String vip = "vip0";
   private static String roleName = "";
//提交游戏角色信息 必须调用
// zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip
public static void sdkJavaSubmitData(final String submitDatas) {
       String[] info = submitDatas.split("\\$");
           roleLevel=info[4];
           vip=info[9];
           roleName=info[3];
}

    //切换账号 必须调用
    public static void sdkJavaLogout() {

    }

    //退出游戏 必须调用
    public static void sdkJavaExit() {
        System.exit(0);
    }



    //支付 必须调用 userID.."$"..payid.."$"..costMoney.."$"..url.."$"..ip.."$"..port.."$"..signUrl.."$"..produceID.."$"..produceName.."$"..ServerIndex.."$"..myDiamond
    public static void sdkJavaPay(final String payDatas) {
        // userID.."$"..payid.."$"..costMoney.."$"..url
        String[] payData = payDatas.split("\\$");
        String userId=payData[0];
        String payid = payData[1];
        String costMoney=payData[2];
        String payUrl=payData[9];
        String ip = payData[4];
        String port = payData[5];
        String produceID = payData[7];
        String myDiamond = payData[10];
        // Toast.makeText(sdkJavaActivity, userId, Toast.LENGTH_LONG).show();
//        doSdkPay(null,true,ProtocolConfigs.FUNC_CODE_WEIXIN_PAY,userId,payid,costMoney,payUrl,ip,port);
//AppActivity.sdkToastLog(""+payUrl);

        final MiBuyInfoOnline online = new MiBuyInfoOnline();
        online.setCpOrderId(payid); //订单号唯一(不为空)
        online.setCpUserInfo(payUrl+"|"+produceID); //此参数在用户支付成功后会透传给CP的服务器
        online.setMiBi(Integer.parseInt(costMoney)); //必须是大于1的整数, 10代表10米币,即10元人民币(不为空)

        //用户信息※必填※
        final Bundle mBundle = new Bundle();
        mBundle.putString( GameInfoField.GAME_USER_BALANCE, myDiamond );  //用户余额
        mBundle.putString( GameInfoField.GAME_USER_GAMER_VIP, vip );  //vip 等级
        mBundle.putString( GameInfoField.GAME_USER_LV, roleLevel );          //角色等级
        mBundle.putString( GameInfoField.GAME_USER_PARTY_NAME, "三国" );  //工会，帮派
        mBundle.putString( GameInfoField.GAME_USER_ROLE_NAME, roleName ); //角色名称
        mBundle.putString( GameInfoField.GAME_USER_ROLEID, ""+userId );   //角色id
        mBundle.putString( GameInfoField.GAME_USER_SERVER_NAME, ip+":"+port);  //所在服务器

        sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了
                MiCommplatform.getInstance().miUniPayOnline(sdkJavaActivity,online,mBundle,sdkJavaActivity);                          

            }
        });
        

    }

    //小米支付回调
    public void finishPayProcess(int code) {
        AppActivity.sdkToastLog(""+code);
        payHandler.sendEmptyMessage( code );
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
