package com.twsz.opengl.render;

import android.opengl.GLES20;
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
                    "void main(){" +
                    "gl_Position=vPosition;" +
                    "}";

    private final static String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main(){" +
                    "gl_Fragment= vColor;" +
                    "}";
    private              float  height             = 0.0f; // z坐标

    private float radius = 1.0f; //半径
    private int   n      = 360; // 切割360份
    private float[] vertexCoords;
    private Buffer  vertexBufer;
    private int     mProgram;

    static final int COORDS_PER_VERTEX = 3;

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

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);

        int vPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertexBufer);
    }
}
