/****************************************************************************
    类名：SDKJavaActivity 
    作用：《你来嘛英雄》游戏平台SDK Android接入类
    说明：1.所有Protected 和 Public修饰的成员变量和成员方法 不可以删除 必须复写 
         Private修饰的成员函数可以删除
         2.没有相应SDK方法的 对应方法可以为空实现
    时间：2017-01-20
    码农：zjd
    平台：360
****************************************************************************/
package org.cocos2dx.lua;
import org.cocos2dx.lib.Cocos2dxLuaJavaBridge;
import org.cocos2dx.lib.Cocos2dxActivity;
import android.content.Intent;
import android.widget.Toast;

// u360头文件 开始
import java.util.HashMap;
import java.util.Map;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.cocos2dx.lib.Cocos2dxActivity;




import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import android.util.Log;
import android.app.Activity;
import android.text.TextUtils;
import android.content.ClipboardManager;
import android.content.Context;



//快发导包
import com.hjr.sdkkit.framework.mw.entity.ParamsContainer;
import com.hjr.sdkkit.framework.mw.entity.ParamsKey;
import com.hjr.sdkkit.framework.mw.openapi.HJRSDKKitPlateformCore;
import android.content.res.Configuration;
import android.os.Process;
import android.view.KeyEvent;
import com.hjr.sdkkit.framework.mw.entity.DataTypes;

// ucSDK(aliSDK)头文件 结束


public class SDKJavaActivity extends Cocos2dxActivity{

    public static SDKJavaActivity sdkJavaActivity = null;
    
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
    private static final int PLATFROM = 5;
    private static final int USERTYPE = 5;
    protected static final boolean isDebug = true;//是否是测试模式

  
     // protected QihooUserInfo mQihooUserInfo;
    //快发全局变量
      static HJRSDKKitPlateformCore sdkObj = null;
      static String cacheOrderId = "";
  
     private DatasApi dataApi  = null ;
     public static boolean hasInitSuccess = false ;


     //得到sdkJavaActivity

     public static SDKJavaActivity getSDKJavaActivity(){

        return sdkJavaActivity;
     }
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdkJavaActivity = this;

