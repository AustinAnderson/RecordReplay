package step;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

import common.SwingUtil;

public class ClickStep implements StepInterface{
	public static ClickStep create(){
		return new ClickStep(MouseInfo.getPointerInfo().getLocation());
	}
	protected Robot robot=SwingUtil.getRobot();
	protected ClickStep(Point where){
		target=where;
	}
	protected Point target=null;
	@Override
	public void action() {
        robot.mouseMove(target.x, target.y);
        SwingUtil.trySleep();
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        SwingUtil.trySleep();
	}
	@Override
	public String toString(){
		return "click"+SwingUtil.point2Str(target);
	}
	
}
