package com.itszuvalex.itszucore.logistics

import java.util

import com.itszuvalex.itszucore.api.core.Loc4
import io.netty.buffer.ByteBuf

/**
 * Created by Christopher on 4/5/2015.
 */

trait INetwork[C <: INetworkNode[T], T <: INetwork[C, T]] {

  /**
   *
   * @return Network Identifier.  This should be unique.
   */
  def ID: Int

  /**
   *
   * @return Create an empty new network of this type.
   */
  def create(): T

  /**
   *
   * @param nodes Nodes to make a new network out of
   * @param edges Edges to include in the network.
   * @return Create a new network of this type from the given collection of nodes.
   */
  def create(nodes: util.Collection[C], edges: util.Map[Loc4, util.Set[Loc4]]): T

  /**
   *
   * @return All nodes in this network.
   */
  def getNodes: util.Collection[C]

  def getConnections: util.Map[Loc4, util.Set[Loc4]]

  def canConnect(a: Loc4, b: Loc4): Boolean

  def addConnection(a: Loc4, b: Loc4): Unit

  def removeConnection(a: Loc4, b: Loc4): Unit

  /**
   *
   * Adds a node to the network.  Informs the node of its being added.  Informs all other nodes that this node is added.
   *
   * @param node Node to add.
   */
  def addNode(node: C): Unit

  /**
   *
   * @param node Node to be added.
   * @return true if this node can be added to the network.
   */
  def canAddNode(node: C): Boolean

  /**
   * Removes a node from the network.  Informs the node of its being removed.  Informs all other nodes that this node is being removed.
   *
   * @param node
   */
  def removeNode(node: C): Unit

  /**
   * Removes all nodes in nodes from the network.
   * Use this method for mass-removal, for instance in chunk unloading instances, to prevent creating multiple sub-networks redundantly.
   *
   * @param nodes
   */
  def removeNodes(nodes: util.Collection[C]): Unit

  /**
   * Simply remove all nodes from the network.  Does not inform them.
   */
  def clear(): Unit

  /**
   * Orders all nodes to refresh.
   */
  def refresh(): Unit

  /**
   * Register this network with the Network Manager.  Starts tick updates.
   */
  def register(): Unit

  /**
   * Unregister this network from the Network Manager.  Stops tick updates.
   */
  def unregister(): Unit

  /**
   *
   * @return Number of nodes in the network.
   */
  def size: Int

  /**
   * Called when a tick starts.
   */
  def onTickStart(): Unit

  /**
   * Called when a tick ends.
   */
  def onTickEnd(): Unit

  /**
   *
   * Called when a node is removed from the network.  Maps all out all sub-networks created by the split, creates and registers them, and informs nodes.
   *
   * @param edges All nodes that were connected to all nodes that were removed.
   */
  def split(edges: util.Set[Loc4]): Unit

  /**
   *
   * Called when a node is added to the network.  Sets ownership of all of its nodes to this one, takes over connections.
   *
   * @param iNetwork Network that this network is taking over.
   */
  def merge[V <: T](iNetwork: V): Unit

  /**
   * Called on networks by another network, when that network is incorporating this network.
   *
   * @param iNetwork Network that is taking over this network.
   * @tparam V
   */
  def onTakeover[V <: T](iNetwork: V): Unit

  /**
   * Called on sub networks by a main network, when that network is splitting apart.
   *
   * @param iNetwork Network that will split into this sub network.
   * @tparam V
   */
  def onSplit[V <: T](iNetwork: V): Unit

  /**
   *
   * Called when the client receives a network packet update from the server network.
   *
   * @param data Data sent in a network packet.
   */
  def handlePacketData(data: ByteBuf): Unit

}
