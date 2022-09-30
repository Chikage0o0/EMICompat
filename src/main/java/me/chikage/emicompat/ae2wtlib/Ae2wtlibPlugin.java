package me.chikage.emicompat.ae2wtlib;

import de.mari_023.ae2wtlib.wct.WCTMenu;
import de.mari_023.ae2wtlib.wut.WUTHandler;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import me.chikage.emicompat.ae2.transfer.UseCraftingRecipeTransfer;

public class Ae2wtlibPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(WUTHandler.wirelessTerminals.get("crafting").universalTerminal()));
        registry.addRecipeHandler(WCTMenu.TYPE, new UseCraftingRecipeTransfer<>());
    }
}
