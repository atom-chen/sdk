package org.cocos2dx.lua;
import org.cocos2dx.lib.Cocos2dxLuaJavaBridge;  

import android.os.Process;
import android.widget.Toast;

import com.hjr.sdkkit.framework.mw.openapi.callback.HJRSDKKitPlateformCallBack;



/**
 * <li>文件名称: PlatformSDKCallBack.java</li>
 * <li>文件描述: 回调接口示例代码</li>
 * <li>公    司: 快发助手</li>
 * <li>内容摘要: 无</li>
 * <li>新建日期: 2015-10-13 下午4:27:38</li>
 * <li>修改记录: 无</li>
 * @version 产品版本: 2.1.0
 * @author  作者姓名: HooRang
 */
public class PlatformSDKCallBack implements HJRSDKKitPlateformCallBack {
	
	/**
	 * **说明： 
	 * 每个接口的回调状态分为两种
	 * 
	 * 1.HJRSDKKitPlateformCallBack.STATUS_SUCCESS ： 成功
	 * 2.HJRSDKKitPlateformCallBack.STATUS_FAIL    ： 失败
	 * 
	 * 
	 * 
	 * 以下代码仅仅为回调处理的一个简单示例 ，具体每个接口的回调需要游戏方自行处理成功或失败的逻辑
	 * 
	 * 请仔细阅读在线接入文档http://www.kuaifazs.com
	 * 必接接口务必全部接入，愿你接入顺利！
	 * 
	 * 
	 */
	
	
	
	
	
	
	
	
	
	
	private SDKJavaActivity sCurrentActivity ;
	
	public PlatformSDKCallBack(SDKJavaActivity activity){
		
		sCurrentActivity = activity;
		
	}
	
	@Override
	public  void initCallBack(int retStatus, String retMessage) {
		AppActivity.sdkToastLog("初始化回调====");
		// Toast("initCallBack-->retStatus#" + retStatus + ",retMessage#" + retMessage);
		if (retStatus == HJRSDKKitPlateformCallBack.STATUS_SUCCESS) {
			//成功， 只有在sdk初始化成功之后才能调用sdk的登录
			SDKJavaActivity.hasInitSuccess = true; 
		}else {
			//失败
			// ViewHelper.gone(sCurrentActivity.btn_bussines_login);
		}
	}
	
	

	@Override
	public void exitGameCallBack(int retStatus, String retMessage) {
		// Toast("exitGameCallBack-->retStatus#" + retStatus + ",retMessage#" + retMessage);
		if (retStatus == HJRSDKKitPlateformCallBack.STATUS_SUCCESS) {
			// 退出提示框点击了确定，做资源回收，退出应用
			
			Process.killProcess(android.os.Process.myPid());
			
		}else {
			//点击了取消
		}
	}

	
	@Override
	public void loginCallBack(	String loginUserId, 
								String loginUserName,
								String loginAuthToken,
								final String loginOpenId,
								boolean switchUserFlag ,
								int retStatus,
								String retMessage) {

		AppActivity.sdkToastLog("登录回调=======");
		// Toast("loginCallBack-->retStatus#" + retStatus + ",retMessage#" 
		// 			+ retMessage+",loginUserId#"+loginUserId+",loginUserName#"
		// 			+loginUserName+",loginAuthToken#"+loginAuthToken+",loginOpenId#"+loginOpenId +",switchUserFlag#" + switchUserFlag);
		
//		 Toast(switchUserFlag ? "来自切换帐号登录" : "正常登录");
		
		if (retStatus == HJRSDKKitPlateformCallBack.STATUS_SUCCESS) {
			//登录成功 
			 
			// ViewHelper.gone(sCurrentActivity.btn_bussines_login,sCurrentActivity.exp_lv_tree);
			// ViewHelper.visible(sCurrentActivity.btn_data_enterGame);
			//调用游戏登录
			if (switchUserFlag) { //请注意，这里非常重要，如果在游戏中进行了切换帐号，需要游戏退出到刚刚登录完成的地方，如demo中，退出到“进入游戏”
					
					SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
				            @Override
				            public void run() {
				                //此时已在主线程中，可以更新UI了


				            	  SDKJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
						            @Override
						            public void run() {
						             		Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("goToLogin", "1"); 
											AppActivity.sdkToastLog("java调用lua======");
						           			AppActivity.sdkLoginCallBack( loginOpenId,5,5,"");

						            }
						          });

				                

				             }
       			 	});

					return ;

				}

			AppActivity.sdkToastLog("执行登录======");


			AppActivity.sdkLoginCallBack(loginOpenId,5,5,"");
			 AppActivity.sdkToastLog("调用sdkLoginCallBack");	

				
			
		}else {
			
			if (switchUserFlag) { 
				//切换帐号登录失败：游戏可以什么都不用处理，就像什么也没有发生一样，继续呆在游戏中
			}else {
				//正常登录失败：游戏这里必须做处理，可以做失败提示或者再次激活登录按钮让玩家可以再次发起登录操作
			}
		}
	}

	@Override
	public void logoutCallBack(int retStatus, String retMessage) {

		// Toast("logoutCallBack-->retStatus#" + retStatus + ",retMessage#" + retMessage);
		if (retStatus == HJRSDKKitPlateformCallBack.STATUS_SUCCESS) {
			//注销成功， 调用sdk的登录
				/** 
				* testAndroid lua中的方法名 
				* androidLua 传递给lua函数的参数值 
				*/  


				    SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
			            @Override
			            public void run() {
			                //此时已在主线程中，可以更新UI了


			            	  SDKJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
					            @Override
					            public void run() {
					             		Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("goToLogin", "parm"); 
										AppActivity.sdkToastLog("java调用lua======");
					           			AppActivity.sdkInitAndLogin();
					            }
					          });

			                

			         }
        });

				
				
			
		}
	}

	@Override
	public void payCallBack(String payKitOrderId, int retStatus,String retMessage) {
		// Toast("payCallBack-->retStatus#" + retStatus + ",retMessage#" + retMessage+",payKitOrderId#" + payKitOrderId);
			AppActivity.sdkToastLog("pay回调====");
		SDKJavaActivity.cacheOrderId = payKitOrderId;
		if (retStatus == HJRSDKKitPlateformCallBack.STATUS_SUCCESS) {
			//支付成功 ，确认到账后就可以调用sdk的支付统计接口：hjrSDK.Collections.onDatas(DataTypes.DATA_PAY, pc);
			Toast("充值成功");

		}else{
			
		}
	}

	@Override
	public void getOrderResultCallBack(String orderStatus,int retStatus, String retMessage) {
		Toast("getOrderResultCallBack-->retStatus#" + retStatus + ",retMessage#" + retMessage);
				AppActivity.sdkToastLog("order回调====");
		if (retStatus == HJRSDKKitPlateformCallBack.STATUS_SUCCESS) {
			/**
			 * orderStatus : 
			 * 0：整个充值流程已经成功走完;
			 * 4：标识充值成功但是添加道具失败；
			 * 否则表示失败
			 */
		}
	}


	private void Toast(String msg){
		Toast.makeText(sCurrentActivity, msg, Toast.LENGTH_SHORT).show();
		// sCurrentActivity.lbl_message.setText(msg);
	}

	@Override
	public void pushReceiveCallBack(int retStatus, String retMessage) {
		Toast("pushReceiveCallBack-->retStatus#" + retStatus + ",retMessage#" + retMessage);
	}


}


