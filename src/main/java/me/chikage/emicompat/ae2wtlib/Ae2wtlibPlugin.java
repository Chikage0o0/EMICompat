package me.chikage.emicompat.ae2wtlib;

import appeng.client.gui.AEBaseScreen;
import de.mari_023.ae2wtlib.wct.WCTMenu;
import de.mari_023.ae2wtlib.wut.WUTHandler;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import me.chikage.emicompat.ae2.Ae2Plugin;
import me.chikage.emicompat.ae2.transfer.UseCraftingRecipeTransfer;
import org.reflections.Reflections;

import java.util.Set;


public class Ae2wtlibPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(WUTHandler.wirelessTerminals.get("crafting").universalTerminal()));
        registry.addRecipeHandler(WCTMenu.TYPE, new UseCraftingRecipeTransfer<>());

        Reflections reflections = new Reflections("de.mari_023.ae2wtlib");
        Set<Class<? extends AEBaseScreen>> subClass = reflections.getSubTypesOf(AEBaseScreen.class);
        subClass.forEach(i -> registry.addExclusionArea(i, (screen, consumer) -> {
            if (screen != null)
                Ae2Plugin.mapRects(screen.getExclusionZones()).forEach(bounds -> consumer.accept((Bounds) bounds));
        }));
    }
}
