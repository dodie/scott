package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import hu.advancedweb.scott.helper.TestHelper;

/**
 * Note that this file is sensitive to formatting.
 * 
 * @author David Csakvari
 */
public class WeirdFormattingTest {
	
	@Test
	public void simpleFormatting() throws Exception {
		String outer = "outer1";
		assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo(outer));
		{
			String inner = "inner1";
			assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner));
		}
		outer = "outer2";
		assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo(outer));
	}
	
	@Test
	public void inlineFormatting_1() throws Exception {
		String outer = "outer1";
		assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo(outer));
		{String inner = "inner1"; assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner)); }
		outer = "outer2";
		assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo(outer));
	}
	
	@Test
	public void inlineFormatting_2() throws Exception {
		String outer = "outer1"; assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo(outer)); {
			String inner = "inner1";
			assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner));
		}
		outer = "outer2";
		assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo(outer));
	}

	@Test
	public void inlineFormatting_3() throws Exception {
		String outer = "outer1"; 
		assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo(outer));
		{ String inner = "inner1"; assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner)); } outer = "outer2"; assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo(outer));
	}
	
	@Test
	public void inlineFormatting_4() throws Exception {
		String outer = "outer1"; assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo(outer)); { String inner = "inner1"; assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner)); } outer = "outer2"; assertThat(TestHelper.getLastRecordedStateFor("outer"), equalTo(outer));
	}
	
	@SuppressWarnings("null")
	@Test
	public void simpleFormattingWithTryCatch() {
		String o = null;
		
		try {
			String inner = "inner";
			assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner));
			o.length();
		} catch (Exception e) {
			o = "fallback";
			assertThat(TestHelper.getLastRecordedStateFor("e"), equalTo(e.toString()));
			assertThat(TestHelper.getLastRecordedStateFor("o"), equalTo(o));
		}	
	}

	@SuppressWarnings("null")
	@Test
	@Ignore // FIXME: See issue 19: multiple variable declarations in different scopes on the same line cause problems.
	public void inlineFormattingWithTryCatch() {
		String o = null; try { String inner = "inner";	assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner)); o.length(); } catch (Exception e) { o = "fallback"; assertThat(TestHelper.getLastRecordedStateFor("e"), equalTo(e.toString())); assertThat(TestHelper.getLastRecordedStateFor("o"), equalTo(o)); }	
	}
	
	@SuppressWarnings("null")
	@Test
	public void expandedFormattingWithTryCatch() {
		String o = null;
		
		try 
		{
			String inner = "inner";
			assertThat(TestHelper.getLastRecordedStateFor("inner"), equalTo(inner));
			o.length();
		}
		catch (Exception e)
		{
			o = "fallback";
			assertThat(TestHelper.getLastRecordedStateFor("e"), equalTo(e.toString()));
			assertThat(TestHelper.getLastRecordedStateFor("o"), equalTo(o));
		}	
	}
}
