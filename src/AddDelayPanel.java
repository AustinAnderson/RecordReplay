import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import common.SwingUtil;
import step.DelayStep;
//test svn commit to git
public class AddDelayPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JSpinner spinner=new JSpinner();
	public AddDelayPanel(final RecordReplay mainDisplay){
		SwingUtil.initializeButton(this, new JButton("Add Delay"), new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mainDisplay.addStep(new DelayStep((int)(double)spinner.getValue()));
			}
		});
        add(new JLabel("Duration (ms): "));
        spinner.setPreferredSize(new Dimension(50,SwingUtil.DefaultFeildSize.height));
        spinner.setModel(new SpinnerNumberModel(20.0,0.0,2000.0,1.0));
        add(spinner);
	}
}
