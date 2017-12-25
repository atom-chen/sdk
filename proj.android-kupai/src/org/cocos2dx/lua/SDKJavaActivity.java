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
import org.json.JSONException;
import org.json.JSONObject;

import com.coolcloud.uac.android.api.Coolcloud;
import com.coolcloud.uac.android.api.ErrInfo;
import com.coolcloud.uac.android.api.OnResultListener;
import com.coolcloud.uac.android.common.Constants;
import com.coolcloud.uac.android.common.Params;
import com.coolcloud.uac.android.gameassistplug.GameAssistApi;
import com.coolcloud.uac.android.gameassistplug.GameAssistConfig;
import com.yulong.android.paysdk.api.CoolpayApi;
import com.yulong.android.paysdk.base.IPayResult;
import com.yulong.android.paysdk.base.common.CoolPayResult;
import com.yulong.android.paysdk.base.common.CoolYunAccessInfo;
import com.yulong.android.paysdk.base.common.PayInfo;

import android.os.AsyncTask;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import android.telephony.TelephonyManager;


//微信
import java.net.URL;
import java.lang.Exception;
import com.aw.dc.nlmyx.coolpad.R;
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
    private static Coolcloud coolcloud = null;
    private static CoolpayApi api = null;
    private static String appid = "5000008689";
    private static String appKey = "35a79ced642142ca964500a5f8508f96";
    private static String payKey = "NDc2RTg1MjY0N0JDNzE1OEM3MjdFNUMyQTE1NUY3NURCNTkzNEQ3OU1UWTNNVGs0TmpjME1qTTVOek0xTXpZM01qY3JNVGsyTnpreU1qWTFOREExT0RZNE1EQTBPVFF3TVRFME5EUTNOVFV4TXprME9UY3lOamd6";
    
    private static String openId = null;
    private static String accessToken = null;
    private static int rtnCode = -1;
    private static int pay_orientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER;
    private static int pay_style = CoolpayApi.PAY_STYLE_ACTIVITY;

    private static final int PLATFROM = 5;
    private static final int USERTYPE = 15;
    protected static final boolean isDebug = false;//是否是测试模式
    
    //小米登陆参数
    

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
        //定义成员变量 private Coolcloud coolcloud = null;
        //在 Activity 的 onCreate 方法中创建成员变量实例对象 
        coolcloud = Coolcloud.get(sdkJavaActivity, appid);
    }


/**
     * 若获取到悬浮窗权限，开启小助手悬浮窗
     */
    protected static void initGameAssist(){
        GameAssistConfig mGameAssistConfig = new GameAssistConfig();
        mGameAssistConfig.setHideGift(true);

            GameAssistApi mGameAssistApi = (GameAssistApi) coolcloud.getGameAssistApi(sdkJavaActivity, mGameAssistConfig);
            mGameAssistApi.addOnSwitchingAccountListen(new GameAssistApi.SwitchingAccount() {
                        @Override
                        public void onSwitchingAccounts() { // 重要
                            //切换账号
                            doSwitchAccount();
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

sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {

                //此时已在主线程中，可以更新UI了
                       // 调用SDK执行登陆操作
            if (null != coolcloud) {
                initGameAssist();

                    // showProcessDialog();
                final Bundle input = new Bundle();
                    // 设置横屏显示
                input.putInt(Constants.KEY_SCREEN_ORIENTATION,ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
                input.putString(Constants.KEY_SCOPE, "get_basic_userinfo");
                    // 设置登录方式，这里采用新建账户登录
                input.putString(Constants.KEY_RESPONSE_TYPE,Constants.RESPONSE_TYPE_CODE);



            sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
                //此时已在主线程中，可以更新UI了

                       // 调用SDK执行登陆操作             
                coolcloud.login(sdkJavaActivity, input, new Handler(),new OnResultListener() {
                    @Override
                    public void onResult(Bundle result) { //返回成功,获取授权码
                        String code = result.getString(Params.KEY_AUTHCODE);
                        // new LoginTask().execute(code);
                        AppActivity.sdkToastLog("kiki "+"code[Login]:" + code);
                        AppActivity.sdkLoginCallBack("", USERTYPE, PLATFROM,code);  
                    }
                    @Override
                    public void onError(ErrInfo error) { //出现错误,通过error.getError()和error.getMessage()获取错误信息
                        AppActivity.sdkToastLog("zqll "+"error:" + error.getMessage());
                    }
                    @Override
                    public void onCancel() { //操作被取消
                    } 
                });


            }
            });





            } else {
                AppActivity.sdkToastLog("ssqq "+"coolcloudApi is null");
            }   


            }
            });            


}



//提交游戏角色信息 必须调用
// zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip
public static void sdkJavaSubmitData(final String submitDatas) {
       String[] info = submitDatas.split("\\$");

}

    //切换账号 必须调用
    public static void sdkJavaLogout() {
        doSwitchAccount();
    }


