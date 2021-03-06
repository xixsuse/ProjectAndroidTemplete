package com.badlogic.gdx.scenes.scene2d.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteDrawable extends BaseDrawable {
    private Sprite sprite;

    public SpriteDrawable(Sprite sprite) {
        setSprite(sprite);
    }

    public SpriteDrawable(SpriteDrawable drawable) {
        super(drawable);
        setSprite(drawable.sprite);
    }

    public void draw(SpriteBatch batch, float x, float y, float width, float height) {
        this.sprite.setBounds(x, y, width, height);
        Color color = this.sprite.getColor();
        this.sprite.setColor(Color.tmp.set(color).mul(batch.getColor()));
        this.sprite.draw(batch);
        this.sprite.setColor(color);
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        setMinWidth(sprite.getWidth());
        setMinHeight(sprite.getHeight());
    }

    public Sprite getSprite() {
        return this.sprite;
    }
}
