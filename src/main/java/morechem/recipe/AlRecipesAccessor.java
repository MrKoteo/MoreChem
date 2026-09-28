package morechem.recipe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Shared reflection bridge to Alchemistry recipes.
 * Handles the difference between the original (al132) and the EnderDev fork packages.
 */
public final class AlRecipesAccessor {
    private static final String ORIGINAL_PKG = "al132.alchemistry";
    private static final String FORK_PKG = "io.enderdev.alchemistry";

    private AlRecipesAccessor() {
    }

    /**
     * Detects the installed Alchemistry package.
     *
     * @param logSuffix appended to the console detection message
     * @return the detected base package
     */
    public static String basePackage(String logSuffix) {
        try {
            Class.forName(ORIGINAL_PKG + ".recipes.ModRecipes");
            System.out.println("MCRX: Alchemistry original detected" + logSuffix);
            return ORIGINAL_PKG;
        } catch (ClassNotFoundException e) {
            System.out.println("MCRX: Alchemistry fork (EnderDev) detected" + logSuffix);
            return FORK_PKG;
        }
    }

    public static boolean isOriginal(String basePkg) {
        return ORIGINAL_PKG.equals(basePkg);
    }

    /**
     * Loads a class from the "recipes" package of the given Alchemistry package.
     */
    public static Class<?> recipeClass(String basePkg, String simpleName) {
        try {
            return Class.forName(basePkg + ".recipes." + simpleName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Alchemistry class not found: " + simpleName, e);
        }
    }

    /**
     * Returns the mutable list of recipes to register into.
     *
     * @param modRecipesGetter getter on ModRecipes (original only), e.g. "getDissolverRecipes"
     * @param registerClassName register class holding the recipes (fork only), e.g. "DissolverRegister"
     */
    @SuppressWarnings("unchecked")
    public static List<Object> recipeList(String basePkg, String modRecipesGetter, String registerClassName) {
        try {
            if (isOriginal(basePkg)) {
                Class<?> modRecipesClass = Class.forName(basePkg + ".recipes.ModRecipes");
                Field instanceField = modRecipesClass.getField("INSTANCE");
                Object modRecipes = instanceField.get(null);
                Method getListMethod = modRecipesClass.getMethod(modRecipesGetter);
                return (List<Object>) getListMethod.invoke(modRecipes);
            }
            Class<?> regClass = Class.forName(basePkg + ".recipes.register." + registerClassName);
            Field companionField = regClass.getField("Companion");
            Object companionObj = companionField.get(null);
            Method getInstMethod = companionObj.getClass().getMethod("getINSTANCE");
            Object reg = getInstMethod.invoke(companionObj);
            Method getRecipesMethod = regClass.getMethod("getRecipes");
            return (List<Object>) getRecipesMethod.invoke(reg);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Alchemistry recipe list: " + registerClassName, e);
        }
    }
}
