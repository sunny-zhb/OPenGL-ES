package com.twsz.opengl.render;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * desc: 说些什么
 * 作者: zhouhaibo
 * create on 2017/10/26 15:00.
 */

public class FGLView extends GLSurfaceView {

    private FGLRender render;

    public FGLView(Context context) {
        this(context, null);
    }

    public FGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setRenderer(render = new FGLRender(this));
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void setShape(Class<? extends Shape> name) {
        render.setShape(name);
    }
}
