package morechem.recipe;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;



public class DissolverRecipeRegistrar {
// L = recipeLines

// ex:    input, reversible, quantity, rolls, relative,   prob, out, prob, out...
// reversible - auto add to  Combiner
// quantity - input count
// rolls -  roll count
// relative - type (Absolute/Relative)

private static List<String> L = new ArrayList<>(Arrays.asList(
"morechem:compound:1,   true, 1, 1, false, 100, alchemistry:element:47*2, alchemistry:element:6*2",
"morechem:compound:2,   true, 1, 1, false, 100, alchemistry:element:6*2,  alchemistry:element:8*4",
"morechem:compound:3,   true, 1, 1, false, 100, alchemistry:element:6,    alchemistry:element:8*3",
"morechem:compound:4,   true, 1, 1, false, 100, alchemistry:element:47*2, alchemistry:element:24*2, alchemistry:element:8*7",
"morechem:compound:5,   true, 1, 1, false, 100, alchemistry:element:24,   alchemistry:element:8*4",
"morechem:compound:6,   true, 1, 1, false, 100, alchemistry:element:42,   alchemistry:element:8*4",
"morechem:compound:7,   true, 1, 1, false, 100, alchemistry:element:34,   alchemistry:element:8*3",
"morechem:compound:8,   true, 1, 1, false, 100, alchemistry:element:34,   alchemistry:element:8*4",
"morechem:compound:9,   true, 1, 1, false, 100, alchemistry:element:16,   alchemistry:element:8*4",
"morechem:compound:10,  true, 1, 1, false, 100, alchemistry:element:15,   alchemistry:element:8*4",
"morechem:compound:11,  true, 1, 1, false, 100, alchemistry:element:35,   alchemistry:element:8*3",
"morechem:compound:12,  true, 1, 1, false, 100, alchemistry:element:6*2,  alchemistry:element:1*3,  alchemistry:element:8*2",
"morechem:compound:13,  true, 1, 1, false, 100, alchemistry:element:17,   alchemistry:element:8*3",
"morechem:compound:14,  true, 1, 1, false, 100, alchemistry:element:17,   alchemistry:element:8*4",
"morechem:compound:15,  true, 1, 1, false, 100, alchemistry:element:6,    alchemistry:element:7",
"morechem:compound:16,  true, 1, 1, false, 100, alchemistry:element:47,   morechem:compound:102",
"morechem:compound:17,  true, 1, 1, false, 100, alchemistry:element:53,   alchemistry:element:8*3",
"morechem:compound:18,  true, 1, 1, false, 100, alchemistry:element:25,   alchemistry:element:8*4",
"morechem:compound:19,  true, 1, 1, false, 100, alchemistry:element:7,    alchemistry:element:8*3",
"morechem:compound:20,  true, 1, 1, false, 100, alchemistry:element:16,   alchemistry:element:6,    alchemistry:element:7",
"morechem:compound:21,  true, 1, 1, false, 100, alchemistry:element:11,   alchemistry:element:1,    morechem:compound:3",
"morechem:compound:22,  true, 1, 1, false, 100, alchemistry:element:13*2, alchemistry:element:8",
"morechem:compound:23,  true, 1, 1, false, 100, alchemistry:element:13,   alchemistry:element:8",
"morechem:compound:24,  true, 1, 1, false, 100, alchemistry:element:6,    alchemistry:element:8*2",
"morechem:compound:25,  true, 1, 1, false, 100, alchemistry:element:13*2, alchemistry:element:16*3",
"morechem:compound:26,  true, 1, 1, false, 100, alchemistry:element:11*3, alchemistry:element:13,   alchemistry:element:9*6",
"morechem:compound:27,  true, 1, 1, false, 100, alchemistry:element:13*4, alchemistry:element:6*3",
"morechem:compound:28,  true, 1, 1, false, 100, alchemistry:element:13*6, alchemistry:element:14*2, alchemistry:element:8*13",
"morechem:compound:29,  true, 1, 1, false, 100, alchemistry:element:13,   alchemistry:element:17*2",
"morechem:compound:30,  true, 1, 1, false, 100, alchemistry:element:13,   alchemistry:element:17*3",
"morechem:compound:31,  true, 1, 1, false, 100, alchemistry:element:13,   alchemistry:element:17*4",
"morechem:compound:32,  true, 1, 1, false, 100, alchemistry:element:13,   alchemistry:element:17*6",
// 33
"morechem:compound:34,  true, 1, 1, false, 100, alchemistry:element:13,   alchemistry:element:17*4, alchemistry:element:37",
"morechem:compound:35,  true, 1, 1, false, 100, alchemistry:element:13,   alchemistry:element:17*6, alchemistry:element:19",
"morechem:compound:36,  true, 1, 1, false, 100, alchemistry:element:13,   alchemistry:element:17*6, alchemistry:element:11",
"morechem:compound:37,  true, 1, 1, false, 100, alchemistry:element:13,   alchemistry:element:9*4",
"morechem:compound:38,  true, 1, 1, false, 100, alchemistry:element:13,   alchemistry:element:9*4,  alchemistry:element:3",
"morechem:compound:39,  true, 1, 1, false, 100, alchemistry:element:13,   alchemistry:element:9*6,  alchemistry:element:11*3",
"morechem:compound:40,  true, 1, 1, false, 100, alchemistry:element:13,   alchemistry:element:31,   alchemistry:element:49, alchemistry:element:15",
"morechem:compound:41,  true, 1, 1, false, 100, alchemistry:element:33,   alchemistry:element:1*3",
"morechem:compound:42,  true, 1, 1, false, 100, alchemistry:element:5*3,  alchemistry:element:1*6,  alchemistry:element:7*3",
"morechem:compound:43,  true, 1, 1, false, 100, alchemistry:element:56,   alchemistry:element:35,   alchemistry:element:8*3, alchemistry:compound:7*2",
"morechem:compound:44,  true, 1, 1, false, 100, alchemistry:element:56,   alchemistry:element:35*2, alchemistry:element:8*6, alchemistry:compound:7*2",
"morechem:compound:45,  true, 1, 1, false, 100, alchemistry:element:56,   alchemistry:element:26,   alchemistry:element:14*4,alchemistry:element:8*10",
"morechem:compound:46,  true, 1, 1, false, 100, alchemistry:element:56,   alchemistry:element:52,   alchemistry:element:8*4, alchemistry:compound:7*3",
"morechem:compound:47,  true, 1, 1, false, 100, alchemistry:element:6*2,  alchemistry:element:1*2",
"morechem:compound:48,  true, 1, 1, false, 100, morechem:compound:150,    morechem:compound:151,    alchemistry:compound:15",
"morechem:compound:49,  true, 1, 1, false, 100, alchemistry:element:6*2,  alchemistry:element:1*5,  alchemistry:compound:27",
"morechem:compound:50,  true, 1, 1, false, 100, alchemistry:element:6*2,  alchemistry:element:1*5,  morechem:compound:117",
"morechem:compound:51,  true, 1, 1, false, 100, alchemistry:element:1,    alchemistry:element:7,    alchemistry:element:8*3",
"morechem:compound:52,  true, 1, 1, false, 100, morechem:compound:152,    morechem:compound:19*3",
"morechem:compound:53,  true, 1, 1, false, 100, morechem:compound:153,    morechem:compound:117,    alchemistry:element:16",
"morechem:compound:54,  true, 1, 1, false, 100, morechem:compound:153,    morechem:compound:19",
"morechem:compound:55,  true, 1, 1, false, 100, alchemistry:element:6*4,  alchemistry:element:1*8,  alchemistry:element:7*2, alchemistry:element:8*3",
"morechem:compound:56,  true, 1, 1, false, 100, alchemistry:element:6*6,  alchemistry:element:1*5,  alchemistry:element:8*2, alchemistry:element:7",
"morechem:compound:57,  true, 1, 1, false, 100, alchemistry:element:6*5,  alchemistry:element:1*10, alchemistry:element:7*2, alchemistry:element:8*3",
"morechem:compound:58,  true, 1, 1, false, 100, morechem:compound:155,    morechem:compound:154,    alchemistry:compound:15",
"morechem:compound:59,  true, 1, 1, false, 100, alchemistry:element:6*6,  alchemistry:element:1*8,  alchemistry:element:8*7",
"morechem:compound:60,  true, 1, 1, false, 100, alchemistry:element:6*10, alchemistry:element:1*22",
"morechem:compound:61,  true, 1, 1, false, 100, alchemistry:element:6*12, alchemistry:element:1*26",
"morechem:compound:62,  true, 1, 1, false, 100, alchemistry:element:6*14, alchemistry:element:1*18, alchemistry:element:7*2, alchemistry:element:8*5",
"morechem:compound:63,  true, 1, 1, false, 100, alchemistry:element:6*18, alchemistry:element:1*32, alchemistry:element:8*2",
"morechem:compound:64,  true, 1, 1, false, 100, morechem:compound:116,    100, morechem:compound:117*3",
"morechem:compound:65,  true, 1, 1, false, 100, alchemistry:element:6*18, alchemistry:element:1*38",
"morechem:compound:66,  true, 1, 1, false, 100, alchemistry:element:6*20, alchemistry:element:1*24, alchemistry:element:8*2, alchemistry:element:7*2",
"morechem:compound:67,  true, 1, 1, false, 100, alchemistry:element:6*20, alchemistry:element:1*42",
"morechem:compound:68,  true, 1, 1, false, 100, alchemistry:element:6*2,  alchemistry:element:9*3,  alchemistry:element:17*3",
"morechem:compound:69,  true, 1, 1, false, 100, morechem:compound:154,    alchemistry:element:17*2",
"morechem:compound:70,  true, 1, 1, false, 100, morechem:compound:154,    morechem:compound:151",
"morechem:compound:71,  true, 1, 1, false, 100, morechem:compound:154,    alchemistry:element:8*1",
"morechem:compound:72,  true, 1, 1, false, 100, morechem:compound:154*2,  alchemistry:compound:15*2",
"morechem:compound:73,  true, 1, 1, false, 100, morechem:compound:150,    morechem:compound:154*16, alchemistry:element:6,   alchemistry:element:8*2, alchemistry:element:1",
"morechem:compound:74,  true, 1, 1, false, 100, morechem:compound:150,    alchemistry:element:6*2,  alchemistry:element:1",
"morechem:compound:75,  true, 1, 1, false, 100, morechem:compound:150,    morechem:compound:154*3,  alchemistry:compound:15",
"morechem:compound:76,  true, 1, 1, false, 100, morechem:compound:150,    morechem:compound:154,    morechem:compound:151,   alchemistry:compound:27",
"morechem:compound:77,  true, 1, 1, false, 100, morechem:compound:150*2,  morechem:compound:154*2,  alchemistry:element:8",
"morechem:compound:78,  true, 1, 1, false, 100, morechem:compound:150,    morechem:compound:154,    alchemistry:compound:15",
"morechem:compound:79,  true, 1, 1, false, 100, morechem:compound:150,    morechem:compound:154,    alchemistry:element:6,   alchemistry:element:1",
"morechem:compound:80,  true, 1, 1, false, 100, morechem:compound:150*2,  morechem:compound:151",
"morechem:compound:81,  true, 1, 1, false, 100, morechem:compound:150,    morechem:compound:151,    alchemistry:element:17",
"morechem:compound:82,  true, 1, 1, false, 100, morechem:compound:150,    alchemistry:element:6,    alchemistry:element:8*2",
"morechem:compound:83,  true, 1, 1, false, 100, morechem:compound:150*3,  morechem:compound:154*2,  alchemistry:element:6*2, alchemistry:element:8*2, alchemistry:element:1",
"morechem:compound:84,  true, 1, 1, false, 100, morechem:compound:150*3,  alchemistry:element:6*8,  alchemistry:element:1*7, alchemistry:element:8*2",
"morechem:compound:85,  true, 1, 1, false, 100, morechem:compound:150*3,  alchemistry:element:6*3,  alchemistry:element:1*3, alchemistry:element:8*2",
"morechem:compound:86,  true, 1, 1, false, 100, alchemistry:element:6*45, alchemistry:element:1*73, alchemistry:element:7,   alchemistry:element:8*15",
"morechem:compound:87,  true, 1, 1, false, 100, alchemistry:element:6,    alchemistry:element:1,    alchemistry:element:17*3",
"morechem:compound:88,  true, 1, 1, false, 100, alchemistry:element:29,   alchemistry:compound:7*4, morechem:compound:9, 100, alchemistry:compound:7",
"morechem:compound:89,  true, 1, 1, false, 100, alchemistry:element:29*2, morechem:compound:3,      alchemistry:compound:15*2",
"morechem:compound:90,  true, 1, 1, false, 100, alchemistry:element:29,   alchemistry:element:26,   alchemistry:element:16*2",
"morechem:compound:91,  true, 1, 1, false, 100, morechem:deiterium*2,     alchemistry:element:8",
"morechem:compound:92,  true, 1, 1, false, 100, alchemistry:element:31*2, morechem:compound:9*3,    100, alchemistry:compound:7*18",
"morechem:compound:93,  true, 1, 1, false, 100, alchemistry:element:1*6,  alchemistry:element:6*3,  alchemistry:element:8*3",
"morechem:compound:94,  true, 1, 1, false, 100, alchemistry:element:1*8,  alchemistry:element:6*6,  alchemistry:element:8*6",

"morechem:compound:95,  true, 1, 1, false, 100, alchemistry:element:1*18, alchemistry:element:6*12, alchemistry:element:8, alchemistry:element:7*4, alchemistry:element:16, alchemistry:element:17*2",

"morechem:compound:96,  true, 1, 1, false, 100, alchemistry:element:12,   morechem:compound:3",
"morechem:compound:97,  true, 1, 1, false, 100, alchemistry:element:7*2,  alchemistry:element:1*4",
"morechem:compound:98,  true, 1, 1, false, 100, alchemistry:compound:27*2,morechem:compound:151",
"morechem:compound:99,  true, 1, 1, false, 100, alchemistry:compound:29*2,morechem:compound:19",
"morechem:compound:100, true, 1, 1, false, 100, alchemistry:element:8*3",
"morechem:compound:101, true, 1, 1, false, 100, alchemistry:element:39*2, alchemistry:element:8*3",
"morechem:compound:102, true, 1, 1, false, 100, alchemistry:element:6,    alchemistry:element:7,    alchemistry:element:8",
"morechem:compound:103, true, 1, 1, false, 100, alchemistry:element:20,   alchemistry:element:8",
"morechem:compound:104, true, 1, 1, false, 100, alchemistry:element:7,    alchemistry:element:1*3",
"morechem:compound:105, true, 1, 1, false, 100, morechem:compound:9,      alchemistry:element:1*2",
"morechem:compound:106, true, 1, 1, false, 100, alchemistry:element:16,   alchemistry:element:8*3",
"morechem:compound:107, true, 1, 1, false, 100, alchemistry:element:6*2,  alchemistry:element:1*4",
"morechem:compound:108, true, 1, 1, false, 100, alchemistry:element:6*3,  alchemistry:element:1*6",
"morechem:compound:109, true, 1, 1, false, 100, alchemistry:element:6*8,  alchemistry:element:1*8",
"morechem:compound:110, true, 1, 1, false, 100, alchemistry:element:82,   alchemistry:element:7*6",
"morechem:compound:111, true, 1, 1, false, 100, alchemistry:element:26,   alchemistry:element:16",
"morechem:compound:112, true, 1, 1, false, 100, alchemistry:element:26,   alchemistry:element:73,   alchemistry:element:8*4",
"morechem:compound:113, true, 1, 1, false, 100, alchemistry:compound:1*2, alchemistry:element:21*2, alchemistry:element:8*3",
"morechem:compound:114, true, 1, 1, false, 100, alchemistry:compound:26,  alchemistry:element:22,   alchemistry:element:8*3",
// 115
"morechem:compound:116, true, 1, 1, false, 100, morechem:compound:155,    morechem:compound:150",
"morechem:compound:117, true, 1, 1, false, 100, alchemistry:element:7,    alchemistry:element:8*2",
"morechem:compound:118, true, 1, 1, false, 100, alchemistry:element:6*6,  alchemistry:element:1*7,  alchemistry:element:7*3, alchemistry:element:8*11",

"morechem:compound:119, true, 1, 1, false, 100, morechem:compound:157,    morechem:compound:117*3,  alchemistry:compound:15",
"morechem:compound:120, true, 1, 1, false, 100, morechem:compound:157,    morechem:compound:117*3,  alchemistry:compound:27, alchemistry:compound:15",
"morechem:compound:121, true, 1, 1, false, 100, alchemistry:element:6*12, alchemistry:element:1*5,  alchemistry:element:7*7, alchemistry:element:8*12",
"morechem:compound:122, true, 1, 1, false, 100, morechem:compound:152,    alchemistry:element:7*3,  alchemistry:element:8*9",
"morechem:compound:123, true, 1, 1, false, 100, morechem:compound:127*2,  morechem:compound:64",
"morechem:compound:124, true, 1, 1, false, 100, morechem:compound:158,    morechem:compound:19*4",
"morechem:compound:125, true, 1, 1, false, 100, morechem:compound:64*2,   morechem:compound:159",
"morechem:compound:126, true, 1, 1, false, 100, morechem:compound:124,    morechem:compound:160",
"morechem:compound:127, true, 1, 1, false, 100, alchemistry:element:6*4,  alchemistry:element:1*8,  alchemistry:element:7*8, alchemistry:element:8*8",
"morechem:compound:128, true, 1, 1, false, 100, alchemistry:element:6*7,  alchemistry:element:1*5,  alchemistry:element:7*5, alchemistry:element:8*8",
// 129
"morechem:compound:130, true, 1, 1, false, 100, alchemistry:element:6*10, alchemistry:element:1*8,  100, morechem:compound:99",
"morechem:compound:131, true, 1, 1, false, 100, morechem:compound:102*6,  morechem:compound:161*6",
"morechem:compound:132, true, 1, 1, false, 100, morechem:compound:99,     morechem:compound:64,     alchemistry:element:13*4",
// 133
"morechem:compound:134, true, 1, 1, false, 100, alchemistry:element:6*3,  alchemistry:element:1*4,  alchemistry:element:7*4, alchemistry:element:8*6",
"morechem:compound:135, true, 1, 1, false, 100, alchemistry:element:6*2,  alchemistry:element:1*4,  alchemistry:element:7*4, alchemistry:element:8*4",
"morechem:compound:136, true, 1, 1, false, 100, alchemistry:element:6*8,  alchemistry:element:1*8,  alchemistry:element:7*2, alchemistry:element:8*5",
"morechem:compound:137, true, 1, 1, false, 100, morechem:compound:99,     100, alchemistry:compound*4,  100, alchemistry:element:13*2",
"morechem:compound:138, true, 1, 1, false, 100, alchemistry:element:6*6,  alchemistry:element:1*6,  alchemistry:element:7*6, alchemistry:element:8*6",
"morechem:compound:139, true, 1, 1, false, 100, alchemistry:element:6*8,  morechem:compound:117*8", 
"morechem:compound:140, true, 4, 1, false, 100, alchemistry:element:6*4,  alchemistry:element:1*6",
"morechem:compound:141, true, 1, 4, false, 100, morechem:compound:152,    alchemistry:element:7*3,  alchemistry:element:8",


"morechem:compound:142, true, 1, 1, false, 100, alchemistry:element:26,   alchemistry:element:16*2",
"morechem:compound:143, true, 1, 1, false, 100, alchemistry:element:82,   alchemistry:element:16",
"morechem:compound:144, true, 1, 1, false, 100, alchemistry:element:12*2, alchemistry:element:16,   alchemistry:element:14,  alchemistry:element:8*4",
"morechem:compound:145, true, 1, 1, false, 100, alchemistry:element:26*7, alchemistry:element:16*8",
"morechem:compound:146, true, 1, 1, false, 100, alchemistry:compound:43,  alchemistry:compound:7*7",
"morechem:compound:147, true, 1, 1, false, 100, morechem:compound:150,    alchemistry:compound:15",
"morechem:compound:148, true, 1, 1, false, 100, alchemistry:element:20,   alchemistry:element:13*2, alchemistry:element:14*2,alchemistry:element:8*8",
"morechem:compound:149, true, 1, 1, false, 100, alchemistry:element:1*2,  alchemistry:element:16",
"morechem:compound:150, true, 1, 1, false, 100, alchemistry:element:6,    alchemistry:element:1*4",
"morechem:compound:151, true, 1, 1, false, 100, alchemistry:element:6,    alchemistry:element:8",
"morechem:compound:152, true, 1, 1, false, 100, alchemistry:element:6*3,  alchemistry:element:1*5",
"morechem:compound:153, true, 1, 1, false, 100, alchemistry:element:6*3,  alchemistry:element:1*7",
"morechem:compound:154, true, 1, 1, false, 100, alchemistry:element:6,    alchemistry:element:1*2",
"morechem:compound:155, true, 1, 1, false, 100, alchemistry:element:6*6,  alchemistry:element:1*5",
"morechem:compound:156, true, 1, 1, false, 100, alchemistry:element:6*20, alchemistry:element:1*25, alchemistry:element:7*3, alchemistry:element:8",
"morechem:compound:157, true, 1, 1, false, 100, alchemistry:element:6*6,  alchemistry:element:1*2",
"morechem:compound:158, true, 1, 1, false, 100, alchemistry:element:6*5,  alchemistry:element:1*8",
"morechem:compound:159, true, 1, 1, false, 100, alchemistry:compound:34,  alchemistry:element:1*8,  alchemistry:element:7*4, alchemistry:element:8*2",
"morechem:compound:160, true, 1, 1, false, 100, alchemistry:element:6*3,  morechem:compound:161*3",
"morechem:compound:161, true, 1, 1, false, 100, alchemistry:element:1,    alchemistry:element:7,    alchemistry:element:8",
"morechem:compound:162, true, 1, 1, false, 100, alchemistry:element:26,   alchemistry:element:8",
"morechem:compound:163, true, 1, 1, false, 100, alchemistry:element:11*2, alchemistry:element:8",
"morechem:compound:164, true, 1, 1, false, 100, alchemistry:element:3*2,  alchemistry:element:8",
"morechem:compound:165, true, 1, 1, false, 100, alchemistry:element:20,   alchemistry:element:6,    alchemistry:element:8*3",
"morechem:compound:166, true, 1, 1, false, 100, alchemistry:element:1*3,  alchemistry:element:15,   alchemistry:element:8*4",
"morechem:compound:167, true, 1, 1, false, 100, alchemistry:element:1*2,  alchemistry:element:8*2",
"morechem:compound:168, true, 1, 1, false, 100, alchemistry:element:6*6,  alchemistry:element:1*6",
"morechem:compound:169, true, 1, 1, false, 100, alchemistry:element:6*6,  alchemistry:element:1*5,  alchemistry:compound:15",
"morechem:compound:170, true, 1, 1, false, 100, alchemistry:element:6*6,  alchemistry:element:1*5,  alchemistry:compound:27",
"morechem:compound:171, true, 1, 1, false, 100, alchemistry:element:1,    alchemistry:element:9",
"morechem:compound:172, true, 1, 1, false, 100, alchemistry:element:23*2, alchemistry:element:8*5",


"morechem:compound:173, true, 1, 1, false, 100, morechem:compound:15*6,	  alchemistry:element:19*4, alchemistry:element:26,   alchemistry:compound:7*3",
"morechem:compound:174, true, 1, 1, false, 100, morechem:compound:15*6,   alchemistry:element:19*3, alchemistry:element:26",
"morechem:compound:175, true, 1, 1, false, 100, morechem:compound:15*18,  alchemistry:element:26*7",
"morechem:compound:176, true, 1, 1, false, 100, morechem:compound:20*3,   alchemistry:element:26",
"morechem:compound:177, true, 1, 1, false, 100, alchemistry:element:27,   alchemistry:element:17*2",
"morechem:compound:178, true, 1, 1, false, 100, morechem:compound:9,      alchemistry:element:29",
"morechem:compound:179, true, 1, 1, false, 100, morechem:compound:3,      alchemistry:element:27",
"morechem:compound:180, true, 1, 1, false, 100, alchemistry:element:27*3, alchemistry:element:8*4",
"morechem:compound:181, true, 1, 1, false, 100, alchemistry:element:56,   alchemistry:element:22,    alchemistry:element:8*3",
"morechem:compound:182, true, 1, 1, false, 100, alchemistry:element:28*3, alchemistry:element:30*3,  alchemistry:element:26*2, alchemistry:element:8*4",
"morechem:compound:183, true, 1, 1, false, 100, alchemistry:element:6*12, alchemistry:element:1*22,  alchemistry:element:7*2,  alchemistry:element:8*2",
"morechem:compound:184, true, 1, 1, false, 100, alchemistry:element:6*14, alchemistry:element:1*10,  alchemistry:element:7*2,  alchemistry:element:8*2",
"morechem:compound:185, true, 1, 1, false, 100, alchemistry:element:6*11, alchemistry:element:1*12,  alchemistry:element:8*3",
"morechem:compound:186, true, 1, 1, false, 100, alchemistry:element:6*9,  alchemistry:element:1*8,   alchemistry:element:8*4",
"morechem:compound:187, true, 1, 1, false, 100, alchemistry:element:6*8,  alchemistry:element:1*9,   morechem:compound:117",
"morechem:compound:188, true, 1, 1, false, 100, alchemistry:element:6*13, alchemistry:element:1*18,  alchemistry:element:8*2",
"morechem:compound:189, true, 1, 1, false, 100, alchemistry:element:6*18, alchemistry:element:1*21,  morechem:compound:19",
"morechem:compound:190, true, 1, 1, false, 100, alchemistry:element:6*16, alchemistry:element:1*18,  alchemistry:element:7*2,  alchemistry:element:8*4, alchemistry:element:16",
"morechem:compound:191, true, 1, 1, false, 100, alchemistry:element:6*9,  alchemistry:element:1*13,  morechem:compound:19",
"morechem:compound:192, true, 1, 1, false, 100, morechem:compound:119,    alchemistry:element:8",
"morechem:compound:193, true, 1, 1, false, 100, morechem:compound:119,    morechem:compound:104",
"morechem:compound:194, true, 1, 1, false, 100, morechem:compound:97,     morechem:compound:51",
"morechem:compound:195, true, 1, 1, false, 100, alchemistry:element:15*4",
"morechem:compound:197, true, 1, 1, false, 100, alchemistry:element:27*3, morechem:compound:10*2",
"morechem:compound:198, true, 1, 1, false, 100, alchemistry:element:27,   alchemistry:element:30,    alchemistry:element:8*2",
"morechem:compound:200, true, 1, 1, false, 100, alchemistry:element:48,   alchemistry:element:34",
"morechem:compound:202, true, 1, 1, false, 100, alchemistry:element:22,   alchemistry:element:8*2",
"morechem:compound:203, true, 1, 1, false, 100, alchemistry:element:6*8,  alchemistry:element:1*7,   alchemistry:element:7*3,  alchemistry:element:8*2",
"morechem:compound:204, true, 1, 1, false, 100, alchemistry:element:6*6,  alchemistry:element:1*11,  alchemistry:element:7,    alchemistry:element:8*4",
"morechem:compound:205, true, 1, 1, false, 100, alchemistry:element:6*24, alchemistry:element:1*38,  alchemistry:element:8*19",
"morechem:compound:206, true, 1, 1, false, 100, alchemistry:element:6*22, alchemistry:element:1*24,  alchemistry:element:7*2,  alchemistry:element:8*8",








// minecraft addition


"minecraft:poisonous_potato, false, 1, 1, false, 5,   morechem:compound:86, 10, alchemistry:compound:19, 20, alchemistry:element:19*5",
"minecraft:sponge:1,         false, 1, 1, false, 100, alchemistry:compound:8*8, alchemistry:compound:13*8, alchemistry:compound:7*16",
"minecraft:bowl, 			 false, 1, 1, false, 15,  alchemistry:compound",
"minecraft:cooked_fish,      false, 1, 1, false, 100, alchemistry:compound:9*4, alchemistry:element:34*2",
"minecraft:cooked_fish:1,    false, 1, 1, false, 100, alchemistry:compound:9*4, alchemistry:element:34*4",

"minecraft:magma_cream,      false, 1, 1, true,  10,  alchemistry:element:25,   1.25,alchemistry:element:13,5,alchemistry:compound:4,0.5,alchemistry:compound:5,2.5,alchemistry:compound:1,5,alchemistry:element:16,2.5,alchemistry:compound:10,2,alchemistry:element:82,1,alchemistry:element:9,1,alchemistry:element:35",
"minecraft:fermented_spider_eye,false,1,1,false, 100, alchemistry:compound:9*2, alchemistry:compound:52*2, 100, alchemistry:compound, alchemistry:compound:21, 100, alchemistry:compound:11",
"minecraft:rabbit_foot,      false, 1, 1, false, 100, alchemistry:compound:9*2",
"minecraft:rabbit_hide,      false, 1, 1, false, 75,  alchemistry:compound:9",
"minecraft:cookie,      	 false, 1, 1, false, 100, alchemistry:compound:59,  alchemistry:compound, 10, alchemistry:compound:19",
"minecraft:speckled_melon,	 false, 1, 1, false, 100, alchemistry:element:79*8, 10, alchemistry:compound:20, 2, alchemistry:compound:7, 1, alchemistry:compound:11",
"minecraft:melon,	 		 false, 1, 1, true,  416, minecraft:air,            50,  alchemistry:compound:20, 1,alchemistry:compound:7*4,1, alchemistry:compound:11*2",
"minecraft:pumpkin_pie,	     false, 1, 1, true,  50,  alchemistry:compound:20,  100, alchemistry:compound:11, alchemistry:compound:13*8, alchemistry:compound:9*2",
"minecraft:snowball,	     false, 1, 1, true,  100, alchemistry:compound:7*4",
"minecraft:melon_seeds,	     false, 1, 1, true,  10,  alchemistry:compound*1",
"minecraft:pumpkin_seeds,	 false, 1, 1, true,  10,  alchemistry:compound*1",
"minecraft:beetroot_seeds,	 false, 1, 1, true,  20,  alchemistry:compound*1,    10, alchemistry:compound:10",

"minecraft:chorus_fruit,     false, 1, 1, false, 15,  alchemistry:element:80, 	2, alchemistry:element:60, 100, alchemistry:element:14, 10,alchemistry:element:3, 1,alchemistry:element:90, 2, alchemistry:element:88, 5, alchemistry:element:71",
"minecraft:chorus_fruit_popped,false,1,1, false, 4,   alchemistry:element:60,   100, alchemistry:element:14*2, 2,alchemistry:element:3, 2,alchemistry:element:90, 4,alchemistry:element:88, 10, alchemistry:element:71",
///
"minecraft:stone_button,     false, 1, 1,  true, 20,  minecraft:air, 2,alchemistry:element:13,4,alchemistry:element:26,1.5,alchemistry:element:79,20,alchemistry:compound:1,0.5,alchemistry:element:66,1.25,alchemistry:element:40,1,alchemistry:element:74,1,alchemistry:element:28,1,alchemistry:element:31",
"minecraft:wooden_button,    false, 1, 1,  false,25 , alchemistry:compound",
"minecraft:stone_pressure_plate, false,1,2,true, 100, minecraft:air, 2,alchemistry:element:13,4,alchemistry:element:26,1.5,alchemistry:element:79,20,alchemistry:compound:1,0.5,alchemistry:element:66,1.25,alchemistry:element:40,1,alchemistry:element:74,1,alchemistry:element:28,1,alchemistry:element:31",
"minecraft:wooden_pressure_plate,false,1,1,false,50,  alchemistry:compound",
"minecraft:tripwire_hook,    false, 1, 8,  false,12.5, alchemistry:element:26, 1.875, alchemistry:compound",
"minecraft:light_weighted_pressure_plate,  false,1,1,false,100, alchemistry:element:79*32",
"minecraft:heavy_weighted_pressure_plate,  false,1,1,false,100, alchemistry:element:26*32",
"minecraft:redstone_torch,   false, 1, 1,  false,100, alchemistry:compound:10, alchemistry:compound:17, 10, alchemistry:compound",

"minecraft:sandstone,        false, 1, 1,  false,100, alchemistry:compound:1*8, 2, alchemistry:element:79",
"minecraft:sandstone:1,      false, 1, 1,  false,100, alchemistry:compound:1*8, 2, alchemistry:element:79",
"minecraft:sandstone:2,      false, 1, 1,  false,100, alchemistry:compound:1*8, 2, alchemistry:element:79",
"minecraft:stone_slab:1,     false, 1, 1,  false,50,  alchemistry:compound:1*8, 1, alchemistry:element:79",
"minecraft:sandstone_stairs, false, 1, 1,  false,75,  alchemistry:compound:1*8, 1.5, alchemistry:element:79",

"minecraft:red_sandstone,    false, 1, 1,  false,100, alchemistry:compound:1*8, 20, alchemistry:compound:10",
"minecraft:red_sandstone:1,  false, 1, 1,  false,100, alchemistry:compound:1*8, 20, alchemistry:compound:10",
"minecraft:red_sandstone:2,  false, 1, 1,  false,100, alchemistry:compound:1*8, 20, alchemistry:compound:10",
"minecraft:stone_slab2,      false, 1, 1,  false,50,  alchemistry:compound:1*8, 10, alchemistry:compound:10",
"minecraft:red_sandstone_stairs,false,1,1, false,75,  alchemistry:compound:1*8, 15, alchemistry:compound:10",

"minecraft:purpur_slab,      false, 1, 1,  false,50 , alchemistry:compound:1*4, 25,  alchemistry:element:71",
"minecraft:purpur_stairs,    false, 1, 1,  false,75,  alchemistry:compound:1*4,37.5, alchemistry:element:71",

"minecraft:brick,			 false,1,1, false,   100, alchemistry:compound:8",
"minecraft:brick_block,		 false,1,1, false,   100, alchemistry:compound:8*4",
"minecraft:stone_slab:4,	 false,1,1, false,   100, alchemistry:compound:8*2",
"minecraft:brick_stairs,	 false,1,1, false,   100, alchemistry:compound:8*3",

"minecraft:milk_bucket,		 false,1,1, false,   100, alchemistry:element:26*48,alchemistry:compound:9*2, alchemistry:compound:7*16, alchemistry:compound:11",
"minecraft:book,			 false,1,1, false,   100, alchemistry:compound:9*3, alchemistry:compound*9"


));

