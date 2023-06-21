package io.github.thatrobin.florafixer.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import io.github.thatrobin.florafixer.FloraFixer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public interface FlowerComponent extends AutoSyncedComponent {

    ComponentKey<FlowerComponent> FLOWER_DATA = ComponentRegistry.getOrCreate(FloraFixer.identifier("flower_data"), FlowerComponent.class);

    void syncWithAll();

    void addLand(BlockPos pos, Vec3d land);

    boolean containsLand(BlockPos pos);

    void removeLand(BlockPos pos);

    Vec3d getLand(BlockPos pos);

    List<BlockPos> getAllPos();

}
