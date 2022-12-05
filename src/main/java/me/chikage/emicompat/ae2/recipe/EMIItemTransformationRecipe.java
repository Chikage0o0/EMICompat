package me.chikage.emicompat.ae2.recipe;

import appeng.core.definitions.AEBlocks;
import appeng.core.localization.ItemModText;
import appeng.recipes.transform.TransformRecipe;
import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.chikage.emicompat.ae2.Ae2Plugin;
import me.chikage.emicompat.ae2.render.WaterBlockRenderer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EMIItemTransformationRecipe implements EmiRecipe {

    protected final EmiRecipeCategory category;
    protected ResourceLocation id;
    protected List<EmiIngredient> input;
    protected List<EmiStack> output;
    protected int width, height;
    protected boolean supportsRecipeTree;
    protected final TransformRecipe recipe;

    public EMIItemTransformationRecipe(TransformRecipe recipe) {
        this.recipe = recipe;
        this.category = Ae2Plugin.ITEM_TRANSFORMATION;
        this.id = recipe.getId();
        this.width = 150;
        this.height = 72;
        this.supportsRecipeTree = true;
        this.input = recipe.getIngredients().stream()
                .filter(it -> !it.isEmpty())
                .map(EmiIngredient::of)
                .toList();
        this.output = List.of(EmiStack.of(recipe.getResultItem()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {

        int y = 10;
        int col1 = 10;


        int nInputs = input.size();
        if (nInputs < 3) {
            y += 9 * (3 - nInputs);
        }

        for (EmiIngredient i : input) {
            var slot = widgets.addSlot(i, col1, y);
            y += slot.getBounds().height();
        }

        int yOffset = 28;
        final int col2 = col1 + 25;

        var arrow1 = widgets.addTexture(EmiTexture.EMPTY_ARROW, col2, yOffset);
        final int col3 = col2 + arrow1.getBounds().width() + 6;

        MutableComponent circumstance;
        if (recipe.circumstance.isExplosion()) {
            circumstance = ItemModText.EXPLOSION.text();
        } else {
            circumstance = ItemModText.SUBMERGE_IN.text();
        }
        widgets.addText(EmiPort.ordered(circumstance), col3, yOffset-15, 8289918, false);
        if (recipe.circumstance.isFluid()) {
            widgets.add(new WaterBlockRenderer(col3, yOffset, 14, 14));
        } else if (recipe.circumstance.isExplosion()) {
            widgets.addSlot(EmiIngredient.of(List.of(EmiStack.of(Blocks.TNT), EmiStack.of(AEBlocks.TINY_TNT.stack()))), col3, yOffset).drawBack(false);
        }


        final int col4 = col3 + 16 + 5;
        var arrow2 = widgets.addTexture(EmiTexture.EMPTY_ARROW, col4, yOffset);

        final int col5 = arrow2.getBounds().x() + arrow2.getBounds().width() + 10;
        widgets.addSlot(output.get(0), col5, yOffset);
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

