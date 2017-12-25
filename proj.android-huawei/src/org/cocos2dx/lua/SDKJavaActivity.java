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

//华为
import com.huawei.gameservice.sdk.GameServiceSDK;
import com.huawei.gameservice.sdk.control.GameEventHandler;
import com.huawei.gameservice.sdk.model.Result;
import com.huawei.gameservice.sdk.model.RoleInfo;
import com.huawei.gameservice.sdk.model.UserResult;
import com.huawei.gameservice.sdk.util.LogUtil;
import com.huawei.gameservice.sdk.util.StringUtil;
import com.huawei.gameservice.sdk.view.GameServiceBaseActivity;
import com.huawei.gb.huawei.net.ReqTask;
import java.util.regex.Pattern;

import com.huawei.gameservice.sdk.model.PayResult;
import android.telephony.TelephonyManager;

import java.util.HashMap;
import java.util.Map;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.res.Configuration;

import com.huawei.gb.huawei.net.ReqTask.Delegate;

//微信
import java.net.URL;
import java.lang.Exception;
import com.nlmyx.hw.huawei.R;
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
    
    private static final int PLATFROM = 5;
    private static final int USERTYPE = 11;
    protected static final boolean isDebug = true;//是否是测试模式

    private static long uid;
    
    private static int RELOAD_BUTTON = 1;
    
    private static int HIDE_BUTTON = 2;

    private static boolean initSDKSuccess=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdkJavaActivity = this;
        //ucSDK(aliSDK) onCreate 开始
        initSDK(1);
        //ucSDK(aliSDK) onCreate 结束
    }

    //游戏初始化 必须调用
    public static void sdkJavaInit() {
        sdkJavaLogin();
    }

    //登录游戏 必须调用
    public static void sdkJavaLogin() {
        // 调用SDK执行登陆操作
        // initSDK();
        if (initSDKSuccess){
            login(1);
            checkUpdate();
        }else{
            initSDK(2);
        }
    }

//得到sdkJavaActivity

     public static SDKJavaActivity getSDKJavaActivity(){

        return sdkJavaActivity;
     }


/**
 * 初始化 initialization
 */
private static void initSDK(final int data) {
GameServiceSDK.init(sdkJavaActivity, GlobalParam.APP_ID, GlobalParam.PAY_ID,
null, new GameEventHandler() {

    @Override
    public void onResult(Result result) {
        AppActivity.sdkToastLog("初始化 "+result.rtnCode);
        if (result.rtnCode != Result.RESULT_OK) {
            return;
        }
        initSDKSuccess=true;
        if (data==2){
            login(1);
            checkUpdate();
        }
        // login(1);
        // checkUpdate();
    }

    @Override
    public String getGameSign(String appId, String cpId, String ts) {
        // AppActivity.sdkToastLog("校验="+createGameSign(appId + cpId + ts));
        return createGameSign(appId + cpId + ts);
    }

    });
}
    
