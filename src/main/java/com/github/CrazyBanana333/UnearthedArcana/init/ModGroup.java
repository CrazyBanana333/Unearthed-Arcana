package com.github.CrazyBanana333.UnearthedArcana.init;

import com.github.CrazyBanana333.UnearthedArcana.UnearthedArcana;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModGroup {
    public static final DeferredRegister<CreativeModeTab> DEF_REG = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UnearthedArcana.MODID);

    public static final RegistryObject<CreativeModeTab> TAB = DEF_REG.register("unearthedarcana", () -> CreativeModeTab.builder()
            // Set name of tab to display
            .title(Component.translatable("Unearthed Arcana"))
            // Set icon of creative tab
            .icon(() -> new ItemStack(ModItems.THE_THRONGLER.get()))
            // Add default items to tab
            .displayItems((params, output) -> {
                output.accept(ModItems.THE_THRONGLER.get());

            })
            .build()
    );
}
