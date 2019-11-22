package tutorial;

import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.PlanIdentifier;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.project.Project;
import com.atlassian.bamboo.specs.api.builders.task.Task;
import com.atlassian.bamboo.specs.api.model.task.TaskProperties;
import com.atlassian.bamboo.specs.builders.task.ScriptTask;
import com.atlassian.bamboo.specs.util.BambooServer;
import com.atlassian.bamboo.specs.api.builders.permission.Permissions;
import com.atlassian.bamboo.specs.api.builders.permission.PermissionType;
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;
import org.jetbrains.annotations.NotNull;

/**
 * Plan configuration for Bamboo.
 *
 * @see <a href="https://confluence.atlassian.com/display/BAMBOO/Bamboo+Specs">Bamboo Specs</a>
 */
@BambooSpec
public class PlanSpec {

    /**
     * Run 'main' to publish your plan.
     */
    public static void main(String[] args) throws Exception {
        // by default credentials are read from the '.credentials' file
        BambooServer bambooServer = new BambooServer("http://localhost:8085");

        Plan plan = new PlanSpec().createPlan();
        bambooServer.publish(plan);

        PlanPermissions planPermission = new PlanSpec().createPlanPermission(plan.getIdentifier());
        bambooServer.publish(planPermission);
    }

    PlanPermissions createPlanPermission(PlanIdentifier planIdentifier) {
        Permissions permissions = new Permissions()
                .userPermissions("vagrant", PermissionType.ADMIN)
                .groupPermissions("bamboo-admin", PermissionType.ADMIN)
                .loggedInUserPermissions(PermissionType.BUILD);
             /*   .anonymousUserPermissionView();*/

        return new PlanPermissions(planIdentifier)
                .permissions(permissions);
    }

    Project project() {
        return new Project()
                .name("MVN test project v.1.0.0")
                .key("PRO11")
                .description("My test project v.1.0.0");
    }

    Plan createPlan() {
        return new Plan(project(), "MVN+Intellij test plan v1.0.0", "PLN")
                .description("Plan created from Bamboo Java Specs")
                .stages(new Stage("Stage1")
                                .jobs(new Job("Build and run","RUN")
                                        .tasks(new ScriptTask().inlineBody("echo Hello world!"))),
                        new Stage("Stage2")
                                .jobs(new Job("Build or run","RON")
                                        .tasks(new ScriptTask().inlineBody("echo World HELLO!"))));
    }
}
