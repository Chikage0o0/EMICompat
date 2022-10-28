package me.chikage.emicompat.ae2;

import appeng.api.config.CondenserOutput;
import appeng.api.features.P2PTunnelAttunementInternal;
import appeng.client.gui.AEBaseScreen;
import appeng.core.AppEng;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.items.CraftingTermMenu;
import appeng.menu.me.items.WirelessCraftingTermMenu;
import appeng.recipes.handlers.ChargerRecipe;
import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.transform.TransformRecipe;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import me.chikage.emicompat.ae2.recipe.*;
import me.chikage.emicompat.ae2.transfer.UseCraftingRecipeTransfer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Ae2Plugin implements EmiPlugin {
    public static final Map<ResourceLocation, EmiRecipeCategory> ALL = new LinkedHashMap<>();

    public static final EmiRecipeCategory
            INSCRIBER = register("inscriber", EmiStack.of(AEBlocks.INSCRIBER.stack())),
            CONDENSER = register("condenser", EmiStack.of(AEBlocks.CONDENSER.stack())),
            ATTUNEMENT = register("attunement", EmiStack.of(AEParts.ME_P2P_TUNNEL)),
            ITEM_TRANSFORMATION = register("item_transformation", EmiStack.of(AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED)),
            CHARGER = register("charger", EmiStack.of(AEBlocks.CHARGER.stack()));


    @Override
    public void register(EmiRegistry registry) {
        var recipes = registry.getRecipeManager();
        ALL.forEach((id, category) -> registry.addCategory(category));

        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(AEParts.CRAFTING_TERMINAL.stack()));
        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(AEItems.WIRELESS_CRAFTING_TERMINAL.stack()));
        registry.addRecipeHandler(CraftingTermMenu.TYPE, new UseCraftingRecipeTransfer<>());
        registry.addRecipeHandler(WirelessCraftingTermMenu.TYPE, new UseCraftingRecipeTransfer<>());

        registry.addGenericExclusionArea((screen, consumer) -> {
            if (screen != null) {
                if (screen instanceof AEBaseScreen<? extends AEBaseMenu> baseScreen) {
                    mapRects(baseScreen.getExclusionZones()).forEach(consumer);
                }
            }
        });

        registry.addWorkstation(INSCRIBER, EmiStack.of(AEBlocks.INSCRIBER.stack()));
        recipes.getAllRecipesFor(InscriberRecipe.TYPE).stream()
                .parallel().map(EMIInscriberRecipe::new)
                .forEach(registry::addRecipe);

        registry.addWorkstation(CONDENSER, EmiStack.of(AEBlocks.CONDENSER.stack()));
        registry.addRecipe(new EMICondenserRecipe(CondenserOutput.MATTER_BALLS));
        registry.addRecipe(new EMICondenserRecipe(CondenserOutput.SINGULARITY));

        for (var entry : P2PTunnelAttunementInternal.getTagTunnels().entrySet()) {
            registry.addRecipe(new EMIAttunementRecipe(List.of(EmiIngredient.of(Ingredient.of(entry.getKey()))),
                    List.of(EmiStack.of(entry.getValue()))));
        }

        recipes.getAllRecipesFor(TransformRecipe.TYPE).stream()
                .parallel().map(EMIItemTransformationRecipe::new)
                .forEach(registry::addRecipe);

        registry.addWorkstation(CHARGER, EmiStack.of(AEBlocks.CHARGER.stack()));
        recipes.getAllRecipesFor(ChargerRecipe.TYPE).stream()
                .parallel().map(EMIChargerRecipe::new)
                .forEach(registry::addRecipe);
    }

    private static EmiRecipeCategory register(String name, EmiRenderable icon) {
        ResourceLocation id = new ResourceLocation(AppEng.MOD_ID, name);
        EmiRecipeCategory category = new EmiRecipeCategory(id, icon);
        ALL.put(id, category);
        return category;
    }


    public static List<Bounds> mapRects(List<Rect2i> exclusionZones) {
        return exclusionZones.stream()
                .map(ez -> new Bounds(ez.getX(), ez.getY(), ez.getWidth(), ez.getHeight()))
                .collect(Collectors.toList());
    }
}
