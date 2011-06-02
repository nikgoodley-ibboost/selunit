package org.selunit.job;

import java.util.List;

public interface TestJob extends TestJobInfo {
	public List<String> getSuites();
}
