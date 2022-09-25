package me.chikage.emicompat.createaddition.recipe;

import com.mrh0.createaddition.index.CABlocks;
import com.mrh0.createaddition.recipe.rolling.RollingRecipe;
import com.simibubi.create.compat.emi.CreateEmiAnimations;
import com.simibubi.create.compat.emi.recipes.CreateEmiRecipe;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.chikage.emicompat.createaddition.CreateAdditionPlugin;

import java.util.List;


public class EMIRollingMillRecipe extends CreateEmiRecipe<RollingRecipe> {
    public EMIRollingMillRecipe(RollingRecipe recipe) {
        super(CreateAdditionPlugin.RollingMill, recipe, 177, 53, c -> {
        });
        this.input = List.of(EmiIngredient.of(recipe.getIngredient()));
        this.output = List.of(EmiStack.of(recipe.getResultItem()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        addTexture(widgets, AllGuiTextures.JEI_DOWN_ARROW, 43, 4);
        addTexture(widgets, AllGuiTextures.JEI_ARROW, 85, 32);
        addTexture(widgets, AllGuiTextures.JEI_SHADOW, 32, 40);

        addSlot(widgets, input.get(0), 15, 9);


        addSlot(widgets, output.get(0), 140, 28).recipeContext(this);


        widgets.addDrawable(48, 44, 0, 0, (matrices, mouseX, mouseY, delta) -> {
            int scale = 22;

            CreateEmiAnimations.blockElement(CABlocks.ROLLING_MILL.getDefaultState())
                    .rotateBlock(22.5, 22.5, 0)
                    .scale(scale)
                    .render(matrices);
        });
    }
}
