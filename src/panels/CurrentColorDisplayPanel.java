package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import common.SwingUtil;

public class CurrentColorDisplayPanel extends JPanel{
	private final static int SAMPLE_PIXELS_FROM_CENTER=5;//how many pixels in each direction to sample in addition to the current position's
	private final static int BigPixelScale=5;//the side length of the square for each sampled pixel
	private final static int SAMPLE_PIXELS_COUNT=(2*SAMPLE_PIXELS_FROM_CENTER)+1;
	private final static int TopMargin=BigPixelScale*2;
	private String currentColor=null;
	private Robot robot=SwingUtil.getRobot();
	@Override
	public void paintComponent(Graphics g){
		Point current=MouseInfo.getPointerInfo().getLocation();
		BufferedImage capture=robot.createScreenCapture(
			new Rectangle(
				current.x-SAMPLE_PIXELS_FROM_CENTER,
				current.y-SAMPLE_PIXELS_FROM_CENTER,
				SAMPLE_PIXELS_COUNT,SAMPLE_PIXELS_COUNT
			)
		);
		Image image=capture.getScaledInstance(
			BigPixelScale*SAMPLE_PIXELS_COUNT,
			BigPixelScale*SAMPLE_PIXELS_COUNT,
			Image.SCALE_FAST
		);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, BigPixelScale*SAMPLE_PIXELS_COUNT, TopMargin);
		g.setColor(Color.BLACK);
		Font currentFont=g.getFont();
		g.setFont(currentFont.deriveFont((float)(BigPixelScale*1.75)));
		currentColor=SwingUtil.color2Str(
            new Color(
                capture.getRGB(
                    SAMPLE_PIXELS_FROM_CENTER,
                    SAMPLE_PIXELS_FROM_CENTER
                )
            )
        );
		g.drawString(currentColor,0,TopMargin/2+TopMargin/4);
		g.drawImage(image, 0, TopMargin, null);
		g.setColor(Color.white);
		int startCoord=SAMPLE_PIXELS_FROM_CENTER*BigPixelScale-1;
		g.drawRect(startCoord,startCoord+TopMargin,BigPixelScale+1,BigPixelScale+1);
		g.setColor(Color.BLACK);
		g.drawRect(startCoord-1,startCoord-1+TopMargin,BigPixelScale+3,BigPixelScale+3);
	}
	public CurrentColorDisplayPanel(){
		new Timer().schedule(new TimerTask(){
			@Override
			public void run() {
				repaint();
			}
		}, 0,50);
		int sideLength=((SAMPLE_PIXELS_FROM_CENTER*2)+1)*BigPixelScale;
		this.setMinimumSize(new Dimension(sideLength,sideLength+TopMargin));
	}
	public String getCurrentColor(){
		return currentColor;
	}
	public static void main(String[] args){
		JFrame testFrame=new JFrame();
		CurrentColorDisplayPanel drawPanel=new CurrentColorDisplayPanel();
		int sideLength=((SAMPLE_PIXELS_FROM_CENTER*2)+1)*BigPixelScale;
		drawPanel.setPreferredSize(new Dimension(sideLength,sideLength+TopMargin));
		drawPanel.setVisible(true);
		testFrame.add(drawPanel);
		testFrame.pack();
		testFrame.setVisible(true);
	}
}
