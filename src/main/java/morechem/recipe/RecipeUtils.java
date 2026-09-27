package morechem.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public final class RecipeUtils {
    private static final Map<String, ItemStack> ITEM_CACHE = new ConcurrentHashMap<>(512);

    public static ItemStack parseItemStack(String spec) {
        if (spec == null) return null;
        String s = spec.trim();
        if (s.isEmpty()) return null;

        ItemStack cached = ITEM_CACHE.get(s);
        if (cached != null) return cached.copy();

        int count = 1;
        int star = s.indexOf('*');
        if (star >= 0) {
            String cntStr = s.substring(star + 1).trim();
            count = parseInt(cntStr, 1);
            s = s.substring(0, star).trim();
        }
        int firstColon = s.indexOf(':');
        if (firstColon < 0) return null;
        int secondColon = s.indexOf(':', firstColon + 1);

        String modid, name, metaStr = null;
        if (secondColon >= 0) {
            modid = s.substring(0, firstColon);
            name = s.substring(firstColon + 1, secondColon);
            metaStr = s.substring(secondColon + 1);
        } else {
            modid = s.substring(0, firstColon);
            name = s.substring(firstColon + 1);
        }
        int meta = 0;
        if (metaStr != null) {
            String m = metaStr.trim();
            if ("*".equals(m)) meta = OreDictionary.WILDCARD_VALUE;
            else meta = parseInt(m, 0);
        }
        Item item = Item.REGISTRY.getObject(new ResourceLocation(modid, name));
        if (item == null) return null;

        ItemStack stack = new ItemStack(item, count, meta);
        ItemStack proto = stack.copy();
        proto.setCount(1);
        ITEM_CACHE.put(s, proto);

        if (stack.getCount() != 1) {
            ItemStack ret = proto.copy();
            ret.setCount(count);
            return ret;
        }
        return proto.copy();
    }
    public static List<String> splitByComma(String line) {
        List<String> res = new ArrayList<>(8);
        int len = line.length();
        int i = 0;
        StringBuilder sb = new StringBuilder(32);
        while (i < len) {
            char c = line.charAt(i);
            if (c == ',') {
                String token = sb.toString().trim();
                if (!token.isEmpty()) res.add(token);
                sb.setLength(0);
                i++;
                continue;
            } else {
                sb.append(c);
            }
            i++;
        }
        String last = sb.toString().trim();
        if (!last.isEmpty()) res.add(last);
        return res;
    }
    public static int parseInt(String s, int def) {
        if (s == null) return def;
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; }
    }
    public static double parseDouble(String s, double def) {
        if (s == null) return def;
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return def; }
    }
}
