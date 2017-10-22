package hu.advancedweb.maven;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.lang.String.format;
import static org.codehaus.plexus.util.StringUtils.join;

@Mojo(name = "prepare-agent", defaultPhase = LifecyclePhase.INITIALIZE,
        requiresDependencyResolution = ResolutionScope.RUNTIME,
        threadSafe = true)
public class ScottAgentMojo extends AbstractMojo {

    private static final String SCOTT_ARTIFACT_NAME = "hu.advancedweb:scott";
    private static final String ARG_LINE = "argLine";

    @Parameter(property = "project", readonly = true)
    private MavenProject project;

    @Parameter(property = "plugin.artifactMap", required = true, readonly = true)
    Map<String, Artifact> pluginArtifactMap;

    @Parameter(property = "scott.track.method_annotation")
    List<String> trackMethodAnnotations;

    @Parameter(property = "scott.inject_junit4_rule.method_annotation")
    List<String> junit4RuleMethodAnnotations;

    @Parameter(property = "scott.inject_junit5_extension.method_annotation")
    List<String> junit5RuleMethodAnnotations;

    public void execute() throws MojoExecutionException, MojoFailureException {
        final Properties projectProperties = this.project.getProperties();
        final File agentJarFile = pluginArtifactMap.get(SCOTT_ARTIFACT_NAME).getFile();
        String arguments = getArgument(agentJarFile);
        getLog().info(ARG_LINE + " set to " + arguments);
        projectProperties.setProperty(ARG_LINE, arguments);
    }

    private String getArgument(final File agentJarFile) {
        return format("-javaagent:%s", agentJarFile)
            + createScottArgument("scott.track.method_annotation", trackMethodAnnotations)
            + createScottArgument("scott.inject_junit4_rule.method_annotation", junit4RuleMethodAnnotations)
            + createScottArgument("scott.inject_junit5_extension.method_annotation", junit5RuleMethodAnnotations);
    }

    private String createScottArgument(String name, List<String> arguments) {
        if (arguments == null || arguments.isEmpty())
            return "";

        return format(" -D%s=\"%s\"", name, join(arguments.iterator(), ","));
    }

}
