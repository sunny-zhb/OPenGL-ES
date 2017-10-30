package com.twsz.opengl.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * desc: 说些什么
 * 作者: zhouhaibo
 * create on 2017/10/27 15:38.
 */

public class Square extends Shape {
    private final String vertexShaderCode   =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "void main(){" +
                    "gl_Position = vPosition *vMatrix;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main(){" +
                    "gl_FragColor = vColor;" +
                    "}";

    static final         int     COORDS_PER_VERTEX = 3;
    static final         int     vertexStride      = COORDS_PER_VERTEX * 4;
    private final static float[] squareCoords      = {
            -0.5F, 0.5F, 0,
            -0.5F, -0.5F, 0,
            0.5F, -0.5F, 0,
            0.5F, 0.5F, 0,
    };

    static short index[] = {
            0, 1, 2, 0, 2, 3
    };

    private final float color[] = {
            1.0F, 0F, 0F, 1F
    };
    private Buffer mVertexBuffer;
    private int    mProgram;
    private Buffer indexBuffer;


    private float[] mViewMatrix    = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix     = new float[16];

    public Square(View view) {
        super(view);
        mVertexBuffer = ByteBuffer.allocateDirect(squareCoords.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(squareCoords).position(0);

        indexBuffer = ByteBuffer.allocateDirect(index.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(index)
                .position(0);

        int vertexShader   = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0F, 0, 0, 0, 0, 1F, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
        int vMatrix         = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(vMatrix,1,false,mMVPMatrix,0);
        int vPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, mVertexBuffer);

        int vColor = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(vColor, 1, color, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        GLES20.glDisableVertexAttribArray(vPosition);
    }
}
