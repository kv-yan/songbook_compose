package ru.betel.app.ui.action_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.koin.androidx.compose.get
import ru.betel.app.R
import ru.betel.app.ui.widgets.MyTextFields
import ru.betel.app.ui.widgets.SearchTopAppBar
import ru.betel.app.ui.widgets.dropdown_menu.AddNewItemDropdownMenu
import ru.betel.app.ui.widgets.dropdown_menu.TemplateDropdownMenu
import ru.betel.app.view_model.settings.SettingViewModel
import ru.betel.app.view_model.song.SongViewModel
import ru.betel.app.view_model.template.TemplateViewModel
import ru.betel.domain.model.ui.SearchAppBarState
import ru.betel.domain.model.ui.SongbookTextSize

@Composable
private fun ActionBarContent(
    navController: NavController,
    templateViewModel: TemplateViewModel,
    settingViewModel: SettingViewModel,
    onSearchClicked: () -> Unit,
    onMenuIconClick: () -> Unit,
    onSettingsBtnClick: () -> Unit,
) {
    val appTheme = settingViewModel.appTheme.value
    Surface(
        color = appTheme.actionBarColor, modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onMenuIconClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "menu_btn",
                    tint = appTheme.actionBarIconColor,
                    modifier = Modifier
                        .width(18.dp)
                        .height(10.dp)
                )
            }

            Text(
                text = "Ցուցակներ", style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontFamily = FontFamily(Font(R.font.mardoto_regular)),
                    fontWeight = FontWeight(500),
                    color = Color.White,
                )
            )

            // action btn part
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { onSearchClicked() }, modifier = Modifier.size(27.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "search btn",
                        tint = appTheme.actionBarIconColor,
                        modifier = Modifier
                            .width(16.dp)
                            .height(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(18.dp))
                val isShowTemplateMenu = remember {
                    mutableStateOf(false)
                }

                IconButton(
                    onClick = { isShowTemplateMenu.value = true }, modifier = Modifier.size(25.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_category_more),
                        contentDescription = null,
                        tint = appTheme.actionBarIconColor,
                        modifier = Modifier
                            .width(16.dp)
                            .height(12.dp)
                    )
                }
                TemplateDropdownMenu(
                    expanded = isShowTemplateMenu,
                    templateViewModel.templateSelectedType,
                    templateViewModel = templateViewModel
                )

                Spacer(modifier = Modifier.width(20.dp))


                val expanded = remember { mutableStateOf(false) }
                /******* Dropdown Menu *******/
                AddNewItemDropdownMenu(
                    expanded = expanded,
                    navController = navController,
                    userStatus = settingViewModel.checkUserLoginStatus.value
                )

                IconButton(
                    onClick = { expanded.value = true },
                    modifier = Modifier
                        .width(12.dp)
                        .height(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_item),
                        contentDescription = "image description",
                        tint = appTheme.actionBarIconColor,
                        modifier = Modifier
                            .width(12.dp)
                            .height(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(18.dp))
                IconButton(onClick = { onSettingsBtnClick() }, modifier = Modifier.size(27.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more_vert),
                        contentDescription = "add new item btn",
                        tint = appTheme.actionBarIconColor,
                        modifier = Modifier
                            .width(3.dp)
                            .height(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }

}


@Composable
fun TemplateActionBar(
    searchAppBarState: MutableState<SearchAppBarState>,
    navController: NavController,
    settingViewModel: SettingViewModel,
    templateViewModel: TemplateViewModel,
    textSize: SongbookTextSize,
    onMenuIconClick: () -> Unit,
    onSettingsBtnClick: () -> Unit,
) {
    val viewModel: SongViewModel = get()
    val appTheme = settingViewModel.appTheme.value
    when (searchAppBarState.value) {
        SearchAppBarState.CLOSED -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = appTheme.screenBackgroundColor),
                verticalArrangement = Arrangement.Center
            ) {
                ActionBarContent(navController = navController,
                    templateViewModel = templateViewModel,
                    settingViewModel = settingViewModel,
                    onSearchClicked = {
                        searchAppBarState.value = SearchAppBarState.OPENED
                    },
                    onMenuIconClick = { onMenuIconClick() },
                    onSettingsBtnClick = { onSettingsBtnClick() })

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 11.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val textFieldState = remember {
                        mutableStateOf("")
                    }
                    MyTextFields(appTheme = appTheme,
                        placeholder = "Առաջնորդի անուն",
                        fieldText = templateViewModel.performerNameFilter,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = null,
                                tint = appTheme.primaryTextColor,
                                modifier = Modifier.size(16.dp)
                            )
                        })
                    Spacer(modifier = Modifier.width(10.dp))

                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                    ) {
                        IconButton(onClick = {
                            templateViewModel.isOpeningAllTemplate.value =
                                !templateViewModel.isOpeningAllTemplate.value

                        }) {
                            Icon(
                                painter = painterResource(id = if (templateViewModel.isOpeningAllTemplate.value) R.drawable.ic_close_all else R.drawable.ic_open_all),
                                contentDescription = null,
                                tint = appTheme.primaryTextColor,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = appTheme.actionBarColor)
                    .padding(horizontal = 11.dp, vertical = 6.dp)
            ) {
                SearchTopAppBar(
                    appTheme = appTheme,
                    text = templateViewModel.searchQuery,
                    onTextChange = { text ->
                        templateViewModel.searchQuery.value = text
                    },
                    onCloseClicked = {
                        searchAppBarState.value = SearchAppBarState.CLOSED
                        viewModel.searchAppBarText.value = ""
                    },
                    textSize = textSize
                )
            }
        }
    }
}
