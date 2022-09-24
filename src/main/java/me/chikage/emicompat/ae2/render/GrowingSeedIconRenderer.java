package me.chikage.emicompat.ae2.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.Util;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class GrowingSeedIconRenderer implements EmiRenderable {
    private final List<EmiStack> stages;
    private long nextFrame;
    private int currentStage;

    public GrowingSeedIconRenderer(List<ItemStack> stages) {
        this.stages = stages.stream().map(EmiStack::of).toList();
    }

    @Override
    public void render(PoseStack matrices, int x, int y, float delta) {
        var now = Util.getMillis();
        if (now > nextFrame + 2000) {
            currentStage++;
            nextFrame = now;
        }

        if (currentStage >= stages.size()) {
            currentStage = 0;
        }

        stages.get(currentStage).render(matrices, x, y, delta);
    }
}
