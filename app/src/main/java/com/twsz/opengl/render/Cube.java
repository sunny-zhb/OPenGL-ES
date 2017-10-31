package com.twsz.opengl.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * desc: 说些什么
 * 作者: zhouhaibo
 * create on 2017/10/27 15:38.
 */

public class Cube extends Shape {

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;"+
                    "varying  vec4 vColor;"+
                    "attribute vec4 aColor;"+
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" +
                    "  vColor=aColor;"+
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private final static float[] squareCoords = {
            -1F, 1F, 1F,
            -1F, -1F, 1F,
            1F, -1F, 1F,
            1F, 1F, 1F,
            -1F, 1F, -1F,
            -1F, -1F, -1F,
            1F, -1F, -1F,
            1F, 1F, -1F,


    };

    static short index[] = {
            6, 7, 4, 6, 4, 5,
            6, 3, 7, 6, 2, 3,
            6, 5, 1, 6, 1, 2,
            0, 3, 2, 0, 2, 1,
            0, 1, 5, 0, 5, 4,
            0, 7, 3, 0, 4, 7

    };

    private final float color[] = {
            0F, 1.0F, 0F, 1F,
            0F, 1.0F, 0F, 1F,
            0F, 1.0F, 0F, 1F,
            0F, 1.0F, 0F, 1F,
            1.0F, 0F, 0F, 1F,
            1.0F, 0F, 0F, 1F,
            1.0F, 0F, 0F, 1F,
            1.0F, 0F, 0F, 1F,
    };
    private int         mProgram;
    private FloatBuffer mVertexBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer mColorBuffer;

    private float[] mViewMatrix    = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix     = new float[16];


    public Cube(View view) {
        super(view);
        mVertexBuffer = ByteBuffer.allocateDirect(squareCoords.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertexBuffer.put(squareCoords);
        mVertexBuffer.position(0);

        mColorBuffer = ByteBuffer.allocateDirect(color.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mColorBuffer.put(color);
        mColorBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(index.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        indexBuffer.put(index);
        indexBuffer.position(0);

        int vertexShader   = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        Matrix.setLookAtM(mViewMatrix, 0, 5.0F, 5.0F, 10.0F, 0, 0, 0, 0, 1F, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
        int vMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mMVPMatrix, 0);
        int vPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);

        int vColor = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(vColor);
        GLES20.glVertexAttribPointer(vColor, 4, GLES20.GL_FLOAT, false, 0, mColorBuffer);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        GLES20.glDisableVertexAttribArray(vPosition);
    }
}

