package Tetris;
import java.awt.Graphics;  
import java.awt.Color;  
import java.awt.event.ActionEvent;  
import java.awt.Rectangle;  
import java.awt.event.MouseEvent;  
import java.awt.event.ActionListener;  
import java.awt.event.MouseMotionListener;  
import java.awt.event.MouseListener;  
import java.io.IOException;  
import java.awt.image.BufferedImage;  
import javax.swing.JPanel;  
import javax.imageio.ImageIO;  
import javax.swing.Timer;  

public class Title extends JPanel implements MouseMotionListener, MouseListener
{
	private static final long serialVerUID = 1L;
	private int mousX,mousY;
	private Rectangle bnds;
	private boolean lftClick = false;
	private BufferedImage ttle;
	private BufferedImage instt;
	private BufferedImage ply;
	private Window win;
	private BufferedImage[] playBtn = new BufferedImage[2];
	private Timer tmr;
	
	public Title(Window win)
	{
		try
		{
			ttle = ImageIO.read(Board.class.getResource("/Title.png"));
			instt = ImageIO.read(Board.class.getResource("/arrow.png"));
			ply = ImageIO.read(Board.class.getResourceAsStream("/play.png"));
		}
		catch(IOException obj)
		{
			obj.printStackTrace();
		}
		tmr = new Timer(1000/60, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				repaint();
			}
		});
		
		tmr.start();
		mousX = 0;
		mousY = 0;
		
		playBtn[0] = ply.getSubimage(0,0,100,80);
		playBtn[1] = ply.getSubimage(100, 0, 100, 80);
		
		bnds = new Rectangle(Window.WIDTH / 2-50, Window.HEIGHT / 2 - 100, 100, 80);
		
		this.win = win;
	}
	public void painComponent(Graphics gr)
	{
		super.paintComponent(gr);
		if(lftClick && bnds.contains(mousX,mousY))
		{
			win.startTetris();
		}
		gr.setColor(Color.BLACK);
		gr.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		gr.drawImage(ttle, Window.WIDTH / 2 - ttle.getWidth() / 2, Window.HEIGHT / 2 - ttle.getHeight() / 2 - 200, null);
		gr.drawImage(instt, Window.WIDTH / 2 - instt.getWidth() / 2, Window.HEIGHT / 2 - instt.getHeight() / 2 + 150,null);
		
		if(bnds.contains(mousX,mousY))
		{
			gr.drawImage(playBtn[0],  Window.WIDTH / 2 - 50, Window.HEIGHT / 2 - 100,null);
		}
		else
		{
			gr.drawImage(playBtn[1], Window.WIDTH / 2 - 50, Window.HEIGHT / 2 - 100,null);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent me)
	{
		
	}
	
	@Override
	public void mousePressed (MouseEvent me)
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
}
