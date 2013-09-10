/*******************************************************************************
 * Copyright 2011 selunit.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.selunit.job;

import java.util.List;

public interface JobExecutor<J extends TestJob> {
	public void addHandler(JobExecutorHandler<J> handler);

	public void removeHandler(JobExecutorHandler<J> handler);

	public List<JobExecutorHandler<J>> getHandlers();

	public JobStatus getStatus();

	public void init(J job) throws TestJobException;

	public void start(List<String> suites) throws TestJobException;

	public void stop(boolean shutdown) throws TestJobException;

}
