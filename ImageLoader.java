package Tetris;
import java.io.IOException;  
import java.awt.image.BufferedImage;  
import javax.sound.sampled.AudioSystem;  
import javax.imageio.ImageIO;  
import javax.sound.sampled.Clip;  

public class ImageLoader 
{
	public static BufferedImage loadImage(String strPath)
	{
		try
		{
			System.out.println(strPath);
			return ImageIO.read(ImageLoader.class.getResource(strPath));
		}
		catch(IOException obj)
		{
			obj.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
	public static Clip loadMusic(String dir)
	{
		try
		{
			Clip clp = AudioSystem.getClip();
			clp.open(AudioSystem.getAudioInputStream(ImageLoader.class.getResource(dir)));
		}
		catch(Exception obj)
		{
			obj.printStackTrace();
		}
		return null;
	}
}
