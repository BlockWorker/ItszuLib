package com.itszuvalex.itszulib.core.traits.block

import java.util.Random

import com.itszuvalex.itszulib.util.InventoryUtils
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.inventory.IInventory
import net.minecraft.util.BlockPos
import net.minecraft.world.World

/**
 * Created by Itszuvalex on 11/28/14.
 */
trait DroppableInventory extends Block {
  var shouldDrop = true

  override def breakBlock(world: World, pos: BlockPos, state: IBlockState): Unit = {
    if (shouldDrop) {
      world.getTileEntity(pos) match {
        case ti: IInventory =>
          val random = new Random
          (0 until ti.getSizeInventory).map(ti.getStackInSlotOnClosing).foreach(InventoryUtils.dropItem(_, world, pos, random))
          world.notifyNeighborsOfStateChange(pos, state.getBlock)
        case _ =>
      }
    }
    super.breakBlock(world, pos, state)
  }
}
