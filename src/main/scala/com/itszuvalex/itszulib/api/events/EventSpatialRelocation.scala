package com.itszuvalex.itszulib.api.events

import net.minecraft.block.Block
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.eventhandler.{Event, Cancelable}

import scala.beans.BeanProperty

/**
 * Created by Itszuvalex on 1/1/15.
 */
object EventSpatialRelocation {

  /**
   * Posted when a block or item using the SpatialRelocation format will be picked up from the world.
   *
   * @param world
   * @param pos
   */
  @Cancelable
  class Pickup(world: World, pos: BlockPos) extends EventSpatialRelocation(world, pos)
  /**
   * Posted when a block or item using the SpatialRelocation format will place the given block at the given world coordinates.
   *
   * @param world
   * @param pos
   * @param block
   */
  @Cancelable
  class Placement(world: World, pos: BlockPos, @BeanProperty val block: Block)
    extends EventSpatialRelocation(world, pos)

}

/**
 * Base class for all SpatialRelocation events.
 *
 * @param world
 * @param pos
 */
@Cancelable abstract class EventSpatialRelocation(@BeanProperty val world: World, @BeanProperty val pos: BlockPos) extends Event
