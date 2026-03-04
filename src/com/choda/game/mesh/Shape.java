package com.choda.game.mesh;

import com.choda.game.util.Mat4;

import java.util.ArrayList;
import java.util.List;

public class Shape {
    private List<Triangle> triangles;
    private Mat4 transform;
    public Shape() {
        this.triangles = new ArrayList<>();
        this.transform = Mat4.makeIdentity();
    }
    public Shape(List<Triangle> triangles, Mat4 transform) {
        this.triangles = triangles;
        this.transform = transform;
    }
    public List<Triangle> getTriangles() {
        return this.triangles;
    }
    public Mat4 getTransform() {
        return this.transform;
    }
}
