package com.itszuvalex.itszulib

import com.itszuvalex.itszulib.network.PacketHandler
import com.itszuvalex.itszulib.proxy.ProxyCommon
import com.itszuvalex.itszulib.testing.{BlockLocTrackerTest, BlockPortalTest, ItemPreviewable}
import net.minecraft.creativetab.CreativeTabs
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInterModComms, FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.common.{SidedProxy, Mod}
import org.apache.logging.log4j.LogManager

/**
 * Created by Christopher on 4/5/2015.
 */
@Mod(modid = ItszuLib.ID, name = ItszuLib.ID, version = ItszuLib.VERSION, modLanguage = "scala")
object ItszuLib {
  final val ID      = "ItszuLib"
  final val VERSION = Version.FULL_VERSION
  final val logger  = LogManager.getLogger(ID)

  @SidedProxy(clientSide = "com.itszuvalex.itszulib.proxy.ProxyClient",
              serverSide = "com.itszuvalex.itszulib.proxy.ProxyServer")
  var proxy: ProxyCommon = null

  @EventHandler def preInit(event: FMLPreInitializationEvent): Unit = {
    PacketHandler.init()
    //    PlayerUUIDTracker.init()
    //    PlayerUUIDTracker.setFile(new File())
    proxy.init()
    NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy)
  }

  @EventHandler def load(event: FMLInitializationEvent): Unit = {
    GameRegistry.registerBlock(new BlockPortalTest, "BlockPortalTest").setCreativeTab(CreativeTabs.tabBlock)
    GameRegistry.registerBlock(new BlockLocTrackerTest, "BlockLocTrackerTest").setCreativeTab(CreativeTabs.tabBlock)
    GameRegistry.registerBlock(new BlockTankTest, "BlockTankTest").setCreativeTab(CreativeTabs.tabBlock)
    val prev = new ItemPreviewable
    prev.setCreativeTab(CreativeTabs.tabDecorations)
    GameRegistry.registerItem(prev, "TilePreviewable")
  }

  @EventHandler def postInit(event: FMLPostInitializationEvent): Unit = {

  }

  @EventHandler def imcCallback(event: FMLInterModComms.IMCEvent) {
    InterModComms.imcCallback(event)
  }

}
