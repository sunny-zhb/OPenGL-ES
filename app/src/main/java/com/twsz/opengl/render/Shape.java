package com.twsz.opengl.render;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
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

    int createProgram(String vertexShader, String fragmentShader) {
        int vertex   = loadShader(GLES20.GL_SHADER_TYPE, vertexShader);
        int fragment = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        int program  = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertex);
        GLES20.glAttachShader(program, fragment);
        GLES20.glLinkProgram(program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program,GLES20.GL_LINK_STATUS,linkStatus,0);
        if (linkStatus[0] !=GLES20.GL_TRUE){
            Log.e("Shape","Could not load program:"+GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteProgram(program);
            program=0;
        }
        return program;
    }
}
