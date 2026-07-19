@file:OptIn(InternalResourceApi::class)

package myanimetracker.shared.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceContentHash
import org.jetbrains.compose.resources.ResourceItem

private const val MD: String = "composeResources/myanimetracker.shared.generated.resources/"

@delegate:ResourceContentHash(1_923_597_512)
internal val Res.drawable.ic_arrow_back: DrawableResource by lazy {
      DrawableResource("drawable:ic_arrow_back", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_arrow_back.xml", -1, -1),
      ))
    }

@delegate:ResourceContentHash(1_501_075_269)
internal val Res.drawable.ic_bookmark: DrawableResource by lazy {
      DrawableResource("drawable:ic_bookmark", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_bookmark.xml", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-1_473_141_166)
internal val Res.drawable.ic_chevron_right: DrawableResource by lazy {
      DrawableResource("drawable:ic_chevron_right", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_chevron_right.xml", -1, -1),
      ))
    }

@delegate:ResourceContentHash(839_128_939)
internal val Res.drawable.ic_close: DrawableResource by lazy {
      DrawableResource("drawable:ic_close", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_close.xml", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-831_933_995)
internal val Res.drawable.ic_error: DrawableResource by lazy {
      DrawableResource("drawable:ic_error", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_error.xml", -1, -1),
      ))
    }

@delegate:ResourceContentHash(1_108_533_419)
internal val Res.drawable.ic_home: DrawableResource by lazy {
      DrawableResource("drawable:ic_home", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_home.xml", -1, -1),
      ))
    }

@delegate:ResourceContentHash(1_683_416_009)
internal val Res.drawable.ic_search: DrawableResource by lazy {
      DrawableResource("drawable:ic_search", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_search.xml", -1, -1),
      ))
    }

@delegate:ResourceContentHash(308_904_567)
internal val Res.drawable.ic_search_off: DrawableResource by lazy {
      DrawableResource("drawable:ic_search_off", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_search_off.xml", -1, -1),
      ))
    }

@delegate:ResourceContentHash(1_947_291_047)
internal val Res.drawable.ic_star: DrawableResource by lazy {
      DrawableResource("drawable:ic_star", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_star.xml", -1, -1),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainDrawable0Resources(map: MutableMap<String, DrawableResource>) {
  map.put("ic_arrow_back", Res.drawable.ic_arrow_back)
  map.put("ic_bookmark", Res.drawable.ic_bookmark)
  map.put("ic_chevron_right", Res.drawable.ic_chevron_right)
  map.put("ic_close", Res.drawable.ic_close)
  map.put("ic_error", Res.drawable.ic_error)
  map.put("ic_home", Res.drawable.ic_home)
  map.put("ic_search", Res.drawable.ic_search)
  map.put("ic_search_off", Res.drawable.ic_search_off)
  map.put("ic_star", Res.drawable.ic_star)
}
