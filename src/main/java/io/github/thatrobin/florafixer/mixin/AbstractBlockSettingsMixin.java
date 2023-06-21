package io.github.thatrobin.florafixer.mixin;

import io.github.thatrobin.florafixer.component.FlowerComponent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.enums.Instrument;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = AbstractBlock.Settings.class)
public abstract class AbstractBlockSettingsMixin {

    @Shadow private Optional<AbstractBlock.Offsetter> offsetter;

    @Shadow private Instrument instrument;

    @Inject(method = "offset", at = @At("RETURN"), cancellable = true)
    public void offset(AbstractBlock.OffsetType offsetType, CallbackInfoReturnable<AbstractBlock.Settings> cir) {
        switch (offsetType) {
            default -> this.offsetter = Optional.empty();
            case XYZ -> this.offsetter = Optional.of((state, world, pos) -> {
                Block block = state.getBlock();
                long l = MathHelper.hashCode(pos.getX(), 0, pos.getZ());
                double d = ((double) ((float) (l >> 4 & 0xFL) / 15.0f) - 1.0) * (double) block.getVerticalModelOffsetMultiplier();
                float f = block.getMaxHorizontalModelOffset();
                double e = MathHelper.clamp(((double) ((float) (l & 0xFL) / 15.0f) - 0.5) * 0.5, (double) (-f), (double) f);
                double g = MathHelper.clamp(((double) ((float) (l >> 8 & 0xFL) / 15.0f) - 0.5) * 0.5, (double) (-f), (double) f);
                return new Vec3d(e, d, g);
            });
            case XZ -> this.offsetter = Optional.of((state, world, pos) -> {
                if(world instanceof World) {
                    FlowerComponent component = FlowerComponent.FLOWER_DATA.get(world);
                    return component.getLand(pos);
                } else {
                    Block block = state.getBlock();
                    long l = MathHelper.hashCode(pos.getX(), 0, pos.getZ());
                    float f = block.getMaxHorizontalModelOffset();
                    double d = MathHelper.clamp(((double)((float)(l & 0xFL) / 15.0f) - 0.5) * 0.5, (double)(-f), (double)f);
                    double e = MathHelper.clamp(((double)((float)(l >> 8 & 0xFL) / 15.0f) - 0.5) * 0.5, (double)(-f), (double)f);
                    return new Vec3d(d, 0.0, e);
                }
            });
        }
        cir.setReturnValue((AbstractBlock.Settings)(Object)this);
    }
}
