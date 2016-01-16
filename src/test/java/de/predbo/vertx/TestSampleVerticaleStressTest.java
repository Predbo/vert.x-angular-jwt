package de.predbo.vertx;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class TestSampleVerticaleStressTest extends SampleVerticaleTestBase {

	private final static int NUMBER_OF_ALL_REQUESTS = 1_000;
	private final static int NUMBER_OF_PARALLEL_REQUESTS = 10;
	private Async _async;

	@Test 
	public void doStressTest(TestContext context) throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_PARALLEL_REQUESTS);
		_async = context.async(NUMBER_OF_ALL_REQUESTS);
		for (int i=0; i<=NUMBER_OF_ALL_REQUESTS ; i++) {
			executorService.execute(new RequestSender(i));
		}

		_async.awaitSuccess();	
		
		executorService.shutdown();
	}

	private class RequestSender implements Runnable {

		private final int _requestNumber;

		private RequestSender(int requestNumber) {
			_requestNumber = requestNumber;
		}
		
		@Override
		public void run() {
			final long startRequest = new  Date().getTime();
			
			sendRequestAndConsumeResponse();
			
			_async.countDown();
			
			System.out.println(new StringBuilder().append(_requestNumber)
							   .append(". request sent by ")
							   .append(Thread.currentThread().getName())
							   .append(" took ")
							   .append(new Date().getTime() - startRequest)
							   .append("ms")
							   .toString());
		}

		
		private void sendRequestAndConsumeResponse() {
			InputStream inputStream = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL("http://localhost:8080/login.html");
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				inputStream = connection.getInputStream();
				// to ensure response stream is fully consumed
				IOUtils.toString(inputStream, "UTF-8"); 
			} catch (Exception e) {
				System.err.println("Request failed" + e);
			} finally {
				IOUtils.closeQuietly(inputStream);
				if (connection != null) {
					connection.disconnect();
				}
			}
		}
	}
}
