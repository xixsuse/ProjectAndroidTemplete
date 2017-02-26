package com.badlogic.gdx.graphics.g2d;

import com.google.android.gms.cast.TextTrackStyle;

public class PolygonRegion {
    final TextureRegion region;
    final float[] textureCoords;
    final short[] triangles;
    final float[] vertices;

    public PolygonRegion(TextureRegion region, float[] vertices, short[] triangles) {
        this.region = region;
        this.vertices = vertices;
        this.triangles = triangles;
        float[] textureCoords = new float[vertices.length];
        this.textureCoords = textureCoords;
        float u = region.f51u;
        float v = region.f52v;
        float uvWidth = region.u2 - u;
        float uvHeight = region.v2 - v;
        int width = region.regionWidth;
        int height = region.regionHeight;
        int i = 0;
        int n = vertices.length;
        while (i < n) {
            textureCoords[i] = ((vertices[i] / ((float) width)) * uvWidth) + u;
            i++;
            textureCoords[i] = ((TextTrackStyle.DEFAULT_FONT_SCALE - (vertices[i] / ((float) height))) * uvHeight) + v;
            i++;
        }
    }

    public float[] getVertices() {
        return this.vertices;
    }

    public short[] getTriangles() {
        return this.triangles;
    }

    public float[] getTextureCoords() {
        return this.textureCoords;
    }

    public TextureRegion getRegion() {
        return this.region;
    }
}
