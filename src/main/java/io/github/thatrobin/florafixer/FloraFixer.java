package io.github.thatrobin.florafixer;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.util.Identifier;

public class FloraFixer implements ModInitializer {

    public static String NAMESPACE = "flora_fixer";

    @Override
    public void onInitialize() {
    }

    public static Identifier identifier(String path) {
        return Identifier.of(NAMESPACE, path);
    }
}
