package com.hjt.phoneshow.util;

public class MathUtil {
	// Math.random()*9会随机返回一个0-9的小数，这样下面的算法就能返回一个6位的随机数
	int numcode = (int) ((Math.random() * 9 + 1) * 100000);

}
