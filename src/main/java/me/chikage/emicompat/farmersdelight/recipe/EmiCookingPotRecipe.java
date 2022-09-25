package me.chikage.emicompat.farmersdelight.recipe;

import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import com.nhoryzon.mc.farmersdelight.recipe.CookingPotRecipe;
import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.chikage.emicompat.farmersdelight.FarmersDelightPlugin;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmiCookingPotRecipe implements EmiRecipe {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(FarmersDelightMod.MOD_ID, "textures/gui/cooking_pot.png");

    private static final EmiTexture BACKGROUND = new EmiTexture(GUI_TEXTURE, 29, 16, 117, 57);

    private static final EmiTexture FIRE = new EmiTexture(GUI_TEXTURE, 176, 0, 17, 15);

    private final CookingPotRecipe recipe;
    protected final EmiRecipeCategory category;
    protected ResourceLocation id;
    protected List<EmiIngredient> input, ingredients, container;
    protected List<EmiStack> output;
    protected int width, height;
    protected boolean supportsRecipeTree;

    public EmiCookingPotRecipe(CookingPotRecipe recipe) {
        this.recipe = recipe;


        this.category = FarmersDelightPlugin.COOKING;
        this.id = recipe.getId();
        this.width = BACKGROUND.width;
        this.height = BACKGROUND.height;
        this.supportsRecipeTree = true;
        this.ingredients = recipe.getIngredients().stream()
                .filter(it -> !it.isEmpty())
                .map(EmiIngredient::of)
                .toList();
        this.container = List.of(EmiIngredient.of(Ingredient.of(recipe.getContainer())));


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
        gui.addTexture(FIRE, 18, 39);

        gui.addFillingArrow(60, 10, 50 * recipe.getCookTime()).tooltip((x, y) ->
                List.of(ClientTooltipComponent.create(EmiPort.ordered(EmiPort.translatable("emi.cooking.time", recipe.getCookTime() / 20f))))
        );

        gui.addSlot(output.get(0), 94, 9).drawBack(false);
        gui.addSlot(output.get(0), 94, 38);
        gui.addSlot(container.get(0), 62, 38);

        final int slotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 3; ++col) {
                final int i = row * 3 + col;
                EmiIngredient ingredient = (
                        i < ingredients.size()
                                ? ingredients.get(i)
                                : EmiStack.EMPTY);
                gui.addSlot(ingredient,
                        col * slotSize,
                        row * slotSize);
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

}
