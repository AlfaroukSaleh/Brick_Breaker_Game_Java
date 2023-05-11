# Brick_Breaker_Game_Java
Building a Brick Breaker game using Java and utilizing the Swing GUI and connecting the game to database using Jdbc to save the high scores of players.
Welcome to my Brick Breaker game, I hope you enjoy it ^_^

The folder includes the background image and the sqlite database file including the previous high scores.

Just run the BrickBreakerGame.java class and the game will start.

The game will ask you to enter your name to fetch your current high score from the database in case you played this game before, or create a new record for you in case it's your first time playing the game. Names are stored in the format "Name" with first letter capitalized and the rest of letters are stored in lower case.

![Screen Shot 2023-05-11 at 3 18 49 AM](https://github.com/AlfaroukSaleh/Brick_Breaker_Game_Java/assets/50967999/6981571f-96e6-4bbd-a131-876eddc35e65)


You play the game, and try to shoot the ball on bricks to destroy the 40 bricks, in case the ball falls and you don't catch it with the paddle, you lose the game. (You control the paddle's position using right and left arrows, and shoot the ball using the space bar).

![Screen Shot 2023-05-11 at 3 19 19 AM](https://github.com/AlfaroukSaleh/Brick_Breaker_Game_Java/assets/50967999/96a659bc-7188-4551-bd03-d2ee82d2a8ed)

![Screen Shot 2023-05-11 at 3 19 53 AM](https://github.com/AlfaroukSaleh/Brick_Breaker_Game_Java/assets/50967999/102458bf-5c33-4e9f-853c-c5e9fa68f55b)

![Screen Shot 2023-05-11 at 3 20 23 AM](https://github.com/AlfaroukSaleh/Brick_Breaker_Game_Java/assets/50967999/dd0a5bf9-0281-4802-a5fe-152564ab2484)

In case you destroy the 40 bricks, your score will be 40 (equal to the number of bricks) and your elapsed time to destroy the 40 bricks will be calculated. 

In case your new elapsed time is lower than your own previous high score, then it will be updated in the database.

Also in case your score qualifies to be in the top 5 high scores of all players, then it will be placed in the right position in the top 5 high scores. The top 5 high scores are sorted ascendingly according to the elapsed time to win. The shorter time you take the better.

![Screen Shot 2023-05-11 at 3 21 41 AM](https://github.com/AlfaroukSaleh/Brick_Breaker_Game_Java/assets/50967999/655b5e5c-1ce1-4bf6-92af-54f02e05b55b)

Ps: In case you face any problems with the database, make sure the Jdbc jar is added in the project properties.
