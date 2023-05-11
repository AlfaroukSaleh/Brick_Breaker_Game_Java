
import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Image;



public class  BrickBreakerGame extends JPanel implements ActionListener, KeyListener {

	private static final Color[] COLORS = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.BLACK}; // array of colors for the bricks generator to choose from
	//private static final Color[] COLORS = {Color.BLUE};
	private Image backgroundImage;

    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private Timer timer;
    private Ball ball;
    private Paddle paddle;
    private ArrayList<Brick> bricks;
    private boolean inGame;
    private int score;
    private long startTime;
    private long elapsedTime;
    private long topScore=1000000;
    boolean brokePersonalScore=false;
    
    ArrayList<String> highScorers = new ArrayList<String>();
    
    private String playerName;
    private Connection connection;
    private final String DB_PATH = "jdbc:sqlite:brick_breaker.db";
    private Map<String, Long> playerTopScores;
    private Map<Long, String> topScores;

    private JButton playAgainButton;

    public BrickBreakerGame() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        //initGame();
        try {
            backgroundImage = ImageIO.read(new File("pixel-art-sky-background-with-clouds-cloudy-blue-sky-for-8bit-game-on-white-background-vector.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(e -> playAgain());
        add(playAgainButton, BorderLayout.SOUTH);
        playAgainButton.setVisible(false);
    }

    
    private void initGame() {
        startTime = System.currentTimeMillis();

        inGame = true;
        score = 0;
        //System.out.println(playerName);

        if(fetchSelfHighScore(playerName)!=0) {// to check if this player played this game before or not.
        	topScore=Math.min(topScore,fetchSelfHighScore(playerName));
        }
        
        paddle = new Paddle(WIDTH / 2 - 40, HEIGHT - 30,WIDTH,HEIGHT);
        ball = new Ball(WIDTH / 2, HEIGHT - 50,paddle,WIDTH,HEIGHT);
        bricks = new ArrayList<>();
        generateBricks();

        timer = new Timer(1000 / 60, this);
        timer.start();
    }

    
    private void playAgain() {
        startTime = System.currentTimeMillis();

        inGame = true;
        score = 0;
        
        paddle = new Paddle(WIDTH / 2 - 40, HEIGHT - 30,WIDTH,HEIGHT);
        ball = new Ball(WIDTH / 2, HEIGHT - 50,paddle,WIDTH,HEIGHT);
        bricks.clear();
        generateBricks();
        timer.start();
        repaint();
    }
    
    
    private void generateBricks() { // Generates the initial bricks of the game
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                Color brickColor = COLORS[(int) (Math.random() * COLORS.length)];
                bricks.add(new Brick(10 + j * (90 + 2), 10 + i * (40 + 2), brickColor));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	 if (backgroundImage != null) {
    	        g.drawImage(backgroundImage, 0, 0, this);
    	    }
        
        draw(g);
    }

    
    private void draw(Graphics g) {
    	if (inGame) { // while the game is active (player didn't win or lose)
            playAgainButton.setVisible(false);
            ball.draw(g);
            ball.drawTrajectory(g); 
            paddle.draw(g);
            for (Brick brick : bricks) {
                brick.draw(g);
            }
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Score: " + score, 10, HEIGHT - 10);

            if(topScore!=1000000) // in case the user plays the game for the first time ever (so he has no previous high score)
                g.drawString("Your best time: " + topScore+ " Seconds", 590, HEIGHT - 10);
        } else {
        	 if (bricks.isEmpty()) { // check if all bricks have been destroyed
        		 g.drawString("You Won!", WIDTH / 2 - 30, HEIGHT / 2);
        		 g.drawString("Time Taken: " + elapsedTime + " Seconds", WIDTH / 2 - 60, HEIGHT / 2 + 40);
        		 //if(topScore>elapsedTime) { 
        		 //topScore=Math.min(topScore, elapsedTime); 
        		 //brokePersonalScore=true;
        		 //System.out.println(brokePersonalScore);
        		 //}
        		 //fetchTopScores();
        		  g.drawString("Top 5 High Scores:", 10, HEIGHT - 130);
        		  int i = 1;
        		  
        		  for (String n: highScorers) { // Printing the top scorers with the least elapsed time to destroy the bricks
        			  if(i==6)break;
        			  //System.out.println(n+ " "+ playerTopScores.get(n));
        			  g.drawString(i + ". " + n + ": " + playerTopScores.get(n) + " seconds", 10, HEIGHT - 130 + i * 20);
        			  i++;
        			  
        		  }
        		  //System.out.println(topScores);
        		 /* for (Map.Entry<String,Long> entry : playerTopScores.entrySet()) {
        			  System.out.println(entry.getValue()+ " "+entry.getKey()+"\n");
        		      g.drawString(i + ". " + entry.getValue() + ": " + entry.getKey() + " seconds", 10, HEIGHT - 130 + i * 20);
        		      i++;
        		  }
        		  */
        		  
        		  topScores.clear();
        	 } else {
                 g.drawString("Game Over", WIDTH / 2 - 50, HEIGHT / 2);
             }
            g.drawString("Score: " + score, WIDTH / 2 - 30, HEIGHT / 2 + 20);
            playAgainButton.setVisible(true);
        }
    }

    private long getElapsedTimeInSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            ball.move();
            paddle.move();
            ball.updateTrajectory(paddle); 
            paddle.updateBallPosition(ball);
            checkCollisions();
        }
        repaint();
    }



    private void checkCollisions() {
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.reverseYDirection();
        }

        for (int i = 0; i < bricks.size(); i++) {
            if (ball.getBounds().intersects(bricks.get(i).getBounds())) {
                Color hitBrickColor = bricks.get(i).getColor();
                removeConnectedBricks(i, hitBrickColor);
                ball.reverseYDirection();
                break;
            }
        }

        
        if (bricks.isEmpty()) {
            inGame = false;
            elapsedTime = getElapsedTimeInSeconds();
            //System.out.println(fetchSelfHighScore(playerName)+"\n");
            
            
            
            if(topScore==1000000) { // in case this is the first time for this user
            topScore=elapsedTime;
            saveScore(playerName, elapsedTime);
            }
            
            else if(topScore!=1000000 && topScore>elapsedTime ) {// in case the user broke his previous high score
            	topScore=elapsedTime;
            	updateScore(playerName,elapsedTime);
            }
            fetchTopScores();
        }

        if (ball.getY() >= HEIGHT) {
        	System.out.println(ball.getY()+" "+HEIGHT);
            inGame = false;
        }
    }

    
    private void removeConnectedBricks(int brickIndex, Color brickColor) {
        if (brickIndex < 0 || brickIndex >= bricks.size()) {
            return;
        }

        Brick brick = bricks.get(brickIndex);
        if (!brick.getColor().equals(brickColor)) {
            return;
        }

        Rectangle brickBounds = brick.getBounds();
        bricks.remove(brickIndex);
        score++;

        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, -1, 0, 1};

        for (int direction = 0; direction < 4; direction++) {
            int nextX = brickBounds.x + dx[direction] * (brickBounds.width + 2);
            int nextY = brickBounds.y + dy[direction] * (brickBounds.height + 2);

            for (int i = 0; i < bricks.size(); i++) {
                if (bricks.get(i).getBounds().contains(nextX, nextY)) {
                    removeConnectedBricks(i, brickColor);
                }
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            paddle.setDx(-5);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddle.setDx(5);
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE && ball.isAttachedToPaddle()) {
            ball.setAttachedToPaddle(false); // Detach the ball from the paddle
        }
    }



    @Override
    public void keyReleased(KeyEvent e) {
        paddle.setDx(0);
    }
    
    private void initDatabase() {
        try {
            connection = DriverManager.getConnection(DB_PATH);
            connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS scores (name TEXT, elapsed_time INTEGER);"
            );
            //fetchTopScores();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private void fetchTopScores() { // This Method fetches the top 5 players, who destroyed all bricks in least time possible
        topScores = new TreeMap<>();
        playerTopScores = new HashMap<>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(
                "SELECT name, elapsed_time FROM scores ORDER BY elapsed_time ASC LIMIT 5;"
            );
            while (resultSet.next()) {
            	//System.out.println(resultSet.getString("name")+" "+ resultSet.getLong("elapsed_time") +"\n");
            	highScorers.add(resultSet.getString("name"));
                topScores.put(resultSet.getLong("elapsed_time"), resultSet.getString("name"));
                playerTopScores.put(resultSet.getString("name"), resultSet.getLong("elapsed_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private int fetchSelfHighScore(String playerName) { // fetches the current player's highscore
        int currentHighScore=0;
        try {
            String query = "SELECT elapsed_time FROM scores WHERE name = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, playerName);

            ResultSet resultSet = preparedStatement.executeQuery();

            
            while (resultSet.next()) {
                currentHighScore = resultSet.getInt("elapsed_time");
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currentHighScore;
    }
    
    
    private void saveScore(String name, long elapsedTime) { // saves the score of the current player
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO scores (name, elapsed_time) VALUES (?, ?);"
            );
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, elapsedTime);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    private void updateScore(String name, long elapsedTime) { // update the player's score in case he broke his old highscore
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE scores SET elapsed_time = ? WHERE name = ?;"
            );
            
            preparedStatement.setLong(1, elapsedTime);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Brick Breaker Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            BrickBreakerGame game = new BrickBreakerGame();
            //frame.add(new BrickBreakerGame());
            String playerName = JOptionPane.showInputDialog(frame, "Enter your name:");
            
            // Store the player name to recognize his old and new scores (format is first letter capitalized and the rest of letters are converted to lower case)
            game.setPlayerName(playerName.substring(0, 1).toUpperCase() + playerName.substring(1).toLowerCase());
            game.initDatabase();
            game.initGame();
            frame.add(game);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    
}