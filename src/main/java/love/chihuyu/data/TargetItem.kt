package love.chihuyu.data

import org.bukkit.Material

object TargetItem {

    var targetItem: Material? = null

    //TODO:setting score per material
    val targetData = hashMapOf(
        // NORMAL
        TargetCategory.NORMAL_BLOCK to mapOf(
            Material.BOOKSHELF to 0,
            Material.COBBLESTONE to 0,
            Material.GRANITE to 0,
            Material.POLISHED_GRANITE to 0,
            Material.ANDESITE to 0,
            Material.POLISHED_ANDESITE to 0,
            Material.DIORITE to 0,
            Material.POLISHED_DIORITE to 0,
            Material.DEEPSLATE to 0,
            Material.COBBLED_DEEPSLATE to 0,
            Material.POLISHED_DEEPSLATE to 0,
            Material.CALCITE to 0,
            Material.TUFF to 0,
            Material.DIRT to 0,
            Material.ROOTED_DIRT to 0,
            Material.GRAVEL to 0,
            Material.WHITE_WOOL to 0,
            Material.BRICKS to 0,
            Material.DRIPSTONE_BLOCK to 0,
            Material.OBSIDIAN to 0,
            Material.STONE_BRICKS to 0,
            Material.MOSSY_STONE_BRICKS to 0,
            Material.CRACKED_STONE_BRICKS to 0,
            Material.CHISELED_STONE_BRICKS to 0,
            Material.DRIED_KELP_BLOCK to 0,
            Material.HAY_BLOCK to 0,
            Material.IRON_BARS to 0,
            Material.CHAIN to 0,
            Material.SCAFFOLDING to 0,
            Material.LECTERN to 0,
            Material.IRON_TRAPDOOR to 0,
            Material.MOSS_BLOCK to 0,
            Material.TORCH to 0,
            Material.CHEST to 0,
            Material.FURNACE to 0,
            Material.LADDER to 0,
            Material.CAULDRON to 0,
            Material.IRON_DOOR to 0,
            Material.DEAD_BRAIN_CORAL_BLOCK to 0,
            Material.DEAD_FIRE_CORAL_BLOCK to 0,
            Material.DEAD_HORN_CORAL_BLOCK to 0,
            Material.DEAD_TUBE_CORAL_BLOCK to 0,
            Material.DEAD_BUBBLE_CORAL_BLOCK to 0,
            Material.PRISMARINE to 0,
            Material.PRISMARINE_BRICKS to 0,
            Material.DARK_PRISMARINE to 0,
            Material.WHITE_CARPET to 0,
            Material.CRAFTING_TABLE to 0,
            Material.ANVIL to 0,
            Material.TINTED_GLASS to 0,
            Material.MOSS_CARPET to 0,
            Material.ARMOR_STAND to 0,
            Material.ITEM_FRAME to 0,
            Material.GLOW_ITEM_FRAME to 0,
            Material.SMOKER to 0,
            Material.SMITHING_TABLE to 0,
            Material.GRINDSTONE to 0,
            Material.FLETCHING_TABLE to 0,
            Material.CARTOGRAPHY_TABLE to 0,
            Material.BLAST_FURNACE to 0,
            Material.BARREL to 0,
            Material.COMPOSTER to 0,
            Material.STONECUTTER to 0,
            Material.LOOM to 0,
            Material.BELL to 0,
            Material.POINTED_DRIPSTONE to 0,
            Material.FLOWER_POT to 0,
            Material.AZALEA_LEAVES to 0,
            Material.AZALEA_LEAVES to 0,
            Material.WHITE_BED to 0,
        ),
        TargetCategory.NORMAL_FOOD to mapOf(
            Material.SUGAR_CANE to 0,
            Material.KELP to 0,
            Material.POISONOUS_POTATO to 0,
            Material.SALMON to 0,
            Material.COD to 0,
            Material.DRIED_KELP to 0,
            Material.APPLE to 0,
            Material.GOLDEN_APPLE to 0,
            Material.BREAD to 0,
            Material.MELON_SLICE to 0,
            Material.WHEAT to 0,
            Material.CARROT to 0,
            Material.GLOW_BERRIES to 0,
            Material.SWEET_BERRIES to 0,
            Material.POTATO to 0,
            Material.GOLDEN_CARROT to 0,
            Material.GLISTERING_MELON_SLICE to 0,
            Material.MUTTON to 0,
            Material.COOKED_MUTTON to 0,
            Material.BEEF to 0,
            Material.COOKED_BEEF to 0,
            Material.CHICKEN to 0,
            Material.COOKED_CHICKEN to 0,
            Material.RABBIT to 0,
            Material.BAKED_POTATO to 0,
            Material.RABBIT_STEW to 0,
            Material.MUSHROOM_STEW to 0,
            Material.BEETROOT to 0,
            Material.BEETROOT_SOUP to 0,
            Material.PUMPKIN_PIE to 0,
            Material.HANGING_ROOTS to 0,
            Material.TARGET to 0,
            Material.REPEATER to 0,
            Material.COMPARATOR to 0,
            Material.PISTON to 0,
            Material.STICKY_PISTON to 0,
            Material.SLIME_BLOCK to 0,
            Material.SLIME_BALL to 0,
            Material.REDSTONE_TORCH to 0,
            Material.TRIPWIRE_HOOK to 0,
            Material.TRAPPED_CHEST to 0,
            Material.DAYLIGHT_DETECTOR to 0,
            Material.NOTE_BLOCK to 0,
            Material.TNT to 0,
            Material.REDSTONE_LAMP to 0,
            Material.DROPPER to 0,
            Material.DISPENSER to 0,
            Material.HOPPER to 0,
            Material.OBSERVER to 0,
            Material.LIGHTNING_ROD to 0,
            Material.LEVER to 0,
            Material.POWERED_RAIL to 0,
            Material.DETECTOR_RAIL to 0,
            Material.RAIL to 0,
            Material.ACTIVATOR_RAIL to 0,
        ),
        TargetCategory.NORMAL_MATERIAL to mapOf(
            Material.STICK to 0,
            Material.BOWL to 0,
            Material.DANDELION to 0,
            Material.CORNFLOWER to 0,
            Material.POPPY to 0,
            Material.OXEYE_DAISY to 0,
            Material.AZURE_BLUET to 0,
            Material.LEATHER to 0,
            Material.FLINT to 0,
            Material.BLUE_ORCHID to 0,
            Material.SEA_PICKLE to 0,
            Material.AZALEA to 0,
            Material.AZALEA_LEAVES to 0,
            Material.RABBIT_FOOT to 0,
            Material.PUMPKIN_SEEDS to 0,
            Material.BEETROOT_SEEDS to 0,
            Material.WHEAT_SEEDS to 0,
            Material.BAMBOO to 0,
            Material.SPORE_BLOSSOM to 0,
            Material.ALLIUM to 0,
            Material.BROWN_MUSHROOM to 0,
            Material.RED_MUSHROOM to 0,
            Material.SUNFLOWER to 0,
            Material.BRICK to 0,
            Material.CLAY_BALL to 0,
            Material.BOOK to 0,
            Material.PAPER to 0,
            Material.ENDER_PEARL to 0,
            Material.FEATHER to 0,
            Material.SPIDER_EYE to 0,
            Material.GUNPOWDER to 0,
            Material.STRING to 0,
            Material.BONE to 0,
            Material.ARROW to 0,
            Material.ROTTEN_FLESH to 0,
            Material.FERMENTED_SPIDER_EYE to 0,
            Material.PHANTOM_MEMBRANE to 0,
        ),
        TargetCategory.NORMAL_ORE to mapOf(
            Material.RAW_COPPER to 0,
            Material.RAW_COPPER_BLOCK to 0,
            Material.REDSTONE to 0,
            Material.REDSTONE_BLOCK to 0,
            Material.DIAMOND to 0,
            Material.DIAMOND_BLOCK to 0,
            Material.EMERALD to 0,
            Material.EMERALD_BLOCK to 0,
            Material.LAPIS_LAZULI to 0,
            Material.LAPIS_BLOCK to 0,
            Material.AMETHYST_SHARD to 0,
            Material.AMETHYST_BLOCK to 0,
            Material.RAW_IRON to 0,
            Material.RAW_IRON_BLOCK to 0,
            Material.IRON_NUGGET to 0,
            Material.COAL to 0,
            Material.CHARCOAL to 0,
            Material.COAL_BLOCK to 0,
            Material.GOLD_INGOT to 0,
            Material.GOLD_BLOCK to 0,
            Material.GOLD_NUGGET to 0,
            Material.COPPER_INGOT to 0,
            Material.COPPER_BLOCK to 0,
            Material.RAW_GOLD to 0,
            Material.RAW_GOLD_BLOCK to 0,
        ),
        TargetCategory.NORMAL_ARMOR to mapOf(
            Material.LEATHER_HELMET to 0,
            Material.LEATHER_CHESTPLATE to 0,
            Material.LEATHER_LEGGINGS to 0,
            Material.LEATHER_BOOTS to 0,
            Material.IRON_HELMET to 0,
            Material.IRON_CHESTPLATE to 0,
            Material.IRON_LEGGINGS to 0,
            Material.IRON_BOOTS to 0,
            Material.GOLDEN_HELMET to 0,
            Material.GOLDEN_CHESTPLATE to 0,
            Material.GOLDEN_LEGGINGS to 0,
            Material.GOLDEN_BOOTS to 0,
            Material.DIAMOND_HELMET to 0,
            Material.DIAMOND_CHESTPLATE to 0,
            Material.DIAMOND_LEGGINGS to 0,
            Material.DIAMOND_BOOTS to 0,
        ),
        TargetCategory.NORMAL_TOOL to mapOf(
            Material.FLINT_AND_STEEL to 0,
            Material.WOODEN_SHOVEL to 0,
            Material.WOODEN_AXE to 0,
            Material.WOODEN_HOE to 0,
            Material.WOODEN_PICKAXE to 0,
            Material.WOODEN_SWORD to 0,
            Material.IRON_SHOVEL to 0,
            Material.IRON_AXE to 0,
            Material.IRON_HOE to 0,
            Material.IRON_PICKAXE to 0,
            Material.IRON_SWORD to 0,
            Material.GOLDEN_SHOVEL to 0,
            Material.GOLDEN_AXE to 0,
            Material.GOLDEN_HOE to 0,
            Material.GOLDEN_PICKAXE to 0,
            Material.GOLDEN_SWORD to 0,
            Material.DIAMOND_SHOVEL to 0,
            Material.DIAMOND_AXE to 0,
            Material.DIAMOND_HOE to 0,
            Material.DIAMOND_PICKAXE to 0,
            Material.DIAMOND_SWORD to 0,
            Material.BOW to 0,
            Material.BUCKET to 0,
            Material.WATER_BUCKET to 0,
            Material.LAVA_BUCKET to 0,
            Material.POWDER_SNOW_BUCKET to 0,
            Material.AXOLOTL_BUCKET to 0,
            Material.PUFFERFISH_BUCKET to 0,
            Material.SALMON_BUCKET to 0,
            Material.COD_BUCKET to 0,
            Material.TROPICAL_FISH_BUCKET to 0,
            Material.SHIELD to 0,
            Material.CROSSBOW to 0,
            Material.ENCHANTED_BOOK to 0,
            Material.GOAT_HORN to 0,
            Material.NAME_TAG to 0,
        ),
        // NETHER
        TargetCategory.NETHER_BLOCK to mapOf(
            Material.SOUL_SOIL to 0,
            Material.GLOWSTONE to 0,
            Material.SMOOTH_BASALT to 0,
            Material.SOUL_SAND to 0,
            Material.NETHERRACK to 0,
            Material.QUARTZ_BLOCK to 0,
            Material.CRACKED_DEEPSLATE_TILES to 0,
            Material.RESPAWN_ANCHOR to 0,
            Material.CHISELED_QUARTZ_BLOCK to 0,
            Material.DEEPSLATE_TILES to 0,
            Material.CRACKED_DEEPSLATE_BRICKS to 0,
            Material.BASALT to 0,
            Material.QUARTZ_BRICKS to 0,
            Material.CHISELED_DEEPSLATE to 0,
            Material.BREWING_STAND to 0,
            Material.NETHER_BRICKS to 0,
            Material.CRACKED_NETHER_BRICKS to 0,
            Material.CHISELED_NETHER_BRICKS to 0,
            Material.NETHER_BRICK_STAIRS to 0,
            Material.MAGMA_BLOCK to 0,
            Material.CRYING_OBSIDIAN to 0,
            Material.NETHER_WART_BLOCK to 0,
            Material.SOUL_TORCH to 0,
        ),
        TargetCategory.NETHER_FOOD to mapOf(),
        TargetCategory.NETHER_ORE to mapOf(
            Material.QUARTZ to 0,
        ),
        TargetCategory.NETHER_MATERIAL to mapOf(
            Material.BLAZE_ROD to 0,
            Material.NETHER_WART to 0,
            Material.GHAST_TEAR to 0,
            Material.MAGMA_CREAM to 0,
            Material.SPECTRAL_ARROW to 0,
        ),
        TargetCategory.NETHER_ARMOR to mapOf(),
        TargetCategory.NETHER_TOOL to mapOf(
            Material.WARPED_FUNGUS_ON_A_STICK to 0,
        ),
        // HARD
        TargetCategory.HARD_BLOCK to mapOf(
            Material.COBWEB to 0,
            Material.LILY_PAD to 0,
            Material.YELLOW_CANDLE to 0,
            Material.GREEN_CANDLE to 0,
            Material.LIME_CANDLE to 0,
            Material.RED_CANDLE to 0,
            Material.BROWN_CANDLE to 0,
            Material.LIGHT_BLUE_CANDLE to 0,
            Material.MAGENTA_CANDLE to 0,
            Material.BLUE_CANDLE to 0,
            Material.PINK_CANDLE to 0,
            Material.PURPLE_CANDLE to 0,
            Material.ORANGE_CANDLE to 0,
            Material.WHITE_CANDLE to 0,
            Material.LIGHT_GRAY_CANDLE to 0,
            Material.BLACK_CANDLE to 0,
            Material.GRAY_CANDLE to 0,
            Material.CANDLE to 0,
            Material.CYAN_CANDLE to 0,
            Material.STONE to 0,
            Material.GRASS_BLOCK to 0,
            Material.INFESTED_COBBLESTONE to 0,
            Material.DIRT_PATH to 0,
            Material.SCULK_VEIN to 0,
            Material.SCULK_CATALYST to 0,
            Material.SCULK_SENSOR to 0,
            Material.SCULK_SHRIEKER to 0,
            Material.SCULK to 0,
            Material.PURPUR_BLOCK to 0,
            Material.CHORUS_PLANT to 0,
            Material.FIRE_CORAL_BLOCK to 0,
            Material.TUBE_CORAL_BLOCK to 0,
            Material.BONE_BLOCK to 0,
            Material.DEAD_BUBBLE_CORAL to 0,
            Material.BRAIN_CORAL_FAN to 0,
            Material.TURTLE_EGG to 0,
            Material.FIRE_CORAL_FAN to 0,
            Material.TUBE_CORAL_FAN to 0,
            Material.DEAD_BRAIN_CORAL_FAN to 0,
            Material.DRAGON_EGG to 0,
            Material.DEAD_BUBBLE_CORAL_FAN to 0,
            Material.DEAD_BUSH to 0,
            Material.END_ROD to 0,
            Material.OCHRE_FROGLIGHT to 0,
            Material.BUBBLE_CORAL_BLOCK to 0,
            Material.FROGSPAWN to 0,
            Material.BUBBLE_CORAL to 0,
            Material.TUBE_CORAL to 0,
            Material.DEAD_HORN_CORAL to 0,
            Material.DEAD_TUBE_CORAL to 0,
            Material.BRAIN_CORAL to 0,
            Material.HORN_CORAL_FAN to 0,
            Material.LODESTONE to 0,
            Material.SEAGRASS to 0,
            Material.SHULKER_BOX to 0,
            Material.SHULKER_SHELL to 0,
            Material.BEACON to 0,
            Material.CHORUS_FLOWER to 0,
            Material.PURPUR_PILLAR to 0,
            Material.ENDER_CHEST to 0,
            Material.NETHER_SPROUTS to 0,
            Material.MUSHROOM_STEM to 0,
            Material.BEE_NEST to 0,
            Material.BROWN_MUSHROOM_BLOCK to 0,
            Material.RED_MUSHROOM_BLOCK to 0,
        ),
        TargetCategory.HARD_FOOD to mapOf(
            Material.ENCHANTED_GOLDEN_APPLE to 0,
            Material.CHORUS_FRUIT to 0,
        ),
        TargetCategory.HARD_ORE to mapOf(
            Material.COAL_ORE to 0,
            Material.DEEPSLATE_COAL_ORE to 0,
            Material.IRON_ORE to 0,
            Material.DEEPSLATE_IRON_ORE to 0,
            Material.COPPER_ORE to 0,
            Material.DEEPSLATE_COPPER_ORE to 0,
            Material.GOLD_ORE to 0,
            Material.DEEPSLATE_GOLD_ORE to 0,
            Material.NETHER_GOLD_ORE to 0,
            Material.REDSTONE_ORE to 0,
            Material.DEEPSLATE_REDSTONE_ORE to 0,
            Material.EMERALD_ORE to 0,
            Material.DEEPSLATE_EMERALD_ORE to 0,
            Material.LAPIS_ORE to 0,
            Material.DEEPSLATE_LAPIS_ORE to 0,
            Material.DIAMOND_ORE to 0,
            Material.DEEPSLATE_DIAMOND_ORE to 0,
            Material.NETHER_QUARTZ_ORE to 0,
            Material.NETHERITE_SCRAP to 0,
            Material.NETHERITE_INGOT to 0,
            Material.ANCIENT_DEBRIS to 0,
            Material.AMETHYST_CLUSTER to 0,
        ),
        TargetCategory.HARD_MATERIAL to mapOf(
            Material.WITHER_SKELETON_SKULL to 0,
            Material.WITHER_ROSE to 0,
            Material.NETHER_STAR to 0,
            Material.END_CRYSTAL to 0,
            Material.SADDLE to 0,
            Material.LEATHER_HORSE_ARMOR to 0,
            Material.DIAMOND_HORSE_ARMOR to 0,
            Material.GOLDEN_HORSE_ARMOR to 0,
            Material.IRON_HORSE_ARMOR to 0,
            Material.ENDER_EYE to 0,
            Material.POPPED_CHORUS_FRUIT to 0,
            Material.SHULKER_SHELL to 0,
            Material.NAUTILUS_SHELL to 0,
            Material.HEART_OF_THE_SEA to 0,
            Material.DISC_FRAGMENT_5 to 0,
            Material.MUSIC_DISC_5 to 0,
            Material.ECHO_SHARD to 0,
            Material.MUSIC_DISC_PIGSTEP to 0,
            Material.MUSIC_DISC_OTHERSIDE to 0,
        ),
        TargetCategory.HARD_ARMOR to mapOf(
            Material.NETHERITE_HELMET to 0,
            Material.NETHERITE_CHESTPLATE to 0,
            Material.NETHERITE_LEGGINGS to 0,
            Material.NETHERITE_BOOTS to 0,
            Material.CREEPER_HEAD to 0,
            Material.SKELETON_SKULL to 0,
            Material.ZOMBIE_HEAD to 0,
            Material.DRAGON_HEAD to 0,
            Material.ELYTRA to 0,
        ),
        TargetCategory.HARD_TOOL to mapOf(
            Material.NETHERITE_SWORD to 0,
            Material.NETHERITE_PICKAXE to 0,
            Material.NETHERITE_AXE to 0,
            Material.NETHERITE_SHOVEL to 0,
            Material.NETHERITE_HOE to 0,
            Material.TOTEM_OF_UNDYING to 0,
            Material.RECOVERY_COMPASS to 0,
        ),
        // OTHER BLOCKS
        TargetCategory.DESERT_BLOCK to mapOf(
            Material.SAND to 0,
            Material.SANDSTONE to 0,
            Material.CHISELED_SANDSTONE to 0,
            Material.CUT_SANDSTONE to 0,
            Material.SMOOTH_RED_SANDSTONE to 0,
            Material.SMOOTH_SANDSTONE to 0,
            Material.CACTUS to 0,
        ),
        TargetCategory.MESA_BLOCK to mapOf(
            Material.WHITE_TERRACOTTA to 0,
            Material.ORANGE_TERRACOTTA to 0,
            Material.MAGENTA_TERRACOTTA to 0,
            Material.LIGHT_BLUE_TERRACOTTA to 0,
            Material.YELLOW_TERRACOTTA to 0,
            Material.LIME_TERRACOTTA to 0,
            Material.PINK_TERRACOTTA to 0,
            Material.GRAY_TERRACOTTA to 0,
            Material.LIGHT_GRAY_TERRACOTTA to 0,
            Material.CYAN_TERRACOTTA to 0,
            Material.PURPLE_TERRACOTTA to 0,
            Material.BLUE_TERRACOTTA to 0,
            Material.BROWN_TERRACOTTA to 0,
            Material.GREEN_TERRACOTTA to 0,
            Material.RED_TERRACOTTA to 0,
            Material.BLACK_TERRACOTTA to 0,
            Material.TERRACOTTA to 0,
            Material.RED_SAND to 0,
            Material.CUT_RED_SANDSTONE to 0,
            Material.CHISELED_RED_SANDSTONE to 0,
            Material.RED_SANDSTONE to 0,
        ),
        TargetCategory.COLORED to mapOf(
            Material.ORANGE_WOOL to 0,
            Material.MAGENTA_WOOL to 0,
            Material.LIGHT_BLUE_WOOL to 0,
            Material.YELLOW_WOOL to 0,
            Material.LIME_WOOL to 0,
            Material.PINK_WOOL to 0,
            Material.GRAY_WOOL to 0,
            Material.LIGHT_GRAY_WOOL to 0,
            Material.CYAN_WOOL to 0,
            Material.PURPLE_WOOL to 0,
            Material.BLUE_WOOL to 0,
            Material.BROWN_WOOL to 0,
            Material.GREEN_WOOL to 0,
            Material.RED_WOOL to 0,
            Material.BLACK_WOOL to 0,
            Material.RED_STAINED_GLASS to 0,
            Material.GREEN_STAINED_GLASS to 0,
            Material.LIGHT_BLUE_STAINED_GLASS to 0,
            Material.PINK_STAINED_GLASS to 0,
            Material.LIGHT_GRAY_STAINED_GLASS to 0,
            Material.CYAN_STAINED_GLASS to 0,
            Material.WHITE_STAINED_GLASS to 0,
            Material.BLUE_STAINED_GLASS to 0,
            Material.ORANGE_STAINED_GLASS to 0,
            Material.MAGENTA_STAINED_GLASS to 0,
            Material.GRAY_STAINED_GLASS to 0,
            Material.PURPLE_STAINED_GLASS to 0,
            Material.YELLOW_STAINED_GLASS to 0,
            Material.BROWN_STAINED_GLASS to 0,
            Material.LIME_STAINED_GLASS to 0,
            Material.BLACK_STAINED_GLASS to 0,
            Material.BLACK_CARPET to 0,
            Material.RED_CARPET to 0,
            Material.GREEN_CARPET to 0,
            Material.YELLOW_CARPET to 0,
            Material.ORANGE_CARPET to 0,
            Material.MAGENTA_CARPET to 0,
            Material.PINK_CARPET to 0,
            Material.GRAY_CARPET to 0,
            Material.LIGHT_GRAY_CARPET to 0,
            Material.CYAN_CARPET to 0,
            Material.LIGHT_BLUE_CARPET to 0,
            Material.LIME_CARPET to 0,
            Material.BROWN_CARPET to 0,
            Material.BLUE_CARPET to 0,
            Material.PURPLE_CARPET to 0,
            Material.RED_BANNER to 0,
            Material.GREEN_BANNER to 0,
            Material.BROWN_BANNER to 0,
            Material.BLUE_BANNER to 0,
            Material.PURPLE_BANNER to 0,
            Material.CYAN_BANNER to 0,
            Material.LIGHT_GRAY_BANNER to 0,
            Material.GRAY_BANNER to 0,
            Material.PINK_BANNER to 0,
            Material.YELLOW_BANNER to 0,
            Material.LIGHT_BLUE_BANNER to 0,
            Material.LIME_BANNER to 0,
            Material.MAGENTA_BANNER to 0,
            Material.ORANGE_BANNER to 0,
            Material.WHITE_BANNER to 0,
            Material.BLACK_BANNER to 0,
            Material.MAGENTA_BED to 0,
            Material.ORANGE_BED to 0,
            Material.LIGHT_BLUE_BED to 0,
            Material.YELLOW_BED to 0,
            Material.LIME_BED to 0,
            Material.PINK_BED to 0,
            Material.GRAY_BED to 0,
            Material.LIGHT_GRAY_BED to 0,
            Material.CYAN_BED to 0,
            Material.PURPLE_BED to 0,
            Material.BLUE_BED to 0,
            Material.BROWN_BED to 0,
            Material.GREEN_BED to 0,
            Material.RED_BED to 0,
            Material.BLACK_BED to 0,
            Material.BLACK_DYE to 0,
            Material.WHITE_DYE to 0,
            Material.ORANGE_DYE to 0,
            Material.MAGENTA_DYE to 0,
            Material.LIGHT_BLUE_DYE to 0,
            Material.YELLOW_DYE to 0,
            Material.LIME_DYE to 0,
            Material.PINK_DYE to 0,
            Material.GRAY_DYE to 0,
            Material.LIGHT_GRAY_DYE to 0,
            Material.CYAN_DYE to 0,
            Material.PURPLE_DYE to 0,
            Material.BLUE_DYE to 0,
            Material.BROWN_DYE to 0,
            Material.GREEN_DYE to 0,
            Material.RED_DYE to 0,
        ),
        TargetCategory.ICE_SPIKES to mapOf(
            Material.PACKED_ICE to 0,
            Material.ICE to 0,
            Material.BLUE_ICE to 0,
            Material.POWDER_SNOW_BUCKET to 0,
            Material.SNOWBALL to 0,
            Material.SNOW_BLOCK to 0,
            Material.FROSTED_ICE to 0,
        ),
        TargetCategory.BIOME_FLOWER to mapOf(
            Material.ALLIUM to 0,
            Material.RED_TULIP to 0,
            Material.ORANGE_TULIP to 0,
            Material.WHITE_TULIP to 0,
            Material.PINK_TULIP to 0,
            Material.LILY_OF_THE_VALLEY to 0,
            Material.ROSE_BUSH to 0,
            Material.PEONY to 0,
            Material.LILAC to 0,
        ),
        // WOOD
        TargetCategory.BIOME_OAK to mapOf(
            Material.OAK_PLANKS to 0,
            Material.OAK_WOOD to 0,
            Material.OAK_LEAVES to 0,
            Material.OAK_FENCE to 0,
            Material.OAK_SIGN to 0,
            Material.OAK_DOOR to 0,
            Material.OAK_TRAPDOOR to 0,
            Material.OAK_BOAT to 0,
            Material.OAK_CHEST_BOAT to 0,
            Material.STRIPPED_OAK_WOOD to 0,
            Material.OAK_SAPLING to 0,
        ),
        TargetCategory.BIOME_SPRUCE to mapOf(
            Material.COARSE_DIRT to 0,
            Material.MOSSY_COBBLESTONE to 0,
            Material.SPRUCE_PLANKS to 0,
            Material.SPRUCE_WOOD to 0,
            Material.SPRUCE_LEAVES to 0,
            Material.SPRUCE_FENCE to 0,
            Material.SPRUCE_SIGN to 0,
            Material.SPRUCE_DOOR to 0,
            Material.SPRUCE_TRAPDOOR to 0,
            Material.SPRUCE_BOAT to 0,
            Material.SPRUCE_CHEST_BOAT to 0,
            Material.STRIPPED_SPRUCE_WOOD to 0,
            Material.SPRUCE_SAPLING to 0,
        ),
        TargetCategory.BIOME_ACACIA to mapOf(
            Material.ACACIA_PLANKS to 0,
            Material.ACACIA_WOOD to 0,
            Material.ACACIA_LEAVES to 0,
            Material.ACACIA_FENCE to 0,
            Material.ACACIA_SIGN to 0,
            Material.ACACIA_DOOR to 0,
            Material.ACACIA_TRAPDOOR to 0,
            Material.ACACIA_BOAT to 0,
            Material.ACACIA_CHEST_BOAT to 0,
            Material.STRIPPED_ACACIA_WOOD to 0,
            Material.ACACIA_SAPLING to 0,
        ),
        TargetCategory.BIOME_BIRCH to mapOf(
            Material.BIRCH_PLANKS to 0,
            Material.BIRCH_WOOD to 0,
            Material.BIRCH_LEAVES to 0,
            Material.BIRCH_FENCE to 0,
            Material.BIRCH_SIGN to 0,
            Material.BIRCH_DOOR to 0,
            Material.BIRCH_TRAPDOOR to 0,
            Material.BIRCH_BOAT to 0,
            Material.BIRCH_CHEST_BOAT to 0,
            Material.STRIPPED_BIRCH_WOOD to 0,
            Material.BIRCH_SAPLING to 0,
        ),
        TargetCategory.BIOME_DARK_OAK to mapOf(
            Material.DARK_OAK_PLANKS to 0,
            Material.DARK_OAK_WOOD to 0,
            Material.DARK_OAK_LEAVES to 0,
            Material.DARK_OAK_FENCE to 0,
            Material.DARK_OAK_SIGN to 0,
            Material.DARK_OAK_DOOR to 0,
            Material.DARK_OAK_TRAPDOOR to 0,
            Material.DARK_OAK_BOAT to 0,
            Material.DARK_OAK_CHEST_BOAT to 0,
            Material.STRIPPED_ACACIA_WOOD to 0,
            Material.DARK_OAK_SAPLING to 0,
        ),
        TargetCategory.BIOME_JUNGLE to mapOf(
            Material.JUNGLE_PLANKS to 0,
            Material.JUNGLE_WOOD to 0,
            Material.JUNGLE_LEAVES to 0,
            Material.JUNGLE_FENCE to 0,
            Material.JUNGLE_SIGN to 0,
            Material.JUNGLE_DOOR to 0,
            Material.JUNGLE_TRAPDOOR to 0,
            Material.JUNGLE_BOAT to 0,
            Material.JUNGLE_CHEST_BOAT to 0,
            Material.STRIPPED_JUNGLE_WOOD to 0,
            Material.JUNGLE_SAPLING to 0,
        ),
        TargetCategory.BIOME_MANGROVE to mapOf(
            Material.MUD to 0,
            Material.MANGROVE_PLANKS to 0,
            Material.MANGROVE_WOOD to 0,
            Material.MANGROVE_LEAVES to 0,
            Material.MANGROVE_FENCE to 0,
            Material.MANGROVE_SIGN to 0,
            Material.MANGROVE_DOOR to 0,
            Material.MANGROVE_TRAPDOOR to 0,
            Material.MANGROVE_BOAT to 0,
            Material.MANGROVE_CHEST_BOAT to 0,
            Material.STRIPPED_MANGROVE_WOOD to 0,
            Material.MANGROVE_PROPAGULE to 0,
        ),
        TargetCategory.BIOME_CRIMSON to mapOf(
            Material.CRIMSON_STEM to 0,
            Material.CRIMSON_HYPHAE to 0,
            Material.CRIMSON_PLANKS to 0,
            Material.CRIMSON_TRAPDOOR to 0,
            Material.CRIMSON_DOOR to 0,
            Material.CRIMSON_FUNGUS to 0,
            Material.CRIMSON_ROOTS to 0,
            Material.STRIPPED_CRIMSON_STEM to 0,
            Material.WEEPING_VINES to 0,
        ),
        TargetCategory.BIOME_WARPED to mapOf(
            Material.WARPED_STEM to 0,
            Material.WARPED_HYPHAE to 0,
            Material.STRIPPED_WARPED_STEM to 0,
            Material.WARPED_PLANKS to 0,
            Material.WARPED_TRAPDOOR to 0,
            Material.WARPED_DOOR to 0,
            Material.WARPED_FUNGUS to 0,
            Material.WARPED_ROOTS to 0,
            Material.TWISTING_VINES to 0,
            Material.WARPED_WART_BLOCK to 0,
        ),
    )
}