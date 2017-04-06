package com.lwx.user.debug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.elvishew.xlog.XLog;
import com.lwx.user.R;
import com.lwx.user.net.UserAgentImpl;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
      new Thread(new Runnable() {
          @Override
          public void run() {
              XLog.d(UserAgentImpl.getInstance().login("()", "()"));
          }
      }).start();
    }
}
