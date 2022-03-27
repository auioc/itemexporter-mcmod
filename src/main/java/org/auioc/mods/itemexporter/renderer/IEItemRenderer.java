package org.auioc.mods.itemexporter.renderer;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class IEItemRenderer {

    public static NativeImage render(Item item, int imageSize) {
        var itemStack = new ItemStack(item);

        var fbo = new TextureTarget(imageSize, imageSize, true, false);
        fbo.bindWrite(true);

        Matrix4f backup = RenderSystem.getProjectionMatrix().copy();
        Matrix4f projection = Matrix4f.orthographic(0.0F, 16.0F, 0.0F, 16.0F, -150.0F, 150.0F);
        Matrix4f modelview = Matrix4f.createTranslateMatrix(1.0E-4F, 1.0E-4F, 0.0F);
        projection.multiply(modelview);
        RenderSystem.setProjectionMatrix(projection);
        Minecraft.getInstance().getItemRenderer().renderGuiItem(itemStack, 0, 0);
        RenderSystem.setProjectionMatrix(backup);

        var image = new NativeImage(fbo.width, fbo.height, true);
        fbo.bindRead();
        image.downloadTexture(0, false);
        image.flipY();

        fbo.unbindWrite();

        return image;
    }

}
