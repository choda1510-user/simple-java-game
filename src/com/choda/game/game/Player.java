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
        camera.setPosition(position.add(new Vec3(0.0, 2.0, 0.0)));
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

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public void move(Vec3 vec3, long deltaTime) {
        velocity = lerp(velocity, vec3, deltaTime);
        position = new Vec3(Mat4.makeTranslation(velocity).dot(new Vec4(position)));
    }

    public Vec3 lerp(Vec3 v1, Vec3 v2, long deltaTime) {
        double d = (double)deltaTime / 300.0;
        if (d > 1) {
            d = 1.0;
        }
        return new Vec3(
                Math.abs((1 - d) * v1.x + d * v2.x) < 0.0001 ? 0 : (1 - d) * v1.x + d * v2.x,
                Math.abs((1 - d) * v1.y + d * v2.y) < 0.0001 ? 0 : (1 - d) * v1.y + d * v2.y,
                Math.abs((1 - d) * v1.z + d * v2.z) < 0.0001 ? 0 : (1 - d) * v1.z + d * v2.z
        );
    }

}
