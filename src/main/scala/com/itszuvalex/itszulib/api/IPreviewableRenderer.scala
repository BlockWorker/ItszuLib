package com.itszuvalex.itszulib.api

import net.minecraft.item.ItemStack
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{SideOnly, Side}

/**
  * Created by Christopher Harris (Itszuvalex) on 8/26/15.
  */
@SideOnly(Side.CLIENT)
trait IPreviewableRenderer {

   /**
    * Coordinates are the location to render at.  This is usually the facing off-set location that, if the player right-clicked, a block would be placed at.
    *
    * @param stack ItemStack of IPreviewable Item
    * @param world World
    * @param pos BlockPos
    * @param rx X Render location
    * @param ry Y Render location
    * @param rz Z Render location
    */
   def renderAtLocation(stack: ItemStack, world: World, pos: BlockPos,
                        rx: Double, ry: Double, rz: Double): Unit

 }
