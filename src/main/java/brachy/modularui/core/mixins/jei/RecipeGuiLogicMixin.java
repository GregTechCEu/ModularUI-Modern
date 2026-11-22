package brachy.modularui.core.mixins.jei;

import brachy.modularui.integration.jei.recipe.ModularUIJeiCategory;

import net.minecraft.client.renderer.Rect2i;

import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.common.util.ImmutableSize2i;
import mezz.jei.gui.recipes.IRecipeLayoutWithButtons;
import mezz.jei.gui.recipes.RecipeGuiLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;

@Mixin(value = RecipeGuiLogic.class, remap = false)
public class RecipeGuiLogicMixin {

    @ModifyArg(method = "getVisibleRecipeLayoutsWithButtons",
               at = @At(value = "INVOKE",
                        target = "Ljava/util/Optional;map(Ljava/util/function/Function;)Ljava/util/Optional;"))
    private Function<IRecipeLayoutWithButtons<?>, ImmutableSize2i> modularui$useCategoryMaxHeightForLayout(Function<IRecipeLayoutWithButtons<?>, ImmutableSize2i> original) {
        return layoutWithButtons -> {
            ImmutableSize2i size = original.apply(layoutWithButtons);
            IRecipeLayoutDrawable<?> recipeLayout = layoutWithButtons.getRecipeLayout();
            if (recipeLayout.getRecipeCategory() instanceof ModularUIJeiCategory<?> muiCategory) {
                Rect2i area = recipeLayout.getRect();
                // size.width = totalWidth = area.width + 2*padding + buttonsWidth
                int borderWidth = size.width() - area.getWidth();
                // size.height() contains the layout's areaWithBorder.height
                int borderHeight = size.height() - area.getHeight();
                return new ImmutableSize2i(muiCategory.getMaxWidth() + borderWidth,
                        muiCategory.getMaxHeight() + borderHeight);
            }
            return size;
        };
    }
}
