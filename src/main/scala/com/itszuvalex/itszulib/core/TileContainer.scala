package com.itszuvalex.itszulib.core

import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.{EnumFacing, BlockPos}
import net.minecraft.world.World
;

abstract class TileContainer(material: Material) extends BlockContainer(material) {
  setHardness(3f)
  setResistance(3f)

  override def onBlockActivated(par1World: World, par2: BlockPos, par3: IBlockState, par5EntityPlayer: EntityPlayer,
                                par6: EnumFacing, par7: Float, par8: Float, par9: Float): Boolean = {
    par1World.getTileEntity(par2) match {
      case base: TileEntityBase =>
        if (base.canPlayerUse(par5EntityPlayer)) {
          return base.onSideActivate(par5EntityPlayer, par6)
        }
      case _ =>
    }
    super.onBlockActivated(par1World, par2, par3, par5EntityPlayer, par6, par7, par8, par9)
  }
}
