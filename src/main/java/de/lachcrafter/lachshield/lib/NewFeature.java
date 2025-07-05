package de.lachcrafter.lachshield.lib;

public abstract class NewFeature {

    private final String name;

    public NewFeature(String name) {
        this.name = name;
    }

    /**
     * @return the feature name
     */
    public String getName() {
        return name;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onReload();
}
