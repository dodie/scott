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
import java.util.Map;
import java.util.Properties;

import static java.lang.String.format;

@Mojo(name = "prepare-agent", defaultPhase = LifecyclePhase.INITIALIZE,
        requiresDependencyResolution = ResolutionScope.RUNTIME,
        threadSafe = true)
public class ScottAgentMojo extends AbstractMojo {

    private static final String SCOTT_ARTIFACT_NAME = "hu.advancedweb:scott";
    private static final String SUREFIRE_ARG_LINE = "argLine";

    @Parameter(property = "project", readonly = true)
    private MavenProject project;

    @Parameter(property = "plugin.artifactMap", required = true, readonly = true)
    Map<String, Artifact> pluginArtifactMap;

    public void execute() throws MojoExecutionException, MojoFailureException {
        final Properties projectProperties = this.project.getProperties();
        final File agentJarFile = pluginArtifactMap.get(SCOTT_ARTIFACT_NAME).getFile();
        String newValue = getArgument(agentJarFile);
        getLog().info(SUREFIRE_ARG_LINE + " set to " + newValue);
        projectProperties.setProperty(SUREFIRE_ARG_LINE, newValue);
    }

    private String getArgument(final File agentJarFile) {
        return format("-javaagent:%s", agentJarFile);
    }

}
