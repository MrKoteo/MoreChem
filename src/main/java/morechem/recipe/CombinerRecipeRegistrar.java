package morechem.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CombinerRecipeRegistrar {
// L = recipeLines

// ex:    output, quantity, inp1, inp2...

private static List<String> L = new ArrayList<>(Arrays.asList(
    "alchemistry:neon_light, 1, alchemistry:element:10, alchemistry:compound:1*2"

    // others
));

    // ---------- Reflection setup ----------
    private static final Constructor<?> COMBINER_CONSTRUCTOR;
    private static final List<Object> RECIPE_LIST;
    private static String DEFAULT_STAGE;

    static {
        try {
            Class<?> combinerClass;
            String basePkg;
            try {
                basePkg = "al132.alchemistry";
                combinerClass = Class.forName(basePkg + ".recipes.CombinerRecipe");
                System.out.println("MCRX: Alchemistry original detected (Combiner)");
                DEFAULT_STAGE = ""; 
            } catch (ClassNotFoundException e) {
                basePkg = "io.enderdev.alchemistry";
                combinerClass = Class.forName(basePkg + ".recipes.CombinerRecipe");
                System.out.println("MCRX: Alchemistry fork EnderDev detected (Combiner)");
                DEFAULT_STAGE = "";
            }

            // CombinerRecipe(ItemStack output, List<ItemStack> inputs, String stage)
            COMBINER_CONSTRUCTOR = combinerClass.getConstructor(
                    ItemStack.class, List.class, String.class
            );
            COMBINER_CONSTRUCTOR.setAccessible(true);

            // get recipes list
            if (basePkg.startsWith("al132")) {
                Class<?> modRecipesClass = Class.forName(basePkg + ".recipes.ModRecipes");
                Field instanceField = modRecipesClass.getField("INSTANCE");
                Object modRecipes = instanceField.get(null);
                Method getListMethod = modRecipesClass.getMethod("getCombinerRecipes");
                RECIPE_LIST = (List<Object>) getListMethod.invoke(modRecipes);
            } else {
                Class<?> regClass = Class.forName(basePkg + ".recipes.register.CombinerRegister");
                Field companionField = regClass.getField("Companion");
                Object companionObj = companionField.get(null);
                Method getInstMethod = companionObj.getClass().getMethod("getINSTANCE");
                Object combinerReg = getInstMethod.invoke(companionObj);
                Method getRecipesMethod = regClass.getMethod("getRecipes");
                RECIPE_LIST = (List<Object>) getRecipesMethod.invoke(combinerReg);
            }
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
