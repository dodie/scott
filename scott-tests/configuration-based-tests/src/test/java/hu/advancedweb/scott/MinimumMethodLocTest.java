package hu.advancedweb.scott;

import org.junit.Test;

import hu.advancedweb.scott.helper.CustomClassLoader;
import hu.advancedweb.scott.helper.TestScottRuntime;
import hu.advancedweb.scott.helper.TestScottRuntimeVerifier;
import hu.advancedweb.scott.instrumentation.transformation.config.Configuration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MinimumMethodLocTest {

	@Test
	public void defaultConfigExcludesNone() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName()).build();

		Class<?> clazz = CustomClassLoader
				.loadAndTransform("hu.advancedweb.scott.examples.ClassWithVaryingMethodSizes", configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Object obj = clazz.getDeclaredConstructor().newInstance();

			clazz.getDeclaredMethod("methodWith1LineBodyInlineDeclaration").invoke(obj);
			verify(testRuntime, times(1)).trackMethodStart(eq("methodWith1LineBodyInlineDeclaration"), any());

			clazz.getDeclaredMethod("methodWith1LineBody").invoke(obj);
			verify(testRuntime, times(1)).trackMethodStart(eq("methodWith1LineBody"), any());

			clazz.getDeclaredMethod("methodWith3LineBody").invoke(obj);
			verify(testRuntime, times(1)).trackMethodStart(eq("methodWith3LineBody"), any());

			clazz.getDeclaredMethod("methodWith9LineBody").invoke(obj);
			verify(testRuntime, times(1)).trackMethodStart(eq("methodWith9LineBody"), any());
		});
	}

	@Test
	public void explicitlyInclude1LineMethod() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName()).setMinimumMethodLoc(1).build();

		Class<?> clazz = CustomClassLoader
				.loadAndTransform("hu.advancedweb.scott.examples.ClassWithVaryingMethodSizes", configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Object obj = clazz.getDeclaredConstructor().newInstance();

			clazz.getDeclaredMethod("methodWith1LineBodyInlineDeclaration").invoke(obj);
			verify(testRuntime, times(1)).trackMethodStart(eq("methodWith1LineBodyInlineDeclaration"), any());

			clazz.getDeclaredMethod("methodWith1LineBody").invoke(obj);
			verify(testRuntime, times(1)).trackMethodStart(eq("methodWith1LineBody"), any());

			clazz.getDeclaredMethod("methodWith3LineBody").invoke(obj);
			verify(testRuntime, times(1)).trackMethodStart(eq("methodWith3LineBody"), any());

			clazz.getDeclaredMethod("methodWith9LineBody").invoke(obj);
			verify(testRuntime, times(1)).trackMethodStart(eq("methodWith9LineBody"), any());
		});
	}

	@Test
	public void explicitlyExclude1LineMethod() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName()).setMinimumMethodLoc(2).build();

		Class<?> clazz = CustomClassLoader
				.loadAndTransform("hu.advancedweb.scott.examples.ClassWithVaryingMethodSizes", configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Object obj = clazz.getDeclaredConstructor().newInstance();

			clazz.getDeclaredMethod("methodWith1LineBodyInlineDeclaration").invoke(obj);
			verify(testRuntime, times(0)).trackMethodStart(eq("methodWith1LineBodyInlineDeclaration"), any());

			clazz.getDeclaredMethod("methodWith1LineBody").invoke(obj);
			verify(testRuntime, times(0)).trackMethodStart(eq("methodWith1LineBody"), any());

			clazz.getDeclaredMethod("methodWith3LineBody").invoke(obj);
			verify(testRuntime, times(1)).trackMethodStart(eq("methodWith3LineBody"), any());

			clazz.getDeclaredMethod("methodWith9LineBody").invoke(obj);
			verify(testRuntime, times(1)).trackMethodStart(eq("methodWith9LineBody"), any());
		});
	}

	@Test
	public void explicitlyInclude3LineMethod() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName()).setMinimumMethodLoc(3).build();

		Class<?> clazz = CustomClassLoader
				.loadAndTransform("hu.advancedweb.scott.examples.ClassWithVaryingMethodSizes", configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Object obj = clazz.getDeclaredConstructor().newInstance();

			clazz.getDeclaredMethod("methodWith1LineBodyInlineDeclaration").invoke(obj);
			verify(testRuntime, times(0)).trackMethodStart(eq("methodWith1LineBodyInlineDeclaration"), any());

			clazz.getDeclaredMethod("methodWith1LineBody").invoke(obj);
			verify(testRuntime, times(0)).trackMethodStart(eq("methodWith1LineBody"), any());

			clazz.getDeclaredMethod("methodWith3LineBody").invoke(obj);
			verify(testRuntime, times(1)).trackMethodStart(eq("methodWith3LineBody"), any());

			clazz.getDeclaredMethod("methodWith9LineBody").invoke(obj);
			verify(testRuntime, times(1)).trackMethodStart(eq("methodWith9LineBody"), any());
		});
	}

	@Test
	public void explicitlyExclude3LineMethod() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName()).setMinimumMethodLoc(4).build();

		Class<?> clazz = CustomClassLoader
				.loadAndTransform("hu.advancedweb.scott.examples.ClassWithVaryingMethodSizes", configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Object obj = clazz.getDeclaredConstructor().newInstance();

			clazz.getDeclaredMethod("methodWith1LineBodyInlineDeclaration").invoke(obj);
			verify(testRuntime, times(0)).trackMethodStart(eq("methodWith1LineBodyInlineDeclaration"), any());

			clazz.getDeclaredMethod("methodWith1LineBody").invoke(obj);
			verify(testRuntime, times(0)).trackMethodStart(eq("methodWith1LineBody"), any());

			clazz.getDeclaredMethod("methodWith3LineBody").invoke(obj);
			verify(testRuntime, times(0)).trackMethodStart(eq("methodWith3LineBody"), any());

			clazz.getDeclaredMethod("methodWith9LineBody").invoke(obj);
			verify(testRuntime, times(1)).trackMethodStart(eq("methodWith9LineBody"), any());
		});
	}

	@Test
	public void explicitlyInclude9LineMethod() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName()).setMinimumMethodLoc(9).build();

		Class<?> clazz = CustomClassLoader
				.loadAndTransform("hu.advancedweb.scott.examples.ClassWithVaryingMethodSizes", configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Object obj = clazz.getDeclaredConstructor().newInstance();

			clazz.getDeclaredMethod("methodWith1LineBodyInlineDeclaration").invoke(obj);
			verify(testRuntime, times(0)).trackMethodStart(eq("methodWith1LineBodyInlineDeclaration"), any());

			clazz.getDeclaredMethod("methodWith1LineBody").invoke(obj);
			verify(testRuntime, times(0)).trackMethodStart(eq("methodWith1LineBody"), any());

			clazz.getDeclaredMethod("methodWith3LineBody").invoke(obj);
			verify(testRuntime, times(0)).trackMethodStart(eq("methodWith3LineBody"), any());

			clazz.getDeclaredMethod("methodWith9LineBody").invoke(obj);
			verify(testRuntime, times(1)).trackMethodStart(eq("methodWith9LineBody"), any());
		});
	}

	@Test
	public void explicitlyExclude9LineMethod() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setTrackerClass(TestScottRuntime.class.getCanonicalName()).setMinimumMethodLoc(10).build();

		Class<?> clazz = CustomClassLoader
				.loadAndTransform("hu.advancedweb.scott.examples.ClassWithVaryingMethodSizes", configuration);

		TestScottRuntime.verify(mock(TestScottRuntimeVerifier.class), testRuntime -> {
			Object obj = clazz.getDeclaredConstructor().newInstance();

			clazz.getDeclaredMethod("methodWith1LineBodyInlineDeclaration").invoke(obj);
			verify(testRuntime, times(0)).trackMethodStart(eq("methodWith1LineBodyInlineDeclaration"), any());

			clazz.getDeclaredMethod("methodWith1LineBody").invoke(obj);
			verify(testRuntime, times(0)).trackMethodStart(eq("methodWith1LineBody"), any());

			clazz.getDeclaredMethod("methodWith3LineBody").invoke(obj);
			verify(testRuntime, times(0)).trackMethodStart(eq("methodWith3LineBody"), any());

			clazz.getDeclaredMethod("methodWith9LineBody").invoke(obj);
			verify(testRuntime, times(0)).trackMethodStart(eq("methodWith9LineBody"), any());
		});
	}

}
