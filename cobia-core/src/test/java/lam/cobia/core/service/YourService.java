package lam.cobia.core.service;

import lam.cobia.log.Console;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年6月28日
* @version 1.0
*/
public class YourService implements IYourService{
	
	private IMyService myService;
	
	public void setMyService(IMyService myService) {
		this.myService = myService;
	}

	@Override
	public int doYourself(long p) {
		Console.println(p);
		return Long.valueOf(p).intValue();
	}

	@Override
	public String gogo(byte b) {
		Console.println(b);
		return String.valueOf(b);
	}
	
	@Override
	public String doString(String s) {
		return "do with " + s;
	}

}
