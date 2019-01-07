package hu.advancedweb.scott;

import static hu.advancedweb.scott.TestHelper.wrapped;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import org.junit.Test;

public class MockitoTest {

	@Test
	public void simpleMock() throws Exception {
		Foo foo = mock(Foo.class);
		when(foo.bar()).thenReturn("42");

		String result = foo.bar();
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(wrapped("42")));

		assertEquals("42", result);
		verify(foo, times(1)).bar();
	}

	@Test
	public void simpleMockWithConstParam() throws Exception {
		Foo foo = mock(Foo.class);
		when(foo.bar("42")).thenReturn("42");

		String result = foo.bar("42");
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(wrapped("42")));

		assertEquals("42", result);
		verify(foo, times(1)).bar("42");
	}

	@Test
	public void simpleMockWithDynamicParam() throws Exception {
		Foo foo = mock(Foo.class);
		when(foo.bar(anyString())).thenReturn(anyString());

		String result = foo.bar(anyString());
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(wrapped("42")));

		assertEquals("42", result);
		verify(foo, times(1)).bar(anyString());
	}

	@Test
	public void mockInAContainer() throws Exception {
		Foo foo = mock(Foo.class);
		MockHolder<Foo> holder = new MockHolder<>(foo);
		when(foo.bar()).thenReturn("42");

		String result = foo.bar();
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(wrapped("42")));

		assertEquals("42", result);
		verify(foo, times(1)).bar();
		verify(holder.t, times(1)).bar();
	}

	@Test
	public void mockInAContainer_2() throws Exception {
		Foo foo = mock(Foo.class);
		MockHolder<Foo> holder = new MockHolder<>(foo);
		when(holder.t.bar()).thenReturn("42"); // refering to the mock through the holder object

		String result = foo.bar();
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(wrapped("42")));

		assertEquals("42", result);
		verify(foo, times(1)).bar();
		verify(holder.t, times(1)).bar();
	}

	@Test
	public void mockInAContainerWithDynamicParam() throws Exception {
		Foo foo = mock(Foo.class);
		MockHolder<Foo> holder = new MockHolder<>(foo);
		when(foo.bar(anyString())).thenReturn("42");

		String result = foo.bar(anyString());
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(wrapped("42")));

		assertEquals("42", result);
		verify(foo, times(1)).bar(anyString());
		verify(holder.t, times(1)).bar(anyString());
	}

	@Test
	public void mockWithMultipleReturns() throws Exception {
		@SuppressWarnings("unchecked")
		Iterator<String> i = (Iterator<String>) mock(Iterator.class);
		when(i.next()).thenReturn("Hello").thenReturn("Scott");

		String result = i.next() + " " + i.next();
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(wrapped("Hello Scott")));

		assertEquals("Hello Scott", result);
	}

	@Test(expected = IOException.class)
	public void mockWithDoThrow() throws Exception {
		OutputStream mock = mock(OutputStream.class);
		OutputStreamWriter osw = new OutputStreamWriter(mock);
		doThrow(new IOException()).when(mock).close();
		osw.close();
	}

	@Test
	public void mockParameterWithVerify() throws Exception {
		OutputStream mock = mock(OutputStream.class);
		OutputStreamWriter osw = new OutputStreamWriter(mock);
		osw.close();
		verify(mock).close();
	}

	@Test
	public void simpleSpy() throws Exception {
		Foo foo = new FooImpl();
		Foo fooSpy = spy(foo);
		when(fooSpy.bar()).thenReturn("spy");

		String resultOfMockedMethod = fooSpy.bar();
		String resultOfOriginalMethod = fooSpy.bar("42");
		assertThat(TestHelper.getLastRecordedStateForVariable("resultOfMockedMethod"), equalTo(wrapped("spy")));
		assertThat(TestHelper.getLastRecordedStateForVariable("resultOfOriginalMethod"), equalTo(wrapped("bar42")));

		assertEquals("spy", resultOfMockedMethod);
		assertEquals("bar42", resultOfOriginalMethod);
		verify(fooSpy, times(1)).bar();
		verify(fooSpy, times(1)).bar("42");
	}

	@Test
	public void spyInAContainer() throws Exception {
		Foo foo = new FooImpl();
		Foo fooSpy = spy(foo);
		MockHolder<Foo> holder = new MockHolder<>(fooSpy);
		when(holder.t.bar()).thenReturn("spy");

		String resultOfMockedMethod = fooSpy.bar();
		String resultOfOriginalMethod = fooSpy.bar("42");
		assertThat(TestHelper.getLastRecordedStateForVariable("resultOfMockedMethod"), equalTo(wrapped("spy")));
		assertThat(TestHelper.getLastRecordedStateForVariable("resultOfOriginalMethod"), equalTo(wrapped("bar42")));

		assertEquals("spy", resultOfMockedMethod);
		assertEquals("bar42", resultOfOriginalMethod);
		verify(fooSpy, times(1)).bar();
		verify(fooSpy, times(1)).bar("42");
		verify(holder.t, times(1)).bar();
		verify(holder.t, times(1)).bar("42");
	}
	
	Foo mockInField;
	@Test
	public void simpleMockInField() throws Exception {
		mockInField = mock(Foo.class);
		when(mockInField.bar()).thenReturn("42");

		String result = mockInField.bar();
		assertThat(TestHelper.getLastRecordedStateForVariable("result"), equalTo(wrapped("42")));

		assertEquals("42", result);
		verify(mockInField, times(1)).bar();
	}

	public static class MockHolder<T> {
		final T t;

		MockHolder(T t) {
			this.t = t;
		}

		@Override
		public String toString() {
			return t != null ? t.toString() : "(null)";
		}
	}

	static interface Foo {
		String bar();

		String bar(String s);
	}

	static class FooImpl implements Foo {
		@Override
		public String bar() {
			return "bar";
		}

		@Override
		public String bar(String s) {
			return "bar" + s;
		}
	}

	private String anyString() {
		return "42";
	}

}
