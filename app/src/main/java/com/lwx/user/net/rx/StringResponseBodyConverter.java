package com.lwx.user.net.rx;

import com.elvishew.xlog.XLog;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by henry on 17-4-6.
 */

public class StringResponseBodyConverter implements Converter<ResponseBody, String> {
    @Override
    public String convert(ResponseBody value) throws IOException {
        try {
            String ans = value.string();
            XLog.v("HTTP返回:" + ans);
            return ans;
        } finally {
            value.close();
        }
    }
}