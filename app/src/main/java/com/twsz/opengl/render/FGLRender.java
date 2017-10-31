package com.twsz.opengl.render;

import android.opengl.GLES20;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Constructor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * desc: 说些什么
 * 作者: zhouhaibo
 * create on 2017/10/26 15:01.
 */

public class FGLRender extends Shape {

    private Class<? extends Shape> clazz = Cone.class;

    private Shape mShape;

    public FGLRender(View view) {
        super(view);
    }

    public void setShape(Class<? extends Shape> shape) {
        this.clazz = shape;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i("zhb", "onSurfaceCreated");
        GLES20.glClearColor(0.5F, 0.5F, 0.5F, 0F);
        try {
            Constructor<? extends Shape> declaredConstructor = clazz.getDeclaredConstructor(View.class);
            declaredConstructor.setAccessible(true);
            mShape = declaredConstructor.newInstance(mView);
        } catch (Exception e) {
            mShape = new Oval(mView);
            e.printStackTrace();
        }
        mShape.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.i("zhb", "onSurfaceChanged");
        GLES20.glViewport(0, 0, width, height);
        mShape.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.i("zhb", "onDrawFrame");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mShape.onDrawFrame(gl);
    }
}
