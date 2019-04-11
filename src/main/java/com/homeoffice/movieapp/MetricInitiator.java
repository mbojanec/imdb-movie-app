package com.homeoffice.movieapp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

/**
 * @author mbojanec
 *
 */
@ApplicationScoped
public class MetricInitiator {

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> handle;

	private void initialiseBean(@Observes @Initialized(ApplicationScoped.class) Object init) {

		handle = scheduler.scheduleWithFixedDelay(new MetricLogger(), 0, 10, TimeUnit.SECONDS);
	}

	@PreDestroy
	private void stopReporter() {
		handle.cancel(true);
	}
}
