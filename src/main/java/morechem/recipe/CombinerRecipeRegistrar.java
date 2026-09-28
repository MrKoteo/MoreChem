package morechem.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Constructor;
import java.util.*;



public class CombinerRecipeRegistrar {
// L = recipeLines

// ex:    output, quantity, inp1, inp2...

private static List<String> L = new ArrayList<>(Arrays.asList(
    "alchemistry:neon_light, 1, alchemistry:element:10, alchemistry:compound:1*2",


    "minecraft:dye:10, 1, morechem:compound:198*4",
    "minecraft:dye:5,  1, morechem:compound:197*4",
    "minecraft:dye:1,  1, morechem:compound:200*4"
    // others
));

    // ---------- Reflection setup ----------
    private static final String DEFAULT_STAGE = "";
    private static final Constructor<?> COMBINER_CONSTRUCTOR;
    private static final List<Object> RECIPE_LIST;

    static {
        try {
            String basePkg = AlRecipesAccessor.basePackage("");

            Class<?> combinerClass = AlRecipesAccessor.recipeClass(basePkg, "CombinerRecipe");

            // CombinerRecipe(ItemStack output, List<ItemStack> inputs, String stage)
            COMBINER_CONSTRUCTOR = combinerClass.getConstructor(
                    ItemStack.class, List.class, String.class
            );
            COMBINER_CONSTRUCTOR.setAccessible(true);

            // get recipes list
            RECIPE_LIST = AlRecipesAccessor.recipeList(basePkg, "getCombinerRecipes", "CombinerRegister");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize CombinerRecipe integration", e);
        }
    }

    // ---------- reg ----------
    public static void registerAll() {
        if (L == null) return;
        for (String line : L) {
            try {
                parseAndRegister(line);
            } catch (Exception e) {
                System.out.println("Ooooops! That's an error! [MCRX|CombinerRegError]");
                e.printStackTrace();
            }
        }

		L.clear();
		L = null;
    }

    private static void parseAndRegister(String line) throws Exception {
        if (line == null || line.isEmpty()) return;
        List<String> parts = RecipeUtils.splitByComma(line);
        if (parts.size() < 3) return; // min: output, quantity, 1 input

        // 1. Output
        ItemStack output = RecipeUtils.parseItemStack(parts.get(0));
        if (output == null) return;
        int outputQty = RecipeUtils.parseInt(parts.get(1), 1);
        output.setCount(outputQty);

        // 2. Inputs
        List<ItemStack> inputs = new ArrayList<>();
        for (int i = 2; i < parts.size(); i++) {
            ItemStack in = RecipeUtils.parseItemStack(parts.get(i));
            if (in != null) {
                inputs.add(in.copy());
            }
        }
        if (inputs.isEmpty()) return;

        // 3. Create recipe (stage = null)
        Object recipe = COMBINER_CONSTRUCTOR.newInstance(output.copy(), inputs, DEFAULT_STAGE);
        RECIPE_LIST.add(recipe);
    }
}
