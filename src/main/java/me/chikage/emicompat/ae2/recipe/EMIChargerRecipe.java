package me.chikage.emicompat.ae2.recipe;

import appeng.blockentity.misc.ChargerBlockEntity;
import appeng.core.definitions.AEBlocks;
import appeng.core.localization.ItemModText;
import appeng.recipes.handlers.ChargerRecipe;
import appeng.tile.grindstone.CrankBlockEntity;
import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.chikage.emicompat.ae2.Ae2Plugin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EMIChargerRecipe implements EmiRecipe {
    protected final EmiRecipeCategory category;
    protected final ChargerRecipe recipe;
    protected ResourceLocation id;
    protected List<EmiIngredient> input;
    protected List<EmiStack> output;
    protected int width, height;

    protected boolean supportsRecipeTree;

    public EMIChargerRecipe(ChargerRecipe recipe) {
        this.category = Ae2Plugin.CHARGER;
        this.recipe = recipe;
        this.id = recipe.getId();
        this.width = 130;
        this.height = 50;
        this.supportsRecipeTree = true;
        this.input = List.of(EmiIngredient.of(recipe.getIngredient()));
        this.output = List.of(EmiStack.of(recipe.getResultItem()));
    }


    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(input.get(0), 31, 8);
        widgets.addSlot(output.get(0), 81, 8).recipeContext(this);

        widgets.addSlot(EmiIngredient.of(Ingredient.of(AEBlocks.CRANK.stack())), 3, 30).drawBack(false);

        widgets.addFillingArrow(52, 8, 1);

        var turns = (ChargerBlockEntity.POWER_MAXIMUM_AMOUNT + CrankBlockEntity.POWER_PER_CRANK_TURN - 1) / CrankBlockEntity.POWER_PER_CRANK_TURN;

        widgets.addText(EmiPort.ordered(ItemModText.CHARGER_REQUIRED_POWER.text(turns, ChargerBlockEntity.POWER_MAXIMUM_AMOUNT)), 20, 35, 0x7E7E7E, false);
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
