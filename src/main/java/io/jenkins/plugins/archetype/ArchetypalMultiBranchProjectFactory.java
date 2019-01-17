package io.jenkins.plugins.archetype;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.ExtensionList;
import hudson.model.DescriptorVisibilityFilter;
import jenkins.branch.MultiBranchProjectFactory;
import jenkins.branch.MultiBranchProjectFactoryDescriptor;
import jenkins.scm.api.SCMSource;
import jenkins.scm.api.SCMSourceCriteria;
import org.jenkinsci.plugins.workflow.flow.FlowDefinition;
import org.jenkinsci.plugins.workflow.flow.FlowDefinitionDescriptor;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.multibranch.AbstractWorkflowMultiBranchProjectFactory;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.DoNotUse;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.CheckForNull;
import java.io.IOException;
import java.util.Collection;

public class ArchetypalMultiBranchProjectFactory extends AbstractWorkflowMultiBranchProjectFactory {

    private String filename;
    private FlowDefinition flowDefinition;

    @DataBoundConstructor
    public ArchetypalMultiBranchProjectFactory() { }

    @Override
    protected SCMSourceCriteria getSCMSourceCriteria(@NonNull SCMSource scmSource) {
        return null;
    }

    private ArchetypalBranchProjectFactory newProjectFactory() {
        ArchetypalBranchProjectFactory projectFactory = new ArchetypalBranchProjectFactory();
        projectFactory.setFilename(filename);
        projectFactory.setFlowDefinition(flowDefinition);
        return projectFactory;
    }

    @Override
    protected void customize(WorkflowMultiBranchProject project) throws IOException, InterruptedException {
        project.setProjectFactory(newProjectFactory());
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
    public static class DescriptorImpl extends MultiBranchProjectFactoryDescriptor {

        @CheckForNull
        @Override
        public MultiBranchProjectFactory newInstance() {
            return new ArchetypalMultiBranchProjectFactory();
        }

        @Override
        public String getDisplayName() {
            return "Archetypal pipeline";
        }

        /** TODO JENKINS-20020 can delete this in case {@code f:dropdownDescriptorSelector} defaults to applying {@code h.filterDescriptors} */
        @Restricted(DoNotUse.class) // Jelly
        public Collection<FlowDefinitionDescriptor> getDefinitionDescriptors(WorkflowJob context) {
            return DescriptorVisibilityFilter.apply(context, ExtensionList.lookup(FlowDefinitionDescriptor.class));
        }

    }
}
