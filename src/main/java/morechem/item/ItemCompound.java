package morechem.item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import morechem.MorechemMod;
import morechem.creativetab.MorechemCT;

import java.util.List;

/**
 * The meta based compound item ({@code morechem:compound}).
 *
 * <p>Unlike {@link MorechemItems} every variant is a metadata value of a
 * single registry entry, so this class keeps its own registration and model
 * setup. {@link #CL} and {@link #EXTRA_TOOLTIPS} are the single source of
 * truth for the available variants and must stay in sync with the
 * {@code morechem:compound:<meta>} references in the Alchemistry integration.
 */
public final class ItemCompound {

	// List of item descriptions. The entry format is easily editable.
	// Each element: "META,registryName|unlocalizedKey|modelName,tooltip"
	// Model should be named as RegName
    private static final String[] CL = new String[] {
		//"0,compound_0,",
		"1,compound_1,Ag₂C₂",
		"2,compound_2,C₂O₄",
		"3,compound_3,CO₃",
		"4,compound_4,Ag₂Cr₂O₇",
		"5,compound_5,CrO₄",
		"6,compound_6,MoO₄",
		"7,compound_7,SeO₃",
		"8,compound_8,SeO₄",
		"9,compound_9,SO₄",
		"10,compound_10,PO₄",
		"11,compound_11,BrO₃",
		"12,compound_12,C₂H₃O₂",
		"13,compound_13,ClO₃",
		"14,compound_14,ClO₄",
		"15,compound_15,CN",
		"16,compound_16,AgCNO",
		"17,compound_17,IO₃",
		"18,compound_18,MnO₄",
		"19,compound_19,NO₃",
		"20,compound_20,SCN",
		"21,compound_21,NaHCO₃",
		"22,compound_22,Al₂O",
		"23,compound_23,AlO",
		"24,compound_24,CO₂",
		"25,compound_25,Al₂S₃",
		"26,compound_26,Na₃AlF₆",
		"27,compound_27,Al₄C₃",
		"28,compound_28,Al₆Si₂O₁₃",
		"29,compound_29,AlCl₂",
		"30,compound_30,AlCl₃",
		"31,compound_31,AlCl₄",
		"32,compound_32,AlCl₆",
		"33,compound_33,NaAlCl₄",
		"34,compound_34,AlCl₄Rb",
		"35,compound_35,AlCl₆K₃",
		"36,compound_36,AlCl₆Na₃",
		"37,compound_37,AlF₄",
		"38,compound_38,AlF₄Li",
		"39,compound_39,AlF₆Na₃",
		"40,compound_40,AlGaInP",
		"41,compound_41,AsH₃",
		"42,compound_42,B₃H₆N₃",
		"43,compound_43,Ba(BrO₃)•2H₂O",
		"44,compound_44,Ba(BrO₃)₂•2H₂O",
		"45,compound_45,BaFeSi₄O₁₀",
		"46,compound_46,BaTeO₄•3H₂O",
		"47,compound_47,C₂H₂",
		"48,compound_48,CH₃COOH",
		"49,compound_49,C₂H₅NH₂",
		"50,compound_50,C₂H₅NO₂",
		"51,compound_51,HNO₃",
		"52,compound_52,C₃H₅(NO₃)₃",
		"53,compound_53,C₃H₇NO₂S",
		"54,compound_54,C₃H₇NO₃",
		"55,compound_55,C₄H₈N₂O₃",
		"56,compound_56,C₅H₄NCOOH",
		"57,compound_57,C₅H₁₀N₂O₃",
		"58,compound_58,C₆H₅CH₂OH",
		"59,compound_59,C₆H₈O₇",
		"60,compound_60,C₁₀H₂₂",
		"61,compound_61,C₁₂H₂₆",
		"62,compound_62,C₁₄H₁₈N₂O₅",
		"63,compound_63,C₁₈H₃₂O₂",
		"64,compound_64,C₆H₅CH₃(NO₂)₃",
		"65,compound_65,C₁₈H₃₈",
		"66,compound_66,C₂₀H₂₄O₂N₂",
		"67,compound_67,C₂₀H₄₂",
		"68,compound_68,CFCl₂CF₂Cl",
		"69,compound_69,CH₂Cl₂",
		"70,compound_70,CH₂CO",
		"71,compound_71,CH₂O",
		"72,compound_72,CH₂OHCH₂OH",
		"73,compound_73,CH₃(CH₂)₁₆COOH",
		"74,compound_74,CH₃CCH",
		"75,compound_75,CH₃CH₂CH₂CH₂OH",
		"76,compound_76,CH₃CH₂CONH₂",
		"77,compound_77,CH₃CH₂OCH₂CH₃",
		"78,compound_78,CH₃CH₂OH",
		"79,compound_79,CH₃CHCH₂",
		"80,compound_80,CH₃COCH₃",
		"81,compound_81,CH₃COCl",
		"82,compound_82,CH₃COO-",
		"83,compound_83,CH₃COO(CH₂)₂CH(CH₃)₂",
		"84,compound_84,CH₃COOCH₂C₆H₅",
		"85,compound_85,CH₃COOCHCH₂",
		"86,compound_86,C₄₅H₇₃NO₁₅",
		"87,compound_87,CHCl₃",
		"88,compound_88,[Cu(H₂O)₄]SO₄•H₂O",
		"89,compound_89,Cu₂CO₃(OH)₂",
		"90,compound_90,CuFeS₂",
		"91,compound_91,D₂O",
		"92,compound_92,Ga₂(SO₄)₃•18H₂O",
		"93,compound_93,HC₃H₅O₃",
		"94,compound_94,HC₆H₇O₆",
		"95,compound_95,HC₁₂H₁₇ON₄SCl₂",
		"96,compound_96,MgCO₃",
		"97,compound_97,N₂H₄",
		"98,compound_98,NH₂CONH₂",
		"99,compound_99,NH₄NO₃",
		"100,compound_100,O₃",
		"101,compound_101,Y₂O₃",
		"102,compound_102,CNO",
		"103,compound_103,CaO",
		"104,compound_104,NH₃",
		"105,compound_105,H₂SO₄",
		"106,compound_106,SO₃",
		"107,compound_107,C₂H₄",
		"108,compound_108,(C₃H₆)n",
		"109,compound_109,(C₈H₈)n",
		"110,compound_110,Pb(N₃)₂",
		
		"111,compound_111,FeS",
		"112,compound_112,FeTaO₄",
		"113,compound_113,Sc₂Si₂O₇",
		"114,compound_114,FeTiO₃",
		"116,compound_116,C₆H₅CH₃",
		"117,compound_117,NO₂",

		"118,compound_118,C₆H₇N₃O₁₁",
		"119,compound_119,C₆H₂(NO₂)₃OH",
		"120,compound_120,C₆H₂(NO₂)₃(NH₂)OH",
		"121,compound_121,C₁₂H₅N₇O₁₂",
		"122,compound_122,C₃H₅N₃O₉",
		"123,compound_123,2(C₄H₈N₈O₈) + C₆H₅CH₃(NO₂)₃",
		"124,compound_124,C₅H₈(NO₃)₄",
		"125,compound_125,2(C₆H₅CH₃(NO₂)₃) + CH₄N₄O₂",
		"126,compound_126,C₅H₈(NO₃)₄ + C₃H₆N₆O₆",
		"127,compound_127,C₄H₈N₈O₈",
		"128,compound_128,C₇H₅N₅O₈",
		"129,compound_129,0.9(C₆H₅CH₃(NO₂)₃) + 0.1(Pb(N₃)₂)",
		"130,compound_130,C₁₀H₈ + NH₄NO₃",
		"131,compound_131,C₆N₁₂H₆O₁₂",
		"132,compound_132,NH₄NO₃ + C₆H₅CH₃(NO₂)₃ + 4Al",
		"133,compound_133,0.8(C₆H₅CH₃(NO₂)₃) + 0.2(C₃H₆N₆O₆)",
		"134,compound_134,C₃H₄N₄O₆",
		"135,compound_135,C₂H₄N₄O₄",
		"136,compound_136,C₈H₈N₂O₅",
		"137,compound_137,NH₄NO₃ + 2Al + (C₆H₁₀O₅)₄",
		"138,compound_138,C₆H₆N₆O₆",
		"139,compound_139,C₈(NO₂)₈",

		"140,compound_140,(C₄H₆)n",
		"141,compound_141,(C₃H₅N₃O)₄",
		"142,compound_142,FeS₂",
		"143,compound_143,PbS",
		"144,compound_144,Mg₂FeSiO₄",
		"145,compound_145,Fe₇S₈",
		"146,compound_146,MgSO₄·7H₂O",
		"147,compound_147,CH₃OH",
		"148,compound_148,CaAl₂Si₂O₈",
		"149,compound_149,H₂S",
		"150,compound_150,CH₃",
		"151,compound_151,CO",
		"152,compound_152,C₃H₅",
		"153,compound_153,C₃H₇",
		"154,compound_154,CH₂",
		"155,compound_155,C₆H₅",
		"156,compound_156,C₂₀H₂₅N₃O",
		"157,compound_157,C₆H₂",
		"158,compound_158,C₅H₈",
		"159,compound_159,CH₄N₄O₂",
		"160,compound_160,C₃H₆N₆O₆",
        "161,compound_161,HNO",
        "162,compound_162,FeO",
        "163,compound_163,Na₂O",
        "164,compound_164,Li₂O",
        "165,compound_165,CaCO₃",
        "166,compound_166,H₃PO₄",
        "167,compound_167,H₂O₂",
        "168,compound_168,C₆H₆",
        "169,compound_169,C₆H₅(OH)",
        "170,compound_170,C₆H₅NH₂",
        "171,compound_171,HF",
        "172,compound_172,V₂O₅",

		// NEW
        "173,compound_173,K₄[Fe(CN)₆]·3H₂O",
        "174,compound_174,K₃[Fe(CN)₆]",
        "175,compound_175,Fe₄[Fe(CN)₆]₃",
        "176,compound_176,Fe(SCN)₃",
        "177,compound_177,CoCl₂",
        "178,compound_178,CuSO₄",
        "179,compound_179,CoCO₃",
		"180,compound_180,Co₃O₄",
        "181,compound_181,BaTiO₃",
        "182,compound_182,Ni₀.₅Zn₀.₅Fe₂O₄",
        "183,compound_183,(C₁₂H₂₂N₂O₂)ₙ",
        "184,compound_184,(C₁₄H₁₀N₂O₂)ₙ",
        "185,compound_185,(C₁₁H₁₂O₃)ₙ",
        "186,compound_186,C₉H₈O₄",
        "187,compound_187,C₈H₉NO₂",
        "188,compound_188,C₁₃H₁₈O₂",
        "189,compound_189,C₁₈H₂₁NO₃",
        "190,compound_190,C₁₆H₁₈N₂O₄S",
        "190,compound_190,C₂₂H₂₄N₂O₈",
        "191,compound_191,C₉H₁₃NO₃",
        "192,compound_192,C₆H₃N₃O₈",
        "193,compound_193,C₆H₆N₄O₇",
        "194,compound_194,N₂H₅NO₃",
        "195,compound_195,P₄",
        "196,compound_196,CoAl₂O₄",
        "197,compound_197,Co₃(PO₄)₂",
        "198,compound_198,CoZnO₂",
        "199,compound_199,CdS",
        "200,compound_200,CdSe",
        "201,compound_201,Cr₂O₃",
        "202,compound_202,TiO₂",
        "203,compound_203,C₈H₇N₃O₂",
        "204,compound_204,(C₆H₁₁NO₄)ₙ",
        "205,compound_205,C₂₄H₃₈O₁₉",
        //"206,compound_206,meow",
        //"207,compound_207,meow",
        //"208,compound_208,meow",
        //"209,compound_209,meow",
        //"210,compound_210,meow",



		/*
        "300,compound_300,meow",
        "301,compound_301,meow",
        "302,compound_302,meow",
        "303,compound_303,meow",
        "304,compound_304,meow",
        "305,compound_305,meow",
        "306,compound_306,meow",
        "307,compound_307,meow",
        "308,compound_308,meow",
        "309,compound_309,meow",
        "310,compound_310,meow",
        "311,compound_311,meow",
        "312,compound_312,meow",
        "313,compound_313,meow",
        "314,compound_314,meow",
        "315,compound_315,meow",
        "316,compound_316,meow",
        "317,compound_317,meow",
        "318,compound_318,meow",
        "319,compound_319,meow",
        "320,compound_320,meow",
        "321,compound_321,meow",
        "322,compound_322,meow",
        "323,compound_323,meow",
        "324,compound_324,meow",
        "325,compound_325,meow",
        "326,compound_326,meow",
        "327,compound_327,meow",
        "328,compound_328,meow",
        "329,compound_329,meow"
		*/

        
    };
    // 1 = ₁ | 2 = ₂ | 3 = ₃ | 3 = ₄ ...

