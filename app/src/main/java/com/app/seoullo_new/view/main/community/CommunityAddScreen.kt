package com.app.seoullo_new.view.main.community

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.domain.model.common.ApiState
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.LoadingOverlay
import com.app.seoullo_new.view.base.SeoulloAppBar
import com.app.seoullo_new.view.util.advancedImePadding

/* TODO
*   1. 이미지 리사이징
*   2. 프로그래스 바
*   3. 확인 버튼 누르면 키보드 내리기 */

@Composable
fun CommunityAddScreen(
    viewModel: CommunityAddViewModel = hiltViewModel(),
    onNavigationClick: () -> Unit,
    addPostOnClick: () -> Unit
) {
    val context = LocalContext.current
    val addPostState by viewModel.addPostState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            SeoulloAppBar(
                title = stringResource(R.string.add_post_title),
                onNavigationClick = onNavigationClick,
                showAction = false,
            ) { }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
//                .imePadding()
        ) {
            when (val s = addPostState) {
                is ApiState.Loading -> {
                    // API 로딩 처리
                    LoadingOverlay()
                }

                is ApiState.Success -> {
                    addPostOnClick()
                }

                is ApiState.Error -> {
                    val error = s.message.orEmpty()
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
//            LoadingOverlay()

            CommunityAddView(
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun CommunityAddView(
    viewModel: CommunityAddViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    // picker
    val picker =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            if (uris.isNotEmpty()) {
                viewModel.addImages(uris = uris)
            } else {
                Logging.e("No media selected")
            }
        }
    val legacyPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.addImages(uris = uris)
        } else {
            Logging.e("No media selected")
        }
    }
    val images by viewModel.addImageList.collectAsStateWithLifecycle()

    // TextField
//    var titleText by rememberSaveable { mutableStateOf("") }
    val titleText = rememberTextFieldState()
    val contentText = rememberTextFieldState()

    Column(
        modifier = Modifier
            .padding(top = 10.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            .advancedImePadding()
    ) {
        BasicTextField(
            modifier = modifier.fillMaxWidth(),
            state = titleText,
            textStyle = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            lineLimits = TextFieldLineLimits.SingleLine,
            decorator = { innerTextField ->
                Column {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Box {
                            if (titleText.text.isEmpty()) {
                                // Hint
                                Text(
                                    text = stringResource(R.string.add_post_hint),
                                    fontSize = 18.sp,
                                    color = Color(0xFF848484),
                                )
                            }
                            innerTextField()
                        }
                    }

                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        )

        Spacer(modifier = modifier.height(8.dp))

        BasicTextField(
            modifier = modifier
                .weight(1f)
                .fillMaxWidth(),
            state = contentText,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorator = { innerTextField ->
                Column {
                    Box {
                        if (contentText.text.isEmpty()) {
                            // Hint
                            Text(
                                text = stringResource(R.string.add_post_content_hint),
                                fontSize = 16.sp,
                                color = Color(0xFF848484),
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )

        // 내가 올릴 사진 리스트
        if (images.isNotEmpty()) {
            ImagePickerRow(
                images = images,
                onRemoveClick = { viewModel.removeImage(it.id) }
            )
        }

        HorizontalDivider(
            modifier = modifier.padding(top = 6.dp, bottom = 6.dp),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        // 글쓰기 옵션(사진 등)
        PostOptionRow(
            modifier = modifier
        ) { option ->
            when (option.id) {
                "photo" -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        legacyPicker.launch("image/*")
                    }
                }
            }
        }

        Button(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                viewModel.addPost(
                    title = titleText.text.toString(),
                    content = contentText.text.toString()
                )
            }
        ) {
            Text(
                text = stringResource(R.string.add_post_summit)
            )
        }
    }
}

//@Preview(
//    apiLevel = 34,
//    showBackground = true,
//    widthDp = 360, heightDp = 640
//)
//@Composable
//fun CommunityAddPreview() {
//    CommunityAddView()
//}