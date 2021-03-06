package com.badlogic.gdx.maps.tiled.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTile.BlendMode;

public class StaticTiledMapTile implements TiledMapTile {
    private BlendMode blendMode;
    private int id;
    private MapProperties properties;
    private TextureRegion textureRegion;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BlendMode getBlendMode() {
        return this.blendMode;
    }

    public void setBlendMode(BlendMode blendMode) {
        this.blendMode = blendMode;
    }

    public MapProperties getProperties() {
        if (this.properties == null) {
            this.properties = new MapProperties();
        }
        return this.properties;
    }

    public TextureRegion getTextureRegion() {
        return this.textureRegion;
    }

    public StaticTiledMapTile(TextureRegion textureRegion) {
        this.blendMode = BlendMode.ALPHA;
        this.textureRegion = textureRegion;
    }

    public StaticTiledMapTile(StaticTiledMapTile copy) {
        this.blendMode = BlendMode.ALPHA;
        if (copy.properties != null) {
            getProperties().putAll(copy.properties);
        }
        this.textureRegion = copy.textureRegion;
        this.id = copy.id;
    }
}
