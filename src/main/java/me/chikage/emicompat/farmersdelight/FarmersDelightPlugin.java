package me.chikage.emicompat.farmersdelight;


import com.nhoryzon.mc.farmersdelight.recipe.CookingPotRecipe;
import com.nhoryzon.mc.farmersdelight.recipe.CuttingBoardRecipe;
import com.nhoryzon.mc.farmersdelight.registry.BlocksRegistry;
import com.nhoryzon.mc.farmersdelight.registry.ItemsRegistry;
import com.nhoryzon.mc.farmersdelight.registry.RecipeTypesRegistry;
import com.nhoryzon.mc.farmersdelight.registry.TagsRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import me.chikage.emicompat.farmersdelight.recipe.EmiCookingPotRecipe;
import me.chikage.emicompat.farmersdelight.recipe.EmiCuttingBoardRecipe;
import me.chikage.emicompat.farmersdelight.recipe.EmiDecompositionRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.nhoryzon.mc.farmersdelight.FarmersDelightMod.MOD_ID;

public class FarmersDelightPlugin implements EmiPlugin {
    public static final Map<ResourceLocation, EmiRecipeCategory> ALL = new LinkedHashMap<>();
    public static final EmiRecipeCategory
            COOKING = register("cooking", EmiStack.of(ItemsRegistry.COOKING_POT.get())),
            CUTTING = register("cutting", EmiStack.of(ItemsRegistry.CUTTING_BOARD.get())),
            DECOMPOSITION = register("decomposition", EmiStack.of(ItemsRegistry.RICH_SOIL.get()));


    @Override
    public void register(EmiRegistry registry) {
        RecipeType<CookingPotRecipe> COOKING_T = RecipeTypesRegistry.COOKING_RECIPE_SERIALIZER.type();
        RecipeType<CuttingBoardRecipe> CUTTING_T = RecipeTypesRegistry.CUTTING_RECIPE_SERIALIZER.type();

        var recipes = registry.getRecipeManager();
        ALL.forEach((id, category) -> registry.addCategory(category));

        registry.addWorkstation(VanillaEmiRecipeCategories.CAMPFIRE_COOKING, EmiStack.of(ItemsRegistry.STOVE.get()));
        registry.addWorkstation(VanillaEmiRecipeCategories.CAMPFIRE_COOKING, EmiStack.of(ItemsRegistry.SKILLET.get()));

        registry.addWorkstation(FarmersDelightPlugin.COOKING, EmiStack.of(ItemsRegistry.COOKING_POT.get()));
        recipes.getAllRecipesFor(COOKING_T).stream()
                .parallel()
                .map(EmiCookingPotRecipe::new)
                .forEach(registry::addRecipe);

        registry.addWorkstation(CUTTING, EmiStack.of(ItemsRegistry.CUTTING_BOARD.get()));
        recipes.getAllRecipesFor(CUTTING_T).stream()
                .parallel()
                .map(EmiCuttingBoardRecipe::new)
                .forEach(registry::addRecipe);

        registry.addRecipe(new EmiDecompositionRecipe(
                List.of(EmiIngredient.of(Ingredient.of(BlocksRegistry.ORGANIC_COMPOST.get()))),
                List.of(EmiStack.of(BlocksRegistry.RICH_SOIL.get())),
                Registry.BLOCK.getTag(TagsRegistry.COMPOST_ACTIVATORS).stream()
                        .parallel()
                        .flatMap(HolderSet::stream)
                        .map(Holder::value)
                        .toList()
        ));
    }

    private static EmiRecipeCategory register(String name, EmiRenderable icon) {
        ResourceLocation id = new ResourceLocation(MOD_ID, name);
        EmiRecipeCategory category = new EmiRecipeCategory(id, icon);
        ALL.put(id, category);
        return category;
    }
}
