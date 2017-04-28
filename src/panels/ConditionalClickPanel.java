package panels;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import common.SwingUtil;
import frame.RecordReplay;
import step.ClickStep;
import step.ConditionalClickStep;


public class ConditionalClickPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel typeOfClick=new JLabel("_");
	private boolean conditionalClick=false;
	private boolean clickOnNotColor=false;
	public static final String ClickOnColorText="Click On Color";
	public static final String ClickOnNotColorText="Click On Not Color";
	public static final String NeitherText="Normal Click";
	public static final int ClickOnColorKeyCode=KeyEvent.VK_F;
	public static final int ClickOnNotColorKeyCode=KeyEvent.VK_D;
	private RecordReplay mainProg=null;
	public ConditionalClickPanel(final RecordReplay mainProg){
		this.mainProg=mainProg;
        Toolkit.getDefaultToolkit().addAWTEventListener(
        	new AWTEventListener(){
				@Override
				public void eventDispatched(AWTEvent event) {
					try{
						int eventType=event.getID();
						if(eventType==KeyEvent.KEY_PRESSED&&!conditionalClick){
							conditionalClick=true;
                            KeyEvent keyEvent=null;
                            try{ keyEvent=(KeyEvent)event;
                                if(keyEvent.getKeyCode()==ClickOnColorKeyCode){
                                    clickOnNotColor=false;
                                }else if(keyEvent.getKeyCode()==ClickOnNotColorKeyCode){
                                    clickOnNotColor=true;
                                }
                            }catch(ClassCastException ex){}
							updateDisplayLabel();
						}else if(eventType==KeyEvent.KEY_RELEASED&&conditionalClick){
							conditionalClick=false;
							updateDisplayLabel();
						}
						if(eventType==FocusEvent.FOCUS_GAINED){
							conditionalClick=false;
							updateDisplayLabel();
						}
					}catch(ClassCastException ex){
						ex.printStackTrace();
					}
				}
        	},
        	AWTEvent.KEY_EVENT_MASK|AWTEvent.FOCUS_EVENT_MASK
        );
        setLayout(new GridLayout(1,4));
        JLabel typeOfClickLabel=new JLabel("Type Of Click:    ");
        typeOfClickLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(typeOfClickLabel);
        add(typeOfClick);
	}
	public void addClick(){
		if(conditionalClick){
			mainProg.addStep(ConditionalClickStep.create(clickOnNotColor));
		}else{
			mainProg.addStep(ClickStep.create());
		}
	}
	private void updateDisplayLabel(){
		typeOfClick.setText(NeitherText);
		if(conditionalClick){
			typeOfClick.setText(ClickOnColorText);
			if(clickOnNotColor){
				typeOfClick.setText(ClickOnNotColorText);
			}
		}
		
	}

}
