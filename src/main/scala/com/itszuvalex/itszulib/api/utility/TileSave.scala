package com.itszuvalex.itszulib.api.utility

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.fml.common.registry.GameRegistry

object TileSave {
  def apply(nBTTagCompound: NBTTagCompound) = loadFromNBT(nBTTagCompound)


  def loadFromNBT(compound: NBTTagCompound): TileSave = {
    new TileSave(compound.getInteger("dimension"),
                 new BlockPos(compound.getInteger("posX"),
                              compound.getInteger("posY"),
                              compound.getInteger("posZ")),
                 compound.getString("modID"),
                 compound.getString("blockID"),
                 compound.getInteger("meta"),
                 if (compound.hasKey("nbt")) compound.getCompoundTag("nbt") else null)
  }
}

class TileSave(private var _dimensionID: Int, var pos: BlockPos, var modID: String,
               var blockID: String, var metadata: Int, var te: NBTTagCompound) /*extends ISaveable*/ {
  lazy val block = GameRegistry.findBlock(modID, blockID)
  lazy val state = block.getStateFromMeta(metadata)

  def world = DimensionManager.getWorld(dimensionID)

  def world_=(world: World) = _dimensionID = world.provider.getDimensionId

  def this(dimensionID: Int, pos: BlockPos, block: Block, metadata: Int, te: NBTTagCompound) =
    this(dimensionID,
         pos,
         GameRegistry.findUniqueIdentifierFor(block).modId,
         GameRegistry.findUniqueIdentifierFor(block).name,
         metadata,
         te)

  def this(world: World, pos: BlockPos, block: Block, metadata: Int, te: NBTTagCompound) =
    this(world.provider.getDimensionId,
         pos,
         GameRegistry.findUniqueIdentifierFor(block).modId,
         GameRegistry.findUniqueIdentifierFor(block).name,
         metadata,
         te)

  def this(dimensionID: Int, pos: BlockPos, block: Block, metadata: Int, te: TileEntity) =
    this(dimensionID, pos, block, metadata, if (te != null) {
      val nbt = new NBTTagCompound
      te.writeToNBT(nbt)
      nbt
    } else {
      null
    })

  def this(world: World, pos: BlockPos, block: Block, metadata: Int, te: TileEntity) =
    this(world, pos, block, metadata, if (te != null) {
      val nbt = new NBTTagCompound
      te.writeToNBT(nbt)
      nbt
    } else {
      null
    })

  def this(dimensionID: Int, pos: BlockPos, state: IBlockState, te: TileEntity) =
    this(dimensionID, pos, state.getBlock, state.getBlock.getMetaFromState(state), te)

  def this(world: World, pos: BlockPos, state: IBlockState, te: TileEntity) =
    this(world, pos, state.getBlock, state.getBlock.getMetaFromState(state), te)

  def this(world: World, pos: BlockPos) =
    this(world, pos, world.getBlockState(pos), world.getTileEntity(pos))

  def saveToNBT(compound: NBTTagCompound): Unit = {
    compound.setInteger("dimension", dimensionID)
    compound.setInteger("posX", pos.getX)
    compound.setInteger("posY", pos.getY)
    compound.setInteger("posZ", pos.getZ)
    compound.setString("modID", modID)
    compound.setString("blockID", blockID)
    compound.setInteger("meta", metadata)
    if (te != null) compound.setTag("nbt", te)
  }

  def dimensionID = _dimensionID

  def dimensionID_=(dim: Int) = _dimensionID = dim

  /*
    override def loadFromNBT(compound: NBTTagCompound): Unit =  {
      _dimensionID = compound.getInteger("dimension")
      x = compound.getInteger("posX")
      y = compound.getInteger("posY")
      z = compound.getInteger("posZ")
      modID = compound.getString("modID")
      blockID = compound.getString("blockID")
      metadata = compound.getInteger("meta")
      te = if (compound.hasKey("nbt")) compound.getCompoundTag("nbt") else null
    }*/
}
