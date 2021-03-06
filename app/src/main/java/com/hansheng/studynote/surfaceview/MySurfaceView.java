package com.hansheng.studynote.surfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hansheng.studynote.R;

/**
 * Created by hansheng on 2016/8/1.
 */
public class MySurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    /**
     * 当用户按键时调用
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        surfaceDestroyed(mHolder);
        return super.onKeyUp(keyCode, event);
    }

    private SurfaceHolder mHolder; // 用于控制SurfaceView
    private Thread t; // 声明一条线程
    private volatile boolean flag; // 线程运行的标识，用于控制线程
    private Canvas mCanvas; // 声明一张画布
    private Paint p; // 声明一支画笔
    float m_circle_r = 10;

    public MySurfaceView(Context context) {
        super(context);
        mHolder = getHolder(); // 获得SurfaceHolder对象
        mHolder.addCallback(this); // 为SurfaceView添加状态监听
        p = new Paint(); // 创建一个画笔对象
        p.setColor(Color.WHITE); // 设置画笔的颜色为白色
        setFocusable(true); // 设置焦点
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * 当SurfaceView创建的时候，调用此函数
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        t=new Thread(this);
        flag=true;
        t.start();
    }
    /**
     * 当SurfaceView的视图发生改变的时候，调用此函数
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }
    /**
     * 当SurfaceView销毁的时候，调用此函数
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag=false;
        mHolder.removeCallback(this);
    }
    /**
     * 当屏幕被触摸时调用
     */
    @Override
    public void run() {
        while (flag){
            synchronized (mHolder){
                try {
                    Thread.sleep(100);
                    Draw();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    if (mCanvas != null) {
                        // mHolder.unlockCanvasAndPost(mCanvas);//结束锁定画图，并提交改变。

                    }
                }

            }
        }
    }

    private void Draw() {
        mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画 mCanvas=mHolder.lockCanvas();
        if(mCanvas!=null){
            Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(10);
            paint.setStyle(Paint.Style.FILL);
            if (m_circle_r >= (getWidth() / 10)) {
                m_circle_r = 0;
            } else {
                m_circle_r++;
            }
            Bitmap pic = ((BitmapDrawable) getResources().getDrawable(
                    R.drawable.ic_launcher)).getBitmap();
            mCanvas.drawBitmap(pic, 0, 0, paint);
            for (int i = 0; i < 5; i++)
                for (int j = 0; j < 8; j++)
                    mCanvas.drawCircle(
                            (getWidth() / 5) * i + (getWidth() / 10),
                            (getHeight() / 8) * j + (getHeight() / 16),
                            m_circle_r, paint);
            mHolder.unlockCanvasAndPost(mCanvas); // 完成画画，把画布显示在屏幕上
        }
    }
}
