package morechem.creativetab;

import java.util.function.Supplier;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import morechem.item.MorechemItems;



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
