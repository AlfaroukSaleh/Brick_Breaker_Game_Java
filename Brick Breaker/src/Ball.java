import java.awt.Color;

import java.awt.Graphics;
import java.awt.Rectangle;



class Ball {
        private int x, y, dx = 6, dy = -6;
        private final int DIAMETER = 10;
        private Paddle paddle;
        private boolean attachedToPaddle;
        private int WIDTH,HEIGHT;

        public Ball(int x, int y, Paddle paddle,int WIDTH,int HEIGHT) {
            this.x = x;
            this.y = y;
            this.paddle = paddle;
            attachedToPaddle = true; // Initialize the ball attached to the paddle
            this.WIDTH=WIDTH;
            this.HEIGHT=HEIGHT;
        }


        public void move() {
            if (!attachedToPaddle) {
                x += dx;
                y += dy;

                if (x <= 0 || x + DIAMETER >= WIDTH) {
                    dx = -dx;
                }
                if (y <= 0) {
                    dy = -dy;
                }

                // Check if the ball is on the paddle and stop it
                if (paddle.isBallOnPaddle(this)) {
                    attachedToPaddle = true;
                    dy = -Math.abs(dy); // Ensure the ball moves upwards when it is released
                }
            }
        }



        public void reverseYDirection() {
            dy = -dy;
        }

        public void draw(Graphics g) {
            g.fillOval(x, y, DIAMETER, DIAMETER);
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, DIAMETER, DIAMETER);
        }

        public int getY() {
            return y;
        }
        public int getX() {
            return x;
        }

        
        public boolean isAttachedToPaddle() {
            return attachedToPaddle;
        }

        public void setAttachedToPaddle(boolean attachedToPaddle) {
            this.attachedToPaddle = attachedToPaddle;
        }

        public int getDIAMETER() {
            return DIAMETER;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
        
        public void drawTrajectory(Graphics g) {
            if (attachedToPaddle) {
                g.setColor(new Color(255, 255, 0)); // Yellow Color
                int startX = x + DIAMETER / 2;
                int startY = y;
                int endX = startX + 75 * dx; // control the length of the trajectory
                int endY = startY + 75 * dy; 

                for (int i = 0; i < 5; i++) { 
                    int segmentStartX = startX + i * 15 * dx;
                    int segmentStartY = startY + i * 15 * dy;
                    int segmentEndX = startX + (i * 15 + 7) * dx;
                    int segmentEndY = startY + (i * 15 + 7) * dy;
                    g.drawLine(segmentStartX, segmentStartY, segmentEndX, segmentEndY);
                }
            }
        }



        
        public void updateTrajectory(Paddle paddle) {
            if (attachedToPaddle) {
                int paddleCenter = paddle.getBounds().x + paddle.getBounds().width / 2;
                int screenCenter = WIDTH / 2;
                double scaleFactor = 0.8;

                if (paddleCenter < screenCenter) {
                    scaleFactor = scaleFactor * (double) paddleCenter / screenCenter;
                    dx = (int) (4 * scaleFactor);
                    dy = -4;
                } else if (paddleCenter > screenCenter) {
                    scaleFactor = scaleFactor * (double) (WIDTH - paddleCenter) / screenCenter;
                    dx = -(int) (4 * scaleFactor);
                    dy = -4;
                } else {
                    dx = 0;
                    dy = -4;
                }
            }
        }



    }