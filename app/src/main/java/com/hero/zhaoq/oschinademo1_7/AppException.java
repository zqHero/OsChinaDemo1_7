package com.hero.zhaoq.oschinademo1_7;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.hero.zhaoq.oschinademo1_7.common.UIHelper;

import org.apache.commons.httpclient.HttpException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/21  16:45
 * 应用程序异常类：用于捕获异常和提示错误信息
 */
public class AppException extends Exception implements Thread.UncaughtExceptionHandler {

    private final static boolean Debug = false;//是否保存错误日志

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

    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private AppException(){
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    private AppException(byte type, int code, Exception excp) {
        super(excp);
        this.type = type;
        this.code = code;
        if(Debug){
            this.saveErrorLog(excp);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 自定义异常处理:收集错误信息&发送错误报告
     * @param ex
     * @return true:处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if(ex == null) {
            return false;
        }

        final Context context = AppManager.getAppManager().currentActivity();

        if(context == null) {
            return false;
        }

        final String crashReport = getCrashReport(context, ex);
        //显示异常信息&发送报告
        new Thread() {
            public void run() {
                Looper.prepare();
                UIHelper.sendAppCrashReport(context, crashReport);
                Looper.loop();
            }

        }.start();
        return true;
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

    /**
     * 异常
     * @param e
     * @return
     */
    public static AppException network(Exception e) {
        if(e instanceof UnknownHostException || e instanceof ConnectException){
            return new AppException(TYPE_NETWORK, 0, e);
        }
        else if(e instanceof HttpException){
            return http(e);
        }
        else if(e instanceof SocketException){
            return socket(e);
        }
        return http(e);
    }

    //http异常
    public static AppException http(Exception e) {
        return new AppException(TYPE_HTTP_ERROR, 0 ,e);
    }

    //socket异常
    public static AppException socket(Exception e) {
        return new AppException(TYPE_SOCKET, 0 ,e);
    }

    /**
     * 保存异常日志
     * @param excp
     */
    public void saveErrorLog(Exception excp) {
        String errorlog = "errorlog.txt";
        String savePath = "";
        String logFilePath = "";
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            //判断是否挂载了SD卡
            String storageState = Environment.getExternalStorageState();
            if(storageState.equals(Environment.MEDIA_MOUNTED)){
                savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/OSChina/Log/";
                File file = new File(savePath);
                if(!file.exists()){
                    file.mkdirs();
                }
                logFilePath = savePath + errorlog;
            }
            //没有挂载SD卡，无法写文件
            if(logFilePath == ""){
                return;
            }
            File logFile = new File(logFilePath);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            fw = new FileWriter(logFile,true);
            pw = new PrintWriter(fw);
            pw.println("--------------------"+(new Date().toLocaleString())+"---------------------");
            excp.printStackTrace(pw);
            pw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(pw != null){ pw.close(); }
            if(fw != null){ try { fw.close(); } catch (IOException e) { }}
        }
    }

    /**
     * 获取APP崩溃异常报告
     * @param ex
     * @return
     */
    private String getCrashReport(Context context, Throwable ex) {
        PackageInfo pinfo = ((AppContext)context.getApplicationContext()).getPackageInfo();
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("Version: "+pinfo.versionName+"("+pinfo.versionCode+")\n");
        exceptionStr.append("Android: "+android.os.Build.VERSION.RELEASE+"("+android.os.Build.MODEL+")\n");
        exceptionStr.append("Exception: "+ex.getMessage()+"\n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionStr.append(elements[i].toString()+"\n");
        }
        return exceptionStr.toString();
    }
}
