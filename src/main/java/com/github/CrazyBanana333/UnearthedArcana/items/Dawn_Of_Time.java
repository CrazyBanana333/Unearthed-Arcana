package com.github.CrazyBanana333.UnearthedArcana.items;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.*;
import net.minecraft.world.entity.player.Player;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Dawn_Of_Time extends Item {

    private final Multimap<Attribute, AttributeModifier> dawnOfTimeAttributes;

    public Dawn_Of_Time(Properties properties) {
        super(properties);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 10.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -1.1, AttributeModifier.Operation.ADDITION));

        this.dawnOfTimeAttributes = builder.build();
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.dawnOfTimeAttributes : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);

        CompoundTag tag = item.getOrCreateTag();
        tag.putInt("CustomModelData", 1);



        player.startUsingItem(hand);

        return InteractionResultHolder.consume(item);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player player) {
            int i = this.getUseDuration(stack) - remainingUseTicks;
            if (i >= 10) {
                //Support for enchants later maybe???
                float enchant = 0.0F;

                float f = user.getYRot();
                float g = user.getXRot();
                float h = -Mth.sin(f * ((float)Math.PI / 180F)) * Mth.cos(g * ((float)Math.PI / 180F));
                float k = -Mth.sin(g * ((float)Math.PI / 180F));
                float l = Mth.cos(f * ((float)Math.PI / 180F)) * Mth.cos(g * ((float)Math.PI / 180F));
                float m = Mth.sqrt(h * h + k * k + l * l);
                float n = 3.0F * ((5.0F + enchant) / 4.0F);
                h *= n / m;
                k *= n / m;
                l *= n / m;

                player.push(h, k, l);
                player.startAutoSpinAttack(20);
                if (player.onGround()) {
                    player.move(MoverType.SELF, new Vec3((double)0.0F, (double)1.1999999F, (double)0.0F));
                }

                SoundEvent sound = SoundEvents.TRIDENT_RIPTIDE_3;

                level.playSound(null, player, sound, SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt("CustomModelData", 0);

        }
    }



    }
