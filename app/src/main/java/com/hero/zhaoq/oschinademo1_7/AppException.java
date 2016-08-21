package com.hero.zhaoq.oschinademo1_7;

import android.content.Context;
import android.widget.Toast;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/21  16:45
 * 应用程序异常类：用于捕获异常和提示错误信息
 */
public class AppException extends Exception implements Thread.UncaughtExceptionHandler {

    /** 定义异常类型 */
    public final static byte TYPE_NETWORK 	= 0x01;  //网络连接失败，请检查网络设置
    public final static byte TYPE_SOCKET	= 0x02;  //数据解析异常
    public final static byte TYPE_HTTP_CODE	= 0x03;  //
    public final static byte TYPE_HTTP_ERROR= 0x04;  //
    public final static byte TYPE_XML	 	= 0x05;  //
    public final static byte TYPE_IO	 	= 0x06;  //
    public final static byte TYPE_RUN	 	= 0x07;  //

    //获取类型和代码
    private byte type;
    private int code;

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
//        if(!handleException(ex) && mDefaultHandler != null) {
//            mDefaultHandler.uncaughtException(thread, ex);
//        }
    }

    /**
     * 提示友好的错误信息
     * @param ctx
     */
    public void makeToast(Context ctx){
        switch(this.getType()){
            case TYPE_HTTP_CODE:  //网络连接失败，请检查网络设置
                String err = ctx.getString(R.string.http_status_code_error, this.getCode());
                Toast.makeText(ctx, err, Toast.LENGTH_SHORT).show();
                break;
            case TYPE_HTTP_ERROR: //网络异常，请求超时
                Toast.makeText(ctx, R.string.http_exception_error, Toast.LENGTH_SHORT).show();
                break;
            case TYPE_SOCKET: //网络异常，读取数据超时
                Toast.makeText(ctx, R.string.socket_exception_error, Toast.LENGTH_SHORT).show();
                break;
            case TYPE_NETWORK://网络连接失败，请检查网络设置
                Toast.makeText(ctx, R.string.network_not_connected, Toast.LENGTH_SHORT).show();
                break;
            case TYPE_XML://数据解析异常
                Toast.makeText(ctx, R.string.xml_parser_failed, Toast.LENGTH_SHORT).show();
                break;
            case TYPE_IO://文件流异常
                Toast.makeText(ctx, R.string.io_exception_error, Toast.LENGTH_SHORT).show();
                break;
            case TYPE_RUN://应用程序运行时异常
                Toast.makeText(ctx, R.string.app_run_code_error, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public int getCode() {
        return code;
    }

    public byte getType() {
        return type;
    }
}
