package io.github.andriyko69.kindalimitedspawners.mixin;

import io.github.andriyko69.kindalimitedspawners.logic.SpawnerLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseSpawner.class)
public abstract class BaseSpawnerMixin {
    @Inject(
            method = "serverTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/event/EventHooks;finalizeMobSpawnSpawner(Lnet/minecraft/world/entity/Mob;Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;Lnet/neoforged/neoforge/common/extensions/IOwnedSpawner;Z)Lnet/neoforged/neoforge/event/entity/living/FinalizeSpawnEvent;"
            ),
            cancellable = true
    )
    private void kls$preventSpawnWhenCapReached(ServerLevel level, BlockPos pos, CallbackInfo ci) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof SpawnerBlockEntity spawner)) {
            return;
        }

        if (!SpawnerLogic.canSpawn(spawner)) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "serverTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;tryAddFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    private boolean kls$countOnlySuccessfulSpawnerSpawns(ServerLevel level, Entity entity, ServerLevel originalLevel, BlockPos pos) {
        boolean added = level.tryAddFreshEntityWithPassengers(entity);

        if (!added) {
            return false;
        }

        BlockEntity blockEntity = originalLevel.getBlockEntity(pos);
        if (blockEntity instanceof SpawnerBlockEntity spawner) {
            SpawnerLogic.incrementSpawnedCount(spawner);
        }

        return true;
    }
}