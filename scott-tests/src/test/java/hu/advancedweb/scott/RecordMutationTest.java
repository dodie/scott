package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

import hu.advancedweb.scott.helper.TestHelper;

public class RecordMutationTest {
	
	@Test
	public void simpleMutation() throws Exception {
		int i = 5;
		assertThat(TestHelper.getLastRecorderStateFor("i"), equalTo(Integer.toString(i)));
		
		i = 10;
		assertThat(TestHelper.getLastRecorderStateFor("i"), equalTo(Integer.toString(i)));
		
		i = 15;
		assertThat(TestHelper.getLastRecorderStateFor("i"), equalTo(Integer.toString(i)));
	}
	
	@Test
	public void mutateToNull() throws Exception {
		String s = "Hello World!";
		assertThat(TestHelper.getLastRecorderStateFor("s"), equalTo(s));
		
		s = null;
		assertThat(TestHelper.getLastRecorderStateFor("s"), equalTo("null"));
	}
	
	@Test
	public void mutationWithBranches() throws Exception {
		int i = 5;
		assertThat(TestHelper.getLastRecorderStateFor("i"), equalTo(Integer.toString(i)));
		
		if (i < 5) {
			i = 10;
		}
		assertThat(TestHelper.getLastRecorderStateFor("i"), equalTo(Integer.toString(i)));
		
		i = 15;
		assertThat(TestHelper.getLastRecorderStateFor("i"), equalTo(Integer.toString(i)));
	}
	
	@Test
	public void mutationWithBranches_2() throws Exception {
		String outer = "outer";
		assertThat(TestHelper.getLastRecorderStateFor("outer"), equalTo(outer));
		{
			String inner = "inner";
			assertThat(TestHelper.getLastRecorderStateFor("inner"), equalTo(inner));
		}
		
		outer = "outer_changed";
		assertThat(TestHelper.getLastRecorderStateFor("outer"), equalTo(outer));
	}

	@Test
	public void mutationWithLoops() throws Exception {
		int i = 0;
		
		for (int j = 0; j < 10; j++) {
			i = j*2;
			assertThat(TestHelper.getLastRecorderStateFor("i"), equalTo(Integer.toString(i)));
			assertThat(TestHelper.getLastRecorderStateFor("j"), equalTo(Integer.toString(j)));
		}
	}
	
	@Test
	public void objectMutation() throws Exception {
		MyMutable myMutable = new MyMutable(10);
		assertThat(TestHelper.getLastRecorderStateFor("myMutable"), equalTo(myMutable.toString()));
		
		myMutable.set(15);
		assertThat(TestHelper.getLastRecorderStateFor("myMutable"), equalTo(myMutable.toString()));
		
		myMutable.set(20);
		assertThat(TestHelper.getLastRecorderStateFor("myMutable"), equalTo(myMutable.toString()));
	}
	
	@Test
	public void indirectObjectMutation() throws Exception {
		MyMutable myMutable = new MyMutable(10);
		assertThat(TestHelper.getLastRecorderStateFor("myMutable"), equalTo(myMutable.toString()));
		
		MyMutator myMutator = new MyMutator(myMutable);
		myMutator.mutate(15);
		assertThat(TestHelper.getLastRecorderStateFor("myMutable"), equalTo(myMutable.toString()));
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
