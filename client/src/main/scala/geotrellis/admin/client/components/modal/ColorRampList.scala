package geotrellis.admin.client.components.modal

import scala.scalajs.js

import diode.react._
import geotrellis.admin.client.circuit._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import scalacss.Defaults._
import scalacss.ScalaCssReact._


object ColorRampList {

  val colorRamps = Map[String, Array[String]](
    "blue-to-orange" -> Array(
      "#2586AB", "#4EA3C8", "#7FB8D4", "#ADD8EA",
      "#C8E1E7", "#EDECEA", "#F0E7BB", "#F5CF7D",
      "#F9B737", "#E68F2D", "#D76B27"
    ),
    "blue-to-red" -> Array(
      "#2791C3", "#5DA1CA", "#83B2D1", "#A8C5D8",
      "#CCDBE0", "#E9D3C1", "#DCAD92", "#D08B6C",
      "#C66E4B", "#BD4E2E"
    ),
    "blue-to-yellow-to-red-heatmap" -> Array(
      "#2A2E7F", "#3D5AA9", "#4698D3", "#39C6F0",
      "#76C9B3", "#A8D050", "#F6EB14", "#FCB017",
      "#F16022", "#EE2C24", "#7D1416"
    ),
    "bold-land-use-qualitative" -> Array(
      "#B29CC3", "#4F8EBB", "#8F9238", "#C18437",
      "#B5D6B1", "#D378A6", "#D4563C", "#F9BE47"
    ),
    "dark-red-to-yellow-heatmap" -> Array(
      "#68101A", "#7F182A", "#A33936", "#CF3A27",
      "#D54927", "#E77124", "#ECBE1D", "#F7DA22",
      "#F6EDB1", "#FFFFFF"
    ),
    "green-to-orange" -> Array(
      "#118C8C", "#429D91", "#61AF96", "#75C59B",
      "#A2CF9F", "#C5DAA3", "#E6E5A7", "#E3D28F",
      "#E0C078", "#DDAD62", "#D29953", "#CA8746",
      "#C2773B"
    ),
    "green-to-red-orange" -> Array(
      "#569543", "#9EBD4D", "#BBCA7A", "#D9E2B2",
      "#E4E7C4", "#E6D6BE", "#E3C193", "#DFAC6C",
      "#DB9842", "#B96230"
    ),
    "light-to-dark-green" -> Array(
      "#E8EDDB", "#DCE8D4", "#BEDBAD", "#A0CF88",
      "#81C561", "#4BAF48", "#1CA049", "#3A6D35"
    ),
    "light-to-dark-sunset" -> Array(
      "#FFFFFF", "#FBEDD1", "#F7E0A9", "#EFD299",
      "#E8C58B", "#E0B97E", "#F2924D", "#C97877",
      "#946196", "#2AB7D6", "#474040"
    ),
    "muted-terrain-qualitative" -> Array(
      "#CEE1E8", "#7CBCB5", "#82B36D", "#94C279",
      "#D1DE8D", "#EDECC3", "#CCAFB4", "#C99884FF"
    ),
    "purple-to-dark-purple-to-white-heatmap" -> Array(
      "#A52278", "#993086", "#8C3C97", "#6D328A",
      "#4E2B81", "#3B264B", "#180B11", "#FFFFFF"
    ),
    "yellow-to-red-heatmap" -> Array(
      "#F7DA22", "#ECBE1D", "#E77124", "#D54927",
      "#CF3A27", "#A33936", "#7F182A", "#68101A"
    )
  )

  object Style extends StyleSheet.Inline {
    import dsl._

    val paletteContainer = style(
      width(100 %%),
      verticalAlign.middle
    )

    val colorPaletteList = style(
      listStyleType := "none",
      padding(0 px),
      width(100 %%),
      columnWidth(260 px)
    )

    val colorPalette = style(
      opacity(0.4),
      height(2 em),
      width(45 %%),
      margin(2 px),
      display.inlineBlock,
      border(thin, solid, grey),
      &.hover(
        opacity(1),
        boxShadow := "10px, black",
        cursor.pointer,
        border(thin, solid, black)
      )
    )

    val selectedColorPalette = style(
      opacity(1).important,
      border(thin, solid, black).important
    )

    val unselectedColorPalette = style()

    val color = style(
      height(100 %%),
      display.inlineBlock
    )

  }

  val colorRampSelect = ReactComponentB[ModelProxy[ColorModel]]("ColorRampSelect").render_P({ proxy =>
    val selected: String = proxy().ramp.getOrElse("blue-to-orange")

    <.div(
      Style.paletteContainer,
      <.h3("Color Ramp"),
      <.ul(
        Style.colorPaletteList,
        ^.className := "color-list",
        colorRamps.map { case (name, colors) =>
          <.li(
            if (name == selected) Style.selectedColorPalette else Style.unselectedColorPalette,
            Style.colorPalette,
            ^.key := name,
            ^.onClick --> proxy.dispatch(SelectColorRamp(Some(name))),
            colors.map { color =>
              <.div(
                ^.key := color,
                Style.color,
                ^.style := js.Dictionary("width" -> s"${100.0 / colors.length}%", "backgroundColor" -> color)
              )
            }
          )
        }
      )
    )
  }).componentDidMount(scope => onMount(scope.props)).build

  /* Mounting happens every time the Settings window is summoned. To avoid
   * extraneously resetting the selected Color Ramp to what it already was
   * within the Diode model, we use `Callback.empty` here.
   *
   * We _do_ need `onMount` behaviour here, or else the first Ramp will never
   * be selected if the user doesn't click one themselves.
   */
  def onMount(proxy: ModelProxy[ColorModel]) = proxy().ramp match {
    case None => proxy.dispatch(SelectColorRamp(Some("blue-to-orange")))
    case Some(_) => Callback.empty
  }

}
