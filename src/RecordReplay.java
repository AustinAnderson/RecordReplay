import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JTextField;
import javax.swing.border.Border;

public class RecordReplay extends JFrame{
	private static final long serialVersionUID = 1L;
	private final static int ButtonWidth =300;
    private final static Dimension buttonSize=new Dimension(ButtonWidth,5);
	private List<Point> clickPoints=new ArrayList<Point>();
	private JTextArea recordedPointsDisplay=null;
	private boolean recording=false;
    private JLabel runDisplay=new JLabel(NotRunningText);
    private Robot robot=null;
    public final static String RunningText="Running";
    public final static String NotRunningText="NotRunning";
	public static void main(String[] args) {
    	new RecordReplay();
    }
    public RecordReplay(){
    	try {
			robot=new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
    	this.setLayout(new GridLayout(3,1));
        runDisplay.setVisible(true);
        runDisplay.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        recordedPointsDisplay=new JTextArea(2,3);
        recordedPointsDisplay.setFont(new Font("monospaced",Font.PLAIN,12));
        recordedPointsDisplay.setEditable(false);
        JPanel namePanel=new JPanel();
        namePanel.setLayout(new FlowLayout());
        JLabel displayNamePrompt=new JLabel("Name: ");
        displayNamePrompt.setSize(buttonSize);
        displayNamePrompt.setVisible(true);
        namePanel.add(displayNamePrompt);
        JTextField nameFeild=new JTextField();
        nameFeild.setToolTipText("name for this action");
        nameFeild.setPreferredSize(new Dimension(ButtonWidth,20));
        namePanel.add(nameFeild);
        add(namePanel);
        
        JPanel topPanel=new JPanel();
        topPanel.setLayout(new GridLayout());
        topPanel.add(runDisplay);
        Toolkit.getDefaultToolkit().addAWTEventListener(
        	new AWTEventListener(){
				@Override
				public void eventDispatched(AWTEvent event) {
					addClick(event);
				}
        	},
        	AWTEvent.FOCUS_EVENT_MASK
        );
        initializeButton(topPanel,new JButton("Start Recording"),new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				recording=true;
				runDisplay.setText(getDisplayText());
			}
		});
        initializeButton(topPanel,new JButton("Stop Recording"),new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				recording=false;
				runDisplay.setText(getDisplayText());
			}
		});
        initializeButton(topPanel,new JButton("Clear Recording"),new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
                recordedPointsDisplay.setText("");
                clickPoints.clear();
			}
        });
        initializeButton(topPanel,new JButton("Run"),new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				recording=false;
				for(int i=0;i<clickPoints.size();i++){
					robot.mouseMove(clickPoints.get(i).x, clickPoints.get(i).y);
					trySleep();
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
					trySleep();
				}
			}
        });
        this.add(topPanel);
        this.add(recordedPointsDisplay);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    private void initializeButton(JPanel target,JButton button,ActionListener listener){
    	button.setSize(buttonSize);
    	button.addActionListener(listener);
    	button.setVisible(true);
    	target.add(button);
    }
    private void addClick(AWTEvent event){
       if(recording&&event.paramString().contains("FOCUS_LOST,temporary")){
           System.out.println(MouseInfo.getPointerInfo().getLocation());
           clickPoints.add(MouseInfo.getPointerInfo().getLocation());
           updateTextDisplay();
           this.requestFocus();
           this.setVisible(true);
       }
    }
    private void updateTextDisplay(){
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<clickPoints.size();i++){
            builder.append(" "+String.format("%3d", i)+
                    ":("+String.format("%4d", clickPoints.get(i).x)+","+
                    String.format("%4d",clickPoints.get(i).y)+")");
            if((i+1)%6==0){
                builder.append("\n");
            }
        }
        recordedPointsDisplay.setText(builder.toString());
    }
    public String getDisplayText(){
        if(recording){
            return RunningText;
        }
        return NotRunningText;
    }
    public void trySleep(){
    	try {
			Thread.sleep(20);
		} catch (InterruptedException e) {}
    }
    
}
