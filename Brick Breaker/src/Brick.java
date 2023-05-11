import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

class Brick {
        private int x, y;
        private final int WIDTH = 90;
        private final int HEIGHT = 40;
        private Color fillColor;

        public Brick(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.fillColor = color;
        }

        public void draw(Graphics g) {
            g.setColor(fillColor);
            g.fillRect(x, y, WIDTH, HEIGHT);
            g.setColor(Color.WHITE);
            g.drawRect(x, y, WIDTH, HEIGHT);
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, WIDTH, HEIGHT);
        }
        
        public Color getColor() {
            return fillColor;
        }

        
    }