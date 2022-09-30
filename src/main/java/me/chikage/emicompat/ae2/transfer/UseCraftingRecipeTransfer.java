package me.chikage.emicompat.ae2.transfer;


import appeng.menu.SlotSemantics;
import appeng.menu.me.items.CraftingTermMenu;
import dev.emi.emi.api.EmiRecipeHandler;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class UseCraftingRecipeTransfer implements EmiRecipeHandler<CraftingTermMenu> {
    public UseCraftingRecipeTransfer() {
    }

    @Override
    public List<Slot> getInputSources(CraftingTermMenu handler) {
        List<Slot> list = new ArrayList<>();
        list.addAll(Objects.requireNonNull(handler.getClientRepo()).getAllEntries().stream().map((s) -> {
                    ItemStack item = Objects.requireNonNull(s.getWhat()).wrapForDisplayOrFilter();
                    item.setCount(s.getStoredAmount() > 64 ? 64 : (int) s.getStoredAmount());
                    return new Slot(new SimpleContainer(item), 0, 0, 0);
                })
                .toList());
        list.addAll(handler.getSlots(SlotSemantics.PLAYER_INVENTORY));
        return list;
    }

    @Override
    public @Nullable Slot getOutputSlot(CraftingTermMenu handler) {
        return handler.getSlots(SlotSemantics.CRAFTING_RESULT).get(0);
    }

    @Override
    public List<Slot> getCraftingSlots(CraftingTermMenu handler) {
        return handler.getSlots(SlotSemantics.CRAFTING_GRID);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == VanillaEmiRecipeCategories.CRAFTING && recipe.supportsRecipeTree();
    }

}
