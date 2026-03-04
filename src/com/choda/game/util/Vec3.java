package com.choda.game.util;

public class Vec3 {
    public double x, y, z;
    public Vec3() {

    }
    public Vec3(double value) {
        this.x = value;
        this.y = value;
        this.z = value;
    }
    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vec3(double[] arr) throws IndexOutOfBoundsException, NullPointerException {
        this.x = arr[0];
        this.y = arr[1];
        this.z = arr[2];
    }
    public Vec3(Vec3 v) throws NullPointerException {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    public Vec3(Vec4 v) throws NullPointerException {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    public Vec3 add(Vec3 v) {
        return new Vec3(x + v.x, y + v.y, z + v.z);
    }
    public Vec3 sub(Vec3 v) {
        return new Vec3(x - v.x, y - v.y, z - v.z);
    }
    public Vec3 dot(double scalar) {
        return new Vec3(x * scalar, y * scalar, z * scalar);
    }
    public double dot(Vec3 v) {
        return x * v.x + y * v.y + z * v.z;
    }
    public Vec3 cross(Vec3 v) {
        return new Vec3(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
        );
    }
    public double norm() {
        return Math.sqrt(this.dot(this));
    }
    public Vec3 normalize() {
        return this.dot(1 / norm());
    }
    public double[] toArray() {
        return new double[] { x, y, z };
    }
}
