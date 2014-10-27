package lang.builder;

public class FormEnvironment {

    private FormEnvironment parent;

    public static FormEnvironment root() {
        return new FormEnvironment(null);
    }

    private FormEnvironment(FormEnvironment parent) {
        this.parent = parent;
    }
    
    public FormEnvironment extend() {
        return new FormEnvironment(this);
    }

    public FormEnvironment getParent() {
        return parent;
    }
}
