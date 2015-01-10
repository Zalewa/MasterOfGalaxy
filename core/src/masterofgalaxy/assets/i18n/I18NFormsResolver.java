package masterofgalaxy.assets.i18n;

import java.text.MessageFormat;

public class I18NFormsResolver {
    public enum Form {
        Plural("plural");

        String name;

        Form(String name) {
            this.name = name;
        }
    }

    public static String resolve(String base, String bundle, Form form, Object ... args) {
        String key = MessageFormat.format("{0}_{1}", base, form.name);
        return I18N.resolveNamed(bundle, key, args);
    }
}
