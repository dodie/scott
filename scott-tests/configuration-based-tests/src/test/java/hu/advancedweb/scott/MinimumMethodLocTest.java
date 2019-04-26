package hu.advancedweb.scott;

import org.junit.Test;

import hu.advancedweb.scott.helper.InstrumentedObject;
import hu.advancedweb.scott.helper.TestScottRuntime;
import hu.advancedweb.scott.helper.TestScottRuntimeVerifier;
import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MinimumMethodLocTest {

	@Test
	public void defaultConfigExcludesNone() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName()).build();

		InstrumentedObject instrumentedObject = InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithVaryingMethodSizes", configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			instrumentedObject.invokeMethod("methodWith1LineBodyInlineDeclaration");
			verify(testRuntime, times(1)).trackMethodStart(anyInt(), eq("methodWith1LineBodyInlineDeclaration"), any());

			instrumentedObject.invokeMethod("methodWith1LineBody");
			verify(testRuntime, times(1)).trackMethodStart(anyInt(), eq("methodWith1LineBody"), any());

			instrumentedObject.invokeMethod("methodWith3LineBody");
			verify(testRuntime, times(1)).trackMethodStart(anyInt(), eq("methodWith3LineBody"), any());

			instrumentedObject.invokeMethod("methodWith9LineBody");
			verify(testRuntime, times(1)).trackMethodStart(anyInt(), eq("methodWith9LineBody"), any());
		});
	}

	@Test
	public void explicitlyInclude1LineMethod() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName()).setMinimumMethodLoc(1).build();

		InstrumentedObject instrumentedObject = InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithVaryingMethodSizes", configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			instrumentedObject.invokeMethod("methodWith1LineBodyInlineDeclaration");
			verify(testRuntime, times(1)).trackMethodStart(anyInt(), eq("methodWith1LineBodyInlineDeclaration"), any());

			instrumentedObject.invokeMethod("methodWith1LineBody");
			verify(testRuntime, times(1)).trackMethodStart(anyInt(), eq("methodWith1LineBody"), any());

			instrumentedObject.invokeMethod("methodWith3LineBody");
			verify(testRuntime, times(1)).trackMethodStart(anyInt(), eq("methodWith3LineBody"), any());

			instrumentedObject.invokeMethod("methodWith9LineBody");
			verify(testRuntime, times(1)).trackMethodStart(anyInt(), eq("methodWith9LineBody"), any());
		});
	}

	@Test
	public void explicitlyExclude1LineMethod() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName()).setMinimumMethodLoc(2).build();

		InstrumentedObject instrumentedObject = InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithVaryingMethodSizes", configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			instrumentedObject.invokeMethod("methodWith1LineBodyInlineDeclaration");
			verify(testRuntime, times(0)).trackMethodStart(anyInt(), eq("methodWith1LineBodyInlineDeclaration"), any());

			instrumentedObject.invokeMethod("methodWith1LineBody");
			verify(testRuntime, times(0)).trackMethodStart(anyInt(), eq("methodWith1LineBody"), any());

			instrumentedObject.invokeMethod("methodWith3LineBody");
			verify(testRuntime, times(1)).trackMethodStart(anyInt(), eq("methodWith3LineBody"), any());

			instrumentedObject.invokeMethod("methodWith9LineBody");
			verify(testRuntime, times(1)).trackMethodStart(anyInt(), eq("methodWith9LineBody"), any());
		});
	}

	@Test
	public void explicitlyInclude3LineMethod() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName()).setMinimumMethodLoc(3).build();

		InstrumentedObject instrumentedObject = InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithVaryingMethodSizes", configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			instrumentedObject.invokeMethod("methodWith1LineBodyInlineDeclaration");
			verify(testRuntime, times(0)).trackMethodStart(anyInt(), eq("methodWith1LineBodyInlineDeclaration"), any());

			instrumentedObject.invokeMethod("methodWith1LineBody");
			verify(testRuntime, times(0)).trackMethodStart(anyInt(), eq("methodWith1LineBody"), any());

			instrumentedObject.invokeMethod("methodWith3LineBody");
			verify(testRuntime, times(1)).trackMethodStart(anyInt(), eq("methodWith3LineBody"), any());

			instrumentedObject.invokeMethod("methodWith9LineBody");
			verify(testRuntime, times(1)).trackMethodStart(anyInt(), eq("methodWith9LineBody"), any());
		});
	}

	@Test
	public void explicitlyExclude3LineMethod() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName()).setMinimumMethodLoc(4).build();

		InstrumentedObject instrumentedObject = InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithVaryingMethodSizes", configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			instrumentedObject.invokeMethod("methodWith1LineBodyInlineDeclaration");
			verify(testRuntime, times(0)).trackMethodStart(anyInt(), eq("methodWith1LineBodyInlineDeclaration"), any());

			instrumentedObject.invokeMethod("methodWith1LineBody");
			verify(testRuntime, times(0)).trackMethodStart(anyInt(), eq("methodWith1LineBody"), any());

			instrumentedObject.invokeMethod("methodWith3LineBody");
			verify(testRuntime, times(0)).trackMethodStart(anyInt(), eq("methodWith3LineBody"), any());

			instrumentedObject.invokeMethod("methodWith9LineBody");
			verify(testRuntime, times(1)).trackMethodStart(anyInt(), eq("methodWith9LineBody"), any());
		});
	}

	@Test
	public void explicitlyInclude9LineMethod() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName()).setMinimumMethodLoc(9).build();

		InstrumentedObject instrumentedObject = InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithVaryingMethodSizes", configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			instrumentedObject.invokeMethod("methodWith1LineBodyInlineDeclaration");
			verify(testRuntime, times(0)).trackMethodStart(anyInt(), eq("methodWith1LineBodyInlineDeclaration"), any());

			instrumentedObject.invokeMethod("methodWith1LineBody");
			verify(testRuntime, times(0)).trackMethodStart(anyInt(), eq("methodWith1LineBody"), any());

			instrumentedObject.invokeMethod("methodWith3LineBody");
			verify(testRuntime, times(0)).trackMethodStart(anyInt(), eq("methodWith3LineBody"), any());

			instrumentedObject.invokeMethod("methodWith9LineBody");
			verify(testRuntime, times(1)).trackMethodStart(anyInt(), eq("methodWith9LineBody"), any());
		});
	}

	@Test
	public void explicitlyExclude9LineMethod() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName()).setMinimumMethodLoc(10).build();

		InstrumentedObject instrumentedObject = InstrumentedObject.create("hu.advancedweb.scott.examples.ClassWithVaryingMethodSizes", configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			instrumentedObject.invokeMethod("methodWith1LineBodyInlineDeclaration");
			verify(testRuntime, times(0)).trackMethodStart(anyInt(), eq("methodWith1LineBodyInlineDeclaration"), any());

			instrumentedObject.invokeMethod("methodWith1LineBody");
			verify(testRuntime, times(0)).trackMethodStart(anyInt(), eq("methodWith1LineBody"), any());

			instrumentedObject.invokeMethod("methodWith3LineBody");
			verify(testRuntime, times(0)).trackMethodStart(anyInt(), eq("methodWith3LineBody"), any());

			instrumentedObject.invokeMethod("methodWith9LineBody");
			verify(testRuntime, times(0)).trackMethodStart(anyInt(), eq("methodWith9LineBody"), any());
		});
	}

}
