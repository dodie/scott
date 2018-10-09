package hu.advancedweb.scott;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TryWithResourcesRecordingTest {

    @Test
    public void try_with_resources() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(out)) {

            printStream.append("Hello World");

            assertThat(TestHelper.getLastRecordedStateForVariable("out"), equalTo(out.toString()));
        }
    }
}
