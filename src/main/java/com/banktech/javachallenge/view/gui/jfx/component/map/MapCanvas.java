package com.banktech.javachallenge.view.gui.jfx.component.map;

import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.Torpedo;
import com.banktech.javachallenge.service.domain.submarine.Entity;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.view.domain.ViewModel;
import com.banktech.javachallenge.view.gui.MapUtil;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class MapCanvas extends javafx.scene.canvas.Canvas {
    private static final double SCALE = 0.5;
    private static final double SPEED_SCALE = 2;
    private ViewModel viewModel;

    public MapCanvas() {
        super(1700 * SCALE, 800 * SCALE);
        paintComponent(getGraphicsContext2D());
    }

    public MapCanvas(int width, int height) {
        super(width * SCALE, height * SCALE);
        paintComponent(getGraphicsContext2D());
    }

    private void paintComponent(GraphicsContext g) {
        g.clearRect(0,0,getWidth(),getHeight());
        g.setStroke(Color.GRAY);
        g.setFill(Color.ALICEBLUE);
        g.strokeRect(0, 0, getWidth(), getHeight());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setStroke(Color.FORESTGREEN);
        g.setFill(Color.FORESTGREEN.brighter());
        if (viewModel != null && viewModel.getGame() != null) {
            List<Position> islands = viewModel.getGame().getMapConfiguration().getIslandPositions();
            for (Position island : islands) {
                drawFillCircle(g, island, viewModel.getGame().getMapConfiguration().getIslandSize());
            }
            g.setStroke(Color.BLUE);
            g.setFill(Color.BLUE.brighter());
            List<OwnSubmarine> ownSubmarines = viewModel.getOwnSubmarines();
            for (OwnSubmarine submarine : ownSubmarines) {
                drawFillCircle(g, submarine.getPosition(), viewModel.getGame().getMapConfiguration().getSubmarineSize());
            }
            g.setStroke(Color.BLACK);
            for (OwnSubmarine submarine : ownSubmarines) {
                drawSpeed(g, submarine.getPosition(), submarine.getAngle(), submarine.getVelocity());
            }
            g.setStroke(Color.RED);
            g.setFill(Color.RED.brighter());
            List<Entity> enemySubmarines = viewModel.getDetectedSubmarines();
            for (Entity submarine : enemySubmarines) {
                drawFillCircle(g, submarine.getPosition(), viewModel.getGame().getMapConfiguration().getSubmarineSize());
            }
            g.setStroke(Color.BLACK);
            for (Entity submarine : enemySubmarines) {
                drawSpeed(g, submarine.getPosition(), submarine.getAngle(), submarine.getVelocity());
            }
            g.setStroke(Color.RED.brighter());
            g.setFill(Color.RED.brighter());
            List<Torpedo> torpedos = viewModel.getShootedTorpedos();
            for (Torpedo torpedo : torpedos) {
                drawFillCircle(g, torpedo.getCurrentPosition(), 2);
                drawCircle(g, torpedo.getCurrentPosition(), viewModel.getGame().getMapConfiguration().getTorpedoRange());
                drawSpeed(g, torpedo.getCurrentPosition(), torpedo.getAngle(), torpedo.getSpeed());
            }
            g.setStroke(Color.RED.brighter());
            g.setFill(Color.RED.brighter());
            List<Entity> detectedTorpedos = viewModel.getDetectedTorpedos();
            for (Entity torpedo : detectedTorpedos) {
                drawFillCircle(g, torpedo.getPosition(), 2);
                drawCircle(g, torpedo.getPosition(), viewModel.getGame().getMapConfiguration().getTorpedoExplosionRadius());
                drawSpeed(g, torpedo.getPosition(), torpedo.getAngle(), torpedo.getVelocity());
            }
        }
    }

    private void drawSpeed(GraphicsContext g, Position position, double angle, double speed) {
        g.beginPath();
        g.moveTo(scale(position.getX()), scale(position.getY()));
        g.lineTo(scale(position.getX() + SPEED_SCALE * Math.cos(MapUtil.angleToRadian(angle)) * speed),
                scale(position.getY() + SPEED_SCALE * Math.sin(MapUtil.angleToRadian(angle)) * speed));
        g.stroke();

    }

    private void drawFillCircle(GraphicsContext g, Position position, int radius) {
        g.fillOval(scale(position.getX() - radius), scale(position.getY() - radius), scale(radius * 2), scale(radius * 2));
    }

    private void drawCircle(GraphicsContext g, Position position, int radius) {
        g.strokeOval(scale(position.getX() - radius), scale(position.getY() - radius), scale(radius * 2), scale(radius * 2));
    }

    private int scale(double number) {
        return (int) (SCALE * number);
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        paintComponent(getGraphicsContext2D());
    }

}