/**
     * 切换账号 飘浮窗和游戏中的切换账号都在这里边实现
     */
    private static void doSwitchAccount() {
        final Bundle mInput = new Bundle();
        //设置屏幕横竖屏默认为竖屏
        int screen = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT; 
        mInput.putInt(Constants.KEY_SCREEN_ORIENTATION, screen);
        //设置需要权限 一般都为get_basic_userinfo这个常量
        mInput.putString(Constants.KEY_SCOPE, "get_basic_userinfo");
        //获取类型为AuthCode
        mInput.putString(Constants.KEY_RESPONSE_TYPE, Constants.RESPONSE_TYPE_CODE); 


        sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了
                      

                coolcloud.loginNew(sdkJavaActivity, mInput, new Handler(), new OnResultListener() {
            @Override
            public void onResult(Bundle result) {
                // 返回成功,获取授权码
                String code = result.getString(Params.KEY_AUTHCODE);
                AppActivity.sdkToastLog("code "+code);
                AppActivity.sdkLoginCallBack("", USERTYPE, PLATFROM,code);
                // new LoginTask().execute(code);
            }
            @Override
            public void onError(ErrInfo s) { //登录失败,通过error.getError()和error.getMessage()获取错误信息
                AppActivity.sdkToastLog("zqll "+"error:" + s.getMessage());
            }
            @Override
            public void onCancel() { // 登录被取消
            } });


            }
        });


        
    }



    //退出游戏 必须调用
    public static void sdkJavaExit() {
        sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
                //此时已在主线程中，可以更新UI了
                       // 调用SDK执行登陆操作             
                coolcloud.logout(sdkJavaActivity); // 退出账号,返回为空 操作必须在线程中调用
            }
        });
        System.exit(0);
    }


/**
     * 成功0；失败-1
     */
    private static IPayResult payResult = new IPayResult() {
        @Override
        public void onResult(CoolPayResult result) {
            if (null != result) {
                String resultStr = result.getResult();
                AppActivity.sdkToastLog(resultStr + "[" + result.getResultStatus() + "]");
            }
        }
    };

@Override
    protected void onActivityResult(int requestCode, int resultCode,
            android.content.Intent data) {
        if (null != api) {
            api.onPayResult(requestCode, resultCode, data);
        }
    };

    //支付 必须调用 userID.."$"..payid.."$"..costMoney.."$"..url.."$"..ip.."$"..port.."$"..signUrl.."$"..produceID.."$"..produceName.."$"..ServerIndex.."$"..myDiamond.."$"..xValue
    public static void sdkJavaPay(final String payDatas) {
        // userID.."$"..payid.."$"..costMoney.."$"..url
        String[] payData = payDatas.split("\\$");
        String userId=payData[0];
        String payid = payData[1];
        String costMoney=payData[2];
        String produceName=payData[8];
        String payUrl=payData[9];
        String ip = payData[4];
        String port = payData[5];
        String produceID = payData[7];
        String myDiamond = payData[10];
        String openId = payData[11];
        String accessToken = payData[12];
        String sort = payData[13];

// AppActivity.sdkToastLog(openId);
// AppActivity.sdkToastLog(accessToken);
        //初始化
        api = CoolpayApi.createCoolpayApi(sdkJavaActivity, appid);
        //设置酷云信息
        final CoolYunAccessInfo accountInfo = new CoolYunAccessInfo();
        accountInfo.setAccessToken(accessToken);//accessToken
        accountInfo.setOpenId(openId);//openId

        //设置商品信息
        final PayInfo payInfo = new PayInfo();
        int price = Integer.parseInt(costMoney);
        payInfo.setPrice(price*100);// 支付价格,单位为分
        // payInfo.setPrice(1);// 支付价格,单位为分
        payInfo.setAppId(appid);//设置appId
        payInfo.setPayKey(payKey);//设置paykey
        payInfo.setName(produceName);
        payInfo.setPoint(Integer.parseInt(sort));  //设置商品编号 计费点（商品的）ID
        //设置商品数量（当前不支持多数量支付，请设置为1）  
        payInfo.setQuantity(1);

        //设置CP透传信息（如果没有，可以不设置）
        payInfo.setCpPrivate(produceID);
        // 设置CP的订单号（如果没有可以不设置）
        payInfo.setCpOrder(payid);
        //设置订单有效期
        payInfo.setValidityPeriod(18);
        //调用支付接口      
        sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了


                sdkJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
                                    @Override
                                    public void run() {
                                            api.startPay(sdkJavaActivity, payInfo, accountInfo, payResult,pay_style, pay_orientation);

                                    }
                                  });

                             
            }
        });  
        

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

        Bitmap bmp = BitmapFactory.decodeResource(AppActivity.getContext().getResources(), R.drawable.icon);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();

        msg.thumbData = bmpToByteArray(thumbBmp, true);


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
