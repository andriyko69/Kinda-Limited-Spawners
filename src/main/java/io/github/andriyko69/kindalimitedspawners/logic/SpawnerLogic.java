package io.github.andriyko69.kindalimitedspawners.logic;

import io.github.andriyko69.kindalimitedspawners.Config;
import io.github.andriyko69.kindalimitedspawners.registry.ModAttachments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;

public final class SpawnerLogic {
    private SpawnerLogic() {
    }

    public static void setUnlimited(SpawnerBlockEntity spawner, boolean unlimited) {
        spawner.setData(ModAttachments.IS_UNLIMITED, unlimited);
        spawner.setChanged();
    }

    public static boolean canInsertNetherStar(SpawnerBlockEntity spawner) {
        return !isUnlimited(spawner);
    }

    public static boolean isNetherStar(ItemStack stack) {
        return stack.is(Items.NETHER_STAR);
    }

    public static boolean isUnlimited(SpawnerBlockEntity spawner) {
        return spawner.getData(ModAttachments.IS_UNLIMITED);
    }

    public static int getSpawnedCount(SpawnerBlockEntity spawner) {
        return spawner.getData(ModAttachments.SPAWNED_COUNT);
    }

    public static boolean isCapReached(SpawnerBlockEntity spawner) {
        return getSpawnedCount(spawner) >= Config.spawnerCapacity;
    }

    public static boolean canSpawn(SpawnerBlockEntity spawner) {
        return isUnlimited(spawner) || !isCapReached(spawner);
    }

    public static boolean canRemoveNetherStar(SpawnerBlockEntity spawner) {
        return Config.allowNetherStarRemoval
                && isUnlimited(spawner)
                && !isCapReached(spawner);
    }

    public static void incrementSpawnedCount(SpawnerBlockEntity spawner) {
        int current = getSpawnedCount(spawner);
        if (current < Config.spawnerCapacity) {
            spawner.setData(ModAttachments.SPAWNED_COUNT, current + 1);
            spawner.setChanged();
        }
    }
}