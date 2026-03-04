package com.choda.game.game;

import com.choda.game.util.Mat4;
import com.choda.game.util.Vec3;

public class Camera {
    private Vec3 position, front, up;
    private Mat4 project;
    {
        position = new Vec3();
        front = new Vec3(0.0, 0.0, -1.0);
        up = new Vec3(0.0, 1.0, 0.0);
        project = Mat4.makeIdentity();
    }
    public Camera() {

    }
    public void setProject(double fov, double aspectRatio, double front, double back) {
        project = Mat4.makeFrustumY(fov, aspectRatio, front, back);
    }
    public Mat4 getProject() {
        return project;
    }
    public Mat4 getLookUp() {
        return Mat4.makeLookAt(position, position.add(front), up);
    }
    public void setPosition(Vec3 position) {
        this.position = position != null ? position : this.position;
    }
    public void setFront(Vec3 front) {
        this.front = front != null ? front.normalize() : this.front;
    }
}
