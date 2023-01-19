package com.grishko188.pointofinterest.features.about

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grishko188.pointofinterest.BuildConfig
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.core.utils.chromeTabsIntent
import com.grishko188.pointofinterest.core.utils.launch

@Composable
fun AboutScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(48.dp)
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(156.dp)
                    .clip(CircleShape),
                painter = painterResource(id = R.mipmap.ic_poi_splash_icon),
                contentDescription = "Application logo",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.size(32.dp))
            Text(
                text = stringResource(id = R.string.app_name).uppercase(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                modifier = Modifier.testTag("App description"),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.about_screen_descriprion),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }

        Column(
            modifier = Modifier
                .height(128.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val chromeTabsIntent = chromeTabsIntent()
            val profileUrl = stringResource(id = R.string.ulr_github_profile)

            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.title_developed_by),
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.size(8.dp))
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(4.dp))
                        .border(width = 2.dp, color = MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(4.dp))
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { chromeTabsIntent.launch(context, profileUrl) }
                        .testTag("Git hub profile link"),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(28.dp)
                            .padding(6.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_github_logo),
                        contentDescription = "GitHub logo",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        modifier = Modifier.padding(end = 6.dp),
                        text = stringResource(id = R.string.author_name),
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.title_app_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE.toString()),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }
    }
}