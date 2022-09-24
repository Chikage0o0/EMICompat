package me.chikage.emicompat.ae2.recipe;

import appeng.core.localization.ItemModText;
import appeng.entity.GrowingCrystalEntity;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.chikage.emicompat.ae2.Ae2Plugin;
import me.chikage.emicompat.ae2.render.WaterBlockRenderer;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static dev.emi.emi.EmiRenderHelper.CLIENT;

public class EMIThrowingInWaterRecipe implements EmiRecipe {

    protected final EmiRecipeCategory category;
    protected ResourceLocation id;
    protected List<EmiIngredient> input;
    protected List<EmiStack> output;
    protected int width, height;
    protected boolean supportsRecipeTree;
    protected boolean supportsAccelerators;

    public EMIThrowingInWaterRecipe(List<EmiIngredient> input, List<EmiStack> output, boolean supportsAccelerators) {
        this.category = Ae2Plugin.ThrowingInWater;
        this.id = category.getId();
        this.width = 150;
        this.height = 72;
        this.supportsRecipeTree = true;
        this.input = input;
        this.output = output;
        this.supportsAccelerators = supportsAccelerators;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {

        int y = 10;
        int col1 = 10;
        for (EmiIngredient i : input) {
            var slot = widgets.addSlot(i, col1, y);
            y += slot.getBounds().height();
        }

        int yOffset = (input.size() - 1) / 2 * 18 + 10;
        final int col2 = col1 + 25;

        var arrow1 = widgets.addTexture(EmiTexture.EMPTY_ARROW, col2, yOffset);
        final int col3 = col2 + arrow1.getBounds().width() + 6;

        widgets.add(new WaterBlockRenderer(col3, yOffset, 14, 14));

        final int col4 = col3 + 16 + 5;
        var arrow2 = widgets.addTexture(EmiTexture.EMPTY_ARROW, col4, yOffset);

        final int col5 = arrow2.getBounds().x() + arrow2.getBounds().width() + 10;
        widgets.addSlot(output.get(0), col5, yOffset);

        if (supportsAccelerators) {
            var durationY = yOffset + 18;

            List<Component> tooltipLines = new ArrayList<>();
            tooltipLines.add(ItemModText.WITH_CRYSTAL_GROWTH_ACCELERATORS.text());
            for (var i = 1; i <= 5; i++) {
                var duration = GrowingCrystalEntity.getGrowthDuration(i).toMillis();
                tooltipLines.add(new TextComponent(i + ": " + DurationFormatUtils.formatDurationWords(
                        duration, true, true)));
            }

            var defaultDuration = GrowingCrystalEntity.getGrowthDuration(0).toMillis();
            widgets.addDrawable(col3 - 10, durationY, 36, 8, (matrices, mouseX, mouseY, delta) -> {
                matrices.pushPose();
                matrices.translate(0, 0, 300);
                CLIENT.font.draw(matrices, DurationFormatUtils.formatDurationWords(defaultDuration, true, true), 0, 0, 0);
                matrices.popPose();
            }).tooltip(((mouseX, mouseY) -> tooltipLines.stream().map(t -> ClientTooltipComponent.create(t.getVisualOrderText())).toList()));
        }
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

