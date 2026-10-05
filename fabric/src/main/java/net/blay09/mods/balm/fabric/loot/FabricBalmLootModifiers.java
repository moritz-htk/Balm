package net.blay09.mods.balm.fabric.loot;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.balm.common.CommonBalmLootTables;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class FabricBalmLootModifiers {
    private static volatile Map<LootTable, ResourceKey<LootTable>> lootTableIds = new IdentityHashMap<>();

    private FabricBalmLootModifiers() {
    }

    public static void initialize() {
        LootTableEvents.ALL_LOADED.register((resourceManager, lootRegistry) -> {
            final var tableIds = new IdentityHashMap<LootTable, ResourceKey<LootTable>>();
            for (final var entry : lootRegistry.entrySet()) {
                tableIds.put(entry.getValue(), entry.getKey());
            }
            lootTableIds = tableIds;
        });
    }

    public static void applyModifiers(LootTable lootTable, LootContext lootContext, List<ItemStack> drops) {
        final var lootTableId = lootTableIds.get(lootTable);
        final var lootModifiers = ((CommonBalmLootTables) Balm.lootModifiers()).lootModifiers;
        for (final BalmLootModifier modifier : lootModifiers.values()) {
            modifier.apply(lootContext, drops, lootTableId);
        }
    }
}