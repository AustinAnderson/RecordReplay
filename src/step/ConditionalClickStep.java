package step;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;

import common.SwingUtil;

public class ConditionalClickStep extends ClickStep {

	
	public static ConditionalClickStep create(boolean clickOnNotColor){
		Point location=MouseInfo.getPointerInfo().getLocation();
		return new ConditionalClickStep(
			location,
			SwingUtil.getRobot().getPixelColor(location.x, location.y),
			clickOnNotColor
		);
	}
	private boolean clickOnNotColor=false;
	private Color color=null;
	private ConditionalClickStep(Point where,Color conditionColor,boolean clickOnNotColor) {
		super(where);
		this.clickOnNotColor=clickOnNotColor;
		color=conditionColor;
	}
	@Override
	public void action() {
		boolean click=(robot.getPixelColor(target.x, target.y)==color);
		if(clickOnNotColor) click=!click;
		
		if(click){
			super.action();
		}
	}
	@Override
	public String toString(){
		String notIndicator="";
		if(clickOnNotColor) notIndicator="!";
		return "if("+notIndicator+SwingUtil.color2Str(color)+")" +
			   "click("+SwingUtil.point2Str(target)+")";
	}

}
