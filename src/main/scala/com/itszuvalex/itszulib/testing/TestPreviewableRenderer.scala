package com.itszuvalex.itszulib.testing

import com.itszuvalex.itszulib.api.IPreviewableRenderer
import com.itszuvalex.itszulib.render.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.{TextureMap, TextureAtlasSprite}
import net.minecraft.util.{ResourceLocation, BlockPos}

import net.minecraft.client.renderer.Tessellator
import net.minecraft.item.ItemStack
import net.minecraft.world.World

/**
  * Created by Christopher Harris (Itszuvalex) on 8/26/15.
  */
class TestPreviewableRenderer extends IPreviewableRenderer {
   /**
    * Coordinates are the location to render at.  This is usually the facing off-set location that, if the player right-clicked, a block would be placed at.
    *
    * @param stack ItemStack of IPreviewable Item
    * @param world World
    * @param pos Location
    */
   override def renderAtLocation(stack: ItemStack, world: World, pos: BlockPos,
                                 rx: Double, ry: Double, rz: Double): Unit = {
     Tessellator.getInstance().getWorldRenderer.startDrawingQuads()
     RenderUtils.renderCube(rx.toFloat, ry.toFloat, rz.toFloat, 0, 0, 0, 1, 1, 1, Minecraft.getMinecraft.getTextureMapBlocks.getAtlasSprite("minecraft:diamond_ore"))
     Tessellator.getInstance().getWorldRenderer.draw()
   }
 }