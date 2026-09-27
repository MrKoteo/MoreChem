package morechem.creativetab;

import java.util.function.Supplier;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import morechem.item.MorechemItems;

/**
 * All creative tabs of the mod in one place.
 *
 * <p>The icon is passed as a {@link Supplier} on purpose: the icons live in
 * {@link MorechemItems}, which in turn references the tabs below. Reading the
 * item fields eagerly would make the two classes deadlock each other during
 * static initialization, so the reference is resolved lazily instead.
 */
public final class MorechemCT {

	public static final CreativeTabs COMPOUNDS = create("morechem_compounds", () -> MorechemItems.ICON);
	public static final CreativeTabs ELEMENTS = create("morechem_elements", () -> MorechemItems.DEITERIUM);

	private MorechemCT() {}

	private static CreativeTabs create(String label, Supplier<Item> icon) {
		return new CreativeTabs(label) {
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(icon.get());
			}
		};
	}
}
