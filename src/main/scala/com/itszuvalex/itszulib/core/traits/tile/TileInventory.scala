package com.itszuvalex.itszulib.core.traits.tile

import com.itszuvalex.itszulib.api.core.Saveable
import com.itszuvalex.itszulib.core.{BaseInventory, TileEntityBase}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing

/**
 * Created by Chris on 11/29/2014.
 */
trait TileInventory extends TileEntityBase with ISidedInventory {
  @Saveable
  val inventory = defaultInventory

  def defaultInventory: BaseInventory

  override def getSlotsForFace(side: EnumFacing) = (0 until inventory.getSizeInventory).toArray

  override def canExtractItem(slot: Int, item: ItemStack, side: EnumFacing) = true

  override def canInsertItem(slot: Int, item: ItemStack, side: EnumFacing) = true

  override def closeInventory(player: EntityPlayer) = inventory.closeInventory(player)

  override def decrStackSize(slot: Int, amount: Int) = {
    val ret = inventory.decrStackSize(slot, amount)
    markDirty()
    ret
  }

  override def getSizeInventory = inventory.getSizeInventory

  override def getInventoryStackLimit = inventory.getInventoryStackLimit

  override def isItemValidForSlot(slot: Int, item: ItemStack) = inventory.isItemValidForSlot(slot, item)

  override def getStackInSlotOnClosing(slot: Int): ItemStack = inventory.getStackInSlotOnClosing(slot)

  override def openInventory(player: EntityPlayer) = inventory.openInventory(player)

  override def setInventorySlotContents(slot: Int, item: ItemStack) = {
    inventory.setInventorySlotContents(slot, item)
    markDirty()
  }

  override def markDirty() = {
    inventory.markDirty()
    notifyNeighborsOfChange()
  }

  override def isUseableByPlayer(player: EntityPlayer) = canPlayerUse(player) && inventory.isUseableByPlayer(player)

  override def getStackInSlot(slot: Int) = inventory.getStackInSlot(slot)

  override def hasCustomName = inventory.hasCustomName

  override def getName = inventory.getName
}
