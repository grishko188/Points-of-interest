package com.grishko188.pointofinterest.features.home.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.flowlayout.FlowRow
import com.grishko188.domain.features.categories.models.CategoryType
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.features.home.ui.models.PoiListItem
import com.grishko188.pointofinterest.ui.composables.uikit.PulsingProgressBar
import com.grishko188.pointofinterest.ui.theme.PointOfInterestTheme
import com.grishko188.pointofinterest.ui.theme.WarmGray400

@Composable
fun AddMoreButton(text: String? = null, onClick: () -> Unit) {
    AssistChip(
        colors = AssistChipDefaults.assistChipColors(
            labelColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = WarmGray400.copy(alpha = 0.2f),
            disabledLabelColor = WarmGray400,
        ),
        shape = RoundedCornerShape(16.dp),
        onClick = { onClick() },
        label = {
            Text(
                text = text ?: stringResource(id = R.string.add_more),
                fontSize = 12.sp,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
        },
        border = AssistChipDefaults.assistChipBorder(
            borderColor = MaterialTheme.colorScheme.onBackground,
            disabledBorderColor = WarmGray400.copy(alpha = 0.2f),
            borderWidth = 1.dp
        )
    )
}

@Composable
fun CategoryDisplayChips(
    categoryListItem: CategoryUiModel,
    size: ChipSize = ChipSizeDefaults.basicChip(),
    onClick: (CategoryUiModel) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .background(categoryListItem.color.copy(alpha = 0.4f), shape = RoundedCornerShape(8.dp))
            .padding(size.paddingHorizontal, size.paddingVertical)
            .clickable { onClick(categoryListItem) }
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "#${categoryListItem.title}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = size.textSize
        )
    }
}

@Composable
fun CategoryFilterChips(
    categoryListItem: CategoryUiModel,
    isSelected: Boolean = false,
    size: ChipSize = ChipSizeDefaults.basicChip(),
    enabled: Boolean = true,
    onClick: (CategoryUiModel) -> Unit = {}
) {
    FilterChip(
        enabled = enabled,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = categoryListItem.color.copy(alpha = 0.5f),
            labelColor = MaterialTheme.colorScheme.onBackground,
            iconColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = WarmGray400.copy(alpha = 0.2f),
            disabledLabelColor = WarmGray400,
            disabledLeadingIconColor = WarmGray400,
            disabledTrailingIconColor = WarmGray400,
            selectedContainerColor = categoryListItem.color.copy(alpha = 0.5f),
            disabledSelectedContainerColor = WarmGray400.copy(alpha = 0.2f),
            selectedLabelColor = MaterialTheme.colorScheme.onBackground,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
            selectedTrailingIconColor = MaterialTheme.colorScheme.onBackground
        ),
        shape = RoundedCornerShape(16.dp),
        onClick = { onClick(categoryListItem) },
        leadingIcon = {
            Icon(
                modifier = Modifier.size(10.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_hash_tag),
                contentDescription = ""
            )
        },
        label = {
            Text(
                text = categoryListItem.title,
                style = MaterialTheme.typography.labelMedium,
                fontSize = size.textSize,
                fontWeight = FontWeight.Medium
            )
        },
        border = FilterChipDefaults.filterChipBorder(
            borderColor = Color.Transparent,
            selectedBorderColor = MaterialTheme.colorScheme.secondary,
            disabledBorderColor = Color.Transparent,
            disabledSelectedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
            borderWidth = 1.dp,
            selectedBorderWidth = 2.dp
        ),
        selected = isSelected
    )
}


@Composable
fun PoiCard(
    poiListItem: PoiListItem,
    onClick: (PoiListItem) -> Unit
) {
    ElevatedCard(
        onClick = { onClick(poiListItem) },
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )

    ) {

        Column {

            Row {

                if (poiListItem.imageUrl.isNullOrEmpty().not()) {
                    SubcomposeAsyncImage(
                        modifier = Modifier.size(112.dp),
                        model = poiListItem.imageUrl,
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
                        loading = { PulsingProgressBar() }
                    )

                    Spacer(modifier = Modifier.size(8.dp))
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = poiListItem.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    if (poiListItem.subtitle != null)
                        Text(
                            text = poiListItem.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )

                    Spacer(modifier = Modifier.height(16.dp))

                    FlowRow(mainAxisSpacing = 4.dp, crossAxisSpacing = 2.dp) {
                        poiListItem.categories.forEach {
                            CategoryDisplayChips(categoryListItem = it, size = ChipSizeDefaults.smallChip())
                        }
                    }
                }
            }

            Row(Modifier.padding(16.dp)) {
                val source = poiListItem.source.takeIf { it.isNullOrEmpty().not() } ?: ""
                val text = buildAnnotatedString {
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append(source)
                    }
                }
                Text(
                    modifier = Modifier.weight(1f),
                    text = text,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = poiListItem.modifiedDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.size(8.dp))

                Row {
                    Icon(
                        modifier = Modifier
                            .size(12.dp)
                            .align(CenterVertically),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_comment),
                        contentDescription = "Personal notes count",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.size(2.dp))
                    Text(
                        text = poiListItem.commentsCount.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Medium
                    )
                }

            }
        }
    }
}

data class ChipSize(
    val textSize: TextUnit,
    val paddingHorizontal: Dp,
    val paddingVertical: Dp
)

object ChipSizeDefaults {
    fun smallChip() = ChipSize(
        textSize = 10.sp,
        paddingHorizontal = 4.dp,
        paddingVertical = 2.dp
    )

    fun basicChip() = ChipSize(
        textSize = 12.sp,
        paddingHorizontal = 8.dp,
        paddingVertical = 4.dp
    )
}

@Preview
@Composable
fun HomeScreenItemsPreview() {
    PointOfInterestTheme(darkTheme = true) {
        val mockItems = arrayListOf(
            CategoryUiModel(id = "_ID", title = "Business", color = Color(0xFF2980B9), categoryType = CategoryType.GLOBAL),
            CategoryUiModel(id = "_ID2", title = "Music", color = Color(0xFF009688), categoryType = CategoryType.GLOBAL),
            CategoryUiModel(id = "_ID3", title = "High!", color = Color(0xFFD50000), categoryType = CategoryType.SEVERITY),
            CategoryUiModel(id = "_ID4", title = "Rules", color = Color(0xFFFFEB3B), categoryType = CategoryType.PERSONAL),
        )
        val mockPoiItem = PoiListItem(
            id = "Some id",
            title = "New business forum will start soon",
            subtitle = "All biggest companies will present their vision for future",
            source = "meduim.com",
            imageUrl = "https://cdn.pixabay.com/photo/2018/03/27/21/43/startup-3267505_1280.jpg",
            commentsCount = 10,
            modifiedDate = "29.10.2022",
            categories = mockItems
        )
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            FlowRow(
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 4.dp
            ) {
                mockItems.forEach {
                    CategoryFilterChips(categoryListItem = it)
                    //Spacer(modifier = Modifier.size(2.dp))
                }
            }

            Spacer(modifier = Modifier.size(16.dp))
            PoiCard(poiListItem = mockPoiItem, onClick = {})
        }
    }
}