/**
 * 生成游戏签名 generate the game sign
*/
private static String createGameSign(String data) {
// 为了安全把浮标密钥放到服务端,并使用https的方式获取下来存储到内存中,CP可以使用自己 的安全方式处理
// For safety, buoy key put into the server and use the https way to get
// down into the client's memory.
// By the way CP can also use their safe approach.
    String str = data;
    try {
        String result = RSAUtil.sha256WithRsa(str.getBytes("UTF-8"), GlobalParam.BUOY_SECRET);
        return result;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

/**
 * 帐号登录 Login 
 *是否在收到登录完成通知接口（onResult回调）后判断登录成功就允许进入游戏，而非在收到鉴权签名校验通知后才允许用户进入游戏
 *用户在游戏内切换小号后，游戏是否返回首页重新登录后进入游戏
*   用户在游戏内支付时，如果账号异常，SDK会返回200102错误码，游戏需要提示用户重新登录才能继续完成支付
 */
private static void login(int authType) {
    GameServiceSDK.login(sdkJavaActivity, new GameEventHandler() {

        @Override
        public void onResult(Result result) {
            AppActivity.sdkToastLog("result.rtnCode："+result.rtnCode);
            if (result.rtnCode != Result.RESULT_OK) {
                // 提示用户登录失败
            } else {
                UserResult userResult = (UserResult) result;
                // 通知鉴权签名校验
                AppActivity.sdkToastLog("userResult.isAuth: "+userResult.isAuth+"  userResult.isChange: "+userResult.isChange);
                if (userResult.isAuth != null && userResult.isAuth == 1) {
                    boolean checkResult = checkSign(GlobalParam.APP_ID + userResult.ts + userResult.playerId,userResult.gameAuthSign);
                    if (checkResult) {

                        AppActivity.sdkLoginCallBack(userResult.playerId, USERTYPE, PLATFROM,userResult.gameAuthSign+"|"+userResult.ts);

                        AppActivity.sdkToastLog("校验签名登陆成功");

                    } else {
                        AppActivity.sdkToastLog("校验签名登陆失败 check game auth sign error");
                    }
                // 通知帐号变换
                } else if (userResult.isChange != null && userResult.isChange == 1) {
                    login(1);
                // 登录成功
                } else {
                    // AppActivity.sdkLoginCallBack(userResult.playerId, USERTYPE, PLATFROM,userResult.gameAuthSign+"|"+userResult.ts);
                    AppActivity.sdkToastLog("华为登陆成功-"+userResult.playerId+"-"+USERTYPE+"-"+PLATFROM+"-"+userResult.gameAuthSign+"|"+userResult.ts);
                }

            }
        }

        @Override
        public String getGameSign(String appId, String cpId, String ts) {
            return null;
        }

    }, authType);
}

/**
     * 校验签名 check the auth sign
     */
    protected static boolean checkSign(String data, String gameAuthSign) {

        /*
         * 建议CP获取签名后去游戏自己的服务器校验签名，公钥值请参考开发指导书5.1 登录鉴权签名的验签公钥
         */
        /*
         * The CP need to deployed a server for checking the sign.
         */
        try {
            return RSAUtil.verify(data.getBytes("UTF-8"), GlobalParam.LOGIN_RSA_PUBLIC, gameAuthSign);
        } catch (Exception e) {
            return false;
        }
    }

/**
 * 检测游戏更新 check the update for game
 */
private static void checkUpdate() {
    GameServiceSDK.checkUpdate(sdkJavaActivity, new GameEventHandler() {

        @Override
        public void onResult(Result result) {
            if (result.rtnCode != Result.RESULT_OK) {
                AppActivity.sdkToastLog("检测更新失败"+ result.rtnCode);
            }
        }

        @Override
        public String getGameSign(String appId, String cpId, String ts) {
            return null;
        }

    });
}


private static void addPlayerInfo(final String submitDatas) {
    HashMap<String, String> playerInfo = new HashMap<String, String>();

/**
 * 将用户的等级等信息保存起来，必须的参数为RoleInfo.GAME_RANK(等级)/RoleInfo.GAME_ROLE(角色名称)
 * /RoleInfo.GAME_AREA(角色所属区)/RoleInfo.GAME_SOCIATY(角色所属公会名称)
 * 全部使用String类型存放
 */
/**
 * the CP save the level, role, area and sociaty of the game player into
 * the SDK
 */
// zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip
String[] infoT = submitDatas.split("\\$");

playerInfo.put(RoleInfo.GAME_RANK, infoT[4]);
playerInfo.put(RoleInfo.GAME_ROLE, infoT[3]);
playerInfo.put(RoleInfo.GAME_AREA, infoT[0]);
playerInfo.put(RoleInfo.GAME_SOCIATY, "");
GameServiceSDK.addPlayerInfo(sdkJavaActivity, playerInfo, new GameEventHandler() {

    @Override
    public void onResult(Result result) {
        AppActivity.sdkToastLog("提交游戏角色信息"+result.rtnCode);
        if (result.rtnCode != Result.RESULT_OK) {
            AppActivity.sdkToastLog("提交游戏角色信息失败");
        }

    }

    @Override
    public String getGameSign(String appId, String cpId, String ts) {
        return null;
    }

});
}


@Override
protected void onResume() {
super.onResume();
// 在界面恢复的时候显示浮标,和onPause配合使用
    GameServiceSDK.showFloatWindow(this);
}
/**
 * 在界面暂停的时候可以隐藏窗口
 */
@Override
protected void onPause() {
super.onPause();
// 在界面暂停的时候,隐藏悬浮按钮,和onResume配合使用
    GameServiceSDK.hideFloatWindow(this);
}
/**
 * 在界面销毁的时候可以释放资源 */
@Override
protected void onDestroy() {
    super.onDestroy();
    // 在最后一个页面销毁的时候,销毁资源
    if(isTaskRoot()){
        GameServiceSDK.destroy(this);
    }
}
    


    //提交游戏角色信息 必须调用
    // zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip
    public static void sdkJavaSubmitData(final String submitDatas) {
        addPlayerInfo(submitDatas);
    }

    //切换账号 必须调用
    public static void sdkJavaLogout() {
        // login(1);
    }

    //退出游戏 必须调用
    public static void sdkJavaExit() {
        System.exit(0);
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



        String price = costMoney+".00"; // 支付金额，单位分
        // String price = "0.01"; // 支付金额，单位元
        String productName = costMoney+"0钻石";
        String productDesc = "少年我跟你讲这玩意很值钱";
        String requestId = payid;
        // String requestId ="abcde";

                //价格必须精确到小数点后两位，使用正则进行匹配
                //The price must be accurate to two decimal places
        boolean priceChceckRet = Pattern.matches("^\\d+[.]\\d{2}$", price);
        if (!priceChceckRet)
        { 
            return;
        }

        if ("".equals(productName))
        {
            return;
        }
                // 禁止输入：# " & / ? $ ^ *:) \ < > | , =
                // the name can not input characters: # " & / ? $ ^ *:) \ < > | , =
        else if (Pattern.matches(".*[#\\$\\^&*)=|\",/<>\\?:].*", productName))
        {
            return;
        }
        if ("".equals(productDesc))
        {
            return;
        }
                // 禁止输入：# " & / ? $ ^ *:) \ < > | , =
                // the description can not input characters: # " & / ? $ ^ *:) \ < > | , =
        else if (Pattern.matches(".*[#\\$\\^&*)=|\",/<>\\\\?\\^:].*", productDesc))
        {
            return;
        }

        //调用公共方法进行支付
        pay(sdkJavaActivity, price, productName, productDesc, requestId, payHandler);
    }


/**
 * 支付
 */
/**
     * 支付回调handler
     */
    /**
     * pay handler
     */
    private static GameEventHandler payHandler = new GameEventHandler()
    {
        @Override
        public String getGameSign(String appId, String cpId, String ts) {
            return null;
        }
        
        @Override
        public void onResult(Result result)
        {
 
            AppActivity.sdkToastLog("华为支付SDK "+result.rtnCode);
            Map<String, String> payResp = ((PayResult)result).getResultMap();
            // // String pay = getString(R.string.pay_result_failed);
            // // 支付成功，进行验签
            // // payment successful, then check the response value
            AppActivity.sdkToastLog("华为支付SDK returnCode"+payResp.get("returnCode"));
            AppActivity.sdkToastLog("华为支付SDK errMsg"+payResp.get("errMsg"));
            // if ("0".equals(payResp.get("returnCode")))
            // {
            //     if ("success".equals(payResp.get("errMsg")))
            //     {
            //         // 支付成功，验证信息的安全性；待验签字符串中如果有isCheckReturnCode参数且为yes，则去除isCheckReturnCode参数
            //         // If the response value contain the param "isCheckReturnCode" and its value is yes, then remove the param "isCheckReturnCode".
            //         if (payResp.containsKey("isCheckReturnCode") && "yes".equals(payResp.get("isCheckReturnCode")))
            //         {
            //             payResp.remove("isCheckReturnCode");
                        
            //         }
            //         // 支付成功，验证信息的安全性；待验签字符串中如果没有isCheckReturnCode参数活着不为yes，则去除isCheckReturnCode和returnCode参数
            //         // If the response value does not contain the param "isCheckReturnCode" and its value is yes, then remove the param "isCheckReturnCode".
            //         else
            //         {
            //             payResp.remove("isCheckReturnCode");
            //             payResp.remove("returnCode");
            //         }
            //         // 支付成功，验证信息的安全性；待验签字符串需要去除sign参数
            //         // remove the param "sign" from response
            //         String sign = payResp.remove("sign");
                    
            //         String noSigna = GameBoxUtil.getSignData(payResp);
                    
            //         // 使用公钥进行验签
            //         // check the sign using RSA public key
            //         boolean s = RSAUtil.doCheck(noSigna, sign, GlobalParam.PAY_RSA_PUBLIC);
                    
            //         if (s)
            //         {
            //             // pay = getString(R.string.pay_result_success);
            //         }
            //         else
            //         {
            //             // pay = getString(R.string.pay_result_check_sign_failed);
            //         }
            //     }
               
            // }
            // else if ("30002".equals(payResp.get("returnCode")))
            // {
            //     // pay = getString(R.string.pay_result_timeout);
            // }
            // // Toast.makeText(sdkJavaActivity, pay, Toast.LENGTH_SHORT).show();
            
            // // 重新生成订单号，订单编号不能重复，所以使用时间的方式，CP可以根据实际情况进行修改，最长30字符
            // // generate the pay ID using the date format, and it can not be repeated. 
            // // CP can generate the pay ID according to the actual situation, a maximum of 30 characters
            // // DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS", Locale.US);
            // // String requestId = format.format(new Date());
            // // ((TextView)findViewById(R.id.requestId)).setText(requestId);
            

        }
    };



public static void pay(
        final Activity activity,
        final String price,
        final String productName,
        final String productDesc,
        final String requestId,
        final GameEventHandler handler)
    {
        Map<String, String> params = new HashMap<String, String>();
        // 必填字段，不能为null或者""，请填写从联盟获取的支付ID
        // the pay ID is required and can not be null or "" 
        params.put(GlobalParam.PayParams.USER_ID, GlobalParam.PAY_ID);
        // 必填字段，不能为null或者""，请填写从联盟获取的应用ID
        // the APP ID is required and can not be null or "" 
        params.put(GlobalParam.PayParams.APPLICATION_ID, GlobalParam.APP_ID);
        // 必填字段，不能为null或者""，单位是元，精确到小数点后两位，如1.00
        // the amount (accurate to two decimal places) is required
        params.put(GlobalParam.PayParams.AMOUNT, price);
        // 必填字段，不能为null或者""，道具名称
        // the product name is required and can not be null or "" 
        params.put(GlobalParam.PayParams.PRODUCT_NAME, productName);
        // 必填字段，不能为null或者""，道具描述
        // the product description is required and can not be null or "" 
        params.put(GlobalParam.PayParams.PRODUCT_DESC, productDesc);
        // 必填字段，不能为null或者""，最长30字节，不能重复，否则订单会失败
        // the request ID is required and can not be null or "". Also it must be unique.
        params.put(GlobalParam.PayParams.REQUEST_ID, requestId);

        String noSign = getSignData(params);
        // AppActivity.sdkToastLog("startPay noSign：" + noSign);
        
        // CP必须把参数传递到服务端，在服务端进行签名，然后把sign传递下来使用；服务端签名的代码和客户端一致
        // the CP need to send the params to the server and sign the params on the server , 
        // then the server passes down the sign to client;
        String sign = RSAUtil.sign(noSign, GlobalParam.PAY_RSA_PRIVATE);
        // AppActivity.sdkToastLog("startPay sign： " + sign);


        AppActivity.sdkToastLog("华为支付2= "+GlobalParam.PAY_ID+" = "+GlobalParam.APP_ID+" = "+price+" = "+productName+" = "+productDesc+" = "+requestId);
        final Map<String, Object> payInfo = new HashMap<String, Object>();
        // 必填字段，不能为null或者""
        // the amount is required and can not be null or "" 
        payInfo.put(GlobalParam.PayParams.AMOUNT, price);
        // 必填字段，不能为null或者""
        // the product name is required and can not be null or ""
        payInfo.put(GlobalParam.PayParams.PRODUCT_NAME, productName);
        // 必填字段，不能为null或者""
        // the request ID is required and can not be null or ""
        payInfo.put(GlobalParam.PayParams.REQUEST_ID, requestId);
        // 必填字段，不能为null或者""
        // the product description is required and can not be null or ""
        payInfo.put(GlobalParam.PayParams.PRODUCT_DESC, productDesc);
        // 必填字段，不能为null或者""，请填写自己的公司名称
        // the user name is required and can not be null or "". Input the company name of CP.
        payInfo.put(GlobalParam.PayParams.USER_NAME, "青岛大卫骆驼网络科技有限公司");
        // 必填字段，不能为null或者""
        // the APP ID is required and can not be null or "". 
        payInfo.put(GlobalParam.PayParams.APPLICATION_ID, GlobalParam.APP_ID);
        // 必填字段，不能为null或者""
        // the user ID is required and can not be null or "". 
        payInfo.put(GlobalParam.PayParams.USER_ID, GlobalParam.PAY_ID);
        // 必填字段，不能为null或者""
        // the sign is required and can not be null or "".
        payInfo.put(GlobalParam.PayParams.SIGN, sign);
        // 选填字段，建议使用RSA256
        // recommended to use RSA256.
        payInfo.put(GlobalParam.PayParams.SIGN_TYPE, GlobalParam.SIGN_TYPE);
        

        payInfo.put(GlobalParam.PayParams.SCREENT_ORIENT,GlobalParam.PAY_ORI_LAND);
       
        AppActivity.sdkToastLog("开始支付SDK");
        // GameServiceSDK.startPay(sdkJavaActivity, payInfo, payHandler);
        sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了
                GameServiceSDK.startPay(sdkJavaActivity, payInfo, payHandler);                             

            }
        });
        AppActivity.sdkToastLog("结束支付SDK");
        
    }
    
    public static String getSignData(Map<String, String> params)
    {
      StringBuffer content = new StringBuffer();
      
      List keys = new ArrayList(params.keySet());
      Collections.sort(keys);
      for (int i = 0; i < keys.size(); i++)
      {
        String key = (String)keys.get(i);
        if (!"sign".equals(key))
        {
          String value = (String)params.get(key);
          if (value != null) {
            content.append((i == 0 ? "" : "&") + key + "=" + value);
          }
        }
      }
      return content.toString();
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
