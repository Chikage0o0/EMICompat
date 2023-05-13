package me.chikage.emicompat.expandeddelight.recipe;

import com.ianm1647.expandeddelight.ExpandedDelight;
import com.ianm1647.expandeddelight.util.recipe.JuicerRecipe;
import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.chikage.emicompat.expandeddelight.ExpandedDelightPlugin;
import me.shedaniel.math.Point;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmiJuicerRecipe implements EmiRecipe {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(ExpandedDelight.MOD_ID, "textures/gui/juicer_gui.png");

    private static final EmiTexture BACKGROUND = new EmiTexture(GUI_TEXTURE, 20, 5, 140, 75);

    private final JuicerRecipe recipe;
    protected final EmiRecipeCategory category;
    protected ResourceLocation id;
    protected List<EmiIngredient> input, ingredients, container;
    protected List<EmiStack> output;
    protected int width, height;
    protected boolean supportsRecipeTree;

    public EmiJuicerRecipe(JuicerRecipe recipe) {
        this.recipe = recipe;


        this.category = ExpandedDelightPlugin.JUICING;
        this.id = recipe.getId();
        this.width = BACKGROUND.width;
        this.height = BACKGROUND.height;
        this.supportsRecipeTree = true;
        this.ingredients = recipe.getIngredients().stream()
                .filter(it -> !it.isEmpty())
                .map(EmiIngredient::of)
                .toList();
        this.container = List.of(EmiIngredient.of(Ingredient.of(recipe.getBottle())));


        this.input = Stream.of(container, ingredients).flatMap(Collection::stream).collect(Collectors.toList());

        for (EmiIngredient ingredient : ingredients) {
            if (ingredient.isEmpty()) continue;
            for (EmiStack stack : ingredient.getEmiStacks()) {
                Item item = stack.getItemStack().getItem();
                Item remainder = item.getCraftingRemainingItem();
                if (remainder != null)
                    stack.setRemainder(EmiStack.of(remainder));
            }
        }
        this.output = List.of(EmiStack.of(recipe.getResultItem()));
    }

    @Override
    public void addWidgets(WidgetHolder gui) {
        gui.addTexture(BACKGROUND, 0, 0);

        gui.addFillingArrow(57, 29, 50 * recipe.getCookTime()).tooltip((x, y) ->
                List.of(ClientTooltipComponent.create(EmiPort.ordered(EmiPort.translatable("emi.cooking.time", recipe.getCookTime() / 20f))))
        );

        gui.addSlot(output.get(0), 95, 29).drawBack(false);
        gui.addSlot(output.get(0), 95, 54);
        gui.addSlot(container.get(0), 59, 54);

        for (int i = 0; i < ingredients.size(); ++i) {
            gui.addSlot(ingredients.get(i),
                    27 + i / 3 * 18,
                    19 + i % 3 * 18);
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
