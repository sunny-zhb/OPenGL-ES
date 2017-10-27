package com.twsz.opengl.render;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.View;

/**
 * desc: 说些什么
 * 作者: zhouhaibo
 * create on 2017/10/26 14:37.
 */

public abstract class Shape implements GLSurfaceView.Renderer {
    View mView;

    public Shape(View view) {
        mView = view;
    }

    int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
