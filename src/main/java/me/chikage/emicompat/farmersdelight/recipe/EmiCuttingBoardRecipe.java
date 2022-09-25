package me.chikage.emicompat.farmersdelight.recipe;

import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import com.nhoryzon.mc.farmersdelight.recipe.CuttingBoardRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.chikage.emicompat.farmersdelight.FarmersDelightPlugin;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmiCuttingBoardRecipe implements EmiRecipe {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(FarmersDelightMod.MOD_ID, "textures/gui/rei/cutting_board.png");
    private static final EmiTexture BACKGROUND = new EmiTexture(GUI_TEXTURE, 0, 0, 117, 57);

    protected final EmiRecipeCategory category;
    protected ResourceLocation id;
    protected final List<EmiIngredient> catalysts;
    protected List<EmiIngredient> input;
    protected List<EmiStack> output;
    protected int width, height;
    protected boolean supportsRecipeTree;

    public EmiCuttingBoardRecipe(CuttingBoardRecipe recipe) {
        this.category = FarmersDelightPlugin.CUTTING;
        this.id = recipe.getId();
        this.width = BACKGROUND.width;
        this.height = BACKGROUND.height;
        this.supportsRecipeTree = true;
        this.input = List.of(EmiIngredient.of(recipe.getIngredients().get(0)));
        this.output = recipe.getResultList().stream()
                .map(EmiStack::of)
                .toList();

        this.catalysts = List.of(EmiIngredient.of(recipe.getTool()));
    }

    @Override
    public void addWidgets(WidgetHolder gui) {
        gui.addTexture(BACKGROUND, 0, 0);

        // TODO figure out how to draw the damn cutting board

        gui.addSlot(catalysts.get(0), 15, 7)
                .drawBack(false)
                .catalyst(true);

        gui.addSlot(input.get(0), 15, 26)
                .drawBack(false);

        final int slotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 2; ++col) {
                final int i = row * 2 + col;
                EmiStack stack = (
                        i < output.size()
                                ? output.get(i)
                                : EmiStack.EMPTY);
                gui.addSlot(stack,
                        77 + col * slotSize,
                        11 + row * slotSize);
            }
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

    @Override
    public List<EmiIngredient> getCatalysts() {
        return catalysts;
    }
}
