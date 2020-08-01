package com.ui.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.base.BaseActivity;
import com.ui.ks.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class BitmapUtils {
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
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

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public static Bitmap decodeFile(String path) {

        int orientation;

        try {

            if(path==null){

                return null;
            }
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 4;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            Bitmap bm = BitmapFactory.decodeFile(path,o2);


            Bitmap bitmap = bm;

            ExifInterface exif = new ExifInterface(path);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.e("orientation", "" + orientation);
            Matrix m=new Matrix();

            if((orientation==3)){

                m.postRotate(180);
                m.postScale((float)bm.getWidth(), (float)bm.getHeight());

//             if(m.preRotate(90)){

                bitmap = Bitmap.createBitmap(bm, 0, 0,bm.getWidth(),bm.getHeight(), m, true);
                return  bitmap;
            }
            else if(orientation==6){

                m.postRotate(90);

                bitmap = Bitmap.createBitmap(bm, 0, 0,bm.getWidth(),bm.getHeight(), m, true);
                return  bitmap;
            }

            else if(orientation==8){

                m.postRotate(270);

                bitmap = Bitmap.createBitmap(bm, 0, 0,bm.getWidth(),bm.getHeight(), m, true);
                return  bitmap;
            }
            return bitmap;
        }
        catch (Exception e) {
        }
        return null;
    }

    public static void savePic(final Context ctx, String picUrl, final String yourTitle) {
        final String saveFile = SysUtils.getPicSavePath(ctx, yourTitle);
        ImageRequest f = new ImageRequest(
                picUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        try {
                            File image = new File(saveFile);

                            if(image.exists()) {
                                image.delete();
                            }

                            //写到此文件
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            response.compress(Bitmap.CompressFormat.PNG, 0, bos);
                            byte[] bitmapdata = bos.toByteArray();

                            FileOutputStream out = new FileOutputStream(image);
                            out.write(bitmapdata);
                            out.close();

                            SysUtils.showSuccess(ctx.getString(R.string.str262));//图片已保存到本地相册中
                            SysUtils.addImageToGallery(saveFile, ctx);

                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                0,
                0,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        SysUtils.showError(ctx.getString(R.string.str263));
                    }
                }//图片下载失败
        );
        ((BaseActivity)ctx).executeRequest(f);
    }
    /**
     * 将二维码图片截图保存到本地相册
     * @throws Exception
     */
    public static void saveImage(Context mContext,View view) throws Exception {

        // SD卡保存路径

        String savePath = Environment.getExternalStorageDirectory()+"/"+String.valueOf(System.currentTimeMillis())+ "temp.png";


        saveMyBitmap(mContext,getBitmapFromRootView(view), savePath);

    }

    // 获取view并转换成bitmap图片

    private static Bitmap getBitmapFromRootView(View view) {



        view.setDrawingCacheEnabled(true);

        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());

        view.setDrawingCacheEnabled(false);

        if (bmp != null) {
            return bmp;

        } else {

            return null;

        }

    }


    // 把bitmao图片保存到对应的SD卡路径中

    private static void saveMyBitmap(Context mContext,Bitmap mBitmap, String path) throws Exception {

        File f = new File(path);

        f.createNewFile();

        FileOutputStream fOut = null;

        fOut = new FileOutputStream(f);

        if (mBitmap != null) {

            // 保存格式为PNG 质量为100

            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            /**
             * 通知相册及时更新显示新保存的图片
             */
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(f);
            intent.setData(uri);
            mContext.sendBroadcast(intent);

        }
        fOut.flush();
        fOut.close();

    }

    /**
     * dp转px
     * @param context
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px转pd
     * @param context
     * @param px
     * @return
     */
    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

}