        //SDK初始化，传入回调接口的实现类
      sdkObj = HJRSDKKitPlateformCore.initHJRPlateform(sdkJavaActivity,new PlatformSDKCallBack(sdkJavaActivity));
      dataApi = new DatasApi(sdkObj);  
      AppActivity.sdkToastLog("初始化");

   
    }
    

  
  //以下接口，无需做任何修改，拷贝进游戏的主Activity即可
  // ------------------------------生命周期函数 开始-------------------------
  // /
  @Override
  protected void onResume() {
    super.onResume();
    if (sdkObj != null) {
      sdkObj.LifeCycle.onResume();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (sdkObj != null) {
      sdkObj.LifeCycle.onPause();
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (sdkObj != null) {
      sdkObj.LifeCycle.onStop();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (sdkObj != null) {
      sdkObj.LifeCycle.onDestroy();
    }
    
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    if (sdkObj != null) {
      sdkObj.LifeCycle.onConfigurationChanged(newConfig);
    }
  }

  
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (sdkObj != null) {
      sdkObj.LifeCycle.onSaveInstanceState(outState);
    }
  }
  
  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (sdkObj != null) {
      sdkObj.LifeCycle.onNewIntent(intent);
    }
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // TODO Auto-generated method stub
    super.onActivityResult(requestCode, resultCode, data);
    if (sdkObj != null) {
      sdkObj.LifeCycle.onActivityResult(requestCode, resultCode, data);
    }
  }

  // /
  // ------------------------------生命周期函数 结束-------------------------
  // /
    

  


        /**
     * 退出
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
            AppActivity.sdkToastLog("调用返回键=====dispatchKeyEvent");

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {            
            //具体的操作代码
            AppActivity.sdkToastLog("调用退出=====dispatchKeyEvent");
             sdkObj.Base.exitGame(sdkJavaActivity);
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
      AppActivity.sdkToastLog("keyCode====="+keyCode);
      AppActivity.sdkToastLog("调用返回键=====onKeyDown");
      if (hasInitSuccess) {
        
        if (keyCode == KeyEvent.KEYCODE_BACK) {
         sdkObj.Base.exitGame(sdkJavaActivity);
        }
      }else {
        this.finish();
        Process.killProcess(Process.myPid());
      }
      return super.onKeyDown(keyCode, event);
  }

    
    
    
    /**
   * 是否是快速的连续点击按钮
   */
  private static long lastClickTime;
  public static boolean isFastDoubleClick() {
    long time =System.currentTimeMillis() - lastClickTime;
    if ((0L < time) && (time < 500L))
      return true;
    lastClickTime = time;
    return false;
  }
    
    
    


    //游戏初始化 必须调用
    public static void sdkJavaInit() {
            sdkJavaLogin();
            
    }

       


    //登录游戏 必须调用
    public static void sdkJavaLogin() {
          // TODO 调用登录接口
      
          sdkObj.User.login(sdkJavaActivity);
          AppActivity.sdkToastLog("登录=======");

    }

    //提交游戏角色信息 必须调用
    // zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip$partyid$partyname$partyroleid$partyrolename$friendlist
    public static void sdkJavaSubmitData(final String submitDatas) {
        AppActivity.sdkToastLog("调用用户信息=======");

        String[] submitData = submitDatas.split("\\$");
        AppActivity.sdkToastLog("KEY_SERVER_ID====="+submitData[0]);

        AppActivity.sdkToastLog("type====="+submitData[6]);

        //创建角色调用
        if ("createRole".equals(submitData[6])){
             AppActivity.sdkToastLog("发送createRole===");
            ParamsContainer pc = new ParamsContainer();
            // 角色标识
            pc.putString(ParamsKey.KEY_ROLE_ID, "1");
            // 角色昵称
            pc.putString(ParamsKey.KEY_ROLE_NAME, submitData[3]);
            // 服务器编号
            pc.putString(ParamsKey.KEY_SERVER_ID, submitData[0]);
            // 服务器名称
            pc.putString(ParamsKey.KEY_SERVER_NAME, submitData[1]);

            sdkObj.Collections.onDatas(DataTypes.DATA_CREATE_ROLE, pc);





            ParamsContainer pc1= new ParamsContainer();
          // 角色id
          pc1.putString(ParamsKey.KEY_ROLE_ID, submitData[2]);
          // 角色昵称
          pc1.putString(ParamsKey.KEY_ROLE_NAME, submitData[3]);
          // 角色等级
          pc1.putInt(ParamsKey.KEY_ROLE_LEVEL,Integer.valueOf(submitData[4]) );  
          // 服务器编号
          pc1.putString(ParamsKey.KEY_SERVER_ID, submitData[0]);
          // 服务器名称
          pc1.putString(ParamsKey.KEY_SERVER_NAME, submitData[1]);
          pc1.put(ParamsKey.KEY_ROLE_CREATETIME, 0L);
          pc1.put(ParamsKey.KEY_ROLE_UPGRADETIME, 0L);
          sdkObj.Collections.onDatas(DataTypes.DATA_ENTER_GAME, pc1);

        }

        if ("levelUp".equals(submitData[6])){
              AppActivity.sdkToastLog("发送levelUp===");

              ParamsContainer pc = new ParamsContainer();
              // 玩家升级后等级
              pc.putString(ParamsKey.KEY_ROLE_LEVEL, submitData[4]);
              /**
               * 角色创建时间
               * 1.需要发uc九游渠道则必传，long类型 时间戳，十位数，特别注意不能取系统当前时间，需要传服务器的角色创建时间，服务端需要保存该值
               *   详情可查看uc官方要求说明：http://bbs.9game.cn/thread-5370208-1-1.html
               * 2.不需要上uc九游渠道，则传0L
               */
              pc.put(ParamsKey.KEY_ROLE_CREATETIME, 0L);

              
              /**
               * 角色升级时间时间
               * 1.需要发uc九游渠道则必传，要求与以上
               * 2.不需要上uc九游渠道，则传0L
               */
              pc.put(ParamsKey.KEY_ROLE_UPGRADETIME, 0L);
              sdkObj.Collections.onDatas(DataTypes.DATA_ROLE_UPGRADE, pc);
        }

        if ( "enterServer".equals(submitData[6])){
            AppActivity.sdkToastLog("发送enterServer===");


          ParamsContainer pc = new ParamsContainer();
          // 角色id
          pc.putString(ParamsKey.KEY_ROLE_ID, submitData[2]);
          // 角色昵称
          pc.putString(ParamsKey.KEY_ROLE_NAME, submitData[3]);
          // 角色等级
          pc.putInt(ParamsKey.KEY_ROLE_LEVEL,Integer.valueOf(submitData[4]) );  
          // 服务器编号
          pc.putString(ParamsKey.KEY_SERVER_ID, submitData[0]);
          // 服务器名称
          pc.putString(ParamsKey.KEY_SERVER_NAME, submitData[1]);
          pc.put(ParamsKey.KEY_ROLE_CREATETIME, 0L);
          pc.put(ParamsKey.KEY_ROLE_UPGRADETIME, 0L);
          sdkObj.Collections.onDatas(DataTypes.DATA_ENTER_GAME, pc);
        }

        
    }

    //切换账号 必须调用
    public static void sdkJavaLogout() {

         AppActivity.sdkToastLog("调用切换账号===");
        sdkObj.User.logout();

    }



    //退出游戏 必须调用
    public static void sdkJavaExit() {
        sdkObj.Base.exitGame(sdkJavaActivity);
    }


 

        //支付 必须调用
    public static void sdkJavaPay(final String payDatas) {
                // userID.."$"..payid.."$"..costMoney.."$"..url
                  String[] payData = payDatas.split("\\$");
                  String  userId=payData[0];
                  String payid = payData[1];
                  String costMoney=payData[2];
                  String payUrl=payData[3];
                  String ip = payData[4];
                  String port = payData[5];
                  String produceID = payData[7];
                  String produceName = payData[8];
                 

                   AppActivity.sdkToastLog("costMoney===="+costMoney+"    payid====="+payid+"      produceName===="+produceName+"      produceID======"+produceID);

                  ParamsContainer pc = new ParamsContainer();
                  // 所购买商品金额, 以元为单位。
                  pc.putInt(ParamsKey.KEY_PAY_AMOUNT, Integer.valueOf(costMoney));
                  // 购买数量 ，通常都是1
                  pc.putInt(ParamsKey.KEY_PAY_PRODUCT_NUM, 1);
                  // 订单号， 没有传""
                  pc.putString(ParamsKey.KEY_PAY_ORDER_ID, payid);
                  //商品ID，请注意值一定是整型
                  pc.putInt(ParamsKey.KEY_PAY_PRODUCT_ID, Integer.valueOf(produceID));
                  // 所购买商品名称
                  pc.putString(ParamsKey.KEY_PAY_PRODUCT_NAME, produceName);
                  //商品描述
                  pc.putString(ParamsKey.KEY_PRODUCT_DESC, "");
                  // 扩展参数, 会作为透传给cp服务端，可以为""  
                  pc.putString(ParamsKey.KEY_EXTINFO, "");
                  
                  sdkObj.Pay.pay(pc);
                    // AppActivity.sdkToastLog("pay调用====");


         // Toast.makeText(sdkJavaActivity, userId, Toast.LENGTH_LONG).show();
        // doSdkPay(null,true,ProtocolConfigs.FUNC_CODE_WEIXIN_PAY,userId,payid,costMoney,payUrl,ip,port);

    }






   









  
    
}
