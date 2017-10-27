package com.twsz.opengl.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * desc: 说些什么
 * 作者: zhouhaibo
 * create on 2017/10/27 10:26.
 */

public class TriangleWithCamera extends Shape {

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;"+
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final int COORDS_PER_VERTEX = 3;

    /**
     * 顶点坐标
     */
    private static float triangleCoords[] = {
            0.5F, 0.5F, 0F, // 上顶点
            -0.5F, -0.5F, 0F, //  左
            0.5F, -0.5F, 0F // 右
    };
    /**
     * 顶点数量
     */
    //顶点个数
    private final  int   vertexCount      = triangleCoords.length / COORDS_PER_VERTEX;
    //顶点之间的偏移量
    private final  int   vertexStride     = COORDS_PER_VERTEX * 4; // 每个顶点四个字节
    private       FloatBuffer vertexBuffer;
    private final int         mProgram;


    private float[] mViewMatrix    = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix     = new float[16];
    private float[] color          = {1f, 1f, 1f, 1f};

    public TriangleWithCamera(View view) {
        super(view);
        ByteBuffer bb = ByteBuffer.allocateDirect(
                triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);



        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        //创建一个空的OpenGLES程序
        mProgram = GLES20.glCreateProgram();
        //将顶点着色器加入到程序
        GLES20.glAttachShader(mProgram, vertexShader);
        //将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram, fragmentShader);
        //连接到着色器程序
        GLES20.glLinkProgram(mProgram);


    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 计算宽高比
        float ratio = (float) width / height;
        // 计算透视阴影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        // 设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0F, 0F, 0, 0, 0, 1.0F, 0);
        // 计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 使用Program
        GLES20.glUseProgram(mProgram);
        // 获取变换矩阵的vMatrix句柄
        int vMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mMVPMatrix, 0);

        // 获取顶点vPosition的句柄
        int vPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        // 获取vColor的句柄
        int vColor = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(vColor, 1, color, 0);

        // 绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        GLES20.glDisableVertexAttribArray(vPosition);
    }
}
