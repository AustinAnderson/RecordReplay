import java.awt.AWTEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import common.SwingUtil;


public class ConditionalClickPanel extends JPanel {

	private JLabel label=new JLabel("_");
	private JLabel currentColor=null;
	private Robot robot=SwingUtil.getRobot();
	private boolean listening=false;
	private Timer updateMouseColor=new Timer();
	public ConditionalClickPanel(){
        Toolkit.getDefaultToolkit().addAWTEventListener(
        	new AWTEventListener(){
				@Override
				public void eventDispatched(AWTEvent event) {
					try{
						int eventType=event.getID();
						if(eventType==KeyEvent.KEY_PRESSED&&!listening){
							listening=true;
							label.setText("@");
						}else if(eventType==KeyEvent.KEY_RELEASED&&listening){
							listening=false;
							label.setText("_");
						}
						if(eventType==FocusEvent.FOCUS_GAINED){
							listening=false;
							label.setText("_");
						}
					}catch(ClassCastException ex){
						ex.printStackTrace();
					}
				}
        	},
        	AWTEvent.KEY_EVENT_MASK|AWTEvent.FOCUS_EVENT_MASK
        );
        currentColor=new JLabel();
        updateMouseColor.schedule(new TimerTask(){
        	public void run(){
        		updateCurrentColor();
        	}
        },0,5);
        add(currentColor);
        add(label);
	}
	public void updateCurrentColor(){
		Point p=MouseInfo.getPointerInfo().getLocation();
		currentColor.setText(SwingUtil.color2Str(robot.getPixelColor(p.x, p.y)));
		
	}

}
