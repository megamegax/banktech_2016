package com.banktech.javachallenge.view.gui.swing.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.submarine.Entity;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.view.domain.ViewModel;
import com.banktech.javachallenge.view.gui.MapUtil;
import com.banktech.javachallenge.service.domain.Torpedo;

public class MapPanel extends JPanel {

    private static final double SCALE = 0.5;
    private static final double SPEED_SCALE = 2;
    private static final Dimension DIMENSION = new Dimension((int) (1700 * SCALE), (int) (800 * SCALE));

    private static final long serialVersionUID = 1L;

    private ViewModel viewModel;

    @Override
    public Dimension getPreferredSize() {
        return DIMENSION;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, DIMENSION.width, DIMENSION.height);
        g.setColor(Color.GRAY);
        if (viewModel != null && viewModel.getGame() != null) {
            List<Position> islands = viewModel.getGame().getMapConfiguration().getIslandPositions();
            for (Position island : islands) {
                drawFillCircle(g, island, viewModel.getGame().getMapConfiguration().getIslandSize());
            }
            g.setColor(Color.BLUE);
            List<OwnSubmarine> ownSubmarines = viewModel.getOwnSubmarines();
            for (OwnSubmarine submarine : ownSubmarines) {
                drawFillCircle(g, submarine.getPosition(), viewModel.getGame().getMapConfiguration().getSubmarineSize());
            }
            g.setColor(Color.BLACK);
            for (OwnSubmarine submarine : ownSubmarines) {
                drawSpeed(g, submarine.getPosition(), submarine.getAngle(), submarine.getVelocity());
            }
            g.setColor(Color.RED);
            List<Entity> enemySubmarines = viewModel.getDetectedSubmarines();
            for (Entity submarine : enemySubmarines) {
                drawFillCircle(g, submarine.getPosition(), viewModel.getGame().getMapConfiguration().getSubmarineSize());
            }
            g.setColor(Color.BLACK);
            for (Entity submarine : enemySubmarines) {
                drawSpeed(g, submarine.getPosition(), submarine.getAngle(), submarine.getVelocity());
            }
            g.setColor(Color.BLACK);
            List<Torpedo> torpedos = viewModel.getShootedTorpedos();
            for (Torpedo torpedo : torpedos) {
                drawFillCircle(g, torpedo.getCurrentPosition(), 2);
                drawCircle(g, torpedo.getCurrentPosition(), viewModel.getGame().getMapConfiguration().getTorpedoRange());
                drawSpeed(g, torpedo.getCurrentPosition(), torpedo.getAngle(), torpedo.getSpeed());
            }

            List<Entity> detectedTorpedos = viewModel.getDetectedTorpedos();
            for (Entity torpedo : detectedTorpedos) {
                drawFillCircle(g, torpedo.getPosition(), 2);
                drawCircle(g, torpedo.getPosition(), viewModel.getGame().getMapConfiguration().getTorpedoRange());
                drawSpeed(g, torpedo.getPosition(), torpedo.getAngle(), torpedo.getVelocity());
            }
        }
    }

    private void drawSpeed(Graphics g, Position position, double angle, double speed) {
        g.drawLine(scale(position.getX()), scale(position.getY()), 
                scale(position.getX() + SPEED_SCALE * Math.cos(MapUtil.angleToRadian(angle)) * speed),
                scale(position.getY() + SPEED_SCALE * Math.sin(MapUtil.angleToRadian(angle)) * speed));
    }


    private void drawFillCircle(Graphics g, Position position, int radius) {
        g.fillOval(scale(position.getX() - radius), scale(position.getY() - radius), scale(radius * 2), scale(radius * 2));
    }

    private void drawCircle(Graphics g, Position position, int radius) {
        g.drawOval(scale(position.getX() - radius), scale(position.getY() - radius), scale(radius * 2), scale(radius * 2));
    }

    private int scale(double number) {
        return (int) (SCALE * number);
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        repaint();
    }

}