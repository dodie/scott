package hu.advancedweb.scott;

import java.util.List;

public class ScottPluginExtension {

    private String toolVersion;

    private List<String> trackMethodAnnotations;

    private List<String> junit4RuleMethodAnnotations;

    private List<String> junit5RuleMethodAnnotations;

    public ScottPluginExtension(String toolVersion){
        this.toolVersion = toolVersion;
    }

    public String getToolVersion() {
        return toolVersion;
    }

    public void setToolVersion(String toolVersion) {
        this.toolVersion = toolVersion;
    }

    public List<String> getTrackMethodAnnotations() {
        return trackMethodAnnotations;
    }

    public void setTrackMethodAnnotations(List<String> trackMethodAnnotations) {
        this.trackMethodAnnotations = trackMethodAnnotations;
    }

    public List<String> getJunit4RuleMethodAnnotations() {
        return junit4RuleMethodAnnotations;
    }

    public void setJunit4RuleMethodAnnotations(List<String> junit4RuleMethodAnnotations) {
        this.junit4RuleMethodAnnotations = junit4RuleMethodAnnotations;
    }

    public List<String> getJunit5RuleMethodAnnotations() {
        return junit5RuleMethodAnnotations;
    }

    public void setJunit5RuleMethodAnnotations(List<String> junit5RuleMethodAnnotations) {
        this.junit5RuleMethodAnnotations = junit5RuleMethodAnnotations;
    }
}
