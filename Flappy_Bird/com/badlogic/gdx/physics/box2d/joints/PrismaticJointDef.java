package com.badlogic.gdx.physics.box2d.joints;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.google.android.gms.cast.TextTrackStyle;

public class PrismaticJointDef extends JointDef {
    public boolean enableLimit;
    public boolean enableMotor;
    public final Vector2 localAnchorA;
    public final Vector2 localAnchorB;
    public final Vector2 localAxisA;
    public float lowerTranslation;
    public float maxMotorForce;
    public float motorSpeed;
    public float referenceAngle;
    public float upperTranslation;

    public PrismaticJointDef() {
        this.localAnchorA = new Vector2();
        this.localAnchorB = new Vector2();
        this.localAxisA = new Vector2(TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f);
        this.referenceAngle = 0.0f;
        this.enableLimit = false;
        this.lowerTranslation = 0.0f;
        this.upperTranslation = 0.0f;
        this.enableMotor = false;
        this.maxMotorForce = 0.0f;
        this.motorSpeed = 0.0f;
        this.type = JointType.PrismaticJoint;
    }

    public void initialize(Body bodyA, Body bodyB, Vector2 anchor, Vector2 axis) {
        this.bodyA = bodyA;
        this.bodyB = bodyB;
        this.localAnchorA.set(bodyA.getLocalPoint(anchor));
        this.localAnchorB.set(bodyB.getLocalPoint(anchor));
        this.localAxisA.set(bodyA.getLocalVector(axis));
        this.referenceAngle = bodyB.getAngle() - bodyA.getAngle();
    }
}
