package com.homeoffice.movieapp;

import java.io.File;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.metrics.json.MetricsModule;
import com.kumuluz.ee.metrics.producers.MetricRegistryProducer;

/**
 * @author mbojanec
 *
 */
public class MetricLogger implements Runnable {

	private ObjectMapper mapper = (new ObjectMapper(
			new JsonFactory().configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false)))
					.registerModule(new MetricsModule(false));

	@Override
	public void run() {

		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("metrics.txt"),
					MetricRegistryProducer.getApplicationRegistry());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}