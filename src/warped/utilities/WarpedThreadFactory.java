package warped.utilities;

import java.util.concurrent.ThreadFactory;

public class WarpedThreadFactory implements ThreadFactory {

	int count = 0;
	String name = "Thread";

	public WarpedThreadFactory(String name) {
		this.name = name;
	}
	
	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r, name + " : " + count++);
	}

}
