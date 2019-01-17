package io.jenkins.plugins.archetype;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.TaskListener;
import jenkins.scm.api.SCMProbeStat;
import jenkins.scm.api.SCMSource;
import jenkins.scm.api.SCMSourceCriteria;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.flow.FlowDefinition;
import org.jenkinsci.plugins.workflow.multibranch.AbstractWorkflowBranchProjectFactory;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.IOException;

public class ArchetypalBranchProjectFactory extends AbstractWorkflowBranchProjectFactory {

    private String filename;
    private FlowDefinition flowDefinition;

    @DataBoundConstructor
    public ArchetypalBranchProjectFactory() { }

    @Override protected FlowDefinition createDefinition() {
        return flowDefinition;
    }

    @Override
    protected SCMSourceCriteria getSCMSourceCriteria(@NonNull SCMSource scmSource) {
        return new SCMSourceCriteria() {
            @Override
            public boolean isHead(@NonNull Probe probe, @NonNull TaskListener listener) throws IOException {
                SCMProbeStat stat = probe.stat(filename);
                System.out.println("hi");
                switch (stat.getType()) {
                    case NONEXISTENT:
                        if (stat.getAlternativePath() != null) {
                            listener.getLogger().format("      ‘%s’ not found (but found ‘%s’, search is case sensitive)%n", filename, stat.getAlternativePath());
                        } else {
                            listener.getLogger().format("      ‘%s’ not found%n", filename);
                        }
                        return false;
                    case DIRECTORY:
                        listener.getLogger().format("      ‘%s’ found but is a directory not a file%n", filename);
                        return false;
                    default:
                        listener.getLogger().format("      ‘%s’ found%n", filename);
                        return true;

                }
            }
        };
    }

    public String getFilename() {
        return filename;
    }

    @DataBoundSetter
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public FlowDefinition getFlowDefinition() {
        return flowDefinition;
    }

    @DataBoundSetter
    public void setFlowDefinition(FlowDefinition flowDefinition) {
        this.flowDefinition = flowDefinition;
    }

    @Extension
    public static class DescriptorImpl extends AbstractWorkflowBranchProjectFactoryDescriptor {

        @Override
        public String getDisplayName() {
            return "Archetypal pipeline";
        }

    }
}
