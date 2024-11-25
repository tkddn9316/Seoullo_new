package com.app.seoullo_new.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.sharp.ArrowBackIosNew
import androidx.compose.material.icons.sharp.Menu
import androidx.compose.material.icons.sharp.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Constants.SELECTED_NEARBY_LIST
import com.app.seoullo_new.utils.Constants.SELECTED_TOUR_LIST
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.InitNavHost
import com.app.seoullo_new.view.ui.theme.Color_92c8e0
import com.app.seoullo_new.view.ui.theme.Color_Gray500
import com.app.seoullo_new.view.ui.theme.Seoullo_newTheme

abstract class BaseComposeActivity<VM : BaseViewModel> : ComponentActivity() {

    abstract val viewModel: VM

    protected val TAG: String by lazy {
        javaClass.simpleName
    }

    var loading by mutableStateOf(true)
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logging.e(TAG, "onCreate")

        viewModel.loading.observe(this) { isLoading ->
            loading = isLoading
        }

        setContent {
            Seoullo_newTheme {
                Surface(
                    color = Color.White, // 배경색을 원하는 색상으로 지정
                    modifier = Modifier.fillMaxSize() // 전체 화면을 채우도록 설정
                ) {
                    Setup()
//                    CircularProgress()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.loading.removeObservers(this)
    }

    @Composable
    abstract fun Setup()

    @Composable
    fun BaseTitle() {
        var isMenuExpanded by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (viewModel.back.value!!) {
                    // back
                    IconButton(
                        onClick = { finish() },
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.ArrowBackIosNew,
                            contentDescription = "Back Button",
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Text(
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.Black
                        ),
                        modifier = Modifier.weight(1f),
                        text = viewModel.title.value!!,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
                if (viewModel.refresh.value!!) {
                    // refresh
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Refresh,
                            contentDescription = "Refresh Button",
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                } else if (viewModel.menu.value!!) {
                    // menu
                    IconButton(
                        onClick = { if (viewModel.menu.value!!) isMenuExpanded = true },
                        modifier = Modifier
                            .padding(6.dp)
//                            .alpha(if (viewModel.menu.value!!) 1f else 0f)
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Menu,
                            contentDescription = "Menu Button"
                        )

                        DropdownMenu(
                            modifier = Modifier.wrapContentSize(),
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Map,
                                            contentDescription = stringResource(id = R.string.tour_list),
                                            modifier = Modifier.padding(12.dp),
                                            colorResource(id = R.color.gray_600)
                                        )
                                        Column {
                                            Text(
                                                text = stringResource(id = R.string.tour_list),
                                                fontSize = 10.sp
                                            )
                                            Text(
                                                text = stringResource(id = R.string.tour_list_description),
                                                color = colorResource(
                                                    id = R.color.gray_600
                                                ),
                                                fontSize = 8.sp,
                                                lineHeight = 8.sp * 1.2f
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    viewModel.menuClickedPosition.value = SELECTED_TOUR_LIST
                                    isMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationSearching,
                                            contentDescription = stringResource(id = R.string.nearby_list),
                                            modifier = Modifier.padding(12.dp),
                                            colorResource(id = R.color.gray_600)
                                        )
                                        Column {
                                            Text(
                                                text = stringResource(id = R.string.nearby_list),
                                                fontSize = 10.sp
                                            )
                                            Text(
                                                text = stringResource(id = R.string.nearby_list_description),
                                                color = colorResource(
                                                    id = R.color.gray_600
                                                ),
                                                fontSize = 8.sp,
                                                lineHeight = 8.sp * 1.2f
                                            )
                                        }

                                    }
                                },
                                onClick = {
                                    viewModel.menuClickedPosition.value = SELECTED_NEARBY_LIST
                                    isMenuExpanded = false
                                }
                            )
                        }
                    }
                } else {
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .padding(6.dp)
                            .alpha(0f)
                    ) {}
                }
            }
            Divider(color = Color_Gray500, thickness = 1.dp)
        }
    }

    @Composable
    fun CircularProgress() {
        // 맨 밑에 배치할 것
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) { },   // 클릭 막기
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color_92c8e0)
            }
        }
    }
}