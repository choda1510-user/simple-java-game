package com.choda.game.game;

import com.choda.game.mesh.Shape;
import com.choda.game.mesh.Triangle;
import com.choda.game.util.Mat4;
import com.choda.game.util.Vec3;
import com.choda.game.util.Vec4;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Renderer {
    private double[][] zBuffer;
    private int[][] colorBuffer;
    private int width, height;
    private Mat4 viewport;
    public Renderer(int width, int height) {
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
                Vec4[] clip = triangle.dot(camera.getProject().dot(camera.getLookUp().dot(shape.getTransform())));
                Triangle ndcTriangle = triangle.w(camera.getProject().dot(camera.getLookUp().dot(shape.getTransform())));
                for (Triangle tri : clippingTriangleInNDC(ndcTriangle)) {
                    Triangle screenTriangle = tri.mul(viewport);
                    if (betweenZ(screenTriangle.getP1().z, screenTriangle.getP2().z, screenTriangle.getP3().z)) {
                        Point min = getMinPoint(screenTriangle, width, height);
                        Point max = getMaxPoint(screenTriangle, width, height);
                        for (int i = min.x; i < max.x; i++) {
                            for (int j = min.y; j < max.y; j++) {
                                Vec3 bcScreen = screenTriangle.barycentric(new Point(i, j));
                                Vec3 bcClip = new Vec3(bcScreen.x / clip[0].w, bcScreen.y / clip[1].w, bcScreen.z / clip[2].w);
                                bcClip = bcClip.dot(1 / (bcClip.x + bcClip.y + bcClip.z));
                                if (screenTriangle.has(new Point(i, j))) {
                                    double z = bcScreen.dot(new Vec3(ndcTriangle.getP1().z, ndcTriangle.getP2().z, ndcTriangle.getP3().z));
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
    private Triangle[] clippingTriangleInNDC(Triangle triangle) {
        Vec3 p1 = triangle.getP1();
        Vec3 p2 = triangle.getP2();
        Vec3 p3 = triangle.getP3();
        Vec3[] polygonDots = findEveryDots(p1, p2, p3);
        return triangulation(polygonDots);
    }
    private Vec3[] findEveryDots(Vec3 p1, Vec3 p2, Vec3 p3) {
        List<Vec3> dots = new LinkedList<>();
        Vec3[] pOfLines = new Vec3[] { p1, p2, p3 };
        Vec3[] dOfLines = new Vec3[] { p2.sub(p1), p3.sub(p2), p1.sub(p3) };
        Vec3 n =  dOfLines[1].cross(dOfLines[2]);
        Vec3[] normalNDC = new Vec3[] {
                // front
                new Vec3(0.0, 0.0, 1.0),
                // back
                new Vec3(0.0, 0.0, -1.0),
                // right
                new Vec3(1.0, 0.0, 0.0),
                // left
                new Vec3(-1.0, 0.0, 0.0),
                // up
                new Vec3(0.0, 1.0, 0.0),
                // down
                new Vec3(0.0, -1.0, 0.0),
        };
        Vec3[] pNDC = new Vec3[] {
                new Vec3(-1.0, 1.0, -1.0),
                new Vec3(1.0, 1.0, -1.0),
                new Vec3(-1.0, -1.0, -1.0),
                new Vec3(1.0, -1.0, -1.0),

                new Vec3(-1.0, -1.0, 1.0),
                new Vec3(1.0, -1.0, 1.0),
                new Vec3(-1.0, -1.0, -1.0),
                new Vec3(1.0, -1.0, -1.0),

                new Vec3(-1.0, 1.0, 1.0),
                new Vec3(-1.0, 1.0, -1.0),
                new Vec3(-1.0, -1.0, 1.0),
                new Vec3(-1.0, -1.0, -1.0),
        };
        Vec3[] dNDC = new Vec3[] {
                new Vec3(0.0, 0.0, 2.0),
                new Vec3(0.0, 0.0, 2.0),
                new Vec3(0.0, 0.0, 2.0),
                new Vec3(0.0, 0.0, 2.0),

                new Vec3(0.0, 2.0, 0.0),
                new Vec3(0.0, 2.0, 0.0),
                new Vec3(0.0, 2.0, 0.0),
                new Vec3(0.0, 2.0, 0.0),

                new Vec3(2.0, 0.0, 0.0),
                new Vec3(2.0, 0.0, 0.0),
                new Vec3(2.0, 0.0, 0.0),
                new Vec3(2.0, 0.0, 0.0),
        };
        for (Vec3 normal : normalNDC) {
            for (int i = 0; i < dOfLines.length; i++) {
                Vec3 dot = findDot(normal, normal, pOfLines[i], dOfLines[i]);
                if (inner(dot, pOfLines)) {
                    dots.add(dot);
                }
            }
        }
        for (int i = 0; i < dNDC.length; i++ ) {
            Vec3 dot = findDot(p1, n, pNDC[i], dNDC[i]);
            if (inner(dot, pOfLines)) {
                dots.add(dot);
            }
        }
        for (Vec3 p : pOfLines) {
            if (innerNDC(p)) {
                dots.add(p);
            }
        }
        Vec3[] result = new Vec3[dots.size()];
        result = dots.toArray(result);
        return result;
    }
    private Vec3 findDot(Vec3 posOnPlain, Vec3 normal, Vec3 posOnLine, Vec3 d) {
        return d.dot(normal.dot(posOnPlain.sub(posOnLine)) / normal.dot(d)).add(posOnLine);
    }
    // 교점이라고 전제함
    private boolean inner(Vec3 dot, Vec3[] pos) {
        double minX = pos[0].x;
        double maxX = pos[0].x;
        double minY = pos[0].y;
        double maxY = pos[0].y;
        double minZ = pos[0].z;
        double maxZ = pos[0].z;
        for (Vec3 p : pos) {
            if (p.x < minX) {
                minX = p.x;
            }
            if (maxX < p.x) {
                maxX = p.x;
            }
            if (p.y < minY) {
                minY = p.y;
            }
            if (maxY < p.y) {
                maxY = p.y;
            }
            if (p.z < minZ) {
                minZ = p.z;
            }
            if (maxZ < p.z) {
                maxZ = p.z;
            }
        }

        if (innerNDC(dot)) {
            return minX <= dot.x && dot.x <= maxX && minY <= dot.y && dot.y <= maxY && minZ <= dot.z && dot.z <= maxZ;
        }
        return false;
    }
    private boolean innerNDC(Vec3 dot) {
        return -1 <= dot.x && dot.x <= 1 && -1 <= dot.y && dot.y <= 1 && -1 <= dot.z && dot.z <= 1;
    }
    private Triangle[] triangulation(Vec3[] dots) {
        return null;
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
