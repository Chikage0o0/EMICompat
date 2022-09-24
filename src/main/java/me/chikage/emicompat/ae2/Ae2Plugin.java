package me.chikage.emicompat.ae2;

import appeng.api.config.CondenserOutput;
import appeng.core.definitions.AEBlocks;
import appeng.recipes.handlers.InscriberRecipe;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import appeng.core.AppEng;
import me.chikage.emicompat.ae2.recipe.EMICondenserRecipe;
import me.chikage.emicompat.ae2.recipe.EMIInscriberRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class Ae2Plugin implements EmiPlugin {
    public static final Map<ResourceLocation, EmiRecipeCategory> ALL = new LinkedHashMap<>();

    public static final EmiRecipeCategory
            Inscriber = register("inscriber", EmiStack.of(AEBlocks.INSCRIBER.stack())),
            Condenser = register("condenser", EmiStack.of(AEBlocks.CONDENSER.stack()));

    @Override
    public void register(EmiRegistry registry) {
        var recipes = registry.getRecipeManager();
        ALL.forEach((id, category) -> registry.addCategory(category));

        registry.addWorkstation(Inscriber, EmiStack.of(AEBlocks.INSCRIBER.stack()));
        recipes.getAllRecipesFor(InscriberRecipe.TYPE).stream()
                .parallel().map(EMIInscriberRecipe::new)
                .forEach(registry::addRecipe);

        registry.addWorkstation(Condenser, EmiStack.of(AEBlocks.CONDENSER.stack()));
        registry.addRecipe(new EMICondenserRecipe(CondenserOutput.MATTER_BALLS));
        registry.addRecipe(new EMICondenserRecipe(CondenserOutput.SINGULARITY));

    }

    private static EmiRecipeCategory register(String name, EmiRenderable icon) {
        ResourceLocation id = new ResourceLocation(AppEng.MOD_ID, name);
        EmiRecipeCategory category = new EmiRecipeCategory(id, icon);
        ALL.put(id, category);
        return category;
    }
}