	//##################################################

	private static Entry[] ENTRIES_BY_META;

	// format: "meta,tooltip"
	private static final String[] EXTRA_TOOLTIPS = new String[] {
		"16," +TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 0.4T" +" | "+TextFormatting.GOLD+"E = 1700 kJ/kg"+" | "+TextFormatting.AQUA+"B = 4000 m/s",
		"64," +TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.0T" +" | "+TextFormatting.GOLD+"E = 4520 kJ/kg"+" | "+TextFormatting.AQUA+"B = 6900 m/s",
		"110,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 0.3T" +" | "+TextFormatting.GOLD+"E = 1600 kJ/kg"+" | "+TextFormatting.AQUA+"B = 5300 m/s",
		"118,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.1T" +" | "+TextFormatting.GOLD+"E = 4200 kJ/kg"+" | "+TextFormatting.AQUA+"B = 7300 m/s",
		"119,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.2T" +" | "+TextFormatting.GOLD+"E = 4200 kJ/kg"+" | "+TextFormatting.AQUA+"B = 7350 m/s",
		"120,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.2T" +" | "+TextFormatting.GOLD+"E = 4200 kJ/kg"+" | "+TextFormatting.AQUA+"B = 7300 m/s",
		"121,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.25T"+" | "+TextFormatting.GOLD+"E = 4500 kJ/kg"+" | "+TextFormatting.AQUA+"B = 7400 m/s",
		"122,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.5T" +" | "+TextFormatting.GOLD+"E = 6700 kJ/kg"+" | "+TextFormatting.AQUA+"B = 7700 m/s",
		"123,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.3T" +" | "+TextFormatting.GOLD+"E = 5500 kJ/kg"+" | "+TextFormatting.AQUA+"B = 8600 m/s",
		"124,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.66T"+" | "+TextFormatting.GOLD+"E = 5800 kJ/kg"+" | "+TextFormatting.AQUA+"B = 8400 m/s",
		"125,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.35T"+" | "+TextFormatting.GOLD+"E = 5000 kJ/kg"+" | "+TextFormatting.AQUA+"B = 7300 m/s",
		"126,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.25T"+" | "+TextFormatting.GOLD+"E = 5500 kJ/kg"+" | "+TextFormatting.AQUA+"B = 8000 m/s",
		"127,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.7T" +" | "+TextFormatting.GOLD+"E = 5700 kJ/kg"+" | "+TextFormatting.AQUA+"B = 9100 m/s",
		"128,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.25T"+" | "+TextFormatting.GOLD+"E = 4500 kJ/kg"+" | "+TextFormatting.AQUA+"B = 7850 m/s",
		"129,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.0T" +" | "+TextFormatting.GOLD+"E = 4000 kJ/kg"+" | "+TextFormatting.AQUA+"B = 7000 m/s",
		"130,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 0.95T"+" | "+TextFormatting.GOLD+"E = 4000 kJ/kg"+" | "+TextFormatting.AQUA+"B = 6000 m/s",
		"131,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 2.0T" +" | "+TextFormatting.GOLD+"E = 7000 kJ/kg"+" | "+TextFormatting.AQUA+"B = 9600 m/s",
		"132,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.5T" +" | "+TextFormatting.GOLD+"E = 6500 kJ/kg"+" | "+TextFormatting.AQUA+"B = 6200 m/s",
		"133,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.2T" +" | "+TextFormatting.GOLD+"E = 5000 kJ/kg"+" | "+TextFormatting.AQUA+"B = 7600 m/s",
		"134,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.75T"+" | "+TextFormatting.GOLD+"E = 5800 kJ/kg"+" | "+TextFormatting.AQUA+"B = 8800 m/s",
		"135,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.5T" +" | "+TextFormatting.GOLD+"E = 5500 kJ/kg"+" | "+TextFormatting.AQUA+"B = 8700 m/s",
		"136,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 0.95T"+" | "+TextFormatting.GOLD+"E = 4000 kJ/kg"+" | "+TextFormatting.AQUA+"B = 6000 m/s",
		"137,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.3T" +" | "+TextFormatting.GOLD+"E = 6500 kJ/kg"+" | "+TextFormatting.AQUA+"B = 4500 m/s",
		"138,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.1T" +" | "+TextFormatting.GOLD+"E = 4500 kJ/kg"+" | "+TextFormatting.AQUA+"B = 8000 m/s",
		"139,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 2.7T" +" | "+TextFormatting.GOLD+"E = 7500 kJ/kg"+" | "+TextFormatting.AQUA+"B = 11000 m/s",
		"160,"+TextFormatting.RED+"BB: "+TextFormatting.YELLOW+"C = 1.6T" +" | "+TextFormatting.GOLD+"E = 5360 kJ/kg"+" | "+TextFormatting.AQUA+"B = 8750 m/s",
	    
	    // others...
	};

