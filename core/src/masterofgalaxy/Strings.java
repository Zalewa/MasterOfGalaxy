package masterofgalaxy;

public class Strings {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.contentEquals("");
    }

    public static boolean hasContents(String str) {
        return str != null && !str.trim().contentEquals("");
    }
}
