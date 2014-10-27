package lang.executor;

public class EnvironmentValue implements Value {

    private ExecutorEnvironment environment;

    public EnvironmentValue(ExecutorEnvironment environment) {
        this.environment = environment;
    }

    public ExecutorEnvironment getEnvironment() {
        return environment;
    }
}
