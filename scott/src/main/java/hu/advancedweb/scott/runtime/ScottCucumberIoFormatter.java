package hu.advancedweb.scott.runtime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hu.advancedweb.scott.runtime.report.FailureRenderer;
import io.cucumber.messages.Messages.Envelope;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.protobuf.util.JsonFormat;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestStepFinished;

/**
 * This class is based on the io.cucumber.core.plugin.HtmlFormatter class
 * in the https://github.com/cucumber/cucumber-jvm/ project.
 *
 * In previous Java versions I could modify the message of the Exception object via a custom listener,
 * but now that results in an illegal access exception when used without Java modules.
 *
 * To overcome this I created this custom formatter that adds the detailed error message upon rendering.
 */
public class ScottCucumberIoFormatter implements ConcurrentEventListener {

	private final MessagesToHtmlWriter writer;

	List<String> results = new ArrayList<>();

	boolean isFailed = false;

	public ScottCucumberIoFormatter(OutputStream out) throws IOException {
		this.writer = new MessagesToHtmlWriter(new UTF8OutputStreamWriter(out));
	}

	@Override
	public void setEventPublisher(EventPublisher publisher) {
		publisher.registerHandlerFor(Envelope.class, new EventHandler<Envelope>() {
			@Override
			public void receive(Envelope event) {
				if (!event.hasStepDefinition() && !event.hasHook() && !event.hasParameterType()) {
					try {
						writer.write(event);
					} catch (IOException var4) {
						throw new IllegalStateException(var4);
					}

					if (event.hasTestRunFinished()) {
						try {
							writer.close();
						} catch (IOException var3) {
							throw new IllegalStateException(var3);
						}
					}

				}
			}
		});

		publisher.registerHandlerFor(TestCaseStarted.class, new EventHandler<TestCaseStarted>() {
			@Override
			public void receive(TestCaseStarted event) {
				results.clear();
				isFailed = false;
			}
		});

		publisher.registerHandlerFor(TestStepFinished.class, new EventHandler<TestStepFinished>() {
			@Override
			public void receive(TestStepFinished event) {
				String errorString =
						FailureRenderer.render(
								null,
								null,
								event.getResult().getStatus().is(Status.FAILED) ? event.getResult().getError() : null);
				results.add(errorString);

				if (event.getResult().getStatus().is(Status.FAILED)) {
					isFailed = true;
				}


			}
		});
	}

	/**
	 * Based on io.cucumber.htmlformatter.MessagesToHtmlWriter.
	 */
	class MessagesToHtmlWriter implements AutoCloseable {
		private final JsonFormat.Printer jsonPrinter = JsonFormat.printer().omittingInsignificantWhitespace();
		private final String template;
		private final Writer writer;
		private boolean preMessageWritten = false;
		private boolean postMessageWritten = false;
		private boolean firstMessageWritten = false;
		private boolean streamClosed = false;

		public MessagesToHtmlWriter(Writer writer) throws IOException {
			this.writer = writer;
			this.template = readResource("index.mustache.html");
		}

		private void writePreMessage() throws IOException {
			writeTemplateBetween(this.writer, this.template, (String)null, "{{css}}");
			writeResource(this.writer, "cucumber-react.css");
			writeTemplateBetween(this.writer, this.template, "{{css}}", "{{messages}}");
		}

		private void writePostMessage() throws IOException {
			writeTemplateBetween(this.writer, this.template, "{{messages}}", "{{script}}");
			writeResource(this.writer, "cucumber-html.js");
			writeTemplateBetween(this.writer, this.template, "{{script}}", (String)null);
		}

		public void write(Envelope envelope) throws IOException {
			if (this.streamClosed) {
				throw new IOException("Stream closed");
			} else {
				if (!this.preMessageWritten) {
					this.writePreMessage();
					this.preMessageWritten = true;
				}

				if (!this.firstMessageWritten) {
					this.firstMessageWritten = true;
				} else {
					this.writer.write(",");
				}

				/*
				 Rewrite message in case of a failure.
				 */
				if (isFailed) {
					String print = this.jsonPrinter.print(envelope);
					Gson gson = new Gson();
					Map map = gson.fromJson(print, Map.class);

					if (map.containsKey("testStepFinished")) {
						Map testStepFinished = (Map) map.get("testStepFinished");

						if (testStepFinished.containsKey("testStepResult")) {
							Map testStepResult = (Map) testStepFinished.get("testStepResult");

							if (Objects.equals(testStepResult.get("status"), "FAILED")) {
								testStepResult.put("message", concat(results, "\n"));
							}
						}
					}

					this.writer.write(gson.toJson(map));
				} else {
					this.writer.write(this.jsonPrinter.print(envelope));
				}
			}
		}

		private String concat(Iterable<String> strings, String separator) {
			StringBuilder sb = new StringBuilder();
			String sep = "";
			for(String s: strings) {
				sb.append(sep).append(s);
				sep = separator;
			}
			return sb.toString();
		}

		public void close() throws IOException {
			if (!this.streamClosed) {
				if (!this.preMessageWritten) {
					this.writePreMessage();
					this.preMessageWritten = true;
				}

				if (!this.postMessageWritten) {
					this.writePostMessage();
					this.postMessageWritten = true;
				}

				this.writer.close();
				this.streamClosed = true;
			}
		}

		private void writeTemplateBetween(Writer writer, String template, String begin, String end) throws IOException {
			int beginIndex = begin == null ? 0 : template.indexOf(begin) + begin.length();
			int endIndex = end == null ? template.length() : template.indexOf(end);
			writer.write(template.substring(beginIndex, endIndex));
		}

		private void writeResource(Writer writer, String name) throws IOException {
			InputStream resource = io.cucumber.htmlformatter.MessagesToHtmlWriter.class.getResourceAsStream(name);
			Objects.requireNonNull(resource, name + " could not be loaded");
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8));
			char[] buffer = new char[1024];

			for(int read = reader.read(buffer); read != -1; read = reader.read(buffer)) {
				writer.write(buffer, 0, read);
			}

		}

		private String readResource(String name) throws IOException {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8));

			try {
				writeResource(writer, name);
			} catch (Throwable e) {
				try {
					writer.close();
				} catch (Throwable e2) {
					e.addSuppressed(e2);
				}

				throw e;
			}

			writer.close();
			return new String(baos.toByteArray(), StandardCharsets.UTF_8);
		}
	}

	static final class UTF8OutputStreamWriter extends OutputStreamWriter {
		UTF8OutputStreamWriter(OutputStream out) {
			super(out, StandardCharsets.UTF_8);
		}
	}

}
