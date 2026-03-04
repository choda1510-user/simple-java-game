package com.choda.game.game;

import com.choda.game.screen.RenderPanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardListenerImpl extends KeyAdapter {
    private Game game;
    private RenderPanel renderPanel;
    private boolean inputState;
    private boolean[] wasdeq;
    public KeyboardListenerImpl(Game game, RenderPanel renderPanel, boolean[] wasdeq) {
        this.game = game;
        this.renderPanel = renderPanel;
        this.wasdeq = wasdeq;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            wasdeq[0] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            wasdeq[1] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            wasdeq[2] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            wasdeq[3] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            wasdeq[4] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            wasdeq[5] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (!inputState) {
                game.inputMode = !game.inputMode;
                inputState = true;
                renderPanel.setCursorVisible(game.inputMode);
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            wasdeq[0] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            wasdeq[1] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            wasdeq[2] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            wasdeq[3] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            wasdeq[4] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            wasdeq[5] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (inputState) {
                inputState = false;
            }
        }
    }
}
