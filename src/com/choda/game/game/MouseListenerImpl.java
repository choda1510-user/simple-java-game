package com.choda.game.game;

import com.choda.game.util.Vec3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListenerImpl extends MouseAdapter {
    private JFrame frame;
    private int lastX, lastY;
    private final double sensitivity = 0.1;
    private double yaw = -90.0f, pitch;
    private Camera camera;
    private Game game;
    private Robot robot;
    public MouseListenerImpl(Camera camera, Game game) {
        this.camera = camera;
        this.game = game;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!game.inputMode) {
            rotateCamera(e.getLocationOnScreen().x, e.getLocationOnScreen().y);
            // https://stackoverflow.com/questions/2481804/capture-trap-the-mouse-cursor-in-a-window-in-java
            int centerX = frame.getLocationOnScreen().x + frame.getWidth() / 2;
            int centerY = frame.getLocationOnScreen().y + frame.getHeight() / 2;
            robot.mouseMove(centerX, centerY);
            lastX = centerX;
            lastY = centerY;
        } else {
            lastX = e.getLocationOnScreen().x;
            lastY = e.getLocationOnScreen().y;
        }
    }
    private void rotateCamera(int xPos, int yPos) {
        double xOffset = xPos - lastX;
        double yOffset = lastY - yPos;
        lastX = xPos;
        lastY = yPos;

        xOffset *= sensitivity;
        yOffset *= sensitivity;

        yaw += xOffset;
        pitch += yOffset;

        if (pitch > 89.0f) {
            pitch = 89.0f;
        }
        if (pitch < -89.0f) {
            pitch = -89.0f;
        }

        Vec3 direction = new Vec3();
        direction.x = (Math.cos(yaw * Math.PI / 180.0) * Math.cos(pitch * Math.PI / 180.0));
        direction.y = (Math.sin(pitch * Math.PI / 180.0));
        direction.z = (Math.sin(yaw * Math.PI / 180.0) * Math.cos(pitch * Math.PI / 180.0));
        camera.setFront(direction);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // https://stackoverflow.com/questions/2481804/capture-trap-the-mouse-cursor-in-a-window-in-java
        if (!game.inputMode) {
            int centerX = frame.getLocationOnScreen().x + frame.getWidth() / 2;
            int centerY = frame.getLocationOnScreen().y + frame.getHeight() / 2;
            robot.mouseMove(centerX, centerY);
            lastX = centerX;
            lastY = centerY;
        }
    }
}
