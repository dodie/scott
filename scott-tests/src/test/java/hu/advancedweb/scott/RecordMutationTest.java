package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import hu.advancedweb.scott.helper.TestHelper;

public class RecordMutationTest {
	
	@Test
	public void simpleMutation() throws Exception {
		int i = 5;
		assertThat(TestHelper.getLastRecordedStateFor("i"), equalTo(Integer.toString(i)));
		
		i = 10;
		assertThat(TestHelper.getLastRecordedStateFor("i"), equalTo(Integer.toString(i)));
		
		i = 15;
		assertThat(TestHelper.getLastRecordedStateFor("i"), equalTo(Integer.toString(i)));
	}
	
	@Test
	public void mutateToNull() throws Exception {
		String s = "Hello World!";
		assertThat(TestHelper.getLastRecordedStateFor("s"), equalTo(s));
		
		s = null;
		assertThat(TestHelper.getLastRecordedStateFor("s"), equalTo("null"));
	}
	
	@Test
	public void mutationWithIfBranch() throws Exception {
		int i = 5;
		assertThat(TestHelper.getLastRecordedStateFor("i"), equalTo(Integer.toString(i)));
		
		if (i < 5) {
			i = 10;
		}
		assertThat(TestHelper.getLastRecordedStateFor("i"), equalTo(Integer.toString(i)));
		
		i = 15;
		assertThat(TestHelper.getLastRecordedStateFor("i"), equalTo(Integer.toString(i)));
	}
	
	@Test
	public void mutationWithBlock() throws Exception {
		String outer = "outer";
		assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo(outer));
		{
			String inner = "inner";
			assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner));
		}
		
		outer = "outer_changed";
		assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo(outer));
	}

//	//FIXME: This test kills the whole Test file, nothing is recorded. If I add a single Sysout to the inner block it seems to fix it.
//	@Test
//	public void mutationWithBlockThatHasASingleDeclaration() throws Exception {
//		String outer = "outer";
//		{
//			String inner = "inner";
//		}
//		
//		assertThat(TestHelper.getLastRecorderStateFor("inner"), equalTo("inner"));
//		assertThat(TestHelper.getLastRecorderStateFor("outer"), equalTo("outer"));
//	}

	@Test
	public void mutationWithBlockThatHasASingleMethodCall() throws Exception {
		String outer = "outer";
		{
			outer.length();
		}
		assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo("outer"));
	}
	
	@Test
	public void mutationWithEmptyBlock() throws Exception {
		String outer = "outer";
		{
			// This is an empty block.
		}
		assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo("outer"));
	}
	
	@Test
	public void mutationWithLoops() throws Exception {
		int i = 0;
		
		for (int j = 0; j < 10; j++) {
			i = j*2;
			assertThat(TestHelper.getLastRecordedStateFor("i"), equalTo(Integer.toString(i)));
			assertThat(TestHelper.getLastRecordedStateFor("j"), equalTo(Integer.toString(j)));
		}
	}
	
	@Test
	public void objectMutation() throws Exception {
		MyMutable myMutable = new MyMutable(10);
		assertThat(TestHelper.getLastRecordedStateFor("myMutable"), equalTo(myMutable.toString()));
		
		myMutable.set(15);
		assertThat(TestHelper.getLastRecordedStateFor("myMutable"), equalTo(myMutable.toString()));
		
		myMutable.set(20);
		assertThat(TestHelper.getLastRecordedStateFor("myMutable"), equalTo(myMutable.toString()));
	}
	
	@Test
	public void indirectObjectMutation() throws Exception {
		MyMutable myMutable = new MyMutable(10);
		assertThat(TestHelper.getLastRecordedStateFor("myMutable"), equalTo(myMutable.toString()));
		
		MyMutator myMutator = new MyMutator(myMutable);
		myMutator.mutate(15);
		assertThat(TestHelper.getLastRecordedStateFor("myMutable"), equalTo(myMutable.toString()));
	}
	

	public static class MyMutator {
		MyMutable myMutable;

		MyMutator(MyMutable myMutable) {
			this.myMutable = myMutable;
		}
		
		void mutate(int state) {
			myMutable.set(state);
		}
	}
	
	public static class MyMutable {
		int state;
		
		MyMutable(int state) {
			this.state = state;
		}
		
		void set(int state) {
			this.state = state;
		}

		@Override
		public String toString() {
			return "MyClass [state=" + state + "]";
		}
	}

}
