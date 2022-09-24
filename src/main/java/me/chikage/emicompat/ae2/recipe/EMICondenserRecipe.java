package me.chikage.emicompat.ae2.recipe;

import appeng.api.config.CondenserOutput;
import appeng.api.implementations.items.IStorageComponent;
import appeng.blockentity.misc.CondenserBlockEntity;
import appeng.core.AppEng;
import appeng.core.definitions.AEItems;
import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import me.chikage.emicompat.ae2.Ae2Plugin;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EMICondenserRecipe implements EmiRecipe {

    private static final int PADDING = 7;
    protected final EmiRecipeCategory category;
    protected final CondenserOutput recipe;
    private final List<EmiStack> viableStorageComponents;
    protected ResourceLocation id;
    protected List<EmiIngredient> input;
    protected List<EmiStack> output;
    protected int width, height;
    protected boolean supportsRecipeTree;

    public EMICondenserRecipe(CondenserOutput recipe) {
        this.category = Ae2Plugin.Condenser;
        this.recipe = recipe;
        this.id = category.getId();
        this.width = 94 + 2 * PADDING;
        this.height = 48 + 2 * PADDING;
        this.supportsRecipeTree = true;
        this.input = Collections.emptyList();
        this.output = List.of(EmiStack.of(switch (recipe) {
            case MATTER_BALLS -> AEItems.MATTER_BALL.stack();
            case SINGULARITY -> AEItems.SINGULARITY.stack();
            default -> ItemStack.EMPTY;
        }));
        this.viableStorageComponents = getViableStorageComponents(recipe);
    }


    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(AppEng.makeId("textures/guis/condenser.png"), PADDING, PADDING, 94, 48, 50, 25);
        widgets.addTexture(AppEng.makeId("textures/guis/states.png"), PADDING + 2, PADDING + 28, 14, 14, 241, 81);
        widgets.addTexture(AppEng.makeId("textures/guis/states.png"), PADDING + 78, PADDING + 28, 16, 16, 240, 240);
        if (recipe == CondenserOutput.MATTER_BALLS) {
            widgets.addTexture(AppEng.makeId("textures/guis/states.png"), PADDING + 78, PADDING + 28, 14, 14, 16, 112)
                    .tooltip((mouseX, mouseY) -> List.of(ClientTooltipComponent.create(EmiPort.translatable("gui.tooltips.ae2.MatterBalls", recipe.requiredPower).getVisualOrderText())));
        } else if (recipe == CondenserOutput.SINGULARITY) {
            widgets.addTexture(AppEng.makeId("textures/guis/states.png"), PADDING + 78, PADDING + 28, 14, 14, 32, 112)
                    .tooltip((mouseX, mouseY) -> List.of(ClientTooltipComponent.create(EmiPort.translatable("gui.tooltips.ae2.Singularity", recipe.requiredPower).getVisualOrderText())));
        }
        widgets.addSlot(output.get(0), PADDING + 54, PADDING + 26).drawBack(false);
        widgets.addSlot(new ListEmiIngredient(viableStorageComponents, viableStorageComponents.size()), PADDING + 50, PADDING).drawBack(false);

    }

    private List<EmiStack> getViableStorageComponents(CondenserOutput condenserOutput) {
        List<EmiStack> viableComponents = new ArrayList<>();
        this.addViableComponent(condenserOutput, viableComponents, AEItems.CELL_COMPONENT_1K.stack());
        this.addViableComponent(condenserOutput, viableComponents, AEItems.CELL_COMPONENT_4K.stack());
        this.addViableComponent(condenserOutput, viableComponents, AEItems.CELL_COMPONENT_16K.stack());
        this.addViableComponent(condenserOutput, viableComponents, AEItems.CELL_COMPONENT_64K.stack());
        this.addViableComponent(condenserOutput, viableComponents, AEItems.CELL_COMPONENT_256K.stack());
        return viableComponents;
    }

    private void addViableComponent(CondenserOutput condenserOutput, List<EmiStack> viableComponents,
                                    ItemStack itemStack) {
        IStorageComponent comp = (IStorageComponent) itemStack.getItem();
        int storage = comp.getBytes(itemStack) * CondenserBlockEntity.BYTE_MULTIPLIER;
        if (storage >= condenserOutput.requiredPower) {
            viableComponents.add(EmiStack.of(itemStack));
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
