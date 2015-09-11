package com.itszuvalex.itszulib.core.traits.block

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.{BlockPos, MathHelper}
import net.minecraft.world.World

/**
 * Created by Itszuvalex on 11/28/14.
 */
trait RotateOnPlace extends Block {
  /**
   * Called whenever the block is added into the world. Args: world, x, y, z
   */
  override def onBlockAdded(world: World, pos: BlockPos, state: IBlockState) {
    super.onBlockAdded(world, pos, state)
    setDefaultDirection(world, pos)
  }


  /**
   * set a blocks direction
   */
  private def setDefaultDirection(world: World, pos: BlockPos) {
    if (!world.isRemote) {
      val south = world.getBlockState(pos.offsetSouth()).getBlock
      val north = world.getBlockState(pos.offsetNorth()).getBlock
      val west = world.getBlockState(pos.offsetWest()).getBlock
      val east = world.getBlockState(pos.offsetEast()).getBlock
      var metadata: Byte = 3
      if (south.isOpaqueCube && !north.isOpaqueCube) {
        metadata = 3
      }
      if (north.isOpaqueCube && !south.isOpaqueCube) {
        metadata = 2
      }
      if (west.isOpaqueCube && !east.isOpaqueCube) {
        metadata = 5
      }
      if (east.isOpaqueCube && !west.isOpaqueCube) {
        metadata = 4
      }
      world.setBlockState(pos, world.getBlockState(pos).getBlock.getStateFromMeta(metadata), 2)
    }
  }

  /**
   * Called when the block is placed in the world.
   */
  override def onBlockPlacedBy(world: World, pos: BlockPos, state: IBlockState, entity: EntityLivingBase, itemStack: ItemStack) {
    super.onBlockPlacedBy(world, pos, state, entity, itemStack)
    val mask = MathHelper.floor_double((entity.rotationYaw * 4.0F / 360.0F).toDouble + 0.5D) & 3
    if (mask == 0) {
      world.setBlockState(pos, state.getBlock.getStateFromMeta(2), 2)
    }
    if (mask == 1) {
      world.setBlockState(pos, state.getBlock.getStateFromMeta(5), 2)
    }
    if (mask == 2) {
      world.setBlockState(pos, state.getBlock.getStateFromMeta(3), 2)
    }
    if (mask == 3) {
      world.setBlockState(pos, state.getBlock.getStateFromMeta(4), 2)
    }
  }
}
