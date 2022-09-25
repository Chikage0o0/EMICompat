package me.chikage.emicompat.ae2.recipe;

import appeng.core.AppEng;
import appeng.core.definitions.AEItems;
import appeng.recipes.handlers.InscriberRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.chikage.emicompat.ae2.Ae2Plugin;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EMIInscriberRecipe implements EmiRecipe {
    protected final EmiRecipeCategory category;
    protected final InscriberRecipe recipe;
    protected ResourceLocation id;
    protected List<EmiIngredient> input;
    protected List<EmiIngredient> top;
    protected List<EmiIngredient> middle;
    protected List<EmiIngredient> bottom;
    protected List<EmiStack> output;
    protected int width, height;

    protected final List<EmiIngredient> catalysts;
    protected boolean supportsRecipeTree;

    public EMIInscriberRecipe(InscriberRecipe recipe) {
        this.category = Ae2Plugin.INSCRIBER;
        this.recipe = recipe;
        this.id = recipe.getId();
        this.width = 97;
        this.height = 64;
        this.supportsRecipeTree = true;
        this.top = List.of(EmiIngredient.of(recipe.getTopOptional()));
        this.middle = List.of(EmiIngredient.of(recipe.getMiddleInput()));
        this.bottom = List.of(EmiIngredient.of(recipe.getBottomOptional()));
        if (!top.isEmpty() && !top.get(0).getEmiStacks().isEmpty() &&
                (Objects.equals(top.get(0).getEmiStacks().get(0).getId(), AEItems.LOGIC_PROCESSOR_PRESS.id()) ||
                        Objects.equals(top.get(0).getEmiStacks().get(0).getId(), AEItems.CALCULATION_PROCESSOR_PRESS.id()) ||
                        Objects.equals(top.get(0).getEmiStacks().get(0).getId(), AEItems.ENGINEERING_PROCESSOR_PRESS.id()) ||
                        Objects.equals(top.get(0).getEmiStacks().get(0).getId(), AEItems.SILICON_PRESS.id()))) {
            this.catalysts = top;
            this.input = Stream.of(middle, bottom).flatMap(Collection::stream).collect(Collectors.toList());
        } else {
            this.catalysts = Collections.emptyList();
            this.input = Stream.of(top, middle, bottom).flatMap(Collection::stream).collect(Collectors.toList());
        }
        this.output = List.of(EmiStack.of(recipe.getResultItem()));
    }


    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(AppEng.makeId("textures/guis/inscriber.png"), 0, 0, 97, 64, 44, 15);
        widgets.addSlot(top.get(0), 0, 0).drawBack(false);
        widgets.addSlot(middle.get(0), 18, 23).drawBack(false);
        widgets.addSlot(bottom.get(0), 0, 46).drawBack(false);
        widgets.addSlot(output.get(0), 68, 24).drawBack(false).recipeContext(this);
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

    @Override
    public List<EmiIngredient> getCatalysts() {
        return catalysts;
    }
}
