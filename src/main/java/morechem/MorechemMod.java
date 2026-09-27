package morechem;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import morechem.item.ItemCompound;
import morechem.item.MorechemItems;
import morechem.recipe.AlRecipesRemover;
import morechem.recipe.CombinerRecipeRegistrar;
import morechem.recipe.DissolverRecipeRegistrar;



@Mod(modid = MorechemMod.MODID, version = MorechemMod.VERSION)
public class MorechemMod {

	public static final String MODID = "morechem";
	public static final String VERSION = "1.3";

	public static final SimpleNetworkWrapper PACKET_HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel("morechem:a");

	@SidedProxy(clientSide = "morechem.ClientProxyMorechemMod", serverSide = "morechem.ServerProxyMorechemMod")
	public static IProxyMorechemMod proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// FML only wires the mod instance into its own lifecycle bus, so the
		// @SubscribeEvent methods below need an explicit Forge bus registration.
		MinecraftForge.EVENT_BUS.register(this);

		//registerMessage(Side.SERVER);
		//registerMessage(Side.CLIENT);
		proxy.preInit(event);
	}

	/** {@link SimpleNetworkWrapper#registerMessage} takes a single side at a time. */
	//private static void registerMessage(Side side) {
	//}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		AlRecipesRemover.registerAll();
		DissolverRecipeRegistrar.registerAll();
		CombinerRecipeRegistrar.registerAll();
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		proxy.serverLoad(event);
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		MorechemItems.register(event);
		ItemCompound.register(event);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		MorechemItems.registerModels();
		ItemCompound.registerModels();
	}

	//@SubscribeEvent
	//public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
	//	if (event.player.world.isRemote)  return;
	//
	//}
}
