package hu.advancedweb.scott;

import static hu.advancedweb.scott.TestHelper.wrapped;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

public class RecordMutationTest {
	
	@Test
	public void simpleMutation() throws Exception {
		int i = 5;
		assertThat(TestHelper.getLastRecordedStateForVariable("i"), equalTo(Integer.toString(i)));
		
		i = 10;
		assertThat(TestHelper.getLastRecordedStateForVariable("i"), equalTo(Integer.toString(i)));
		
		i = 15;
		assertThat(TestHelper.getLastRecordedStateForVariable("i"), equalTo(Integer.toString(i)));
	}
	
	@Test
	public void plusEqualsMutation() throws Exception {
		long l = 1L;
		int i = 1;
		Long lL = Long.valueOf("1");
		Integer iI = Integer.valueOf("1");
		
		i = (int)(i + l);
		i += l;
		assertThat(TestHelper.getLastRecordedStateForVariable("i"), equalTo(Integer.toString(i)));
		
		iI = (int)(iI + lL);
		assertThat(TestHelper.getLastRecordedStateForVariable("iI"), equalTo(Integer.toString(iI)));
		
		i = (int)(i + lL);
		i += lL;
		assertThat(TestHelper.getLastRecordedStateForVariable("i"), equalTo(Integer.toString(i)));
		
		iI = (int)(iI + l);
		assertThat(TestHelper.getLastRecordedStateForVariable("iI"), equalTo(Integer.toString(iI)));
	}
	
	@Test
	public void mutateToNull() throws Exception {
		String s = "Hello World!";
		assertThat(TestHelper.getLastRecordedStateForVariable("s"), equalTo(wrapped(s)));
		
		s = null;
		assertThat(TestHelper.getLastRecordedStateForVariable("s"), equalTo("null"));
	}
	
	@Test
	public void mutationWithIfBranch() throws Exception {
		int i = 5;
		assertThat(TestHelper.getLastRecordedStateForVariable("i"), equalTo(Integer.toString(i)));
		
		if (i < 5) {
			i = 10;
		}
		assertThat(TestHelper.getLastRecordedStateForVariable("i"), equalTo(Integer.toString(i)));
		
		i = 15;
		assertThat(TestHelper.getLastRecordedStateForVariable("i"), equalTo(Integer.toString(i)));
	}
	
	@Test
	public void mutationWithBlock() throws Exception {
		String outer = "outer";
		assertThat(TestHelper.getLastRecordedStateForVariable("outer"), equalTo(wrapped(outer)));
		{
			String inner = "inner";
			assertThat(TestHelper.getLastRecordedStateForVariable("inner"), equalTo(wrapped(inner)));
		}
		
		outer = "outer_changed";
		assertThat(TestHelper.getLastRecordedStateForVariable("outer"), equalTo(wrapped(outer)));
	}

	@Test
	public void mutationWithBlockThatHasASingleMethodCall() throws Exception {
		String outer = "outer";
		{
			outer.length();
		}
		assertThat(TestHelper.getLastRecordedStateForVariable("outer"), equalTo(wrapped("outer")));
	}
	
	@Test
	public void mutationWithEmptyBlock() throws Exception {
		@SuppressWarnings("unused")
		String outer = "outer";
		{
			// This is an empty block.
		}
		assertThat(TestHelper.getLastRecordedStateForVariable("outer"), equalTo(wrapped("outer")));
	}
	
	@Test
	public void mutationWithLoops() throws Exception {
		int i = 0;
		
		for (int j = 0; j < 10; j++) {
			i = j*2;
			assertThat(TestHelper.getLastRecordedStateForVariable("i"), equalTo(Integer.toString(i)));
			assertThat(TestHelper.getLastRecordedStateForVariable("j"), equalTo(Integer.toString(j)));
		}
	}
	
	@Test
	public void objectMutation() throws Exception {
		Mutable mutable = new Mutable(10);
		assertThat(TestHelper.getLastRecordedStateForVariable("mutable"), equalTo(mutable.toString()));
		
		mutable.set(15);
		assertThat(TestHelper.getLastRecordedStateForVariable("mutable"), equalTo(mutable.toString()));
		
		mutable.set(20);
		assertThat(TestHelper.getLastRecordedStateForVariable("mutable"), equalTo(mutable.toString()));
	}
	
	@Test
	public void indirectObjectMutation() throws Exception {
		Mutable mutable = new Mutable(10);
		assertThat(TestHelper.getLastRecordedStateForVariable("mutable"), equalTo(mutable.toString()));
		
		Mutator mutator = new Mutator(mutable);
		mutator.mutate(15);

		assertThat(TestHelper.getLastRecordedStateForVariable("mutable"), equalTo(mutable.toString()));
	}
	
	@Test
	public void multipleIndirectObjectMutation() throws Exception {
		Mutable mutable1 = new Mutable(10);
		Mutable mutable2 = new Mutable(20);
		assertThat(TestHelper.getLastRecordedStateForVariable("mutable1"), equalTo(mutable1.toString()));
		assertThat(TestHelper.getLastRecordedStateForVariable("mutable2"), equalTo(mutable2.toString()));
		
		MultiMutator multiMutator = new MultiMutator(mutable1, mutable2);
		multiMutator.mutate(15);
		
		assertThat(TestHelper.getLastRecordedStateForVariable("mutable1"), equalTo(mutable1.toString()));
		assertThat(TestHelper.getLastRecordedStateForVariable("mutable2"), equalTo(mutable2.toString()));
	}

	
	public static class Mutable {
		int state;
		
		Mutable(int state) {
			this.state = state;
		}
		
		void set(int state) {
			this.state = state;
		}

		@Override
		public String toString() {
			return "Mutable [state=" + state + "]";
		}
	}
	
	public static class Mutator {
		Mutable mutable;

		Mutator(Mutable mutable) {
			this.mutable = mutable;
		}
		
		void mutate(int state) {
			mutable.set(state);
		}
	}

	public static class MultiMutator {
		Mutable mutable1;
		Mutable mutable2;

		MultiMutator(Mutable mutable1, Mutable mutable2) {
			this.mutable1 = mutable1;
			this.mutable2 = mutable2;
		}
		
		void mutate(int state) {
			mutable1.set(state);
			mutable2.set(state);
		}
	}

}
