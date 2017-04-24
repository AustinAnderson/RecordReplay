package common;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public abstract class SwingUtil {
	public final static int DefaultButtonWidth =300;
    public final static Dimension DefaultButtonSize=new Dimension(DefaultButtonWidth,5);
    public final static Dimension DefaultFeildSize=new Dimension(SwingUtil.DefaultButtonWidth,20);
	private static Robot robotInstance=null;
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
    public static void initializeButton(JPanel target,JButton button,ActionListener listener){
    	button.setSize(DefaultButtonSize);
    	button.addActionListener(listener);
    	button.setVisible(true);
    	target.add(button);
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
    	trySleep(20);
    }
    public static void trySleep(int millis){
    	try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {}
    }
}
