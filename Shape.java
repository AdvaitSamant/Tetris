package Tetris;

import java.awt.image.BufferedImage;  
import java.awt.Graphics;  
  
  
public class Shape   
{  
  

private int color;  
  
private int x;  
private int y;   
  
private long time;  
private long lastTime;  
  
private int nrml = 600;  
private int fst = 50;  
  
private int del;  
  
private BufferedImage blk;  
  
private int[][] coordinates;  
  
private int[][] ref;  
  
private int delX;  
  
private Board brd;  
  
  
private boolean isCollided = false;  
private boolean movX = false;  
  
public Shape(int[][] coordinates, BufferedImage blk, Board brd, int color)  
{  

this.coordinates = coordinates;  
this.blk = blk;  
this.brd = brd;  
this.color = color;  
delX = 0;  
x = 4;  
y = 0;  
del = nrml;  
time = 0;  
lastTime = System.currentTimeMillis();  
ref = new int[coordinates.length][coordinates[0].length];  
  
System.arraycopy(coordinates, 0, ref, 0, coordinates.length);  
  
}  
  
public void update()  
{  

movX = true;  
time = time + System.currentTimeMillis() - lastTime;  
lastTime = System.currentTimeMillis();  
  
  
if(isCollided)  
{  
for(int r = 0; r < coordinates.length; r++)  
{  
for(int c = 0; c < coordinates[0].length; c++)  
{  
if(coordinates[r][c] != 0)  
{  
brd.getBoard()[y + r][x + c] = color;  
}  
}  
}  
  
  
checkLine();  
brd.setCurrShape();  
  
}  
  
if(!(x + delX + coordinates[0].length > 10) && !(x + delX < 0))  
{  

for(int r = 0; r < coordinates.length; r++)  
{  
for(int c = 0; c < coordinates[r].length; c++)  
{  
if(coordinates[r][c] != 0)  
{  
if(brd.getBoard()[y + r][x + delX + c] != 0)  
{  
movX = false;  
}  
  
}  
}  
}  
  
if(movX)  
{  

x = x + delX;  
}  
  
}  
  
if(!(y + 1 + coordinates.length > 20))  
{  
  
for(int r = 0; r < coordinates.length; r++)  
{  
for(int c = 0; c < coordinates[r].length; c++)  
{  
if(coordinates[r][c] != 0)  
{     
if(brd.getBoard()[y + 1 + r][x +  c] != 0)  
{  

isCollided = true;  
}  
}  
}  
}  
  
if(time > del)  
{  
y = y + 1;  
time = 0;  
}  
}  
else  
{  

isCollided = true;  
}  
  
delX = 0;  
}  
  
public void render(Graphics gr)  
{  
  
for(int r = 0; r < coordinates.length; r++)  
{  
for(int col = 0; col < coordinates[0].length; col ++)  
{  
if(coordinates[r][col] != 0)  
{  

gr.drawImage(blk, col * 30 + x * 30, r * 30 + y * 30, null);      
}  
}         
}  
  
for(int r = 0; r < ref.length; r++)  
{  
for(int c = 0; c < ref[0].length; c++)  
{  
if(ref[r][r] != 0)  
{  
gr.drawImage(blk, c * 30 + 320, r * 30 + 160, null);      
}     
  
}  
  
}  
  
}  
  
 
private void checkLine()  
{  
int size = brd.getBoard().length - 1;  
  
for(int i = brd.getBoard().length - 1; i > 0; i--)  
{  
int count = 0;  
for(int j = 0; j < brd.getBoard()[0].length; j++)  
{  
if(brd.getBoard()[i][j] != 0)  
{  
  
count = count + 1;  
}  
  
brd.getBoard()[size][j] = brd.getBoard()[i][j];  
}  
if(count < brd.getBoard()[0].length)  
{  
 
size  = size - 1;  
  
}  
  

if(count == brd.getBoard()[0].length)  
{  
brd.addScore();  
}  
}  
}  
  
// a method that handles the scenario, when we press the upward  
// key  
public void rotateShape()  
{  
  
int[][] rotatedShape = null;  
  
rotatedShape = transposeMatrix(coordinates);  
  
rotatedShape = reverseRows(rotatedShape);  
  
if((x + rotatedShape[0].length > 10) || (y + rotatedShape.length > 20))  
{  
return;  
}  
  
for(int row = 0; row < rotatedShape.length; row++)  
{  
for(int col = 0; col < rotatedShape[row].length; col ++)  
{  
if(rotatedShape[row][col] != 0)  
{  
if(brd.getBoard()[y + row][x + col] != 0)  
{  
return;  
}  
}  
}  
}  
coordinates = rotatedShape;  
}  
  
  

private int[][] transposeMatrix(int[][] mtrx)  
{  
int[][] tmp = new int[mtrx[0].length][mtrx.length];  
for (int i = 0; i < mtrx.length; i++)  
{  
for (int j = 0; j < mtrx[0].length; j++)  
{  
tmp[j][i] = mtrx[i][j];  
}  
}  
return tmp;  
}  
  
  

private int[][] reverseRows(int[][] mtrx)  
{  
  
int mdle = mtrx.length / 2;  
  
  
for(int i = 0; i < mdle; i++)  
{  
int[] tmp = mtrx[i];  
  
mtrx[i] = mtrx[mtrx.length - i - 1];  
mtrx[mtrx.length - i - 1] = tmp;  
}  
  
return mtrx;  
  
}  
  
  
  
public int getColor()  
{  
return color;  
}  
  
public void setDeltaX(int delX)  
{  
this.delX = delX;  
}  
  

public void speedUp()  
{  
del = fst;  
}  
  
public void speedDown()  
{  
del = nrml;  
}  
  
public BufferedImage getBlock()  
{  
return blk;  
}  
  
public int[][] getCoords()  
{  
return coordinates;  
}  
  
public int getX()  
{  
return x;  
}  
  
public int getY()  
{  
return y;  
}  
} 