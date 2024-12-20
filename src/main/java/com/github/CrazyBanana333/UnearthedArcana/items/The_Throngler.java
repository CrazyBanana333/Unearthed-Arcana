package com.github.CrazyBanana333.UnearthedArcana.items;


import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.CompressionEncoder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.net.NetworkInterface;
import java.nio.channels.NetworkChannel;
import java.util.UUID;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.w3c.dom.Entity;

public class The_Throngler extends Item{

    private final Multimap<Attribute, AttributeModifier> thronglerAttributes;

    String playerName;

    private final int BlastRadius = 50;

    private CameraType prev_camera_type = CameraType.FIRST_PERSON;
    private boolean using = false;

    public The_Throngler(Properties properties) {
        super(properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 16.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.6F, AttributeModifier.Operation.ADDITION));

        this.thronglerAttributes = builder.build();
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.thronglerAttributes : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    public InteractionResultHolder<ItemStack> use(Level p_77659_1_, Player p_77659_2_, InteractionHand p_77659_3_) {
        ItemStack item = p_77659_2_.getItemInHand(p_77659_3_);
        InteractionHand otherhand = p_77659_3_ == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack otheritem = p_77659_2_.getItemInHand(otherhand);

        if (p_77659_3_ == InteractionHand.OFF_HAND) {
            return InteractionResultHolder.fail(item);
        }else{
            p_77659_2_.startUsingItem(p_77659_3_);
            if (!using) {
                prev_camera_type = Minecraft.getInstance().options.getCameraType();
                using = true;


                playerName = p_77659_2_.getName().toString();
            }
            return InteractionResultHolder.consume(item);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 201;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CUSTOM;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int count) {
        int time = this.getUseDuration(stack) - count;

        for (int i = 0; i < 30; i++) {
            level.addParticle(ParticleTypes.END_ROD, living.getX() + Math.random()*BlastRadius - (double)BlastRadius/2, living.getY() + Math.random()*BlastRadius - (double)BlastRadius/2, living.getZ() + Math.random()*BlastRadius - (double)BlastRadius/2, 0, 0, 0);
            if (i%6 == 0) {
                level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, living.getX() + (Math.random() * 2 - 1)*0.3, living.getY() + 2.2, living.getZ() + (Math.random() * 2 - 1)*0.3, (Math.random() * 2 - 1)*0.2, 0.2, (Math.random() * 2 - 1)*0.2);
            }
        }

        if (time % 50 == 0) {
            if (time < 200){
                for (int i = 0; i < time/25; i++) {
                    LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                    lightningBolt.setPos(living.getX() + (Math.random() * 2 - 1)*i, living.getY(), living.getZ() + (Math.random() * 2 - 1)*i);
                    lightningBolt.setVisualOnly(true);
                    level.addFreshEntity(lightningBolt);
                }
            } else {
                for (int i = 0; i < time/5; i++) {
                    LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                    lightningBolt.setPos(living.getX() + (Math.random() * 2 - 1)*i, living.getY(), living.getZ() + (Math.random() * 2 - 1)*i);
                    lightningBolt.setVisualOnly(true);
                    level.addFreshEntity(lightningBolt);
                }
            }

        }

        if (time >= 200) {
            for (int i = 0; i < 1000; i++) {
                level.addParticle(ParticleTypes.SONIC_BOOM, living.getX() + Math.random() * BlastRadius - (double) BlastRadius / 2, living.getY() + Math.random() * BlastRadius - (double) BlastRadius / 2, living.getZ() + Math.random() * BlastRadius - (double) BlastRadius / 2, 0, 0, 0);
            }
            killAllPlayersInRadius(level, living.getOnPos());
        }

        if (Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON && Minecraft.getInstance().player == living){
            Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
    }

    @Override
    public void releaseUsing(ItemStack p_43394_, Level p_43395_, LivingEntity p_43396_, int p_43397_) {

        Minecraft.getInstance().options.setCameraType(prev_camera_type);
        using = false;
        playerName = null;


    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private static final HumanoidModel.ArmPose RAISE_POSE = HumanoidModel.ArmPose.create("RAISE", false, (model, entity, arm) -> {
                model.rightArm.xRot = (float) Math.PI;
                model.rightArm.zRot = (float) Math.PI/-7;
                model.rightArm.yRot = (float) Math.PI/2;
                model.rightArm.z -= 1;
            });

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (!itemStack.isEmpty()) {
                    if (entityLiving.getUsedItemHand() == hand && entityLiving.getUseItemRemainingTicks() > 0) {
                        return RAISE_POSE;
                    }
                }
                return HumanoidModel.ArmPose.EMPTY;
            }
        });
    }

    private void killAllPlayersInRadius(Level level, BlockPos position) {
        //check a square first
        AABB boundingBox = new AABB(
                position.getX() - BlastRadius, position.getY() - BlastRadius, position.getZ() - BlastRadius,
                position.getX() + BlastRadius, position.getY() + BlastRadius, position.getZ() + BlastRadius
        );

        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, boundingBox)) {
            double dx = position.getX() - entity.getX();
            double dy = position.getY() - entity.getY();
            double dz = position.getZ() - entity.getZ();

            if (Math.sqrt(dx*dx + dy*dy + dz*dz) <= BlastRadius){
                entity.hurt(level.damageSources().generic(), entity.getHealth());
            }

        }
    }

}
