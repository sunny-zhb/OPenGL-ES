package com.twsz.opengl.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * desc: 说些什么
 * 作者: zhouhaibo
 * create on 2017/10/30 10:19.
 */

public class Oval extends Shape {
    private final static String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "void main(){" +
                    "gl_Position=vPosition*vMatrix;" +
                    "}";

    private final static String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main(){" +
                    "gl_FragColor= vColor;" +
                    "}";
    private              float  height             = 0.0f; // z坐标

    private float radius = 1.0f; //半径
    private int   n      = 360; // 切割360份
    private float[] vertexCoords;
    private Buffer  vertexBufer;
    private int     mProgram;

    static final int     COORDS_PER_VERTEX = 3;
    private      float[] color             = {1.0F, 1.0F, 1.0F, 1.0F};

    private float[] mViewMatrix    = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix     = new float[16];


    public Oval(View view) {
        this(view, 0F);
    }

    public Oval(View view, float height) {
        super(view);
        this.height = height;
        vertexCoords = createVertexCoords();

        vertexBufer = ByteBuffer.allocateDirect(vertexCoords.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexCoords)
                .position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragShader   = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragShader);
        GLES20.glLinkProgram(mProgram);

    }
    public void setMVPMatrix(float[] MVPMatrix) {
        mMVPMatrix = MVPMatrix;
    }

    private float[] createVertexCoords() {
        ArrayList<Float> floats = new ArrayList<>();
        floats.add(0f);
        floats.add(0f);
        floats.add(height);
        float angDegSpan = 360F / n;
        for (int i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            floats.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            floats.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            floats.add(height);
        }
        float[] floats1 = new float[floats.size()];
        for (int i = 0; i < floats1.length; i++) {
            floats1[i] = floats.get(i);
        }
        return floats1;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0F, 0, 0, 0, 0, 1.0F, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
        int vMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mMVPMatrix, 0);

        int vPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertexBufer);

        int vColor = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(vColor, 1, color, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCoords.length / 3);

        GLES20.glDisableVertexAttribArray(vPosition);
    }
}
