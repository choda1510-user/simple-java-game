package com.choda.game.game;

import com.choda.game.mesh.Shape;
import com.choda.game.mesh.Triangle;
import com.choda.game.util.Mat4;
import com.choda.game.util.Vec3;

import java.awt.*;
import java.util.List;

public class Render {
    private double[][] zBuffer;
    private int[][] colorBuffer;
    private int width, height;
    private Mat4 viewport;
    public Render(int width, int height) {
        zBuffer = new double[width][height];
        colorBuffer = new int[width][height];
    }
    private void clear(Color color) {
        for (int i = 0; i < zBuffer.length; i++) {
            for (int j = 0; j < zBuffer[i].length; j++) {
                zBuffer[i][j] = 1.0;
                colorBuffer[i][j] = color.getRGB();
            }
        }
    }
    public void render(Game game) {
        clear(Color.BLACK);
        List<Shape> shapes = game.getShapes();
        Camera camera = game.getCamera();
        for (Shape shape : shapes) {
            for (Triangle triangle : shape.getTriangles()) {
                Color color = triangle.getColor();
                Triangle screenTriangle = triangle.w(camera.getProject().dot(camera.getLookUp().dot(shape.getTransform()))).mul(viewport);
                if (betweenZ(screenTriangle.getP1().z, screenTriangle.getP2().z, screenTriangle.getP3().z)) {
                    Point min = getMinPoint(screenTriangle, width, height);
                    Point max = getMaxPoint(screenTriangle, width, height);
                    for (int i = min.x; i < max.x; i++) {
                        for (int j = min.y; j < max.y; j++) {
                            if (screenTriangle.has(new Point(i, j))) {
                                double z = getZ(screenTriangle, new Point(i, j));
                                if (z < zBuffer[i][j]) {
                                    colorBuffer[i][j] = color.getRGB();
                                    zBuffer[i][j] = z;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public int[][] getColorBuffer() {
        return colorBuffer;
    }

    private Point getMinPoint(Triangle triangle, int width, int height) {
        return new Point(
                (int) Math.max(Math.min(Math.min(triangle.getP1().x, Math.min(triangle.getP2().x, triangle.getP3().x)), width), 0),
                (int) Math.max(Math.min(Math.min(triangle.getP1().y, Math.min(triangle.getP2().y, triangle.getP3().y)), height), 0)
        );
    }

    private Point getMaxPoint(Triangle triangle, int width, int height) {
        return new Point(
                (int) Math.min(Math.max(Math.max(triangle.getP1().x, Math.max(triangle.getP2().x, triangle.getP3().x)), 0), width),
                (int) Math.min(Math.max(Math.max(triangle.getP1().y, Math.max(triangle.getP2().y, triangle.getP3().y)), 0), height)
        );
    }
    private double getZ(Triangle triangle, Point p) {
        Vec3 pos = triangle.getP1();
        Vec3 dv1 = triangle.getP3().sub(triangle.getP1());
        Vec3 nmv = triangle.getNormal();
        return pos.z - (dv1.dot(nmv) - (pos.x - p.x) * nmv.x - (pos.y - p.y) * nmv.y) / nmv.z;
    }
    private boolean betweenZ(double ...zs) {
        for (double d : zs) {
            if (d < 0 || 1 < d) {
                return false;
            }
        }
        return true;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Mat4 getViewport() {
        return viewport;
    }

    public void setViewport(Mat4 viewport) {
        this.viewport = viewport;
    }

}
