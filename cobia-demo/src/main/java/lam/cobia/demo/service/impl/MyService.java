package lam.cobia.demo.service.impl;

import lam.cobia.demo.service.IMyService;
import lam.cobia.log.Console;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年1月2日
* @version 1.0
*/
public class MyService implements IMyService {

/*	@Override
	public String doIt(String s, int i) {
		System.out.println(s + "," + i);
		return s + i;
	}

	@Override
	public boolean doSomething(long lon, int i) {
		System.out.println(lon + "," + i);
		return i > 0;
	}*/

	@Override
	public String doIt(String s) {
		Console.println(s );
		return "response of " + s;
	}

	@Override
	public boolean doSomething(long lon) {
		Console.println(lon);
		return lon > 0;
	}

}
