package com.badlogic.gdx.graphics.glutils;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.android.gms.cast.TextTrackStyle;

public class MipMapGenerator {
    private static boolean useHWMipMap;

    private MipMapGenerator() {
    }

    static {
        useHWMipMap = true;
    }

    public static void setUseHardwareMipMap(boolean useHWMipMap) {
        useHWMipMap = useHWMipMap;
    }

    public static void generateMipMap(Pixmap pixmap, int textureWidth, int textureHeight) {
        generateMipMap(GL20.GL_TEXTURE_2D, pixmap, textureWidth, textureHeight);
    }

    public static void generateMipMap(int target, Pixmap pixmap, int textureWidth, int textureHeight) {
        if (!useHWMipMap) {
            generateMipMapCPU(target, pixmap, textureWidth, textureHeight);
        } else if (Gdx.app.getType() != ApplicationType.Android && Gdx.app.getType() != ApplicationType.WebGL) {
            generateMipMapDesktop(target, pixmap, textureWidth, textureHeight);
        } else if (Gdx.graphics.isGL20Available()) {
            generateMipMapGLES20(target, pixmap);
        } else {
            generateMipMapCPU(target, pixmap, textureWidth, textureHeight);
        }
    }

    private static void generateMipMapGLES20(int target, Pixmap pixmap) {
        Gdx.gl.glTexImage2D(target, 0, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap.getHeight(), 0, pixmap.getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
        Gdx.gl20.glGenerateMipmap(target);
    }

    private static void generateMipMapDesktop(int target, Pixmap pixmap, int textureWidth, int textureHeight) {
        if (Gdx.graphics.isGL20Available() && (Gdx.graphics.supportsExtension("GL_ARB_framebuffer_object") || Gdx.graphics.supportsExtension("GL_EXT_framebuffer_object"))) {
            Gdx.gl.glTexImage2D(target, 0, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap.getHeight(), 0, pixmap.getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
            Gdx.gl20.glGenerateMipmap(target);
        } else if (!Gdx.graphics.supportsExtension("GL_SGIS_generate_mipmap")) {
            generateMipMapCPU(target, pixmap, textureWidth, textureHeight);
        } else if (Gdx.gl20 != null || textureWidth == textureHeight) {
            Gdx.gl.glTexParameterf(target, GL11.GL_GENERATE_MIPMAP, TextTrackStyle.DEFAULT_FONT_SCALE);
            Gdx.gl.glTexImage2D(target, 0, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap.getHeight(), 0, pixmap.getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
        } else {
            throw new GdxRuntimeException("texture width and height must be square when using mipmapping in OpenGL ES 1.x");
        }
    }

    private static void generateMipMapCPU(int target, Pixmap pixmap, int textureWidth, int textureHeight) {
        Gdx.gl.glTexImage2D(target, 0, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap.getHeight(), 0, pixmap.getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
        if (Gdx.gl20 != null || textureWidth == textureHeight) {
            int width = pixmap.getWidth() / 2;
            int height = pixmap.getHeight() / 2;
            int level = 1;
            Blending blending = Pixmap.getBlending();
            Pixmap.setBlending(Blending.None);
            while (width > 0 && height > 0) {
                Pixmap tmp = new Pixmap(width, height, pixmap.getFormat());
                tmp.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 0, 0, width, height);
                if (level > 1) {
                    pixmap.dispose();
                }
                pixmap = tmp;
                Gdx.gl.glTexImage2D(target, level, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap.getHeight(), 0, pixmap.getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
                width = pixmap.getWidth() / 2;
                height = pixmap.getHeight() / 2;
                level++;
            }
            Pixmap.setBlending(blending);
            return;
        }
        throw new GdxRuntimeException("texture width and height must be square when using mipmapping.");
    }
}
