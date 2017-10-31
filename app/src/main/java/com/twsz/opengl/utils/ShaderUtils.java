package com.twsz.opengl.utils;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * desc: 说些什么
 * 作者: zhouhaibo
 * create on 2017/10/31 13:57.
 */

public class ShaderUtils {
    private static int createProgram(String vertexShader, String fragmentShader) {

        int shader  = loadShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        int shader1 = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, shader);
            GLES20.glAttachShader(program, shader1);
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e("ShaderUtils", "Could not link program:" + GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    private static int loadShader(int shaderType, String shaderCode) {
        int shader = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public static int createProgram(Resources resources, String vertexRes, String fragmentRes) {
        return createProgram(loadFromAssertsFile(vertexRes, resources), loadFromAssertsFile(fragmentRes, resources));
    }

    private static String loadFromAssertsFile(String fileName, Resources resources) {
        StringBuilder builder = new StringBuilder();
        try {
            InputStream open   = resources.getAssets().open(fileName);
            byte[]      buffer = new byte[1024];
            int         ch;
            while (-1 != (ch = open.read(buffer))) {
                builder.append(new String(buffer, 0, ch));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString().replaceAll("\\r\n", "\n");
    }
}