    static class Entry {
        final int meta;
        final String regName;
        final String tooltip;

		ModelResourceLocation modelLoc;
	    String extraTooltip;
        
	    Entry(int meta, String regName, String tooltip) {
	        this.meta = meta; this.regName = regName; this.tooltip = tooltip;
	    }
    }

    static {
	    // define  max meta from  CL / EXTRA
	    int maxMeta = -1;
	    for (String line : CL) {
	        String[] p = line.split(",", 2);
	        if (p.length < 1) continue;
	        try { maxMeta = Math.max(maxMeta, Integer.parseInt(p[0].trim())); }
	        catch (NumberFormatException ignored) {}
	    }
	    for (String line : EXTRA_TOOLTIPS) {
	        String[] p = line.split(",", 2);
	        if (p.length < 2) continue;
	        try { maxMeta = Math.max(maxMeta, Integer.parseInt(p[0].trim())); }
	        catch (NumberFormatException ignored) {}
	    }
	    ENTRIES_BY_META = new Entry[maxMeta + 1];
	
	    // Fill ENTRIES_BY_META <- CL
	    for (String line : CL) {
	        String[] parts = line.split(",", 3);
	        if (parts.length < 3) continue;
	        try {
	            int meta = Integer.parseInt(parts[0].trim());
	            String reg = parts[1].trim();
	            String tip = parts[2].trim();
	            ENTRIES_BY_META[meta] = new Entry(meta, reg, tip);
	        } catch (NumberFormatException ignored) {}
	    }
	    // Attach EXTRA_TOOLTIPS to the matching entry
	    for (String line : EXTRA_TOOLTIPS) {
	        String[] parts = line.split(",", 2);
	        if (parts.length < 2) continue;
	        try {
	            int meta = Integer.parseInt(parts[0].trim());
	            String tip = parts[1];
	            if (meta >= 0 && meta < ENTRIES_BY_META.length) {
	                Entry e = ENTRIES_BY_META[meta];
	                if (e != null) e.extraTooltip = tip;
	            }
	        } catch (NumberFormatException ignored) {}
	    }
    }

