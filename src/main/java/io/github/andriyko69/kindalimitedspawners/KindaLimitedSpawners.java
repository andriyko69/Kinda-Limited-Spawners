package io.github.andriyko69.kindalimitedspawners;

import io.github.andriyko69.kindalimitedspawners.registry.ModAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(KindaLimitedSpawners.MOD_ID)
public class KindaLimitedSpawners {
    public static final String MOD_ID = "kindalimitedspawners";

    public KindaLimitedSpawners(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModAttachments.register(modEventBus);
    }
}
