package morechem.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;



public class AlRecipesRemover {
	// ---------- CONFIGURATION ----------
	// Target lists (item specifications: "modid:name[:meta]")
	// All invalid items (not found in the registry) will be automatically skipped.
    private static final List<String> DIS_REM_SPECS = Collections.unmodifiableList(Arrays.asList(
            "morechem:compound:1",
            "minecraft:melon"
    ));
    private static final List<String> COM_REM_SPECS = Collections.unmodifiableList(Arrays.asList(
            "alchemistry:compound:1"
    ));
    // ----------------------------------

    // Cache for parsed ItemStack (prototype count=1)
    //private static final Map<String, ItemStack> ITEM_CACHE = new ConcurrentHashMap<>(128);
    // Reflection fields / caches
    private static List<Object> dissolverRecipeList;
    private static List<Object> combinerRecipeList;
    private static Method dissolverGetInputMethod;
    private static Method combinerGetOutputMethod;
    private static Method ingredientGetMatchingStacks;

    static {
        try {
            // Determine the Alchemistry branch by the presence of classes
            Class<?> dissolverClass;
            Class<?> combinerClass;
            Class<?> dissolverRecipeClass;
            Class<?> combinerRecipeClass;
            String basePkg;

            try {
                basePkg = "al132.alchemistry";
                dissolverClass = Class.forName(basePkg + ".recipes.DissolverRecipe");
                combinerClass = Class.forName(basePkg + ".recipes.CombinerRecipe");
                System.out.println("MCRX: Alchemistry original detected");
            } catch (ClassNotFoundException e) {
                basePkg = "io.enderdev.alchemistry";
                dissolverClass = Class.forName(basePkg + ".recipes.DissolverRecipe");
                combinerClass = Class.forName(basePkg + ".recipes.CombinerRecipe");
                System.out.println("MCRX: Alchemistry fork (EnderDev) detected");
            }

            dissolverRecipeClass = dissolverClass;
            combinerRecipeClass = combinerClass;

            // Getting recipe lists for Dissolver
            if (basePkg.startsWith("al132")) {
                // Original: ModRecipes.INSTANCE.getDissolverRecipes()
                Class<?> modRecipesClass = Class.forName(basePkg + ".recipes.ModRecipes");
                Field instanceField = modRecipesClass.getField("INSTANCE");
                Object modRecipes = instanceField.get(null);
                Method getDissolverList = modRecipesClass.getMethod("getDissolverRecipes");
                dissolverRecipeList = (List<Object>) getDissolverList.invoke(modRecipes);

                // Combiner: ModRecipes
                Method getCombinerList = modRecipesClass.getMethod("getCombinerRecipes");
                combinerRecipeList = (List<Object>) getCombinerList.invoke(modRecipes);
            } else {
                // Fork EnderDev: DissolverRegister.Companion.getINSTANCE().getRecipes()
                Class<?> dissolverRegClass = Class.forName(basePkg + ".recipes.register.DissolverRegister");
                Field companionField = dissolverRegClass.getField("Companion");
                Object companionObj = companionField.get(null);
                Method getInstMethod = companionObj.getClass().getMethod("getINSTANCE");
                Object dissolverReg = getInstMethod.invoke(companionObj);
                Method getRecipesMethod = dissolverRegClass.getMethod("getRecipes");
                dissolverRecipeList = (List<Object>) getRecipesMethod.invoke(dissolverReg);

                // Combiner: CombinerRegister.Companion.getINSTANCE().getRecipes()
                Class<?> combinerRegClass = Class.forName(basePkg + ".recipes.register.CombinerRegister");
                Field combinerCompanionField = combinerRegClass.getField("Companion");
                Object combinerCompanionObj = combinerCompanionField.get(null);
                Method combinerGetInstMethod = combinerCompanionObj.getClass().getMethod("getINSTANCE");
                Object combinerReg = combinerGetInstMethod.invoke(combinerCompanionObj);
                Method combinerGetRecipesMethod = combinerRegClass.getMethod("getRecipes");
                combinerRecipeList = (List<Object>) combinerGetRecipesMethod.invoke(combinerReg);
            }

            // recipes
            dissolverGetInputMethod = dissolverRecipeClass.getMethod("getInput"); // -> Ingredient
            combinerGetOutputMethod = combinerRecipeClass.getMethod("getOutput"); // -> ItemStack

            // Ingredient.getMatchingStacks() (func_193365_a)
            ingredientGetMatchingStacks = Ingredient.class.getMethod("func_193365_a");

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize recipe removal system", e);
        }
    }
    public static void registerAll() {
        removeDissolverTargets();
        removeCombinerTargets();
    }

    private static void removeDissolverTargets() {
        if (dissolverRecipeList == null) return;

        List<ItemStack> targets = parseTargetList(DIS_REM_SPECS);
        if (targets.isEmpty()) return;
        Iterator<Object> it = dissolverRecipeList.iterator();
        while (it.hasNext()) {
            Object recipe = it.next();
            try {
                Ingredient input = (Ingredient) dissolverGetInputMethod.invoke(recipe);
                ItemStack[] stacks = (ItemStack[]) ingredientGetMatchingStacks.invoke(input);
                for (ItemStack target : targets) {
                    if (containsStack(stacks, target)) {
                        it.remove();
                        break;
                    }
                }
            } catch (Exception e) {
                // ignore so as not to break the entire cycle
            }
        }
    }
    private static void removeCombinerTargets() {
        if (combinerRecipeList == null) return;

        List<ItemStack> targets = parseTargetList(COM_REM_SPECS);
        if (targets.isEmpty()) return;

        Iterator<Object> it = combinerRecipeList.iterator();
        while (it.hasNext()) {
            Object recipe = it.next();
            try {
                ItemStack output = (ItemStack) combinerGetOutputMethod.invoke(recipe);
                for (ItemStack target : targets) {
                    if (OreDictionary.itemMatches(target, output, false)) {
                        it.remove();
                        break;
                    }
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }
	/**
	* Converts a list of specifications into a list of valid ItemStacks with count=1.
	* Invalid items are skipped.
	*/
    private static List<ItemStack> parseTargetList(List<String> specs) {
        List<ItemStack> result = new ArrayList<>();
        for (String spec : specs) {
            ItemStack stack = RecipeUtils.parseItemStack(spec);
            if (stack != null) {
                ItemStack norm = stack.copy();
                norm.setCount(1);
                result.add(norm);
            }
        }
        return result;
    }
	/**
	* Checks whether target (taking into account the OreDictionary.WILDCARD_VALUE meta mask)
	* is contained in the stacks array.
	*/
    private static boolean containsStack(ItemStack[] stacks, ItemStack target) {
        for (ItemStack s : stacks) {
            if (OreDictionary.itemMatches(target, s, false)) {
                return true;
            }
        }
        return false;
    }
}