	// cache for parseItemStack: specification -> ItemStack (prototype). We will clone only when we need to change count.
	//private static final Map<String, ItemStack> ITEM_CACHE = new ConcurrentHashMap<>(512);
	// reflection cache: methods/fields (initialized once)
	private static final Constructor<?> PROBABILITY_GROUP_CONSTRUCTOR;
	private static final Constructor<?> PROBABILITY_SET_CONSTRUCTOR;
	private static final Constructor<?> DISSOLVER_CONSTRUCTOR;


	// ---------- add recipes ----------
	private interface RecipeAdder {
	    void addRecipe(Object recipe) throws Exception;
	}
	private static final RecipeAdder ADDER;


	static {
	    try {
	        // Determine the mod version and load the main classes
	        Class<?> dissolverClass;
	        Class<?> probSetClass;
	        Class<?> probGroupClass;
	        String basePkg;
	        try {
	            basePkg = "al132.alchemistry";
	            dissolverClass = Class.forName(basePkg + ".recipes.DissolverRecipe");
	            probSetClass = Class.forName(basePkg + ".recipes.ProbabilitySet");
	            probGroupClass = Class.forName(basePkg + ".recipes.ProbabilityGroup");
	            System.out.println("MCRX: Alchemistry original detected");
	        } catch (ClassNotFoundException e) {
	            basePkg = "io.enderdev.alchemistry";
	            dissolverClass = Class.forName(basePkg + ".recipes.DissolverRecipe");
	            probSetClass = Class.forName(basePkg + ".recipes.ProbabilitySet");
	            probGroupClass = Class.forName(basePkg + ".recipes.ProbabilityGroup");
	            System.out.println("MCRX: Alchemistry fork (EnderDev) detected");
	        }
	
	        PROBABILITY_GROUP_CONSTRUCTOR = probGroupClass.getConstructor(List.class, double.class);
	        PROBABILITY_GROUP_CONSTRUCTOR.setAccessible(true);
	
	        PROBABILITY_SET_CONSTRUCTOR = probSetClass.getConstructor(List.class, boolean.class, int.class);
	        PROBABILITY_SET_CONSTRUCTOR.setAccessible(true);
	
	        DISSOLVER_CONSTRUCTOR = dissolverClass.getConstructor(Ingredient.class, boolean.class, probSetClass);
	        DISSOLVER_CONSTRUCTOR.setAccessible(true);
	
	        // ---------- Get recipes list ----------
	        final List<Object> recipeList;
	        if (basePkg.startsWith("al132")) {
	            // Main: ModRecipes.INSTANCE.getDissolverRecipes()
	            Class<?> modRecipesClass = Class.forName(basePkg + ".recipes.ModRecipes");
	            Field instanceField = modRecipesClass.getField("INSTANCE");
	            Object modRecipes = instanceField.get(null);
	            Method getListMethod = modRecipesClass.getMethod("getDissolverRecipes");
	            recipeList = (List<Object>) getListMethod.invoke(modRecipes);
	        } else {
	            // Fork EnderDev: DissolverRegister.Companion.getINSTANCE().getRecipes()
	            Class<?> regClass = Class.forName(basePkg + ".recipes.register.DissolverRegister");
	            // get obj Companion FIXME:rom field "Companion", DissolverRegister
	            Field companionField = regClass.getField("Companion");
	            Object companionObj = companionField.get(null);
	            // From Companion call getINSTANCE()
	            Method getInstMethod = companionObj.getClass().getMethod("getINSTANCE");
	            Object dissolverReg = getInstMethod.invoke(companionObj);
	            // get recipes list
	            Method getRecipesMethod = regClass.getMethod("getRecipes");
	            recipeList = (List<Object>) getRecipesMethod.invoke(dissolverReg);
	        }
	
	        ADDER = (recipe) -> recipeList.add(recipe);
	
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to initialize DissolverRecipe integration", e);
	    }
	}

