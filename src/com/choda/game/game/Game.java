package com.choda.game.game;

import com.choda.game.mesh.Shape;
import com.choda.game.mesh.Triangle;
import com.choda.game.util.Mat4;
import com.choda.game.util.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {
    private Shape floor;
    private boolean[] wasdeq;
    boolean inputMode;
    private Camera camera;
    public Game() {
        wasdeq = new boolean[6];
        ArrayList<Triangle> floorPolygon = new ArrayList<>();
//        floorPolygon.add(new Triangle(
//                new Vec3(-10, 0, 10),
//                new Vec3(10, 0, -10),
//                new Vec3(10, 0, 10),
//                new Color(0x00FFFFFF)
//        ));
//        floorPolygon.add(new Triangle(
//                new Vec3(-10, 0, 10),
//                new Vec3(-10, 0, -10),
//                new Vec3(10, 0, -10),
//                new Color(0x00FFFFFF)
//        ));
        floorPolygon.add(new Triangle(
                new Vec3(-1, 0, 0),
                new Vec3(1, 0, 0),
                new Vec3(0, 1, 0),
                new Color(0x00FFFFFF)
        ));
        this.floor = new Shape(
                floorPolygon,
                Mat4.makeIdentity()
        );
    }
    public List<Shape> getShapes() {
        List<Shape> list = new LinkedList<>();
        list.add(floor);
        return list;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public boolean[] getWasdeq() {
        return wasdeq;
    }

    public void setWasdeq(boolean[] wasdeq) {
        this.wasdeq = wasdeq;
    }
}
