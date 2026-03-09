package io.github.andriyko69.kindalimitedspawners.client;

import io.github.andriyko69.kindalimitedspawners.block.ModBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class SpawnerVisuals {
    private SpawnerVisuals() {
    }

    public static void syncVisualState(Level level, BlockPos pos, boolean unlimited) {
        BlockState state = level.getBlockState(pos);

        if (!state.hasProperty(ModBlockStateProperties.UNLIMITED)) {
            return;
        }

        if (state.getValue(ModBlockStateProperties.UNLIMITED) == unlimited) {
            return;
        }

        BlockState newState = state.setValue(ModBlockStateProperties.UNLIMITED, unlimited);
        level.setBlock(pos, newState, 2);
    }
}