package com.github.CrazyBanana333.UnearthedArcana.items;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;


import net.minecraft.world.item.TooltipFlag;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import javax.annotation.Nullable;
import java.util.List;

public class The_Throngler extends Item{

    private final Multimap<Attribute, AttributeModifier> thronglerAttributes;

    String playerName;

    private final int BlastRadius = 50;

    private CameraType prev_camera_type = CameraType.FIRST_PERSON;
    private boolean using = false;

    private boolean mainHand = true;

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

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        InteractionHand otherhand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack otheritem = player.getItemInHand(otherhand);

        mainHand = hand == InteractionHand.MAIN_HAND;

        if (!mainHand) {
            CompoundTag tag = item.getOrCreateTag();
            tag.putInt("CustomModelData", 1);
        }

        player.startUsingItem(hand);
        if (!using) {
            prev_camera_type = Minecraft.getInstance().options.getCameraType();
            using = true;


            playerName = player.getName().toString();
        }
        return InteractionResultHolder.consume(item);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.unearthedarcana.the_throngler.desc").withStyle(ChatFormatting.DARK_GREEN));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 201;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        if (mainHand) {
            return UseAnim.CUSTOM;
        } else {
            return UseAnim.SPEAR;
        }

    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int count) {
        int time = this.getUseDuration(stack) - count;

        InteractionHand useHand = living.getUsedItemHand();

        if (useHand == InteractionHand.MAIN_HAND) {


            for (int i = 0; i < 30; i++) {
                level.addParticle(ParticleTypes.END_ROD, living.getX() + Math.random() * BlastRadius - (double) BlastRadius / 2, living.getY() + Math.random() * BlastRadius - (double) BlastRadius / 2, living.getZ() + Math.random() * BlastRadius - (double) BlastRadius / 2, 0, 0, 0);
                if (i % 6 == 0) {
                    level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, living.getX() + (Math.random() * 2 - 1) * 0.3, living.getY() + 2.2, living.getZ() + (Math.random() * 2 - 1) * 0.3, (Math.random() * 2 - 1) * 0.2, 0.2, (Math.random() * 2 - 1) * 0.2);
                }
            }

            if (time % 50 == 0) {
                if (time < 200) {
                    for (int i = 0; i < time / 25; i++) {
                        LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                        lightningBolt.setPos(living.getX() + (Math.random() * 2 - 1) * i, living.getY(), living.getZ() + (Math.random() * 2 - 1) * i);
                        lightningBolt.setVisualOnly(true);
                        level.addFreshEntity(lightningBolt);
                    }
                } else {
                    for (int i = 0; i < time / 5; i++) {
                        LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                        lightningBolt.setPos(living.getX() + (Math.random() * 2 - 1) * i, living.getY(), living.getZ() + (Math.random() * 2 - 1) * i);
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

            if (Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON && Minecraft.getInstance().player == living) {
                Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int remainingUseTicks) {

        InteractionHand useHand = user.getUsedItemHand();

        if (useHand == InteractionHand.MAIN_HAND) {

            Minecraft.getInstance().options.setCameraType(prev_camera_type);
            using = false;
            playerName = null;

        } else if (useHand == InteractionHand.OFF_HAND) {
            if (user instanceof Player player) {
                int i = this.getUseDuration(stack) - remainingUseTicks;
                if (i >= 10) {
                    //Support for enchants later maybe???
                    float enchant = 0.0F;

                    float f = user.getYRot();
                    float g = user.getXRot();
                    float h = -Mth.sin(f * ((float) Math.PI / 180F)) * Mth.cos(g * ((float) Math.PI / 180F));
                    float k = -Mth.sin(g * ((float) Math.PI / 180F));
                    float l = Mth.cos(f * ((float) Math.PI / 180F)) * Mth.cos(g * ((float) Math.PI / 180F));
                    float m = Mth.sqrt(h * h + k * k + l * l);
                    float n = 3.0F * ((5.0F + enchant) / 4.0F);
                    h *= n / m;
                    k *= n / m;
                    l *= n / m;

                    player.push(h, k, l);
                    player.startAutoSpinAttack(20);
                    if (player.onGround()) {
                        player.move(MoverType.SELF, new Vec3((double) 0.0F, (double) 1.1999999F, (double) 0.0F));
                    }

                    SoundEvent sound = SoundEvents.TRIDENT_RIPTIDE_3;

                    level.playSound(null, player, sound, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt("CustomModelData", 0);
        }


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
