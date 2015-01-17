package masterofgalaxy.assets.actors;

public enum ShipModuleType {
    Engine, Special;

    public String lowerCaseName() {
        return this.name().toLowerCase();
    }

    public boolean isTypeExclusive() {
        switch (this) {
            case Special:
                return false;
            default:
                return true;
        }
    }

    public boolean isOnlyOneUnitAllowed() {
        switch (this) {
            case Special:
                return true;
            default:
                return false;
        }
    }
}
