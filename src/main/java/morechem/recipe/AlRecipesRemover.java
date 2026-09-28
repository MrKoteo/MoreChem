package morechem.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

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
            String basePkg = AlRecipesAccessor.basePackage("");

            // Getting recipe lists
            dissolverRecipeList = AlRecipesAccessor.recipeList(basePkg, "getDissolverRecipes", "DissolverRegister");
            combinerRecipeList = AlRecipesAccessor.recipeList(basePkg, "getCombinerRecipes", "CombinerRegister");

            // recipes
            dissolverGetInputMethod = AlRecipesAccessor.recipeClass(basePkg, "DissolverRecipe").getMethod("getInput"); // -> Ingredient
            combinerGetOutputMethod = AlRecipesAccessor.recipeClass(basePkg, "CombinerRecipe").getMethod("getOutput"); // -> ItemStack

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
