package com.choda.game.mesh;

import com.choda.game.util.Mat4;
import com.choda.game.util.Vec3;
import com.choda.game.util.Vec4;

import java.awt.*;

public class Triangle {
    private Vec3 p1, p2, p3;
    private Color color;
    {
        this.p1 = new Vec3();
        this.p2 = new Vec3();
        this.p3 = new Vec3();
        this.color = Color.black;
    }
    public Triangle() {

    }
    public Triangle(Vec3 p1, Vec3 p2, Vec3 p3) {
        if (p1 != null) {
            this.p1.x = p1.x;
            this.p1.y = p1.y;
            this.p1.z = p1.z;
        }
        if (p2 != null) {
            this.p2.x = p2.x;
            this.p2.y = p2.y;
            this.p2.z = p2.z;
        }
        if (p3 != null) {
            this.p3.x = p3.x;
            this.p3.y = p3.y;
            this.p3.z = p3.z;
        }
    }
    public Triangle(Vec3 p1, Vec3 p2, Vec3 p3, Color color) {
        this(p1, p2, p3);
        this.color = color != null ? color : this.color;
    }
    public Vec3 getNormal() {
        return p3.sub(p1).cross(p2.sub(p1)).normalize();
    }
    public Triangle w(Mat4 mat4) {
        return new Triangle(new Vec3(mat4.dot(new Vec4(p1)).w()), new Vec3(mat4.dot(new Vec4(p2)).w()), new Vec3(mat4.dot(new Vec4(p3)).w()));
    }
    public Triangle mul(Mat4 mat4) {
        return new Triangle(new Vec3(mat4.dot(new Vec4(p1))), new Vec3(mat4.dot(new Vec4(p2))), new Vec3(mat4.dot(new Vec4(p3))));
    }
    public Vec4[] dot(Mat4 mat4) {
        return new Vec4[] {
                mat4.dot(new Vec4(p1)),
                mat4.dot(new Vec4(p2)),
                mat4.dot(new Vec4(p3))
        };
    }
    // https://nogabi.tistory.com/4
    public boolean has(Point p) {
        Vec3 bcScreen = barycentric(p);
        return !(bcScreen.x < 0) && !(bcScreen.y < 0) && !(bcScreen.z < 0);
    }
    public Vec3 barycentric(Point p) {
        Vec3 u = new Vec3(
                p3.x - p1.x,
                p2.x - p1.x,
                p1.x - p.x
        ).cross(new Vec3(
                p3.y - p1.y,
                p2.y - p1.y,
                p1.y - p.y
        ));
        Vec3 bcScreen = null;
        if (Math.abs(u.z) < 1) {
            bcScreen = new Vec3(-1, 1, 1);
        } else {
            bcScreen = new Vec3(1.0 - (u.x + u.y) / u.z, u.y / u.z, u.x / u.z);
        }
        return bcScreen;
    }
    public Vec3 getP1() {
        return new Vec3(p1);
    }
    public Vec3 getP2() {
        return new Vec3(p2);
    }
    public Vec3 getP3() {
        return new Vec3(p3);
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
}
