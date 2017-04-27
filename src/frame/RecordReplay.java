package frame;
import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import common.SwingUtil;
import panels.AddDelayPanel;
import panels.ConditionalClickPanel;
import panels.CurrentColorDisplayPanel;
import panels.NamePanel;
import step.ClickStep;
import step.StepInterface;

public class RecordReplay extends JFrame{
	private static final long serialVersionUID = 1L;
	private List<StepInterface> stepList=new ArrayList<StepInterface>();
	private JTextArea recordedPointsDisplay=null;
	private boolean recording=false;
    private JLabel runDisplay=new JLabel(NotRunningText);
    private Robot robot=SwingUtil.getRobot();
    private NamePanel namePanel=new NamePanel();
    private ConditionalClickPanel clickPanel=new ConditionalClickPanel(this);
    public final static String RunningText="Recording";
    public final static String NotRunningText="Not Recording";
	public static void main(String[] args) {
    	new RecordReplay();
    }
    public RecordReplay(){
    	try {
			robot=new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
    	this.setLayout(new GridLayout(4,1));
        runDisplay.setVisible(true);
        runDisplay.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        recordedPointsDisplay=new JTextArea(2,3);
        recordedPointsDisplay.setFont(new Font("monospaced",Font.PLAIN,12));
        recordedPointsDisplay.setEditable(false);
        
        JPanel topPanel=new JPanel();
        Toolkit.getDefaultToolkit().addAWTEventListener(
        	new AWTEventListener(){
				@Override
				public void eventDispatched(AWTEvent event) {
					if(namePanel.isListening()){
						namePanel.setHome();
					}else{
						addClickEvent(event);
					}
				}
        	},
        	AWTEvent.FOCUS_EVENT_MASK
        );
        SwingUtil.initializeButton(topPanel,new JButton("Start Recording"),new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				recording=true;
				runDisplay.setText(getDisplayText());
			}
		});
        SwingUtil.initializeButton(topPanel,new JButton("Stop Recording"),new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				recording=false;
				runDisplay.setText(getDisplayText());
			}
		});
        SwingUtil.initializeButton(topPanel,new JButton("Clear Recording"),new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
                recordedPointsDisplay.setText("");
                stepList.clear();
			}
        });
        SwingUtil.initializeButton(topPanel, new JButton("Remove Last"), new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				stepList.remove(stepList.size()-1);
				updateTextDisplay();
			}
        });
        SwingUtil.initializeButton(topPanel,new JButton("Run"),new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				recording=false;
				for(int i=0;i<stepList.size();i++){
					stepList.get(i).action();
				}
			}
        });
        add(new CurrentColorDisplayPanel());
        add(namePanel);
        topPanel.setLayout(new GridLayout());
        topPanel.add(runDisplay);
        JPanel clickAndDelayPanel=new JPanel();
        clickAndDelayPanel.setLayout(new GridLayout(1,2));
        clickAndDelayPanel.add(new AddDelayPanel(this));
        clickAndDelayPanel.add(clickPanel);
        add(clickAndDelayPanel);
        add(topPanel);
        add(recordedPointsDisplay);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    public void addStep(StepInterface step){
        stepList.add(step);
        updateTextDisplay();
    }
    private void addClickEvent(AWTEvent event){
       if(recording&&event.paramString().contains("FOCUS_LOST,temporary")){
           System.out.println(MouseInfo.getPointerInfo().getLocation());
           clickPanel.addClick();
           if(namePanel.homeHasBeenSet()){//try gaining focus by clicking the icon on the task bar
        	   Point home=namePanel.getHome();
        	   Point current=MouseInfo.getPointerInfo().getLocation();
        	   SwingUtil.trySleep(200);
        	   robot.mouseMove(home.x, home.y);
        	   SwingUtil.trySleep(30);
        	   robot.mousePress(InputEvent.BUTTON1_MASK);
        	   SwingUtil.trySleep(30);
        	   robot.mouseRelease(InputEvent.BUTTON1_MASK);
        	   SwingUtil.trySleep(30);
        	   robot.mouseMove(current.x, current.y);
        	   SwingUtil.trySleep(30);
           }else{//if we can't just request focus
        	   super.requestFocus();
        	   super.setVisible(true);
           }
       }
    }
    private void updateTextDisplay(){
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<stepList.size();i++){
            builder.append(" "+String.format("%3d", i)+
                    ":"+stepList.get(i));
            if((i+1)%6==0){
                builder.append("\n");
            }
        }
        recordedPointsDisplay.setText(builder.toString());
    }
    private String getDisplayText(){
        if(recording){
            return RunningText;
        }
        return NotRunningText;
    }
    
}