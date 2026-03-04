package com.choda.game.game;

import com.choda.game.mesh.Shape;
import com.choda.game.util.Mat4;
import com.choda.game.util.Vec3;
import com.choda.game.util.Vec4;

import java.util.LinkedList;
import java.util.List;

public class Player {
    private int id;
    private double speed;
    private List<Shape> shapes;
    private Camera camera;
    private Vec3 velocity;
    private Vec3 position;
    private Vec3 front;
    public Player(int id) {
        this.id = id;
        this.velocity = new Vec3();
        this.position = new Vec3();
        this.front = new Vec3(0.0, 0.0, -1.0);
        shapes = new LinkedList<>();
    }
    public Player(int id, double speed) {
        this(id);
        this.speed = speed;
    }
    public int getId() {
        return id;
    }
    public double getSpeed() {
        return speed;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public Camera getCamera() {
        camera.setPosition(position);
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Vec3 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec3 velocity) {
        this.velocity = velocity;
    }
    public Vec3 getFront() {
        front = camera.getFront();
        front = new Vec3(front.x, 0.0, front.z).normalize();
        return front;
    }

    // https://asteriskhun.tistory.com/40
    public Mat4 getTransform() {
        Vec3 zFront = new Vec3(0.0, 0.0, -1.0);
        Vec3 front = new Vec3(this.front.x, 0.0, this.front.z).normalize();
        double theta = Math.acos(zFront.dot(front) / zFront.norm() * front.norm());
        if (zFront.cross(front).z < 0) {
            theta = 2 * Math.PI - theta;
        }
        return Mat4.makeTranslation(position).dot(Mat4.makeRotation(theta, new Vec3(0.0, 1.0, 0.0)));
    }
    public void move(Vec3 vec3) {
        position = new Vec3(Mat4.makeTranslation(vec3).dot(new Vec4(position)));
    }
    public void rotate(double radian, Vec3 axis) {
        front = new Vec3(Mat4.makeRotation(radian, axis).dot(new Vec4(front))).normalize();
        camera.setFront(front);
    }

}
