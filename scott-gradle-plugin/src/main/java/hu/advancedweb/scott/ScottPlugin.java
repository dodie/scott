package hu.advancedweb.scott;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.process.CommandLineArgumentProvider;
import org.gradle.process.JavaForkOptions;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScottPlugin implements Plugin<Project> {

    public static final String DEFAULT_SCOTT_VERSION = "3.5.0";
    public static final String AGENT_CONFIGURATION_NAME = "scottAgent";
    public static final String PLUGIN_EXTENSION_NAME = "scott";

    @Override
    public void apply(Project project) {

        ScottPluginExtension extension = project.getExtensions().create(PLUGIN_EXTENSION_NAME, ScottPluginExtension.class, DEFAULT_SCOTT_VERSION);
        Configuration configuration = configureAgentDependencies(project,extension);

        project.afterEvaluate( p -> {
            Task test = p.getTasks().getByName("test");
            JavaForkOptions opts = (JavaForkOptions)test;
            opts.getJvmArgumentProviders().add(new ScottAgent(configuration,extension));
        });
    }

    private Configuration configureAgentDependencies(Project project, ScottPluginExtension extension) {
        Configuration agentConf = project.getConfigurations().create(AGENT_CONFIGURATION_NAME);
        agentConf.setVisible(false);
        agentConf.setTransitive(true);
        agentConf.setDescription("The Scott agent to use detailed failure reports and hassle free assertions for Java tests");
        agentConf.defaultDependencies(dependencies ->
            dependencies.add(project.getDependencies().create("hu.advancedweb:scott:" + extension.getToolVersion()))
        );
        return agentConf;
    }

    static class ScottAgent implements CommandLineArgumentProvider {
        private Configuration agentConfig;
        private ScottPluginExtension scottPluginExtension;

        private File agentJar;
        public ScottAgent(Configuration agentConfig, ScottPluginExtension scottPluginExtension){
            this.agentConfig = agentConfig;
            this.scottPluginExtension = scottPluginExtension;
        }

        @Override
        public Iterable<String> asArguments() {
            return Arrays.asList(getArgument());
        }

        private String getArgument() {
            return String.format("-javaagent:%s", getJar())
                    + createScottArgument("scott.track.method_annotation", scottPluginExtension.getTrackMethodAnnotations())
                    + createScottArgument("scott.inject_junit4_rule.method_annotation", scottPluginExtension.getJunit4RuleMethodAnnotations())
                    + createScottArgument("scott.inject_junit5_extension.method_annotation", scottPluginExtension.getJunit5RuleMethodAnnotations());
        }

        private String createScottArgument(String name, List<String> arguments) {
            if (arguments == null || arguments.isEmpty())
                return "";

            return String.format(" -D%s=\"%s\"", name, arguments.stream().collect(Collectors.joining( "," )));
        }

        private File getJar() {
            if (agentJar == null) {
                agentJar = agentConfig
                        .filter( (file) -> file.getName().startsWith("scott"))
                        .getSingleFile();
            }
            return agentJar;
        }
    }
}
