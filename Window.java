package Tetris;
import javax.swing.JFrame;
public class Window 
{
	public static final int WIDTH = 445;
	public static final int HEIGHT = 629;
	
	private Board boardObj;
	private Title titleObj;
	private JFrame windowFrame;
	
	public Window()
	{
		windowFrame = new JFrame("Tetris");
		windowFrame.setSize(WIDTH,HEIGHT);
		windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setResizable(false);
		
		boardObj = new Board();
		titleObj = new Title(this);
		
		windowFrame.addKeyListener(boardObj);
		windowFrame.addMouseMotionListener(titleObj);
		windowFrame.addMouseListener(titleObj);
		windowFrame.setVisible(true);
	}
	
	public void startTetris()
	{
		windowFrame.remove(titleObj);
		windowFrame.addMouseMotionListener(boardObj);
		windowFrame.addMouseListener(boardObj);
		windowFrame.add(boardObj);
		boardObj.sttGame();
		windowFrame.revalidate();
	}
	
	public static void main(String[] args)
	{
		new Window();
	}
}
