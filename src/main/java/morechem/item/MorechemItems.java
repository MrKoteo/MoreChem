package morechem.item;

import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import morechem.MorechemMod;
import morechem.creativetab.MorechemCT;

/**
 * Registration of every mod item that has no metadata.
 *
 * <p>Each item is a real registry entry: {@code morechem:deiterium},
 * {@code morechem:tritium} and so on. The meta based chemical compounds live
 * in {@link ItemCompound} and are registered separately.
 */
public final class MorechemItems {

	// ===== Isotopes =====
	public static final Item DEITERIUM = createItem("deiterium", MorechemCT.ELEMENTS, "²H");
	public static final Item HELIUM_3 = createItem("helium_3", MorechemCT.ELEMENTS, "³He");
	public static final Item TRITIUM = createItem("tritium", MorechemCT.ELEMENTS, "³H");
	public static final Item CARBON_14 = createItem("c_14", MorechemCT.ELEMENTS, "¹⁴C");
	public static final Item POTASSIUM_40 = createItem("k_40", MorechemCT.ELEMENTS, "⁴⁰K");
	public static final Item CESIUM_137 = createItem("cs_137", MorechemCT.ELEMENTS, "¹³⁷Cs");
	public static final Item URANIUM_232 = createItem("u_232", MorechemCT.ELEMENTS, "²³²U");
	public static final Item URANIUM_235 = createItem("u_235", MorechemCT.ELEMENTS, "²³⁵U");
	public static final Item PLUTONIUM_244 = createItem("pu_244", MorechemCT.ELEMENTS, "²⁴⁴Pu");
	public static final Item STRONTIUM_90 = createItem("sr_90", MorechemCT.ELEMENTS, "⁹⁰Sr");
	public static final Item COBALT_60 = createItem("co_60", MorechemCT.ELEMENTS, "⁶⁰Co");
	public static final Item AMERICIUM_241 = createItem("am_241", MorechemCT.ELEMENTS, "²⁴¹Am");



	// ===== Misc =====
	public static final Item ICON = createItem("icon", null, null);

	private static Item[] ITEMS_TO_REGISTER = {
			DEITERIUM, HELIUM_3, TRITIUM, CARBON_14, POTASSIUM_40, CESIUM_137,
			URANIUM_232, URANIUM_235, PLUTONIUM_244, STRONTIUM_90, COBALT_60,
			AMERICIUM_241,

			ICON
	};
	public static final void clearCache() {
		ITEMS_TO_REGISTER = null;

	}


	private MorechemItems() {}

	public static void register(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(ITEMS_TO_REGISTER);
	}

	@SideOnly(Side.CLIENT)
	public static void registerModels() {
		for (Item item : ITEMS_TO_REGISTER) {
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}

	/**
	 * @param name        registry and unlocalized name, e.g. {@code deiterium}
	 * @param creativeTab tab to show the item in, {@code null} to hide it
	 * @param tooltipText single tooltip line, {@code null} for none
	 */
	private static Item createItem(String name, CreativeTabs creativeTab, String tooltipText) {
		TooltipConsumer consumer = tooltipText == null ? null : (stack, world, lines, flag) -> lines.add(tooltipText);

		Item item = new ItemWithTooltip(consumer);
		item.setRegistryName(MorechemMod.MODID, name);
		item.setUnlocalizedName(name);
		item.setCreativeTab(creativeTab);
		return item;
	}

	@FunctionalInterface
	private interface TooltipConsumer {
		void accept(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag);
	}

	private static class ItemWithTooltip extends Item {

		private final TooltipConsumer tooltipConsumer;

		private ItemWithTooltip(TooltipConsumer tooltipConsumer) {
			this.tooltipConsumer = tooltipConsumer;
		}
		@Override
		@SideOnly(Side.CLIENT)
		public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
			super.addInformation(stack, world, tooltip, flag);

			if (tooltipConsumer != null) {
				tooltipConsumer.accept(stack, world, tooltip, flag);
			}
		}
	}
}
