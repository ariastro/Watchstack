@file:OptIn(InternalResourceApi::class)

package myanimetracker.shared.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceContentHash
import org.jetbrains.compose.resources.ResourceItem

private const val MD: String = "composeResources/myanimetracker.shared.generated.resources/"

@delegate:ResourceContentHash(1_364_818_641)
internal val Res.font.inter_medium: FontResource by lazy {
      FontResource("font:inter_medium", setOf(
        ResourceItem(setOf(), "${MD}font/inter_medium.ttf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(89_588_670)
internal val Res.font.inter_regular: FontResource by lazy {
      FontResource("font:inter_regular", setOf(
        ResourceItem(setOf(), "${MD}font/inter_regular.ttf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(374_067_956)
internal val Res.font.inter_semibold: FontResource by lazy {
      FontResource("font:inter_semibold", setOf(
        ResourceItem(setOf(), "${MD}font/inter_semibold.ttf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(1_095_133_912)
internal val Res.font.poppins_black: FontResource by lazy {
      FontResource("font:poppins_black", setOf(
        ResourceItem(setOf(), "${MD}font/poppins_black.ttf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-317_912_413)
internal val Res.font.poppins_bold: FontResource by lazy {
      FontResource("font:poppins_bold", setOf(
        ResourceItem(setOf(), "${MD}font/poppins_bold.ttf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-1_168_950_066)
internal val Res.font.poppins_extrabold: FontResource by lazy {
      FontResource("font:poppins_extrabold", setOf(
        ResourceItem(setOf(), "${MD}font/poppins_extrabold.ttf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-1_933_769_947)
internal val Res.font.poppins_medium: FontResource by lazy {
      FontResource("font:poppins_medium", setOf(
        ResourceItem(setOf(), "${MD}font/poppins_medium.ttf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(992_584_762)
internal val Res.font.poppins_regular: FontResource by lazy {
      FontResource("font:poppins_regular", setOf(
        ResourceItem(setOf(), "${MD}font/poppins_regular.ttf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(429_221_087)
internal val Res.font.poppins_semibold: FontResource by lazy {
      FontResource("font:poppins_semibold", setOf(
        ResourceItem(setOf(), "${MD}font/poppins_semibold.ttf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-113_726_598)
internal val Res.font.space_mono_bold: FontResource by lazy {
      FontResource("font:space_mono_bold", setOf(
        ResourceItem(setOf(), "${MD}font/space_mono_bold.ttf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-1_849_707_496)
internal val Res.font.space_mono_regular: FontResource by lazy {
      FontResource("font:space_mono_regular", setOf(
        ResourceItem(setOf(), "${MD}font/space_mono_regular.ttf", -1, -1),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainFont0Resources(map: MutableMap<String, FontResource>) {
  map.put("inter_medium", Res.font.inter_medium)
  map.put("inter_regular", Res.font.inter_regular)
  map.put("inter_semibold", Res.font.inter_semibold)
  map.put("poppins_black", Res.font.poppins_black)
  map.put("poppins_bold", Res.font.poppins_bold)
  map.put("poppins_extrabold", Res.font.poppins_extrabold)
  map.put("poppins_medium", Res.font.poppins_medium)
  map.put("poppins_regular", Res.font.poppins_regular)
  map.put("poppins_semibold", Res.font.poppins_semibold)
  map.put("space_mono_bold", Res.font.space_mono_bold)
  map.put("space_mono_regular", Res.font.space_mono_regular)
}
