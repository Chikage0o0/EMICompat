package me.chikage.emicompat.ae2;

import appeng.api.config.CondenserOutput;
import appeng.api.features.P2PTunnelAttunementInternal;
import appeng.client.gui.AEBaseScreen;
import appeng.core.AEConfig;
import appeng.core.AppEng;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.items.misc.CrystalSeedItem;
import appeng.menu.me.items.CraftingTermMenu;
import appeng.menu.me.items.WirelessCraftingTermMenu;
import appeng.recipes.handlers.InscriberRecipe;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import me.chikage.emicompat.ae2.recipe.EMIAttunementRecipe;
import me.chikage.emicompat.ae2.recipe.EMICondenserRecipe;
import me.chikage.emicompat.ae2.recipe.EMIInscriberRecipe;
import me.chikage.emicompat.ae2.recipe.EMIThrowingInWaterRecipe;
import me.chikage.emicompat.ae2.render.GrowingSeedIconRenderer;
import me.chikage.emicompat.ae2.transfer.UseCraftingRecipeTransfer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.reflections.Reflections;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class Ae2Plugin implements EmiPlugin {
    public static final Map<ResourceLocation, EmiRecipeCategory> ALL = new LinkedHashMap<>();

    public static final EmiRecipeCategory
            INSCRIBER = register("inscriber", EmiStack.of(AEBlocks.INSCRIBER.stack())),
            CONDENSER = register("condenser", EmiStack.of(AEBlocks.CONDENSER.stack())),
            ATTUNEMENT = register("attunement", EmiStack.of(AEParts.ME_P2P_TUNNEL)),
            THROWINGINWATER = register("throwinginwater", certusQuartzCrystalIcon());


    @Override
    public void register(EmiRegistry registry) {
        var recipes = registry.getRecipeManager();
        ALL.forEach((id, category) -> registry.addCategory(category));

        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(AEParts.CRAFTING_TERMINAL.stack()));
        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(AEItems.WIRELESS_CRAFTING_TERMINAL.stack()));
        registry.addRecipeHandler(CraftingTermMenu.TYPE, new UseCraftingRecipeTransfer<>());
        registry.addRecipeHandler(WirelessCraftingTermMenu.TYPE, new UseCraftingRecipeTransfer<>());


        Reflections reflections = new Reflections("appeng");
        Set<Class<? extends AEBaseScreen>> subClass = reflections.getSubTypesOf(AEBaseScreen.class);
        subClass.forEach(i -> registry.addExclusionArea(i, (screen, consumer) -> {
            if (screen != null)
                mapRects(screen.getExclusionZones()).forEach(bounds -> consumer.accept((Bounds) bounds));
        }));


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


        if (AEConfig.instance().isInWorldCrystalGrowthEnabled()) {
            registry.addRecipe(
                    new EMIThrowingInWaterRecipe(
                            List.of(EmiIngredient.of(Ingredient.of(AEItems.CERTUS_CRYSTAL_SEED))),
                            List.of(EmiStack.of(AEItems.CERTUS_QUARTZ_CRYSTAL.stack())),
                            true));
            registry.addRecipe(
                    new EMIThrowingInWaterRecipe(
                            List.of(EmiIngredient.of(Ingredient.of(AEItems.FLUIX_CRYSTAL_SEED))),
                            List.of(EmiStack.of(AEItems.FLUIX_CRYSTAL.stack())),
                            true));
        }
        if (AEConfig.instance().isInWorldFluixEnabled()) {
            registry.addRecipe(
                    new EMIThrowingInWaterRecipe(
                            List.of(
                                    EmiIngredient.of(Ingredient.of(Items.REDSTONE)),
                                    EmiIngredient.of(Ingredient.of(AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED)),
                                    EmiIngredient.of(Ingredient.of(Items.QUARTZ))),
                            List.of(EmiStack.of(AEItems.FLUIX_DUST.stack(2))),
                            false));
        }

    }

    private static EmiRecipeCategory register(String name, EmiRenderable icon) {
        ResourceLocation id = new ResourceLocation(AppEng.MOD_ID, name);
        EmiRecipeCategory category = new EmiRecipeCategory(id, icon);
        ALL.put(id, category);
        return category;
    }

    private static GrowingSeedIconRenderer certusQuartzCrystalIcon() {
        var stage1 = AEItems.CERTUS_CRYSTAL_SEED.stack();
        CrystalSeedItem.setGrowthTicks(stage1, 0);
        var stage2 = AEItems.CERTUS_CRYSTAL_SEED.stack();
        CrystalSeedItem.setGrowthTicks(stage2, (int) (CrystalSeedItem.GROWTH_TICKS_REQUIRED * 0.4f));
        var stage3 = AEItems.CERTUS_CRYSTAL_SEED.stack();
        CrystalSeedItem.setGrowthTicks(stage3, (int) (CrystalSeedItem.GROWTH_TICKS_REQUIRED * 0.7f));
        var result = AEItems.CERTUS_QUARTZ_CRYSTAL.stack();

        return new GrowingSeedIconRenderer(List.of(
                stage1,
                stage2,
                stage3,
                result));
    }

    public static List<Bounds> mapRects(List<Rect2i> exclusionZones) {
        return exclusionZones.stream()
                .map(ez -> new Bounds(ez.getX(), ez.getY(), ez.getWidth(), ez.getHeight()))
                .collect(Collectors.toList());
    }
}
