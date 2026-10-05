package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.fabric.loot.FabricBalmLootModifiers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LootTable.class)
public class FabricLootTableMixin {
    @Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;", at = @At("RETURN"))
    private void balm$applyLootModifiers(LootContext lootContext, CallbackInfoReturnable<List<ItemStack>> callbackInfo) {
        FabricBalmLootModifiers.applyModifiers((LootTable) (Object) this, lootContext, callbackInfo.getReturnValue());
    }
}