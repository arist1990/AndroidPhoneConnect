package com.example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MyClass {

    public static Point firstPoint = null;
    public static Point secondPoint = null;
    public static boolean isFirst = true;

    public static void main(String[] args){
        System.out.println("哈哈，运行起来吧！");

        JumpJump jumpJump = new JumpJump();

        final JPanel jPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                this.setBackground(Color.BLUE);

                try {
                    BufferedImage bufferedImage = ImageIO.read(new File(SCREENSHOT_LOCATION));
                    BufferedImage newImage = new BufferedImage(675, 1200, bufferedImage.getType());
                    Graphics gTemp = newImage.getGraphics();
                    gTemp.drawImage(bufferedImage, 0, 0, 675, 1200, null);
                    gTemp.dispose();
                    bufferedImage = newImage;
                    g.drawImage(bufferedImage, 0, 0, null);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

//        printScreen();
        jumpJump.setVisible(true);
        jumpJump.getContentPane().add(jPanel);

//        jPanel.validate();
//        jumpJump.repaint();

        jPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("mousePressed");

                if (isFirst) {
                    System.out.println("first:" + e.getX() + "," + e.getY());
                    firstPoint = e.getPoint();
                    isFirst = false;
                } else {
                    secondPoint = e.getPoint();
                    System.out.println("second:" + e.getX() + "," + e.getY());
                    int distance = distance(firstPoint, secondPoint);
                    System.out.println("distance:" + distance);
                    isFirst = true;
                    call((int) (distance * 2.4)); // magic number
//                    try {
//                        Thread.sleep(2500);
//                    } catch (InterruptedException e1) {
//                        e1.printStackTrace();
//                    }

//                    printScreen();
//                    jPanel.validate();
//                    jPanel.repaint();
                }

            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        new Thread(){
            @Override
            public void run() {
                super.run();

                while (jPanel.isVisible()) {
                    printScreen();
                    jPanel.validate();
                    jPanel.repaint();
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    public static int distance(Point a, Point b) {
        return (int) Math.sqrt((a.x - b.getX()) * (a.x - b.getX()) + (a.y - b.getY()) * (a.y - b.getY()));
    }

    public static String pathADB = "/Users/arist/Library/Android/sdk/platform-tools/adb";
    public static String SCREENSHOT_LOCATION = "/Users/arist/Desktop/pic/screenShot.png";

    public static void call(int timeMilli) {
        try {

            String str = pathADB + " shell input touchscreen swipe 170 187 170 187 " + timeMilli;
            Runtime.getRuntime().exec(str);
            System.out.println(str);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printScreen(){
        try {
            Process process1 = Runtime.getRuntime().exec(pathADB + " shell screencap -p /sdcard/screenshot.png");
            process1.waitFor();

            Process process2 = Runtime.getRuntime().exec(pathADB + " pull /sdcard/screenshot.png " + SCREENSHOT_LOCATION);
            process2.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
