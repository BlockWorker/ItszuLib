package com.itszuvalex.itszulib.core.traits.tile

import com.itszuvalex.itszulib.api.core.{Loc4, Saveable}
import com.itszuvalex.itszulib.api.multiblock.{IMultiBlockComponent, MultiBlockInfo}
import com.itszuvalex.itszulib.core.TileEntityBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.BlockPos
import net.minecraft.world.World

/**
 * Created by Chris on 12/7/2014.
 */
trait MultiBlockComponent extends TileEntityBase with IMultiBlockComponent {
  @Saveable(desc = true) val info = new MultiBlockInfo

  def isValidMultiBlock = info.isValidMultiBlock

  def formMultiBlock(world: World, pos: BlockPos): Boolean = {
    val result = info.formMultiBlock(world, pos)
    worldObj.markBlockForUpdate(getPos)
    worldObj.notifyNeighborsOfStateChange(getPos, worldObj.getBlockState(getPos).getBlock)
    result
  }

  override def handleDescriptionNBT(compound: NBTTagCompound): Unit = {
    super.handleDescriptionNBT(compound)
    setRenderUpdate()
  }

  def breakMultiBlock(world: World, pos: BlockPos): Boolean = {
    val result = info.breakMultiBlock(world, pos)
    worldObj.markBlockForUpdate(getPos)
    worldObj.notifyNeighborsOfStateChange(getPos, worldObj.getBlockState(getPos).getBlock)
    result
  }

  def getInfo = info

  def forwardToDifferentController[B, T](f: T => B): B = {
    if (isValidMultiBlock)
      Loc4(info.getControllerPos, worldObj.provider.getDimensionId).getTileEntity(true) match {
        case Some(a) if a.isInstanceOf[T] => return f(a.asInstanceOf[T])
        case _                            =>
      }
    null.asInstanceOf[B]
  }

  def forwardToController[B](f: this.type => B): B = {
    if (isValidMultiBlock)
      Loc4(info.getControllerPos, worldObj.provider.getDimensionId).getTileEntity(true) match {
        case Some(a) if a.isInstanceOf[this.type] => return f(a.asInstanceOf[this.type])
        case _                                    =>
      }
    null.asInstanceOf[B]
  }

  def isController = info.isController(getPos)
}
