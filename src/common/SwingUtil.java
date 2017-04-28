package common;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public final class SwingUtil {
	public final static int DefaultButtonWidth =125;
    public final static Dimension DefaultButtonSize=new Dimension(DefaultButtonWidth,25);
    public final static Dimension DefaultFeildSize=new Dimension(300,20);
	private static Robot robotInstance=null;
	public final static int DefaultSleepTime=40;
	
	private SwingUtil(){}//with this and final makes it only for static methods
	public static Robot getRobot(){
		if(robotInstance==null){
			try {
				robotInstance=new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
		return robotInstance;
	}
	public static Dimension widthModifiedDefaultButtonSize(int delta){
		return new Dimension(DefaultButtonWidth+delta,DefaultButtonSize.height);
	}
    public static void initializeButton(JPanel target,JButton button,ActionListener listener){
    	button.addActionListener(listener);
    	button.setVisible(true);
    	target.add(button);
    }
    public static void initializeButton(JPanel target,JButton button,Dimension size,ActionListener listener){
    	button.setPreferredSize(size);
    	initializeButton(target,button,listener);
    }
    public static String point2Str(Point p){
    	String toReturn="(null,null)";
    	if(p!=null){
    		toReturn="("+String.format("%4d",p.x)+","+
                         String.format("%4d",p.y)+")";
    	}
    	return toReturn;
    }
    public static void trySleep(){
    	trySleep(DefaultSleepTime);
    }
    public static void trySleep(int millis){
    	try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {}
    }
    public static String color2Str(Color c){
    	return ("#"+
    		String.format("%02X",c.getRed())+
    		String.format("%02X",c.getGreen())+
    		String.format("%02X",c.getBlue())
    	).toUpperCase();
    	
    }
}
