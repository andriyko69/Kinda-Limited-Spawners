package io.github.andriyko69.kindalimitedspawners.registry;

import com.mojang.serialization.Codec;
import io.github.andriyko69.kindalimitedspawners.KindaLimitedSpawners;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, KindaLimitedSpawners.MOD_ID);

    public static final Supplier<AttachmentType<Boolean>> IS_UNLIMITED =
            ATTACHMENTS.register("is_unlimited", () ->
                    AttachmentType.builder(() -> false)
                            .serialize(Codec.BOOL)
                            .build()
            );

    public static final Supplier<AttachmentType<Integer>> SPAWNED_COUNT =
            ATTACHMENTS.register("spawned_count", () ->
                    AttachmentType.builder(() -> 0)
                            .serialize(Codec.INT)
                            .build()
            );

    public static void register(IEventBus modEventBus) {
        ATTACHMENTS.register(modEventBus);
    }
}
