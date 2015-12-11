package com.nhl.bootique.job.runnable;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nhl.bootique.job.Job;

public class ErrorHandlingRunnableJobFactory implements RunnableJobFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlingRunnableJobFactory.class);

	private RunnableJobFactory delegate;

	public ErrorHandlingRunnableJobFactory(RunnableJobFactory delegate) {
		this.delegate = delegate;
	}

	@Override
	public RunnableJob runnable(Job job, Map<String, Object> parameters) {

		RunnableJob rj = delegate.runnable(job, parameters);

		return () -> {
			try {
				return rj.run();
			} catch (Throwable th) {
				LOGGER.info("Exception while running job '{}'", job.getMetadata().getName(), th);
				return JobResult.unknown(job.getMetadata(), th);
			}
		};
	}

}