	//##################################################

	/** The single registry entry holding every compound as a metadata value. */
	public static final Item ITEM = new ItemCustom();

	private ItemCompound() {}

	public static void register(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(ITEM);
	}

    // register model for * meta
	@SideOnly(Side.CLIENT)
	public static void registerModels() {
	    for (Entry entry : ENTRIES_BY_META) {
	        if (entry == null) continue;
	        if (entry.modelLoc == null) {
	            entry.modelLoc = new ModelResourceLocation(new ResourceLocation(MorechemMod.MODID, entry.regName), "inventory");
	        }
	        ModelLoader.setCustomModelResourceLocation(ITEM, entry.meta, entry.modelLoc);
	    }
	}
    // package-private class ItemCustom
    static class ItemCustom extends Item {
        ItemCustom() {
            setHasSubtypes(true);
            setUnlocalizedName("compound");
            setRegistryName(MorechemMod.MODID, "compound");
            setCreativeTab(MorechemCT.COMPOUNDS);
        }
		@Override
		public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		    if (!this.isInCreativeTab(tab)) return;
		    for (Entry e : ENTRIES_BY_META) {
		        if (e == null) continue;
		        items.add(new ItemStack(this, 1, e.meta));
		    }
		}
		@Override
		public String getUnlocalizedName(ItemStack stack) {
		    int meta = stack.getMetadata();
		    Entry entry = (meta >= 0 && meta < ENTRIES_BY_META.length) ? ENTRIES_BY_META[meta] : null;
		    if (entry == null) return "item.compound_unknown.name";
		    return "item." + entry.regName + "";
		}
		@Override
		public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
		    super.addInformation(itemstack, world, list, flag);
		    int meta = itemstack.getMetadata();
		    if (meta >= 0 && meta < ENTRIES_BY_META.length) {
		        Entry e = ENTRIES_BY_META[meta];
		        if (e != null) {
		            if (!e.tooltip.isEmpty()) list.add(e.tooltip);
		            if (e.extraTooltip != null) list.add(e.extraTooltip);
		        }
		    }
		}
        @Override
        public int getItemEnchantability() { return 0; }
        @Override
        public int getMaxItemUseDuration(ItemStack itemstack) { return 0; }
        @Override
        public float getDestroySpeed(ItemStack par1ItemStack, IBlockState par2Block) { return 1F; }
    }
}
