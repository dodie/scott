package runlistener;

import java.io.File;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import runlistener.sourceparser.SourceParser;
import runlistener.sourceparser.TestMethodSource;

public class MyHelper extends RunListener {
	
	static String TEST_SOURCE_PATH;
	static String TEST_METHOD_NAME;

	@Override
	public void testStarted(Description description) throws Exception {
		EventRepository.clear();
		
		TEST_SOURCE_PATH = System.getProperty("user.dir") + "/../src/test/java/" + description.getTestClass().getCanonicalName().replace(".", File.separator) + ".java";
		TEST_METHOD_NAME = description.getMethodName();

		super.testStarted(description);
	}
	
	@Override
	public void testFailure(Failure failure) throws Exception {
		TestMethodSource testMethodSource = new SourceParser().loadTestMethodSource(TEST_SOURCE_PATH, TEST_METHOD_NAME);
		
		for (Event event : EventRepository.getEvents()) {
			testMethodSource.commentLine(event.lineNumber, event.value);
		}

		System.out.println(failure.getTestHeader() + " FAILED!");
		System.out.println(renderTestMethodSource(testMethodSource));
		
		super.testFailure(failure);
	}
	
	public String renderTestMethodSource(TestMethodSource testMethodSource) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, String> line : testMethodSource.getSourceLines().entrySet()) {
			sb.append(String.format("%1$4s", line.getKey()));
			sb.append("|  ");
			sb.append(line.getValue());
			sb.append("\n");
		}
		return sb.toString();
	}

}