    // ---------- Public reg ----------
    public static void registerAll() {
		if (L == null) return;
        for (String line : L) {
            try {
                parseAndRegister(line);
            } catch (Exception e) {
                System.out.println("Ooooops! That's an error! [MCRX|RegError]");
                e.printStackTrace();
            }
        }

		L.clear();
		L = null;
    }

    // ---------- parse & register one recipe ----------
	private static void parseAndRegister(String line) throws Exception {
	    if (line == null || line.isEmpty()) return;
	    List<String> parts = RecipeUtils.splitByComma(line);
	    if (parts.size() < 7) return;
	
	    // 1. Input
	    ItemStack input = RecipeUtils.parseItemStack(parts.get(0));
	    if (input == null) return;
	    int quantity = RecipeUtils.parseInt(parts.get(2), 1);
	    if (quantity > 1) {
	        input = input.copy();
	        input.setCount(quantity);
	    }
	
	    boolean reversible = Boolean.parseBoolean(parts.get(1));
	    int rolls = RecipeUtils.parseInt(parts.get(3), 1);
	    boolean relative = Boolean.parseBoolean(parts.get(4));
	
	    // 2. Out groups
	    List<Object> groups = new ArrayList<>();
	    for (int i = 5; i < parts.size(); ) {
	        double prob = RecipeUtils.parseDouble(parts.get(i), 100.0);
	        i++;
	        List<ItemStack> outs = new ArrayList<>();
	        while (i < parts.size() && parts.get(i).indexOf(':') >= 0) {
	            ItemStack outProto = RecipeUtils.parseItemStack(parts.get(i));
	            if (outProto != null) outs.add(outProto.copy());
	            i++;
	        }
	        if (!outs.isEmpty()) {
	            Object group = PROBABILITY_GROUP_CONSTRUCTOR.newInstance(outs, prob);
	            groups.add(group);
	        }
	    }
	
	    Object outputSet = PROBABILITY_SET_CONSTRUCTOR.newInstance(groups, relative, rolls);
	    Object recipe = DISSOLVER_CONSTRUCTOR.newInstance(Ingredient.fromStacks(input), reversible, outputSet);
	
	    ADDER.addRecipe(recipe);
	}
}
