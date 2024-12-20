package com.github.CrazyBanana333.UnearthedArcana.init;

import com.github.CrazyBanana333.UnearthedArcana.UnearthedArcana;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    // Create a Deferred Register to hold Blocks which will all be registered under the "unearthedarcana" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, UnearthedArcana.MODID);

    // Creates a new Block with the id "unearthedarcana:example_block", combining the namespace and path
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));


}
