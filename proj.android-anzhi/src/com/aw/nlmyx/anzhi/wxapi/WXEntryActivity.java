package com.aw.nlmyx.anzhi.wxapi;



import java.io.IOException;
import java.io.InputStream;

import org.cocos2dx.lua.AppActivity;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;



import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;



public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    private static final String APP_ID = "wx9d31c3a4640b546f";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onResp(BaseResp resp) {
        Toast.makeText(this, "errCode = " + resp.errCode, Toast.LENGTH_LONG).show();
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //分享成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //分享拒绝
                break;
        }
        finish();
    }
    
    @Override
    public void onReq(BaseReq arg0) {
        
    }
}