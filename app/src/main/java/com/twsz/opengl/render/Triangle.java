package com.twsz.opengl.render;

import android.opengl.GLES20;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * desc: 三角形
 * 作者: zhouhaibo
 * create on 2017/10/26 15:13.
 */

public class Triangle extends Shape {

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    /**
     * 顶点数量
     */
    static final  int   COORDS_PER_VERTEX = 3;
    /**
     * 顶点坐标
     */
    private static        float triangleCoords[]  = {
            0.5F, 0.5F, 0F, // 上顶点
            -0.5F, -0.5F, 0F, // 右
            0.5F, -0.5F, 0F //  左
    };
    /**
     * 顶点数量
     */
    private       int   vertexCount       = triangleCoords.length / COORDS_PER_VERTEX;
    /**
     * 顶点偏移量
     */
    private final int   vertexStride      = COORDS_PER_VERTEX * 4;// 每个顶点四byte
    /**
     * 颜色 依次为RGBA
     */
    private       float color[]           = {1.0F, 1.0F, 1.0F, 1.0F};
    private final FloatBuffer mVertexBuffer;
    private final int         mProgram;

    public Triangle(View view) {
        super(view);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length * 4).order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put(triangleCoords);
        mVertexBuffer.position(0);
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

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 将程序加载到OpenGL的环境中
        GLES20.glUseProgram(mProgram);
        // 获取顶点着色器成员vPosition的句柄
        int vPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // 起用顶点着色器的句柄
        GLES20.glEnableVertexAttribArray(vPosition);
        // 准备三角形顶点数据
        GLES20.glVertexAttribPointer(vPosition, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, mVertexBuffer);

        //获取片元着色器成员vColor的句柄
        int vColor = GLES20.glGetUniformLocation(mProgram, "vColor");
        // 设置颜色数据
        GLES20.glUniform4fv(vColor, 1, color, 0);
        // 绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        // 禁止顶点句柄
        GLES20.glDisableVertexAttribArray(vPosition);


    }
}
