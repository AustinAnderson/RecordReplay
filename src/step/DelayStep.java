package step;

import common.SwingUtil;

public class DelayStep implements StepInterface {
	public DelayStep(int time){
		delayTimeMillis=time;
	}
	private int delayTimeMillis=0;
	
	@Override
	public void action() {
		SwingUtil.trySleep(delayTimeMillis);
	}
	@Override
	public String toString(){
		return "wait("+delayTimeMillis+"ms)";
	}
}
