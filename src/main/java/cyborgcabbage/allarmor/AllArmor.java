package cyborgcabbage.allarmor;

import cyborgcabbage.allarmor.item.ArmorSet;
import net.fabricmc.api.ModInitializer;
import cyborgcabbage.allarmor.item.MyArmorMaterial;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class AllArmor implements ModInitializer {

    public static final ArmorSet PAPER = new ArmorSet(new MyArmorMaterial(
            "paper",
            3,
            new int[]{1, 1, 1, 1},
            15,
            SoundEvents.ITEM_BOOK_PAGE_TURN,
            0.0F,
            0.0F,
            Items.PAPER
    ));
    public static final ArmorSet ENDER_EYE = new ArmorSet(new MyArmorMaterial(
            "ender_eye",
            3,
            new int[]{1, 1, 1, 1},
            15,
            SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            0.0F,
            0.0F,
            Items.ENDER_EYE
    ));
    public static final ArmorSet BOW = new ArmorSet(new MyArmorMaterial(
            "bow",
            3,
            new int[]{1, 1, 1, 1},
            15,
            SoundEvents.ITEM_CROSSBOW_LOADING_START,
            0.0F,
            0.0F,
            Items.BOW
    ));
    public static final ArmorSet TNT = new ArmorSet(new MyArmorMaterial(
            "tnt",
            3,
            new int[]{1, 1, 1, 1},
            15,
            SoundEvents.BLOCK_WOOL_PLACE,
            0.0F,
            0.0F,
            Items.TNT
    ));
    public static final ArmorSet GLASS = new ArmorSet(new MyArmorMaterial(
            "glass",
            3,
            new int[]{3, 6, 8, 3},
            10,
            SoundEvents.BLOCK_GLASS_PLACE,
            0.0F,
            0.0F,
            Items.GLASS
    ));
    public static final ArmorSet LADDER = new ArmorSet(new MyArmorMaterial(
            "ladder",
            5,
            new int[]{1, 2, 3, 1},
            15,
            SoundEvents.BLOCK_LADDER_PLACE,
            0.0F,
            0.0F,
            Items.LADDER
    ));
    public static final ArmorSet WHEAT = new ArmorSet(new MyArmorMaterial(
            "wheat",
            4,
            new int[]{1, 2, 2, 1},
            10,
            SoundEvents.BLOCK_WOOL_PLACE,
            0.0F,
            0.0F,
            Items.WHEAT
    ));
    public static final ArmorSet EMERALD = new ArmorSet(new MyArmorMaterial(
            "emerald",
            20,
            new int[]{2, 5, 6, 2},
            9,
            SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            0.0F,
            0.0F,
            Items.EMERALD
    ));
    public static final ArmorSet ENCHANTING_TABLE = new ArmorSet(new MyArmorMaterial(
            "enchanting_table",
            33,
            new int[]{3, 6, 8, 3},
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            2.0F,
            0.0F,
            Items.ENCHANTING_TABLE
    ));
    public static final ArmorSet LIGHTNING_ROD = new ArmorSet(new MyArmorMaterial(
            "lightning_rod",
            15,
            new int[]{2, 4, 5, 2},
            10,
            SoundEvents.BLOCK_COPPER_PLACE,
            0.0F,
            0.0F,
            Items.LIGHTNING_ROD
    ));
    public static final ArmorSet BOAT = new ArmorSet(new MyArmorMaterial(
            "boat",
            7,
            new int[]{1, 3, 5, 2},
            10,
            SoundEvents.BLOCK_WOOD_PLACE,
            0.0F,
            0.0F,
            Items.OAK_BOAT,
            Items.BIRCH_BOAT,
            Items.SPRUCE_BOAT,
            Items.JUNGLE_BOAT,
            Items.ACACIA_BOAT,
            Items.DARK_OAK_BOAT
    ));
    public static final ArmorSet BOOK = new ArmorSet(new MyArmorMaterial(
            "book",
            5,
            new int[]{1, 2, 3, 1},
            15,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            0.0F,
            0.0F,
            Items.BOOK
    ));
    public static final ArmorSet GLOWSTONE = new ArmorSet(new MyArmorMaterial(
            "glowstone",
            10,
            new int[]{1, 2, 3, 1},
            15,
            SoundEvents.BLOCK_GLASS_PLACE,
            0.0F,
            0.0F,
            Items.GLOWSTONE
    ));
    public static final ArmorSet NOTE_BLOCK = new ArmorSet(new MyArmorMaterial(
            "note_block",
            7,
            new int[]{1, 3, 5, 2},
            10,
            SoundEvents.BLOCK_WOOD_PLACE,
            0.0F,
            0.0F,
            Items.NOTE_BLOCK
    ));
    public static final ArmorSet JUKEBOX = new ArmorSet(new MyArmorMaterial(
            "jukebox",
            33,
            new int[]{3, 6, 8, 3},
            10,
            SoundEvents.BLOCK_WOOD_PLACE,
            2.0F,
            0.0F,
            Items.JUKEBOX
    ));

    public static final ArmorSet COPPER_INGOT = new ArmorSet(new MyArmorMaterial(
            "copper_ingot",
            15,
            new int[]{2, 4, 5, 2},
            10,
            SoundEvents.BLOCK_COPPER_PLACE,
            0.0F,
            0.0F,
            Items.COPPER_INGOT
    ));
    public static final ArmorSet CACTUS = new ArmorSet(new MyArmorMaterial(
            "cactus",
            10,
            new int[]{1, 2, 3, 1},
            15,
            SoundEvents.BLOCK_GLASS_PLACE,
            0.0F,
            0.0F,
            Items.CACTUS
    ));
    public static final ArmorSet SLIME_BALL = new ArmorSet(new MyArmorMaterial(
            "slime_ball",
            10,
            new int[]{1, 2, 3, 1},
            15,
            SoundEvents.BLOCK_SLIME_BLOCK_PLACE,
            0.0F,
            0.0F,
            Items.SLIME_BALL
    ));
    public static final ArmorSet DRAGON_BREATH = new ArmorSet(new MyArmorMaterial(
            "dragon_breath",
            20,
            new int[]{2, 5, 6, 2},
            9,
            SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            0.0F,
            0.0F,
            Items.DRAGON_BREATH
    ));
    public static final ArmorSet BONE_MEAL = new ArmorSet(new MyArmorMaterial(
            "bone_meal",
            3,
            new int[]{1, 1, 1, 1},
            15,
            SoundEvents.ITEM_BOOK_PAGE_TURN,
            0.0F,
            0.0F,
            Items.BONE_MEAL
    ));
    public static final ArmorSet BEDROCK = new ArmorSet(new MyArmorMaterial(
            "bedrock",
            1000,
            new int[]{100, 100, 100, 100},
            15,
            SoundEvents.BLOCK_STONE_PLACE,
            0.0F,
            0.0F,
            Items.BEDROCK
    ));
    public static final ArmorSet LAPIS_LAZULI = new ArmorSet(new MyArmorMaterial(
            "lapis_lazuli",
            5,
            new int[]{1, 2, 3, 1},
            20,
            SoundEvents.ITEM_ARMOR_EQUIP_TURTLE,
            0.0F,
            0.0F,
            Items.LAPIS_LAZULI
    ));

    public static final ArmorSet ENDER_PEARL = new ArmorSet(new MyArmorMaterial(
            "ender_pearl",
            13,
            new int[]{2, 4, 5, 2},
            12,
            SoundEvents.ITEM_ARMOR_EQUIP_TURTLE,
            0.0F,
            0.0F,
            Items.ENDER_PEARL
    ));
    @Override
    public void onInitialize() {
        PAPER.register();//Finished
        ENDER_EYE.register();//Texture
        BOW.register();//Should work in creative mode
        TNT.register();//Finished
        GLASS.register();//Finished

        LADDER.register();//Finished (maybe allow players to climb the person wearing the armor)
        WHEAT.register();//Finished
        EMERALD.register();//Texture
        ENCHANTING_TABLE.register();//Finished
        LIGHTNING_ROD.register();//Finished

        CACTUS.register();//Finished
        BOAT.register();//Finished
        GLOWSTONE.register();//Finished
        COPPER_INGOT.register();//Finished
        SLIME_BALL.register();//Finished

        DRAGON_BREATH.register();//Texture Later
        BEDROCK.register();//Finished
        NOTE_BLOCK.register();//Finished
        LAPIS_LAZULI.register();//Finished
        ENDER_PEARL.register();//Texture

        //BONE_MEAL.register();//Grows surrounding crops? (try and think of something better)
        //BOOK.register();//Should trigger all recipes when worn
        //JUKEBOX.register();//Should cycle through music discs in inventory

        ServerPlayNetworking.registerGlobalReceiver(new Identifier("allarmor","note_block_armor"), (server, player, handler, buf, responseSender) -> {
            int key = buf.readByte();

            // All operations on the server or world must be executed on the server thread
            server.execute(() ->{
                if(!player.world.isClient()){
                    int c_maj_cd = switch (key) {
                        case 0 -> 0;//C
                        case 1 -> 2;//D
                        case 2 -> 4;//E
                        case 3 -> 5;//F
                        case 4 -> 7;//G
                        case 5 -> 9;//A
                        case 6 -> 11;//B
                        case 7 -> 12;//C
                        case 8 -> 14;
                        default -> 0;//D
                    };
                    player.playSound(
                            Instrument.fromBlockState(player.world.getBlockState(player.getLandingPos())).getSound(),
                            SoundCategory.RECORDS,
                            1f,
                            (float)Math.pow(2.0D, (double)(c_maj_cd - 12) / 12.0D)
                    );
                }

            });
        });
    }
}