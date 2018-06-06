package masterofgalaxy.assets.actors;

import java.util.Locale;

public enum ShipModuleType {
    Engine, Special;

    public String lowerCaseName() {
        return this.name().toLowerCase(Locale.ROOT);
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
