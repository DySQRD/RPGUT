package Jeu;

import java.util.concurrent.atomic.AtomicInteger;

public class testatomicinteger {
	public static void main(String[] args) {
		final AtomicInteger test = new AtomicInteger(0);
		
		System.out.println(test);
		test.incrementAndGet();
		System.out.println(test);
	}
}
