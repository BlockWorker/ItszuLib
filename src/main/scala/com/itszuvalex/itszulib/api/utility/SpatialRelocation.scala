package com.itszuvalex.itszulib.api.utility

import com.itszuvalex.itszulib.api.events.EventSpatialRelocation
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.{World, WorldServer}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.BlockSnapshot
import net.minecraftforge.event.world.BlockEvent.{BreakEvent, PlaceEvent}

/**
 * Created by Chris on 12/30/2014.
 */
object SpatialRelocation {
  val shiftElseRemake = false

  def shiftBlock(world: World, pos: BlockPos, direction: EnumFacing, player: EntityPlayer): Unit = {
    val newPos = new BlockPos(pos.getX + direction.getDirectionVec.getX, pos.getY + direction.getDirectionVec.getY, pos.getZ + direction.getDirectionVec.getZ)
    moveBlock(world, pos, world, newPos, false, player)
  }

  def moveBlock(world: World, pos: BlockPos, destWorld: World, destPos: BlockPos,
                replace: Boolean = false, player: EntityPlayer): Unit = {
    if (!replace && !destWorld.isAirBlock(destPos)) return
    applySnapshot(extractBlock(world, pos, player), destWorld, destPos, player)
  }

  def extractBlock(world: World, pos: BlockPos, player: EntityPlayer): TileSave = {
    world match {
      case world1: WorldServer =>
        if (MinecraftForge
            .EVENT_BUS
            .post(new BreakEvent(world,
                                 pos,
                                 world.getBlockState(pos),
                                 player))) {
          return null
        }
      case _ =>
    }
    if (MinecraftForge.EVENT_BUS.post(new EventSpatialRelocation.Pickup(world, pos))) return null
    val tileEntity = world.getTileEntity(pos)
    val blockState = world.getBlockState(pos)
    val snapshot = new TileSave(world, pos, blockState, tileEntity)
    //    world.setBlockToAir(x, y, z)
    world.removeTileEntity(pos)
    //    TileContainer.shouldDrop = false
    world.setBlockState(pos, Blocks.air.getDefaultState, 2)
    //    TileContainer.shouldDrop = true
    snapshot
  }

  def applySnapshot(s: TileSave, player: EntityPlayer): Unit = applySnapshot(s, s.world, s.pos, player): Boolean

  def applySnapshot(s: TileSave, destWorld: World, destPos: BlockPos, player: EntityPlayer): Boolean = {
    if (s == null) return false
    if (s.block == null) return false
    if (!s.block.canPlaceBlockAt(destWorld, destPos)) return false
    destWorld match {
      case world1: WorldServer =>
        if (MinecraftForge
            .EVENT_BUS
            .post(new PlaceEvent(new BlockSnapshot(destWorld,
                                                   destPos,
                                                   s.state),
                                destWorld.getBlockState(destPos.offsetDown()),
                                player))) {
          return false
        }
      case _ =>
    }
    if (MinecraftForge.EVENT_BUS.post(new EventSpatialRelocation.Placement(destWorld, destPos, s.block))) {
      return false
    }
    destWorld.setBlockState(destPos, s.state, 3)
    if (s.te != null) {
      if (s.pos.getX != destPos.getX) s.te.setInteger("x", destPos.getX)
      if (s.pos.getY != destPos.getY) s.te.setInteger("y", destPos.getY)
      if (s.pos.getZ != destPos.getZ) s.te.setInteger("z", destPos.getZ)
      val newTile = if (s.world == destWorld) {
        TileEntity.createAndLoadEntity(s.te)
      } else {
        val tile = s.block.createTileEntity(destWorld, s.state)
        tile.readFromNBT(s.te)
        tile
      }
      destWorld.setTileEntity(destPos, newTile)
    }
    s.block.onBlockAdded(destWorld, destPos, s.state)
    true
  }
}
