package com.choda.game.util;

public class Vec4 {
    public double x, y, z, w;
    public Vec4() {

    }
    public Vec4(double value) {
        this.x = value;
        this.y = value;
        this.z = value;
        this.w = value;
    }
    public Vec4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public Vec4(double[] arr) throws IndexOutOfBoundsException, NullPointerException {
        this.x = arr[0];
        this.y = arr[1];
        this.z = arr[2];
        this.w = arr[3];
    }
    public Vec4(Vec4 v) throws NullPointerException {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }
    public Vec4(Vec3 v) throws NullPointerException {
        this(v, 1.0);
    }
    public Vec4(Vec3 v, double w) throws NullPointerException {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = w;
    }
    public Vec4 add(Vec4 v) {
        return new Vec4(x + v.x, y + v.y, z + v.z, w + v.w);
    }
    public Vec4 sub(Vec4 v) {
        return new Vec4(x - v.x, y - v.y, z - v.z, w - v.w);
    }
    public Vec4 dot(double scalar) {
        return new Vec4(x * scalar, y * scalar, z * scalar, w * scalar);
    }
    public double dot(Vec4 v) {
        return x * v.x + y * v.y + z * v.z + w * v.w;
    }
    public double norm() {
        return Math.sqrt(this.dot(this));
    }
    public Vec4 normalize() {
        double norm = norm();
        if (norm == 0) {
            return this;
        } else {
            return this.dot(1 / norm());
        }
    }
    public Vec4 w() {
        return new Vec4(x / w, y / w, z / w, 1.0);
    }
    public double[] getArray() {
        return new double[] { x, y, z, w };
    }
}
