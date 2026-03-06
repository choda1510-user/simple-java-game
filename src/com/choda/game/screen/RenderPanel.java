package com.choda.game.screen;

import com.choda.game.game.Game;
import com.choda.game.game.Renderer;
import com.choda.game.util.Mat4;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RenderPanel extends JPanel {
    private BufferedImage bufferedImage;
    private Cursor defaultCursor;
    private Cursor blankCursor;
    private Game game;
    private Renderer renderer;
    public RenderPanel() {
        super();
        setFocusable(true);
        defaultCursor = Cursor.getDefaultCursor();
        BufferedImage blankCursorImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImage, new Point(0, 0), "Blank Cursor");
        setPreferredSize(new Dimension(800, 600));
    }
    public RenderPanel(BufferedImage bufferedImage, Game game, Renderer renderer) {
        this();
        this.bufferedImage = bufferedImage;
        this.game = game;
        this.renderer = renderer;
        setCursorVisible(game.isInputMode());
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setColorBuffer(renderer.getColorBuffer());
        g.drawImage(bufferedImage, 0, 0, this);
    }
    public void rendering() {
        renderer.setWidth(getWidth());
        renderer.setHeight(getHeight());
        renderer.setViewport(getViewport());
        renderer.render(game);
    }
    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage != null ? bufferedImage : this.bufferedImage;
    }
    public boolean getCursorVisible() {
        if (getCursor().equals(defaultCursor)) {
            return true;
        } else if (getCursor().equals(blankCursor)){
            return false;
        } else {
            return false;
        }
    }
    public void setCursorVisible(boolean b) {
        if (b) {
            setCursor(defaultCursor);
        } else {
            setCursor(blankCursor);
        }
    }
    public Mat4 getViewport() {
        return Mat4.makeViewport(0, 0, getWidth(), getHeight());
    }
    public void setColorBuffer(int[][] colorBuffer) {
        for (int i = 0; i < colorBuffer.length; i++ ) {
            for (int j = 0; j  < colorBuffer[i].length; j++) {
                bufferedImage.setRGB(i, j, colorBuffer[i][j]);
            }
        }
    }
}
