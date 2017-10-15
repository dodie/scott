
package hu.advancedweb.scott.runtime.report;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class FailureRendererTest {

    @Test
    public void verifyPlusPus() {
        int i = 0;
        while (i < 10) {
            i++;
        }
        String resultText = FailureRenderer.render("xxxxx", "yyyyy", new RuntimeException("bar"));
        assertPlusPlus(resultText);
    }

    private void assertPlusPlus(String resultText) {
        assertThat("variable loop", resultText, containsString("i=1;2;3...;8;9;[10] =>10"));
    }

    @Test
    public void verifySingleInt() {
        @SuppressWarnings("unused")
		int i = 42;
        String resultText = FailureRenderer.render("xxxxx", "yyyyy", new RuntimeException("bar"));
        assertSingleInt(resultText);
    }

    private void assertSingleInt(String resultText) {
        assertThat("variable loop", resultText, containsString("i=42"));
    }

    @Test
    public void verifyPlusTwo() {
        int i = 0;
        while (i < 10) {
            i+=2;
        }
        String resultText = FailureRenderer.render("xxxxx", "yyyyy", new RuntimeException("bar"));
        assertPlusTwo(resultText);
    }

    private void assertPlusTwo(String resultText) {
        assertThat("variable loop", resultText, containsString("i=2;4;6;8;[10]"));
    }

    @Test
    public void verifyNestedPlusPlus() {
        int i = 0;
        int j = 0;
        while (i < 10) {
            i++;
            while (j < 10) {
                j++;
            }
        }
        String resultText = FailureRenderer.render("xxxxx", "yyyyy", new RuntimeException("bar"));
        assertNestedPlusPlus(resultText);
    }

    private void assertNestedPlusPlus(String resultText) {
        assertThat("variable loop", resultText, containsString("i=1;2;3...;8;9;[10] =>10"));
        assertThat("variable loop", resultText, containsString("j=1;2;3...;8;9;[10] =>10"));
    }


    @Test
    public void verifyNestedPlusTwo() {
        int i = 0;
        int j = 0;
        while (i < 10) {
            i++;
            while (j < 10) {
                j+=2;
            }
        }
        String resultText = FailureRenderer.render("xxxxx", "yyyyy", new RuntimeException("bar"));
        assertNestedPlusTwo(resultText);
    }

    private void assertNestedPlusTwo(String resultText) {
        assertThat("variable loop", resultText, containsString("i=1;2;3...;8;9;[10] =>10"));
        assertThat("variable loop", resultText, containsString("j=2;4;6;8;[10]"));
    }
}