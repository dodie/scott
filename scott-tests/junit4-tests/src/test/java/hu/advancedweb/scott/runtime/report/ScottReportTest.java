package hu.advancedweb.scott.runtime.report;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ScottReportTest {

    @Test
    public void verifyVariableMapSnapshot() {
        ScottReport report = new ScottReport();
        report.addSnapshot(42, "foo", UUID.randomUUID().toString());
        report.addSnapshot(42, "foo", UUID.randomUUID().toString());
        report.addSnapshot(42, "foo", UUID.randomUUID().toString());

        report.addSnapshot(42, "bar", UUID.randomUUID().toString());
        report.addSnapshot(42, "bar", UUID.randomUUID().toString());

        report.addSnapshot(666, "beast", UUID.randomUUID().toString());

        Map<String, List<Snapshot>> variableMapSnapshot = report.getVariableMapSnapshot(42);

        assertThat("line 42 contains foo",variableMapSnapshot.containsKey("foo"), is(Boolean.TRUE));
        assertThat("line 42 foo with 3 values",variableMapSnapshot.get("foo").size(), is(3));

        assertThat("line 42 contains bar",variableMapSnapshot.containsKey("bar"), is(Boolean.TRUE));
        assertThat("line 42 bar with 2 values",variableMapSnapshot.get("bar").size(), is(2));

        variableMapSnapshot = report.getVariableMapSnapshot(666);
        assertThat("line 666 contains beast",variableMapSnapshot.containsKey("beast"), is(Boolean.TRUE));
        assertThat("line 666 beast with 1 value",variableMapSnapshot.get("beast").size(), is(1));

    }

}