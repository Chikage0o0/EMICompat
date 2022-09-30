package me.chikage.emicompat.ae2.transfer;


import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.FillCraftingGridFromRecipePacket;
import appeng.menu.SlotSemantics;
import appeng.menu.me.common.GridInventoryEntry;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.CraftingTermMenu;
import com.google.common.base.Preconditions;
import dev.emi.emi.api.EmiFillAction;
import dev.emi.emi.api.EmiRecipeHandler;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiPlayerInventory;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.*;


public class UseCraftingRecipeTransfer<T extends CraftingTermMenu> implements EmiRecipeHandler<T> {
    private final Comparator<GridInventoryEntry> ENTRY_COMPARATOR = Comparator
            .comparing(GridInventoryEntry::getStoredAmount);

    @Override
    public List<Slot> getInputSources(T handler) {
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
    public boolean canCraft(EmiRecipe recipe, EmiPlayerInventory inventory, AbstractContainerScreen<T> screen) {
        return true;
    }

    @Override
    public List<Slot> getCraftingSlots(T handler) {
        return Collections.emptyList();
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == VanillaEmiRecipeCategories.CRAFTING && recipe.supportsRecipeTree();
    }

    @Override
    public boolean performFill(EmiRecipe recipe, AbstractContainerScreen<T> screen, EmiFillAction action, int amount) {
        if (recipe instanceof EmiCraftingRecipe) {
            var templateItems = findGoodTemplateItems(recipe, screen.getMenu());

            var recipeId = recipe.getId();
            Minecraft.getInstance().setScreen(screen);
            NetworkHandler.instance().sendToServer(new FillCraftingGridFromRecipePacket(recipeId, templateItems, true));

            return true;
        }
        return false;

    }


    protected final Map<AEKey, Integer> getIngredientPriorities(MEStorageMenu menu,
                                                                Comparator<GridInventoryEntry> comparator) {
        var orderedEntries = menu.getClientRepo().getAllEntries()
                .stream()
                .sorted(comparator)
                .map(GridInventoryEntry::getWhat)
                .toList();

        var result = new HashMap<AEKey, Integer>(orderedEntries.size());
        for (int i = 0; i < orderedEntries.size(); i++) {
            result.put(orderedEntries.get(i), i);
        }

        // Also consider the player inventory, but only as the last resort
        for (var item : menu.getPlayerInventory().items) {
            var key = AEItemKey.of(item);
            if (key != null) {
                // Use -1 as lower priority than the lowest network entry (which start at 0)
                result.putIfAbsent(key, -1);
            }
        }

        return result;
    }

    private NonNullList<ItemStack> findGoodTemplateItems(EmiRecipe recipe, MEStorageMenu menu) {
        var ingredientPriorities = getIngredientPriorities(menu, ENTRY_COMPARATOR);

        var templateItems = NonNullList.withSize(9, ItemStack.EMPTY);
        var ingredients = ensure3by3CraftingMatrix(recipe);
        for (int i = 0; i < ingredients.size(); i++) {
            var ingredient = ingredients.get(i);
            if (!ingredient.isEmpty()) {
                // Try to find the best item. In case the ingredient is a tag, it might contain versions the
                // player doesn't actually have
                var stack = ingredientPriorities.entrySet()
                        .stream()
                        .filter(e -> e.getKey() instanceof AEItemKey itemKey && EmiIngredient.areEqual(ingredient, EmiStack.of(itemKey.toStack())))
                        .max(Comparator.comparingInt(Map.Entry::getValue))
                        .map(e -> ((AEItemKey) e.getKey()).toStack())
                        .orElse(ingredient.getEmiStacks().get(0).getItemStack());

                templateItems.set(i, stack);
            }
        }
        return templateItems;
    }

    private NonNullList<EmiIngredient> ensure3by3CraftingMatrix(EmiRecipe recipe) {
        var ingredients = recipe.getInputs();
        var expandedIngredients = NonNullList.withSize(9, EmiIngredient.of(Ingredient.EMPTY));


        Preconditions.checkArgument(ingredients.size() <= 9);

        // shaped recipes can be smaller than 3x3, expand to 3x3 to match the crafting
        // matrix
        if (recipe instanceof ShapedRecipe shapedRecipe) {
            var width = shapedRecipe.getWidth();
            var height = shapedRecipe.getHeight();
            Preconditions.checkArgument(width <= 3 && height <= 3);

            for (var h = 0; h < height; h++) {
                for (var w = 0; w < width; w++) {
                    var source = w + h * width;
                    var target = w + h * 3;
                    var i = ingredients.get(source);
                    expandedIngredients.set(target, i);
                }
            }
        }
        // Anything else should be a flat list
        else {
            for (var i = 0; i < ingredients.size(); i++) {
                expandedIngredients.set(i, ingredients.get(i));
            }
        }

        return expandedIngredients;
    }
}
