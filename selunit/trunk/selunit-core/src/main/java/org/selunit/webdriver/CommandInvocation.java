package org.selunit.webdriver;

public interface CommandInvocation<R> {
	public R invoke() throws TestException;
}
