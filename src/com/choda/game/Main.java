package com.choda.game;

import com.choda.game.game.*;
import com.choda.game.game.Renderer;
import com.choda.game.screen.RenderPanel;
import com.choda.game.util.Vec3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("simple game");
            Camera camera = new Camera();
            Player player = new Player(1, 0.1);
            player.setCamera(camera);
            camera.setProject(45 * Math.PI / 180.0, (double)frame.getHeight() / (double)frame.getWidth(), 0.1, 100.0);
            player.setPosition(new Vec3(0.0, 0.0, 3.1));
            Game game = new Game();
            game.setPlayer(player);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Renderer renderer = new Renderer(toolkit.getScreenSize().width, toolkit.getScreenSize().height);
            RenderPanel renderPanel = new RenderPanel(new BufferedImage(toolkit.getScreenSize().width, toolkit.getScreenSize().height, BufferedImage.TYPE_INT_RGB), game, renderer);
            KeyboardListenerImpl keyboardListener = new KeyboardListenerImpl(game, renderPanel, game.getWasdeq());
            MouseListenerImpl mouseListener = new MouseListenerImpl(game.getCamera(), game);
            mouseListener.setFrame(frame);
            renderPanel.addKeyListener(keyboardListener);
            renderPanel.addMouseListener(mouseListener);
            renderPanel.addMouseMotionListener(mouseListener);
            game.setFrame(frame);
            game.setRender(renderer);
            game.setRenderPanel(renderPanel);

            Timer timer = new Timer(1000 / 30, game);
            timer.start();

            frame.setSize(800, 600);
            frame.setContentPane(renderPanel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);
                    timer.stop();
                }
            });
            frame.setVisible(true);
        });
    }
}
