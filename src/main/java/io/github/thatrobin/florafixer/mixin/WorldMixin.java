package io.github.thatrobin.florafixer.mixin;

import io.github.thatrobin.florafixer.component.FlowerComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(World.class)
public class WorldMixin {

    @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("HEAD"))
    public void onBlockChanged(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        World world = (World) (Object) this;
        if(world.isClient) {
            FlowerComponent component = FlowerComponent.FLOWER_DATA.get(world);
            BlockState oldBlock = world.getBlockState(pos);
            if (!(oldBlock.getBlock() instanceof FlowerBlock)) {
                if (state.getBlock() instanceof FlowerBlock flowerBlock) {
                    Random rand = new Random();
                    float f = flowerBlock.getMaxHorizontalModelOffset();
                    double d = rand.nextDouble(-f, f);
                    double e = rand.nextDouble(-f, f);
                    component.addLand(pos, new Vec3d(d, 0.0, e));
                }
            }
            if (oldBlock.getBlock() instanceof FlowerBlock) {
                if (!(state.getBlock() instanceof FlowerBlock)) {
                    if (component.containsLand(pos)) {
                        component.removeLand(pos);
                    }
                }
            }
            component.syncWithAll();
        }
    }
}
