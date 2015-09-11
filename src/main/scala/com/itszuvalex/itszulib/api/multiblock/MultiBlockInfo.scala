package com.itszuvalex.itszulib.api.multiblock

import com.itszuvalex.itszulib.api.core.NBTSerializable
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.BlockPos
import net.minecraft.world.World

class MultiBlockInfo extends IMultiBlockComponent with NBTSerializable {
  private var isMultiBlock = false
  private var controller_pos = new BlockPos(0, 0, 0)

  def x = controller_pos.getX

  def y = controller_pos.getY

  def z = controller_pos.getZ

  def getControllerPos = controller_pos

  def isController(pos: BlockPos) = isValidMultiBlock && pos.getX == controller_pos.getX && pos.getY == controller_pos.getY && pos.getZ == controller_pos.getZ

  override def isValidMultiBlock = isMultiBlock

  override def formMultiBlock(world: World, pos: BlockPos): Boolean = {
    if (isMultiBlock) {
      if (controller_pos.getX != x || controller_pos.getY != y || controller_pos.getZ != z) {
        return false
      }
    }
    isMultiBlock = true
    controller_pos = new BlockPos(x, y, z)
    true
  }

  override def breakMultiBlock(world: World, pos: BlockPos): Boolean = {
    if (isMultiBlock) {
      if (controller_pos.getX != x || controller_pos.getY != y || controller_pos.getZ != z) {
        return false
      }
    }
    isMultiBlock = false
    true
  }

  override def getInfo = this

  override def saveToNBT(compound: NBTTagCompound) {
    compound.setBoolean("isFormed", isMultiBlock)
    compound.setInteger("c_x", controller_pos.getX)
    compound.setInteger("c_y", controller_pos.getY)
    compound.setInteger("c_z", controller_pos.getZ)
  }

  override def loadFromNBT(compound: NBTTagCompound) {
    isMultiBlock = compound.getBoolean("isFormed")
    controller_pos = new BlockPos(compound.getInteger("c_x"), compound.getInteger("c_y"), compound.getInteger("c_z"))
  }
}
