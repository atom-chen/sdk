/*
 * File Name: LogoActivity.java 
 * History:
 * Created by Administrator on 2016年4月8日
 */
package org.cocos2dx.lua;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

// import org.cocos2dx.lua.R;
import com.aw.nlmyx.anzhi.R;

public class LogoActivity extends Activity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView logo = new ImageView(this);
        logo.setBackgroundResource(R.drawable.game_bg);
        setContentView(logo);
        mHandler.postDelayed(new Runnable() {

            public void run() {
                Intent intent = new Intent(LogoActivity.this, AppActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);

    }

}
