//package me.chikage.emicompat.createaddition.recipe;
//
//
//import com.mrh0.createaddition.blocks.tesla_coil.TeslaCoil;
//import com.mrh0.createaddition.index.CABlocks;
//import com.mrh0.createaddition.recipe.charging.ChargingRecipe;
//import com.simibubi.create.compat.emi.CreateEmiAnimations;
//import com.simibubi.create.compat.emi.recipes.CreateEmiRecipe;
//import com.simibubi.create.foundation.gui.AllGuiTextures;
//import dev.emi.emi.api.stack.EmiIngredient;
//import dev.emi.emi.api.stack.EmiStack;
//import dev.emi.emi.api.widget.WidgetHolder;
//import me.chikage.emicompat.createaddition.CreateAdditionPlugin;
//import net.minecraft.core.Direction;
//
//import java.util.List;
//
//
//public class EMIChargingRecipe extends CreateEmiRecipe<ChargingRecipe> {
//    public EMIChargingRecipe(ChargingRecipe recipe) {
//        super(CreateAdditionPlugin.Charging, recipe, 177, 53, c -> {
//        });
//        this.input = List.of(EmiIngredient.of(recipe.ingredient));
//        this.output = List.of(EmiStack.of(recipe.getResultItem()));
//    }
//
//    @Override
//    public void addWidgets(WidgetHolder widgets) {
//        addTexture(widgets, AllGuiTextures.JEI_DOWN_ARROW, 43, 4);
//        addTexture(widgets, AllGuiTextures.JEI_ARROW, 85, 32);
//        addTexture(widgets, AllGuiTextures.JEI_SHADOW, 32, 40);
//
//        addSlot(widgets, input.get(0), 15, 9);
//
//
//        addSlot(widgets, output.get(0), 140, 28).recipeContext(this);
//
//
//        widgets.addDrawable(48, 44, 0, 0, (matrices, mouseX, mouseY, delta) -> {
//            int scale = 22;
//
//            CreateEmiAnimations.blockElement(CABlocks.TESLA_COIL.getDefaultState().setValue(TeslaCoil.FACING, Direction.DOWN).setValue(TeslaCoil.POWERED, true))
//                    .rotateBlock(22.5, 22.5, 0)
//                    .scale(scale)
//                    .render(matrices);
//        });
//    }
//}
