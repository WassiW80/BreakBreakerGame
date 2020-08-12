package com.breakbreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int totalBrick = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;

    private MapGenerator mapGenerator;

    public GamePlay() {
        mapGenerator = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(1, 1, 692, 592);

        mapGenerator.draw((Graphics2D) graphics);

        graphics.setColor(Color.YELLOW);
        graphics.fillRect(0, 0, 3, 592);
        graphics.fillRect(0, 0, 692, 3);
        graphics.fillRect(691, 0, 3, 592);

        graphics.setColor(Color.BLUE);
        graphics.fillRect(playerX, 550, 100, 8);

        graphics.setColor(Color.GREEN);
        graphics.fillOval(ballPosX, ballPosY, 20, 20);

        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("serif",Font.BOLD,25));
        graphics.drawString("" +score,590,30);

        if (totalBrick<=0){
            play=false;
            ballXDir=0;
            ballYDir=0;
            graphics.setColor(Color.RED);
            graphics.setFont(new Font("serif",Font.BOLD,30));
            graphics.drawString("You Won, Score: " +score,190,300);

            graphics.setFont(new Font("serif",Font.BOLD,25));
            graphics.drawString("Press Enter to Restart." +score,230,350);
        }

        if (ballPosY > 570){
            play=false;
            ballXDir=0;
            ballYDir=0;
            graphics.setColor(Color.RED);
            graphics.setFont(new Font("serif",Font.BOLD,30));
            graphics.drawString("Game Over, Score: " +score,190,300);

            graphics.setFont(new Font("serif",Font.BOLD,25));
            graphics.drawString("Press Enter to Restart." +score,230,350);
        }

        graphics.dispose();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYDir = -ballYDir;
            }
            for( int i = 0; i<mapGenerator.map.length; i++) {
                for(int j = 0; j<mapGenerator.map[0].length; j++) {
                    if(mapGenerator.map[i][j] > 0) {
                        int brickX = j * mapGenerator.brickWidth + 80;
                        int brickY = i * mapGenerator.brickHeight + 50;
                        int brickWidth = mapGenerator.brickWidth;
                        int brickHeight = mapGenerator.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            mapGenerator.setBrickValue(0, i, j);
                            totalBrick--;
                            score += 5;

                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width)
                                ballXDir = -ballXDir;
                            else
                                ballYDir = -ballYDir;
                        }
                    }

                }
            }

            ballPosX += ballXDir;
            ballPosY += ballYDir;

            if (ballPosX < 0)
                ballXDir = -ballXDir;
            if (ballPosY < 0)
                ballYDir = -ballYDir;
            if (ballPosX > 670)
                ballXDir = -ballXDir;
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600)
                playerX = 600;
            else
                moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10)
                playerX = 10;
            else
                moveLeft();
        }

        if (e.getKeyCode()==KeyEvent.VK_ENTER){
            if (!play){
                play=true;
                ballPosX = 120;
                ballPosY = 350;
                ballXDir = -1;
                ballYDir = -2;
                score=0;
                mapGenerator=new MapGenerator(3,7);
                repaint();
            }
        }

    }

    private void moveRight() {
        play = true;
        playerX += 20;
    }

    private void moveLeft() {
        play = true;
        playerX -= 20;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
