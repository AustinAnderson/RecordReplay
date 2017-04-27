package panels;
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import common.SwingUtil;

public class NamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton homeSetButton=new JButton("Set Home");
    private Robot robot=SwingUtil.getRobot();
    private boolean listenForHome=false;
    private Point home=null;
	public NamePanel(){
        setLayout(new FlowLayout());
        add(new JLabel("Home Location:"));
        SwingUtil.initializeButton(this,homeSetButton,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                listenForHome=true;
                homeSetButton.setText("listening...");
            }
        });
        JLabel displayNamePrompt=new JLabel("Name: ");
        add(displayNamePrompt);
        JTextField nameFeild=new JTextField();
        nameFeild.setToolTipText("name for this action");
        nameFeild.setPreferredSize(SwingUtil.DefaultFeildSize);
        add(nameFeild);
	}
	public boolean isListening(){
		return listenForHome;
	}
	public void setHome(){
        home=MouseInfo.getPointerInfo().getLocation();
        listenForHome=false;
        homeSetButton.setText(SwingUtil.point2Str(home));
        SwingUtil.trySleep();
        SwingUtil.trySleep();
        SwingUtil.trySleep();
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        System.out.println("home is "+home);
	}
	public boolean homeHasBeenSet(){
		return home!=null;
	}
	public Point getHome(){
		return home;
	}
}
