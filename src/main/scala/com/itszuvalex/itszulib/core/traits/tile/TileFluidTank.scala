package com.itszuvalex.itszulib.core.traits.tile

import com.itszuvalex.itszulib.api.core.Saveable
import com.itszuvalex.itszulib.core.TileEntityBase
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids._

/**
 * Created by Chris on 11/30/2014.
 */
trait TileFluidTank extends TileEntityBase with IFluidHandler {
  @Saveable val tank = defaultTank

  def defaultTank: IFluidTank

  override def fill(from: EnumFacing, resource: FluidStack, doFill: Boolean) = tank.fill(resource, doFill)

  override def drain(from: EnumFacing, resource: FluidStack, doDrain: Boolean): FluidStack = {
    if (resource == null || !resource.isFluidEqual(tank.getFluid)) null
    else tank.drain(resource.amount, doDrain)
  }

  override def drain(from: EnumFacing, maxDrain: Int, doDrain: Boolean) = tank.drain(maxDrain, doDrain)

  override def getTankInfo(from: EnumFacing): Array[FluidTankInfo] = Array(tank.getInfo)
}
