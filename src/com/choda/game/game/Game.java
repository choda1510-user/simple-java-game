package com.choda.game.game;

import com.choda.game.mesh.Shape;
import com.choda.game.mesh.Triangle;
import com.choda.game.screen.RenderPanel;
import com.choda.game.util.Mat4;
import com.choda.game.util.Vec3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game implements ActionListener {
    private Player player;
    private Shape floor;
    private boolean[] wasdeq;
    boolean inputMode = true;
    private Instant prevTime;
    private JFrame frame;
    private RenderPanel renderPanel;
    private Renderer renderer;
    public Game() {
        prevTime = Instant.now();
        wasdeq = new boolean[6];
        ArrayList<Triangle> floorPolygon = new ArrayList<>();
        double size = 1;
        int repeat = 6;
        double pivot = repeat * size / 2;
        for (int i = 0; i < repeat; i++) {
            for (int j = 0; j < repeat; j++) {
                floorPolygon.add(Triangle.builder()
                        .p1(size * i - pivot, 0, size * j - pivot)
                        .p2(size * (i + 1) - pivot, 0, size * (j + 1) - pivot)
                        .p3(size * i - pivot, 0, size * (j + 1) - pivot)
                        .color(0x00DDDDDD)
                        .build());
                floorPolygon.add(Triangle.builder()
                        .p1(size * i - pivot, 0, size * j - pivot)
                        .p2(size * (i + 1) - pivot, 0, size * j - pivot)
                        .p3(size * (i + 1) - pivot, 0, size * (j + 1) - pivot)
                        .color(0x00DDDDDD)
                        .build());
            }
        }
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
        return player.getCamera();
    }
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean[] getWasdeq() {
        return wasdeq;
    }

    public void setWasdeq(boolean[] wasdeq) {
        this.wasdeq = wasdeq;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setRenderPanel(RenderPanel renderPanel) {
        this.renderPanel = renderPanel;
    }

    public void setRender(Renderer renderer) {
        this.renderer = renderer;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Instant now = Instant.now();
        long deltaTime = now.minusMillis(prevTime.toEpochMilli()).toEpochMilli();
        if (deltaTime == 0) {
            deltaTime = 1;
        }
        prevTime = now;
        player.getCamera().setProject(45 * Math.PI / 180.0, (double)frame.getHeight() / (double)frame.getWidth(), 0.1, 100.0);

        Vec3 playerFront = player.getFront();
        Vec3 playerMove = new Vec3();
        if (wasdeq[0]) {
            playerMove = playerMove.add(playerFront);
        }
        if (wasdeq[1]) {
            playerMove = playerMove.add(new Vec3(0.0, 1.0, 0.0).cross(playerFront));
        }
        if (wasdeq[2]) {
            playerMove = playerMove.add(playerFront.dot(-1));
        }
        if (wasdeq[3]) {
            playerMove = playerMove.add(new Vec3(0.0, 1.0, 0.0).cross(playerFront).dot(-1));
        }
        player.move(playerMove.normalize().dot(deltaTime / 30.0 * player.getSpeed()));

//        Vec3 playerFront = player.getFront();
//        if (wasdeq[0]) {
//            player.move(playerFront.dot(deltaTime / 30.0 * player.getSpeed()));
//        }
//        if (wasdeq[1]) {
//            player.move(new Vec3(0.0, 1.0, 0.0).cross(playerFront).dot(deltaTime / 30.0 * player.getSpeed()));
//        }
//        if (wasdeq[2]) {
//            player.move(playerFront.dot(deltaTime / 30.0 * player.getSpeed()).dot(-1));
//        }
//        if (wasdeq[3]) {
//            player.move(new Vec3(0.0, 1.0, 0.0).cross(playerFront).dot(deltaTime / 30.0 * player.getSpeed()).dot(-1));
//        }


        renderPanel.rendering();
        renderPanel.repaint();
    }

    public boolean isInputMode() {
        return inputMode;
    }

}
