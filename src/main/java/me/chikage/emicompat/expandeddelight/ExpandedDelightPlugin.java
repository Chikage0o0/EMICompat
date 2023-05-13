package me.chikage.emicompat.expandeddelight;


import com.ianm1647.expandeddelight.block.BlockList;
import com.ianm1647.expandeddelight.registry.RecipeRegistry;
import com.ianm1647.expandeddelight.util.recipe.JuicerRecipe;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import me.chikage.emicompat.expandeddelight.recipe.EmiJuicerRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.ianm1647.expandeddelight.ExpandedDelight.MOD_ID;
import static me.chikage.emicompat.EmiCompatPlugin.addAll;

public class ExpandedDelightPlugin implements EmiPlugin {
    public static final Map<ResourceLocation, EmiRecipeCategory> ALL = new LinkedHashMap<>();
    public static final EmiRecipeCategory
            JUICING = register("juicing", EmiStack.of(BlockList.JUICER));


    @Override
    public void register(EmiRegistry registry) {
        RecipeType<JuicerRecipe> JUICER = RecipeRegistry.JUICER_TYPE;

        ALL.forEach((id, category) -> registry.addCategory(category));

        registry.addWorkstation(ExpandedDelightPlugin.JUICING, EmiStack.of(BlockList.JUICER));
        addAll(registry, JUICER, EmiJuicerRecipe::new);
    }

    private static EmiRecipeCategory register(String name, EmiRenderable icon) {
        ResourceLocation id = new ResourceLocation(MOD_ID, name);
        EmiRecipeCategory category = new EmiRecipeCategory(id, icon);
        ALL.put(id, category);
        return category;
    }
}
