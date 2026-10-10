package net.blay09.mods.balm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.balm.fabric.loot.FabricBalmLootModifiers;
import net.blay09.mods.balm.fabric.mixin.FabricLootTableAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@Mixin(LootTable.class)
public abstract class FabricLootTableMixin implements FabricLootTableAccess {
    @Unique
    private ResourceKey<LootTable> balm$lootTableId;

    @WrapMethod(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V")
    private void balm$applyLootModifiers(LootContext lootContext, Consumer<ItemStack> lootConsumer, Operation<Void> original) {
        final Collection<BalmLootModifier> modifiers = FabricBalmLootModifiers.getModifiers();
        if (modifiers.isEmpty()) {
            original.call(lootContext, lootConsumer);
            return;
        }

        final List<ItemStack> drops = new ObjectArrayList<>();
        original.call(lootContext, (Consumer<ItemStack>) drops::add);
        FabricBalmLootModifiers.applyModifiers(modifiers, balm$lootTableId, lootContext, drops);
        drops.forEach(lootConsumer);
    }

    @Override
    public void balm$setLootTableId(ResourceKey<LootTable> lootTableId) {
        this.balm$lootTableId = lootTableId;
    }
}
