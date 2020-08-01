package com.ui.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2016/12/9.
 */

public class UploadUtil {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10*1000;   //超时时间
    private static final String CHARSET = "UTF-8"; //设置编码
    private static Context context;
    /**
     * android上传文件到服务器
     * @param file  需要上传的文件
     * @param RequestURL  请求的rul
     * @return  返回响应的内容
     */
    public static String uploadFile(File file, String RequestURL,String strname){
        String  result = null;
        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--" , LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.connect();

            if(file!=null){
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream( conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的   比如:abc.png
                 */

                sb.append("Content-Disposition: form-data; name=\""+strname+"\"; filename=\""+file.getName()+"\""+LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[2048];
                int len = 0;
                while((len=is.read(bytes))!=-1){
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                final byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流
                 */
                BufferedInputStream bis = null;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[8 * 1024];
                int res = conn.getResponseCode();
                if(res==200){
                    bis=new BufferedInputStream(conn.getInputStream());
//                    InputStream input =  conn.getInputStream();
//                    StringBuffer sb1= new StringBuffer();
                    int ss ;
                    while((ss=bis.read(buffer))!=-1){
//                        sb1.append((char)ss);
                        baos.write(buffer,0,ss);
                        baos.flush();
                    }
                    result=new String(baos.toByteArray(), CHARSET);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * android上传文件到服务器
     */
    public static String uploadFile(File file, String url,String strname,Map<String,String> params){
        // 换行，或者说是回车
        //final String newLine = "\r\n";
        final String newLine = "\r\n";
        // 固定的前缀
        final String preFix = "--";
        //final String preFix = "";
        // 分界线，就是上面提到的boundary，可以是任意字符串，建议写长一点，这里简单的写了一个#
        final String bounDary = "----WebKitFormBoundaryCXRtmcVNK0H70msG";
        //final String bounDary = "";
        //请求返回内容
        String output = "";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--" , LINE_END = "\r\n";

        try {
            //统一资源定位符
            URL uploadFileUrl = new URL(url);
            //打开http链接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) uploadFileUrl.openConnection();
            //设置是否向httpURLConnection输出
            httpURLConnection.setDoOutput(true);
            //设置请求方法默认为get
            httpURLConnection.setRequestMethod("POST");
            //Post请求不能使用缓存
            httpURLConnection.setUseCaches(false);
            //设置token
//            httpURLConnection.setRequestProperty("authorization", (String) GlobalContext.getSessionAttribute("token"));
            //为web端请求
//            httpURLConnection.setRequestProperty("os", "web");
            //从新设置请求内容类型
            httpURLConnection.setReadTimeout(TIME_OUT);
            httpURLConnection.setConnectTimeout(TIME_OUT);
            httpURLConnection.setDoInput(true);  //允许输入流
            httpURLConnection.setDoOutput(true); //允许输出流
            httpURLConnection.setUseCaches(false);  //不允许使用缓存
            httpURLConnection.setRequestMethod("POST");  //请求方式
            httpURLConnection.setRequestProperty("Charset", CHARSET);  //设置编码
            httpURLConnection.setRequestProperty("connection", "keep-alive");
            httpURLConnection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());

            //设置DataOutputStream设置DataOutputStream数据输出流
            //OutputStream outputStream = httpURLConnection.getOutputStream();

            //上传普通文本文件
            if (params.size() != 0 && params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    //获取参数名称和值
                    String key = entry.getKey();
                    String value = params.get(key);
                    //向请求中写分割线
                    dos.writeBytes(PREFIX + BOUNDARY + LINE_END);
                    //向请求拼接参数
                    //String parm = key + "=" + URLEncoder.encode(value,"utf-8") +"\r\n" ;
                    dos.writeBytes("Content-Disposition: form-data; " + "name=\"" + key + "\"" + newLine);
                    //向请求中拼接空格
                    dos.writeBytes(LINE_END);
                    //写入值
                    dos.writeBytes(URLEncoder.encode(value, "utf-8"));
                    //dos.writeBytes(value);
                    //向请求中拼接空格
                    dos.writeBytes(LINE_END);
                }
            }

            //上传文件
            if (file != null && !params.isEmpty()) {
//                //向请求中写分割线
//                //把file装换成byte
//                File del = new File(file.toURI());
//                InputStream inputStream = new FileInputStream(del);
//                byte[] bytes= input2byte(inputStream);
//                String filePrams = "file";
//                String fileName = file.getName();
//                //向请求中加入分隔符号
//                dos.write((preFix + bounDary + newLine).getBytes());
//                //将byte写入
//                dos.writeBytes("Content-Disposition: form-data; name=\""+strname+"\"; filename=\""+file.getName()+"\""+newLine);
//                dos.writeBytes(newLine);
//                dos.write(bytes);
//                //向请求中拼接空格
//                dos.writeBytes(newLine);


                /**
                 * 当文件不为空，把文件包装并且上传
                 */
//                DataOutputStream dos = new DataOutputStream( conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的   比如:abc.png
                 */

                sb.append("Content-Disposition: form-data; name=\""+strname+"\"; filename=\""+file.getName()+"\""+LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[2048];
                int len = 0;
                while((len=is.read(bytes))!=-1){
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                final byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                dos.write(end_data);
//                dos.flush();

            }
            dos.writeBytes(preFix + bounDary + preFix + newLine);
            //请求完成后关闭流
            //得到相应码
            dos.flush();
            Log.d("print","打印出来的返回码为多少"+httpURLConnection.getResponseCode());
            //判断请求没有成功
            if (httpURLConnection.getResponseCode() != 200) {
                return "{result:'fail',response:'errorCode:" + httpURLConnection.getResponseCode() + "'}";
            }
            //判断请求成功
            if (httpURLConnection.getResponseCode() == 200) {
                //将服务器的数据转化返回到客户端
                BufferedInputStream bis = null;
                InputStream inputStream = httpURLConnection.getInputStream();
//                byte[] bytes = new byte[0];
//                bytes = new byte[inputStream.available()];
//                inputStream.read(bytes);
//                output = new String(bytes);
//                inputStream.close();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[8 * 1024];
                int res = httpURLConnection.getResponseCode();
                if(res==200){
                    bis=new BufferedInputStream(httpURLConnection.getInputStream());
//                    InputStream input =  conn.getInputStream();
//                    StringBuffer sb1= new StringBuffer();
                    int ss ;
                    while((ss=bis.read(buffer))!=-1){
//                        sb1.append((char)ss);
                        baos.write(buffer,0,ss);
                        baos.flush();
                    }
                    output=new String(baos.toByteArray(), CHARSET);
                }
            }
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "{result:'fail',response:'" + e.getMessage() + "'}";
        }
        return output;
    }

    /**
     * 将输入流转化成字节流
     * @param inStream
     * @return
     * @throws IOException
     */
    public static final byte[] input2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }


    //   / **
//            * 通过uri获取图片并进行压缩
//    *
//            * @param uri
//    */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 800f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 800) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    //存储进SD卡
    public static File saveFile(Bitmap bm, String fileName){
        File myCaptureFile = new File(fileName);
        try{
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
//100表示不进行压缩，70表示压缩率为30%
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        }catch (Exception e){
            System.out.println("e="+e.toString());
        }
        return myCaptureFile;
    }
    //    android获取sd卡路径方法：
    public static  String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        System.out.println("sdDir.getPath()="+sdDir.getPath());
        return sdDir.getPath();
    }
}
