package me.chikage.emicompat.ae2.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import me.chikage.emicompat.ae2.Ae2Plugin;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EMIAttunementRecipe implements EmiRecipe {
    protected final EmiRecipeCategory category;
    protected ResourceLocation id;
    protected List<EmiIngredient> input;
    protected List<EmiStack> output;
    protected int width, height;
    protected boolean supportsRecipeTree;

    public EMIAttunementRecipe(List<EmiIngredient> input, List<EmiStack> output) {
        this.category = Ae2Plugin.Attunement;
        this.id = category.getId();
        this.width = 150;
        this.height = 36;
        this.supportsRecipeTree = false;
        this.input = input;
        this.output = output;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, width / 2 - 41 + 27, height / 2 - 13 + 4);
        widgets.addSlot(new ListEmiIngredient(input, input.size()), width / 2 - 41 + 4, height / 2 - 13 + 4);
        widgets.addSlot(output.get(0), width / 2 - 41 + 56, height / 2 - 13 + 4);
    }

    @Override
    public boolean supportsRecipeTree() {
        return supportsRecipeTree;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return input;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    @Override
    public int getDisplayWidth() {
        return width;
    }

    @Override
    public int getDisplayHeight() {
        return height;
    }


}
