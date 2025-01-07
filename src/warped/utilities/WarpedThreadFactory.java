/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities;

import java.util.concurrent.ThreadFactory;

public class WarpedThreadFactory implements ThreadFactory  {
	
	private String threadNames;
	public WarpedThreadFactory(String threadNames) {
		this.threadNames = threadNames;
	}
	
	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r, threadNames);
	}
	
	
	
}
