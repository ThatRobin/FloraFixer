package io.github.thatrobin.florafixer.component;

import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

public class FlowerComponentImpl implements FlowerComponent {

    @SuppressWarnings("all")
    private World world;
    @SuppressWarnings("all")
    private HashMap<BlockPos, Vec3d> claimedLand = new HashMap<>();

    public FlowerComponentImpl(World world) {
        this.world = world;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        claimedLand.clear();
        NbtList claimData = tag.getList("flower_data", NbtElement.COMPOUND_TYPE);
        for(int i = 0; i < claimData.size(); i++) {
            NbtCompound curTag = claimData.getCompound(i);
            BlockPos pos = NbtHelper.toBlockPos(curTag.getCompound("pos"));
            NbtCompound off = curTag.getCompound("offset");
            Vec3d offset = new Vec3d(off.getDouble("X"), off.getDouble("Y"), off.getDouble("Z"));
            addLand(pos, offset);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtList claimData = new NbtList();
        claimedLand.forEach(((blockPos, offset) -> {
            NbtCompound compound = new NbtCompound();
            NbtCompound nbtCompound = new NbtCompound();

            NbtCompound pos = NbtHelper.fromBlockPos(blockPos);
            compound.put("pos", pos);

            nbtCompound.putDouble("X", offset.x);
            nbtCompound.putDouble("Y", offset.y);
            nbtCompound.putDouble("Z", offset.z);

            compound.put("offset", nbtCompound);
            claimData.add(compound);
        }));
        tag.put("flower_data", claimData);
    }

    @Override
    public void syncWithAll() {
        this.world.syncComponent(FlowerComponent.FLOWER_DATA);
    }

    @Override
    public void addLand(BlockPos pos, Vec3d land) {
        claimedLand.put(pos, land);
    }

    @Override
    public boolean containsLand(BlockPos pos) {
        return claimedLand.containsKey(pos);
    }

    @Override
    public void removeLand(BlockPos pos) {
        claimedLand.remove(pos);
    }

    @Override
    public Vec3d getLand(BlockPos pos) {
        return claimedLand.get(pos);
    }

    @Override
    public List<BlockPos> getAllPos() {
        return claimedLand.keySet().stream().toList();
    }
}
