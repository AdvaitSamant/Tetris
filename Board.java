package Tetris;

import java.awt.Color;  
import java.awt.BasicStroke;  
import java.awt.Font;  
import java.awt.Graphics2D;  
import java.awt.Graphics;  
import java.awt.Rectangle;  
import java.awt.event.ActionListener;  
import java.awt.event.ActionEvent;  
import java.awt.event.MouseMotionListener;  
import java.awt.event.KeyEvent;  
import java.awt.event.MouseListener;  
import java.awt.event.KeyListener;  
import java.awt.image.BufferedImage;  
import java.awt.event.MouseEvent;  
import javax.sound.sampled.AudioSystem;  
import javax.sound.sampled.LineUnavailableException;  
import javax.swing.JPanel;  
import javax.imageio.ImageIO;  
import javax.swing.Timer;  
import javax.sound.sampled.Clip;  
  
public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener  
{  
  
private static final long serialVerUID = 1L;  
  
private Clip msic;  
  
private BufferedImage blcks;  
private BufferedImage bkground;  
private BufferedImage paus;  
private BufferedImage rfresh;  
  
//the dimensions of the board (the area of playing the game)  
  
private final int brdHeight = 20;  
private final int brdWidth = 10;  
  
// the size of block used in the Tetris game  
  
private final int blckSize = 30;  
  
private int[][] board = new int[brdHeight][brdWidth];  
  
// array for all of the shapes that are possible in the game  
  
private Shape[] diffShapes = new Shape[7];  
  
// for the current shape  
  
private static Shape currShape;  
  
// for the next shape  
private static Shape nxtShape;  
  
// for the game loop  
  
private Timer timerLooper;  
  
private int FPS = 60;  
  
private int delay = 1000 / FPS;  
  
// variables for the mouse events  
  
private int mousX;   
private int mousY;   
  
private boolean lftClick = false;  
  
private Rectangle stpBounds;  
private Rectangle rfreshBounds;  
  
// a boolean fields that is true when the   
// pause button is pressed  
private boolean gmPaused = false;  
  
// a boolean field that is true when the top  
// row is occupied  
private boolean gmOver = false;  
  
// buttons press lapse  
  
private Timer btnLapse = new Timer(300, new ActionListener()  
{  
  
@Override  
public void actionPerformed(ActionEvent ae)   
{  
btnLapse.stop();  
}});  
  
// for handling the total score  
  
private int totalScore = 0;  
  
  
public Board()  
{  
// loading the Assets  
blcks = ImageLoader.loadImage("/tiles.png");  
  
bkground = ImageLoader.loadImage("/background.png");  
paus = ImageLoader.loadImage("/pause.png");  
rfresh = ImageLoader.loadImage("/refresh.png");  
  
msic = ImageLoader.loadMusic("/music.wav");  
  
msic.loop(Clip.LOOP_CONTINUOUSLY);  
  
mousX = 0;  
mousY = 0;  
  
stpBounds = new Rectangle(350, 500, paus.getWidth(), paus.getHeight() + paus.getHeight() / 2);  
rfreshBounds = new Rectangle(350, 500 - rfresh.getHeight() - 20, rfresh.getWidth(), rfresh.getHeight() + rfresh.getHeight() / 2);  
  
// creating the game timerLooper  
  
timerLooper = new Timer(delay, new GameLooper());  
  
// creating the differnt Tetrominoes   
  
diffShapes[0] = new Shape(new int[][]  
{  
{1, 1, 1, 1}   // creating a linear shape  
}, blcks.getSubimage(0, 0, blckSize, blckSize), this, 1);  
  
diffShapes[1] = new Shape(new int[][]  
{  
{1, 1, 1},  
{0, 1, 0},   // creating a T shape  
}, blcks.getSubimage(blckSize, 0, blckSize, blckSize), this, 2);  
  
diffShapes[2] = new Shape(new int[][]  
{  
{1, 1, 1},  
{1, 0, 0},   // creating a L shape  
}, blcks.getSubimage(blckSize * 2, 0, blckSize, blckSize), this, 3);  
  
diffShapes[3] = new Shape(new int[][]{  
{1, 1, 1},  
{0, 0, 1},   // creating a J shape;  
}, blcks.getSubimage(blckSize * 3, 0, blckSize, blckSize), this, 4);  
  
diffShapes[4] = new Shape(new int[][]  
{  
{0, 1, 1},  
{1, 1, 0},   // creating a S shape;  
}, blcks.getSubimage(blckSize * 4, 0, blckSize, blckSize), this, 5);  
  
diffShapes[5] = new Shape(new int[][]  
{  
{1, 1, 0},  
{0, 1, 1},   // creating a Z shape;  
}, blcks.getSubimage(blckSize * 5, 0, blckSize, blckSize), this, 6);  
  
diffShapes[6] = new Shape(new int[][]  
{  
{1, 1},  
{1, 1},   // creating a square shape;  
}, blcks.getSubimage(blckSize * 6, 0, blckSize, blckSize), this, 7);  
  
  
}  
  
private void update()  
{     
// pressing the pause button toggles the game state  
// if the the player is playing the game, then the game is paused  
// if the game is already paused, then the game starts  
if(stpBounds.contains(mousX, mousY) && lftClick && !btnLapse.isRunning() && !gmOver)  
{  
btnLapse.start();  
gmPaused = !gmPaused;  
}  
  
// pressing the refresh button starts the game from the beginning  
if(rfreshBounds.contains(mousX, mousY) && lftClick)  
{  
sttGame();  
}  
if(gmPaused || gmOver)  
{  
return;  
}  
currShape.update();  
}  
  
  
public void paintComponent(Graphics gr)  
{  
super.paintComponent(gr);  
  
gr.drawImage(bkground, 0, 0, null);  
  
  
for(int r = 0; r < board.length; r++)  
{  
for(int c = 0; c < board[r].length; c++)  
{  
  
if(board[r][c] != 0)  
{  
// drawing the tetromino that are currently residing on the board, after they have fall down  
gr.drawImage(blcks.getSubimage((board[r][c] - 1) * blckSize, 0, blckSize, blckSize), c * blckSize, r * blckSize, null);  
}                 
  
}  
}  
for(int r = 0; r < nxtShape.getCoords().length; r++)  
{  
for(int c = 0; c < nxtShape.getCoords()[0].length; c++)  
{  
if(nxtShape.getCoords()[r][c] != 0)  
{  
// drawing the next shape on the board, on the top right hand side  
gr.drawImage(nxtShape.getBlock(), c * 30 + 320, r * 30 + 50, null);   
}  
}         
}  
currShape.render(gr);  
  
if(stpBounds.contains(mousX, mousY))  
{  
gr.drawImage(paus.getScaledInstance(paus.getWidth() + 3, paus.getHeight() + 3, BufferedImage.SCALE_DEFAULT), stpBounds.x + 3, stpBounds.y + 3, null);  
}  
else  
{  
gr.drawImage(paus, stpBounds.x, stpBounds.y, null);  
}  
  
if(rfreshBounds.contains(mousX, mousY))  
{  
// when the mouse hovers over the refresh button, it shifts a bit   
gr.drawImage(rfresh.getScaledInstance(rfresh.getWidth() + 3, rfresh.getHeight() + 3, BufferedImage.SCALE_DEFAULT), rfreshBounds.x + 3, rfreshBounds.y + 3, null);  
}  
else  
{  
// when the mouse goes away the refresh button, regains its place  
gr.drawImage(rfresh, rfreshBounds.x, rfreshBounds.y, null);  
}  
  
if(gmPaused)  
{  
// if the pause button is pressed   
// game should be paused and the GAME PAUSED   
// message is displayed on the screen  
String gmPausedString = "GAME PAUSED";  
  
// message is in the white color  
gr.setColor(Color.WHITE);  
  
// setting the font  
gr.setFont(new Font("Georgia", Font.BOLD, 30));  
  
// displaying the message  
gr.drawString(gmPausedString, 35, Window.HEIGHT / 2);  
}  
if(gmOver)  
{  
// when the Game is over, the GAME OVER message is displayed  
String gmOverString = "GAME OVER";  
// message is in the white color  
gr.setColor(Color.WHITE);  
  
// setting the font  
gr.setFont(new Font("Georgia", Font.BOLD, 30));  
  
// displaying the message  
gr.drawString(gmOverString, 50, Window.HEIGHT / 2);  
}     
  
// color is white for displaying score  
gr.setColor(Color.WHITE);  
  
// font is Georgia for displaying the score  
gr.setFont(new Font("Georgia", Font.BOLD, 20));  
  
// displaying the String "SCORE"  
gr.drawString("SCORE", Window.WIDTH - 125, Window.HEIGHT / 2);  
  
// displaying the total score  
gr.drawString(totalScore + "", Window.WIDTH - 125, Window.HEIGHT / 2 + 30);  
  
Graphics2D gr2d = (Graphics2D)gr;  
  
gr2d.setStroke(new BasicStroke(2));  
gr2d.setColor(new Color(0, 0, 0, 100));  
  
// for drawing the horizontal lines across the board  
for(int i = 0; i <= brdHeight; i++)  
{  
gr2d.drawLine(0, i * blckSize, brdWidth * blckSize, i * blckSize);  
}  
  
// for drawing the vertical lines across the board  
for(int i = 0; i <= brdWidth; i++)  
{  
gr2d.drawLine(i * blckSize, 0, i * blckSize, brdHeight * 30);  
}  
}  
  
// a method that randomly determines the next Tetrominoes   
// that is going to come in the game using the Math.random() method  
public void setNxtShape()  
{  
int idx = (int)(Math.random() * diffShapes.length);  
nxtShape = new Shape(diffShapes[idx].getCoords(), diffShapes[idx].getBlock(), this, diffShapes[idx].getColor());  
}  
  
  
public void setCurrShape()  
{  
// the next Tetromino found   
// is the current shape  
currShape = nxtShape;  
setNxtShape();  
  
// We know that the current Tetromino will fall down and   
// will occupy some of the playing area. Therefore, it   
// is important to check whether the playing area is filled  
// upto the top row or not. If yes, then the game is over.  
for(int r = 0; r < currShape.getCoords().length; r++)  
{  
for(int c = 0; c < currShape.getCoords()[0].length; c++)  
{  
if(currShape.getCoords()[r][c] != 0)  
{  
if(board[currShape.getY() + r][currShape.getX() + c] != 0)  
{  
// if the control reaches here, then it means the game is over.  
gmOver = true;  
}  
}  
}         
}  
  
}  
  
  
public int[][] getBoard()  
{  
return board;  
}  
  
@Override  
public void keyPressed(KeyEvent ke)   
{  
if(ke.getKeyCode() == KeyEvent.VK_UP)  
{  
// for rotating the shape  
// up arrow key is pressed.  
currShape.rotateShape();  
}  
if(ke.getKeyCode() == KeyEvent.VK_RIGHT)  
{  
// for moving the Tetromino in the   
// right direction, the right arrow key is pressed  
currShape.setDeltaX(1);  
}  
if(ke.getKeyCode() == KeyEvent.VK_LEFT)  
{  
// for moving the Tetromino in the   
// left direction, the left arrow key is pressed  
currShape.setDeltaX(-1);  
}  
if(ke.getKeyCode() == KeyEvent.VK_DOWN)  
{  
  
// when the down arrow key is pressed and released,  
// the tetrominos will fall quickly towards  
// bottom  
currShape.speedUp();  
}  
}  
@Override  
public void keyReleased(KeyEvent ke)   
{  
if(ke.getKeyCode() == KeyEvent.VK_DOWN)  
{  
// when the down arrow key is released  
// the speed with which the tetromino is falling down  
// goes back to the normal  
currShape.speedDown();  
}  
}  
  
@Override  
public void keyTyped(KeyEvent ke)   
{  
  
}  
  
public void sttGame()  
{  
// start game occurs after the stop game.  
stpGame();  
  
// in the start game, next tetromino is determined  
// and the current tetromino is also determined  
setNxtShape();  
setCurrShape();  
// as the game is going to start, the gmOver variable is set to false  
// and the timer starts  
gmOver = false;  
timerLooper.start();  
  
}  
public void stpGame()  
{  
// in the stop game,  
// totalScore is set to zero,  
// and the playing aread is cleared  
totalScore = 0;  
  
for(int r = 0; r < board.length; r++)  
{  
for(int c = 0; c < board[r].length; c++)  
{  
board[r][c] = 0;  
}  
}  
timerLooper.stop();  
}  
  
  
class GameLooper implements ActionListener  
{  
  
@Override  
public void actionPerformed(ActionEvent ae)   
{  
update();  
repaint();  
}  
  
}  
  
  
// getting the coordinates of the mouse events  
@Override  
public void mouseDragged(MouseEvent me)   
{  
mousX = me.getX();  
mousY = me.getY();  
}  
  
@Override  
public void mouseMoved(MouseEvent me)   
{  
mousX = me.getX();  
mousY = me.getY();  
}  
  
@Override  
public void mouseClicked(MouseEvent me)   
{  
  
}  
  
@Override  
public void mousePressed(MouseEvent me)   
{  
if(me.getButton() == MouseEvent.BUTTON1)  
{  
lftClick = true;  
}  
}  
  
@Override  
public void mouseReleased(MouseEvent me)   
{  
if(me.getButton() == MouseEvent.BUTTON1)  
{  
lftClick = false;  
}  
}  
  
@Override  
public void mouseEntered(MouseEvent me)   
{  
  
}  
  
@Override  
public void mouseExited(MouseEvent me)   
{  
  
}  
  
// incrementing the total score by 1  
public void addScore()  
{  
totalScore = totalScore + 1;  
}  
  
}