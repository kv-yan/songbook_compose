package ru.betel.app.ui.screens.template

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.betel.app.ui.items.template.TemplateColumnItem
import ru.betel.app.ui.items.template.TemplateDetailsState
import ru.betel.app.ui.widgets.NothingFoundScreen
import ru.betel.app.ui.widgets.loading_anim.LoadingScreen
import ru.betel.app.view_model.settings.SettingViewModel
import ru.betel.app.view_model.template.TemplateViewModel
import ru.betel.domain.model.ui.ActionBarState
import ru.betel.domain.model.ui.Screens
import ru.betel.domain.model.ui.TemplateType

@Composable
fun TemplateScreen(
    navController: NavController,
    actionBarState: MutableState<ActionBarState>,
    viewModel: TemplateViewModel,
    settingViewModel: SettingViewModel,
) {
    actionBarState.value = ActionBarState.TEMPLATE_SCREEN
    val appTheme = settingViewModel.appTheme.value
    val templates by viewModel.templateUiState
    val localTemplate by viewModel.localTemplateState.observeAsState(mutableListOf())
    val searchQuery by viewModel.searchQuery
    val performerNameFilter by viewModel.performerNameFilter

    val isLoading = false
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    val detailsState = remember {
        mutableStateOf(TemplateDetailsState.Closed)
    }
    detailsState.value = if (viewModel.isOpeningAllTemplate.value) {
        TemplateDetailsState.Opened
    } else {
        TemplateDetailsState.Closed
    }

    if (templates.isEmpty()) {
        LaunchedEffect(key1 = null) { viewModel.loadTemplate() }
    }

    val filteredTemplates = viewModel.searchTemplates(searchQuery)
        .filter { it.performerName.contains(performerNameFilter, ignoreCase = true) }

    SwipeRefresh(
        modifier = Modifier
            .fillMaxSize()
            .background(appTheme.screenBackgroundColor),
        state = swipeRefreshState,
        onRefresh = viewModel::loadTemplate,
        refreshTriggerDistance = 40.dp,
    ) {
        Box(contentAlignment = Alignment.Center) {
            val itemsList =
                if (viewModel.templateSelectedType.value == TemplateType.ALL) filteredTemplates else localTemplate

            when {
                itemsList.isEmpty() && searchQuery.isEmpty() && performerNameFilter.isEmpty() -> {
                    LoadingScreen(appTheme)
                }

                itemsList.isEmpty() && (searchQuery.isNotEmpty() || performerNameFilter.isNotEmpty()) -> {
                    NothingFoundScreen(appTheme)
                }

                else -> {
                    LazyColumn(
                        Modifier
                            .fillMaxSize()
                            .background(appTheme.screenBackgroundColor)
                    ) {
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                        items(itemsList, key = { it.id }) { template ->
                            TemplateColumnItem(
                                detailsState = detailsState,
                                appTheme = appTheme,
                                template = template,
                                textSize = settingViewModel.songbookTextSize,
                                searchQuery = viewModel.searchQuery.value,
                            ) {
                                viewModel.singleTemplate.value = template
                                navController.navigate(Screens.SINGLE_TEMPLATE_SCREEN.route)
                            }
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                }
            }
        }
    }
    BackHandler {
        navController.popBackStack()
    }
}
