package hudson.maven.commands;

import hudson.Extension;
import hudson.cli.CLICommand;
import hudson.maven.MavenArgumentInterceptorAction;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.queue.QueueTaskFuture;
import hudson.util.ArgumentListBuilder;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

/**
 * User: zhaohuiyu
 * Date: 2/16/13
 * Time: 6:45 PM
 */
@Extension
public class MavenCommand extends CLICommand {

    @Argument(metaVar = "JOB", usage = "Name of the job to build", required = true)
    public AbstractProject<?, ?> job;

    @Option(name = "-ids", usage = "")
    public String ids;

    @Override
    public String getShortDescription() {
        return "In order to modify maven goals";
    }

    @Override
    protected int run() throws Exception {
        if (job instanceof MavenModuleSet) {
            MavenModuleSet mavenJob = (MavenModuleSet) job;
            String goals = mavenJob.getGoals();
            ModifiedGoalsAction action = new ModifiedGoalsAction(goals);
            QueueTaskFuture<? extends AbstractBuild<?, ?>> future = job.scheduleBuild2(0, new Cause.RemoteCause("", "Rerun"), action);
        }
        return 0;
    }

    private class ModifiedGoalsAction implements MavenArgumentInterceptorAction {

        private String oldGoals;

        public ModifiedGoalsAction(String oldGoals) {
            this.oldGoals = oldGoals;
        }

        public String getGoalsAndOptions(MavenModuleSetBuild build) {
            return String.format("%s -Dids=%s", oldGoals, ids);
        }

        public ArgumentListBuilder intercept(ArgumentListBuilder mavenargs, MavenModuleSetBuild build) {
            return null;
        }

        public String getIconFileName() {
            return null;
        }

        public String getDisplayName() {
            return null;
        }

        public String getUrlName() {
            return null;
        }
    }
}
