package com.itszuvalex.itszulib.testing

import com.itszuvalex.itszulib.ItszuLib
import com.itszuvalex.itszulib.core.TileEntityBase
import com.itszuvalex.itszulib.core.traits.tile.TileMultiFluidTank
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids.{Fluid, FluidStack, FluidTank}

/**
 * Created by Alex on 12.10.2015.
 */
class TileTankTest extends TileEntityBase with TileMultiFluidTank {

  override def getMod: AnyRef = ItszuLib

  override def drain(from: EnumFacing, resource: FluidStack, doDrain: Boolean): FluidStack = null

  override def defaultTanks: Array[FluidTank] = Array(new FluidTank(10000), new FluidTank(5000), new FluidTank(2000))

  override def fill(from: EnumFacing, resource: FluidStack, doFill: Boolean): Int = 0

  override def drain(from: EnumFacing, maxDrain: Int, doDrain: Boolean): FluidStack = null

  override def canFill(from: EnumFacing, fluid: Fluid): Boolean = false

  override def canDrain(from: EnumFacing, fluid: Fluid): Boolean = false

  override def hasDescription: Boolean = true

  override def onSideActivate(player: EntityPlayer, side: EnumFacing): Boolean = {
    player.openGui(getMod, 0, worldObj, pos.getX, pos.getY, pos.getZ)
    true
  }

  override def serverUpdate(): Unit = {
    /*tanks(0).fill(new FluidStack(FluidRegistry.WATER, 5), true)
    tanks(1).fill(new FluidStack(FluidRegistry.LAVA, 2), true)
    setUpdateTanks()*/
    super.serverUpdate()
  }
}
