package frame;
import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.SwingConstants;

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
    private JButton runStopButton=new JButton(getRunStopButtonText());
    private Robot robot=SwingUtil.getRobot();
    private NamePanel namePanel=new NamePanel();
    private ConditionalClickPanel clickPanel=new ConditionalClickPanel(this);
    public final static String RunningText="Recording";
    public final static String NotRunningText="Not Recording";
	public static void main(String[] args) {
    	EventQueue.invokeLater(new Runnable(){
    		@Override
    		public void run(){
    			new RecordReplay();
    		}
    	});
    }
    public String getRunStopButtonText(){
    	String toReturn="Start Recording";
    	if(recording){
    		toReturn="Stop Recording";
    	}
    	return toReturn;
    }
    public RecordReplay(){
        runDisplay.setVisible(true);
        runDisplay.setBorder(BorderFactory.createLineBorder(new Color(122,138,153), 1));
        runDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        recordedPointsDisplay=new JTextArea(2,3);
        recordedPointsDisplay.setFont(new Font("monospaced",Font.PLAIN,12));
        recordedPointsDisplay.setEditable(false);
        
        JPanel actionButtonsPanel=new JPanel();
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
        SwingUtil.initializeButton(actionButtonsPanel,runStopButton,SwingUtil.DefaultButtonSize,new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				recording=!recording;
				runDisplay.setText(getDisplayText());
				runStopButton.setText(getRunStopButtonText());
			}
		});
        SwingUtil.initializeButton(actionButtonsPanel,new JButton("Clear Recording"),SwingUtil.DefaultButtonSize,new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
                recordedPointsDisplay.setText("");
                stepList.clear();
			}
        });
        SwingUtil.initializeButton(actionButtonsPanel, new JButton("Remove Last"),SwingUtil.DefaultButtonSize, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				stepList.remove(stepList.size()-1);
				updateTextDisplay();
			}
        });
        SwingUtil.initializeButton(actionButtonsPanel,new JButton("Run"),SwingUtil.DefaultButtonSize,new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				recording=false;
				for(int i=0;i<stepList.size();i++){
					stepList.get(i).action();
				}
			}
        });
        this.setLayout(new GridLayout(2,1));
        JPanel top=new JPanel();
    	top.setLayout(new GridBagLayout());
    	GridBagConstraints c=new GridBagConstraints();
    	c.gridx=0;
    	c.gridy=0;
        top.add(namePanel,c);
        	JPanel clickAndDelayPanel=new JPanel();
        	clickAndDelayPanel.setLayout(new GridLayout(1,2));
        	clickAndDelayPanel.add(new AddDelayPanel(this));
        	clickAndDelayPanel.add(clickPanel);
    	c.gridx=0;
    	c.gridy=1;
        top.add(clickAndDelayPanel,c);
        
    	c.gridx=1;
    	c.gridy=0;
    	c.gridheight=2;
    	CurrentColorDisplayPanel colorDisplay=new CurrentColorDisplayPanel();
    	top.add(colorDisplay,c);
    	c.gridheight=1;
    	
        	actionButtonsPanel.setLayout(new GridLayout());
        	actionButtonsPanel.add(runDisplay);
    	c.gridx=0;
    	c.gridy=2;
    	c.gridwidth=2;
        top.add(actionButtonsPanel,c);
        add(top);
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
        	   super.toFront();
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