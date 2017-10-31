package com.twsz.opengl.render;

import android.opengl.GLES20;
import android.view.View;

import com.twsz.opengl.utils.ShaderUtils;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * desc: 圆柱体
 * 作者: zhouhaibo
 * create on 2017/10/31 17:30.
 */

public class Cylinder extends Shape {

    private int         mProgram;
    private FloatBuffer vertexBuffer;

    public Cylinder(View view) {
        super(view);
        vertexBuffer = createVertexCoords();
    }

    private FloatBuffer createVertexCoords() {
        return null;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        mProgram = ShaderUtils.createProgram(mView.getResources(), "vshader/Cone.sh", "fshader/Cone.sh");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        int vPositon = GLES20.glGetUniformLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPositon);
        GLES20.glVertexAttribPointer(vPositon, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glDisableVertexAttribArray(vPositon);
    }
}
