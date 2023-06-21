package io.github.thatrobin.florafixer.mixin;

import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import io.github.thatrobin.florafixer.component.FlowerComponent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.EmptyBlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockAbstractBlockStateMixin {

    @Inject(method = "getModelOffset", at = @At("RETURN"), cancellable = true)
    public void getModelOffset(BlockView world, BlockPos pos, CallbackInfoReturnable<Vec3d> cir) {
        if(world instanceof ChunkRendererRegion chunkRendererRegion) {
            world = ((ChunkRendererRegionAccessor)chunkRendererRegion).getWorld();
        }
        if (world instanceof World) {
            FlowerComponent component = FlowerComponent.FLOWER_DATA.get(world);
            if (component.containsLand(pos)) {
                cir.setReturnValue(component.getLand(pos));
            }
        }
    }
}
