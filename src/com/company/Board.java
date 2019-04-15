package com.company;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    private final int c[] = new int[ALL_DOTS];
    private final int d[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;
    private int otherDots;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private boolean leftDir = true;
    private boolean rightDir = false;
    private boolean upDir = false;
    private boolean downDir = false;
    private boolean stillInGame = true;

    private Timer timer;
    private Image ball1;
    private Image apple;
    private Image head1;
    private Image ball2;
    private Image head2;


    public Board() {

        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        ball1 = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head1 = iih.getImage();

        ImageIcon bodyOfSnake = new ImageIcon("src/resources/head.png");
        ball2 = bodyOfSnake.getImage();

        ImageIcon headOfSnake = new ImageIcon("src/resources/dot.png");
        head2 = headOfSnake.getImage();
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        otherDots = 3;

        for (int b = 0; b < otherDots; b++) {
            c[b] = 50 - b * 10;
            d[b] = 50;
        }

        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head1, x[z], y[z], this);
                } else {
                    g.drawImage(ball1, x[z], y[z], this);
                }
            }

            for (int b = 0; b < otherDots; b++){
                if (b == 0) {
                    g.drawImage(head2, c[b], d[b],this);
                } else {
                    g.drawImage(ball2, c[b], d[b], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();
        }

        if ((c[0] == apple_x) && (d[0] == apple_y)) {

            otherDots++;
            locateApple();
        }
    }

    private void movePlayerS() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }

        for (int a = otherDots; a>0; a--){
            c[a] = c[(a - 1)];
            d[a] = d[(a - 1)];
        }

        if (leftDir){
            c[0]-= DOT_SIZE;
        }

        if (rightDir){
            c[0] += DOT_SIZE;
        }

        if(upDir){
            d[0] -=DOT_SIZE;
        }

        if(downDir){
            d[0] += DOT_SIZE;
        }

    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        for (int b = otherDots; b > 0; b--){

            if ((b > 4) && (c[0] == c[b]) && (d[0] == d[b])){
                stillInGame = false;
            }

        }

        if (y[0] >= B_HEIGHT || y[0] < 0 || x[0] >= B_WIDTH || x[0] < 0) {
            inGame = false;
        }

        if (d[0]>= B_HEIGHT||d[0]< 0||c[0] >= B_WIDTH||c[0] < 0){
            stillInGame = false;
        }

//        if (y[0] < 0) {
//            inGame = false;
//        }

//        if (d[0]< 0) {
//            stillInGame = false;
//        }

//        if (x[0] >= B_WIDTH) {
//            inGame = false;
//        }

//        if (c[0] >= B_WIDTH) {
//            stillInGame = false;
//        }

//        if (x[0] < 0) {
//            inGame = false;
//        }

//        if (c[0] < 0) {
//            stillInGame = false;
//        }

        if (!inGame||!stillInGame) {
            timer.stop();
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
            movePlayerS();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_A) && (!rightDir)) {
                leftDir = true;
                upDir = false;
                downDir = false;
            }

            if ((key == KeyEvent.VK_D) && (!leftDir)) {
                rightDir = true;
                upDir = false;
                downDir = false;
            }

            if ((key == KeyEvent.VK_W) && (!downDir)) {
                upDir = true;
                rightDir = false;
                leftDir = false;
            }

            if ((key == KeyEvent.VK_S) && (!upDir)) {
                downDir = true;
                rightDir = false;
                leftDir = false;
            }
        }
    }
}
