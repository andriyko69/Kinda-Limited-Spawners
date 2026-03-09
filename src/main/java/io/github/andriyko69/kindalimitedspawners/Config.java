package io.github.andriyko69.kindalimitedspawners;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = KindaLimitedSpawners.MOD_ID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue SPAWNER_CAPACITY = BUILDER
            .comment("A maximum number of entities that can be spawned by a single spawner. Default is 50.")
            .translation("config.kindalimitedspawners.spawner_capacity")
            .defineInRange("spawnerCapacity", 50, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.BooleanValue ALLOW_NETHER_STAR_REMOVAL = BUILDER
            .comment("Whether the Nether Star can be removed from the spawner to make it limited again. Default is false.")
            .translation("config.kindalimitedspawners.allow_nether_star_removal")
            .define("allowNetherStarRemoval", false);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int spawnerCapacity = 50;
    public static boolean allowNetherStarRemoval = false;

    private static void bake() {
        spawnerCapacity = SPAWNER_CAPACITY.get();
        allowNetherStarRemoval = ALLOW_NETHER_STAR_REMOVAL.get();
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading configEvent) {
        if (configEvent.getConfig().getSpec() == SPEC) {
            bake();
        }
    }

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading configEvent) {
        if (configEvent.getConfig().getSpec() == SPEC) {
            bake();
        }
    }
}
