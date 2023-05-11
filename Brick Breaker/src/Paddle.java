import java.awt.Graphics;
import java.awt.Rectangle;

class Paddle {
        private int x, dx = 0;
        private final int WIDTH = 80, HEIGHT = 10;
        private int H,W;

        public Paddle(int x, int y, int W, int H) {
            this.x = x;
            this.H=H;
            this.W=W;
            
        }

        public void move() {
            x += dx;
            if (x < 0) {
                x = 0;
            }
            if (x + WIDTH > W) {
                x = W - WIDTH;
            }
        }

        public void setDx(int dx) {
            this.dx = dx;
        }

        public void draw(Graphics g) {
            g.fillRect(x, H - HEIGHT - 20, WIDTH, HEIGHT);
        }

        public Rectangle getBounds() {
            return new Rectangle(x, H - HEIGHT - 20, WIDTH, HEIGHT);
        }
        
        public void updateBallPosition(Ball ball) {
            if (ball.isAttachedToPaddle()) {
                ball.setX(x + WIDTH / 2 - ball.getDIAMETER() / 2);
                ball.setY(H - HEIGHT - 20 - ball.getDIAMETER());
            }
        }
        
        public boolean isBallOnPaddle(Ball ball) {
            int ballCenterX = ball.getX() + ball.getDIAMETER() / 2;
            return ball.getY() + ball.getDIAMETER() >= H - HEIGHT - 20
                    && ballCenterX >= x && ballCenterX <= x + WIDTH;
        }


    }