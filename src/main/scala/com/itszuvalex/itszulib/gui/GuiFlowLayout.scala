package com.itszuvalex.itszulib.gui

/**
 * Created by Christopher Harris (Itszuvalex) on 9/4/15.
 */
object GuiFlowLayout {

  object FlowDirection extends Enumeration {
    type FlowDirection = Value
    val Vertical, Horizontal = Value
  }

  /**
   * Reserved for later refinement.
   */
  object FlowVertical extends Enumeration {
    type FlowVertical = Value
    val Top, Center, Bottom = Value
  }

  /**
   * Reserved for later refinement.
   *
   */
  object FlowHorizontal extends Enumeration {
    type FlowHorizontal = Value
    val Left, Center, Right = Value
  }

}

class GuiFlowLayout(override var anchorX: Int,
                    override var anchorY: Int,
                    override var panelWidth: Int,
                    override var panelHeight: Int,
                    elements: GuiElement*) extends GuiPanel {
  add(elements: _*)

  var flowVertical     = GuiFlowLayout.FlowVertical.Top
  var flowHorizontal   = GuiFlowLayout.FlowHorizontal.Left
  var primaryFlow      = GuiFlowLayout.FlowDirection.Horizontal
  var bufferVertical   = 0
  var bufferHorizontal = 0

  private var startIndex = 0

  def numElements = elements.size

  def startingIndex = startIndex // elements.firstIndexWhere(_.shouldRender)

  def endingIndex = elements.lastIndexWhere(_.shouldRender)

  def pageForward(num: Int = 1) = {
    if ((startIndex < elements.size - 1) && !elements.last.shouldRender) {
      (1 to num).exists { i =>
        startIndex += 1
        layoutElements()
        elements.last.shouldRender
                        }
    }
  }

  def pageBackward(num: Int = 1) = {
    if (startIndex > 0) {
      startIndex -= num
      startIndex = Math.max(startIndex, 0)
    }
  }

  override def renderUpdate(screenX: Int, screenY: Int, mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
    layoutElements()
    //    GL11.glScissor(screenX, screenY, panelWidth, panelHeight)
    //    GL11.glEnable(GL11.GL_SCISSOR_TEST)
    super.renderUpdate(screenX, screenY, mouseX, mouseY, partialTicks)
    //    GL11.glDisable(GL11.GL_SCISSOR_TEST)
  }

  def layoutElements(): Unit = {
    primaryFlow match {
      case GuiFlowLayout.FlowDirection.Horizontal =>
        var nextX = bufferHorizontal
        var nextY = bufferVertical
        var rowHeight = 0
        elements.zipWithIndex.foreach { case (e: GuiElement, i: Int) =>
          if (i < startIndex) {
            e.anchorX = panelWidth
            e.anchorY = panelHeight
            e.setShouldRender(false)
          } else {
            e.setShouldRender(true)
            (nextX, nextY) match {
              case (_, y) if y >= panelHeight                                                                    =>
                nextX = panelWidth
                nextY = panelHeight
                e.setShouldRender(false)
              case (x, _) if !(x == bufferHorizontal) && (x + e.spaceHorizontal + bufferHorizontal) > panelWidth =>
                nextX = bufferHorizontal
                nextY += Math.max(rowHeight, e.spaceVertical) + bufferVertical
                if (nextY >= panelHeight) {
                  nextY = panelHeight
                  nextX = panelWidth
                  e.setShouldRender(false)
                }
                rowHeight = 0
              case _                                                                                             =>
            }
            e.anchorX = nextX
            e.anchorY = nextY
            nextX = nextX + e.spaceHorizontal + bufferHorizontal
            rowHeight = Math.max(rowHeight, e.spaceVertical)
          }
                                      }
      case GuiFlowLayout.FlowDirection.Vertical   =>
        var nextX = bufferHorizontal
        var nextY = bufferVertical
        var colWidth = 0
        elements.zipWithIndex.foreach { case (e: GuiElement, i: Int) =>
          if (i < startIndex) {
            e.anchorX = panelWidth
            e.anchorY = panelHeight
            e.setShouldRender(false)
          } else {
            e.setShouldRender(true)
            (nextX, nextY) match {
              case (x, _) if x >= panelWidth                                                                =>
                nextX = panelWidth
                nextY = panelHeight
                e.setShouldRender(false)
              case (_, y) if !(y == bufferVertical) && (y + e.spaceVertical + bufferVertical) > panelHeight =>
                nextX += Math.max(colWidth, e.spaceHorizontal) + bufferHorizontal
                nextY = bufferVertical
                if (nextX >= panelWidth) {
                  nextY = panelHeight
                  nextX = panelWidth
                  e.setShouldRender(false)
                }
                colWidth = 0
              case _                                                                                        =>
            }
            e.anchorX = nextX
            e.anchorY = nextY
            nextY = nextY + e.spaceVertical + bufferVertical
            colWidth = Math.max(colWidth, e.spaceHorizontal)
          }
                                      }
      case _                                      =>
    }
  }

}
