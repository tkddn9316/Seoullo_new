package com.app.seoullo_new.view.map

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.ReverseGeocoding
import com.app.domain.model.common.ApiState
import com.app.domain.model.theme.Language
import com.app.seoullo_new.R
import com.app.seoullo_new.view.util.theme.LocalLanguage
import kotlinx.coroutines.launch

/** 위치 선택 다이얼로그 */
@Composable
fun DirectionSelectDialog(
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val language = LocalLanguage.current

    var startingText by remember { mutableStateOf("") }
    var destinationText by remember { mutableStateOf("") }

    // FocusRequester 및 FocusManager 설정
    val textField1FocusRequester = FocusRequester()
    val textField2FocusRequester = FocusRequester()
    // 현재 Focus 상태 관리
    var isTextField1Focused by remember { mutableStateOf(false) }
    var isTextField2Focused by remember { mutableStateOf(false) }

    AlertDialog(
        title = { Text(text = stringResource(R.string.select_places_title)) },
        text = {
            Column(
                // TODO: 리스트 나오면 제거 예정
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            val tempText = startingText
                            startingText = destinationText
                            destinationText = tempText
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SwapVert,
                            contentDescription = null
                        )
                    }
                    Column {
                        // starting
                        OutlinedTextField(
                            modifier = Modifier
                                .focusRequester(textField1FocusRequester)
                                .onFocusChanged { isTextField1Focused = it.isFocused },
                            value = startingText,
                            onValueChange = { startingText = it },
                            label = { Text(text = stringResource(R.string.starting)) },
                            supportingText = { Text(text = stringResource(R.string.starting_supporting)) },
                            singleLine = true,
                            trailingIcon = {
                                if (startingText.isNotEmpty()) {
                                    IconButton(
                                        onClick = { startingText = "" }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        )

                        // destination
                        OutlinedTextField(
                            modifier = Modifier
                                .focusRequester(textField2FocusRequester)
                                .onFocusChanged { isTextField2Focused = it.isFocused },
                            value = destinationText,
                            onValueChange = { destinationText = it },
                            label = { Text(text = stringResource(R.string.destination)) },
                            supportingText = { Text(text = stringResource(R.string.destination_supporting)) },
                            singleLine = true,
                            trailingIcon = {
                                if (destinationText.isNotEmpty()) {
                                    IconButton(
                                        onClick = { destinationText = "" }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // current location
                    val currentLocationError = stringResource(R.string.current_location_error)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
//                                viewModel.getCurrentLocationAddress(
//                                    languageCode = if (language == Language.ENGLISH) "en" else "ko"
//                                )
//                                when(currentAddress) {
//                                    is ApiState.Initial -> {}
//                                    is ApiState.Loading -> {}
//                                    is ApiState.Success -> {
//                                        val address = (currentAddress as ApiState.Success<ReverseGeocoding>).data ?: ReverseGeocoding()
//                                        when {
//                                            isTextField1Focused -> {
//                                                startingText = address.address
//                                            }
//                                            isTextField2Focused -> {
//                                                destinationText = address.address
//                                            }
//                                            else -> {
//
//                                            }
//                                        }
//                                    }
//                                    is ApiState.Error -> {
//                                        val error = (currentAddress as ApiState.Error).message
//                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
//                                    }
//                                }

                                when {
                                    isTextField1Focused -> {
                                        viewModel.getAddressText(
                                            context = context,
                                            language = if (language == Language.ENGLISH) "en" else "ko"
                                        ) {
                                            startingText = it
                                        }
                                    }
                                    isTextField2Focused -> {
                                        viewModel.getAddressText(
                                            context = context,
                                            language = if (language == Language.ENGLISH) "en" else "ko"
                                        ) {
                                            destinationText = it
                                        }
                                    }
                                    else -> {
                                        Toast.makeText(context, currentLocationError, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.NearMe,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier.padding(10.dp,0.dp,0.dp,0.dp),
                            text = stringResource(R.string.current_location)
                        )
                    }

                    VerticalDivider(
                        modifier = Modifier.padding(4.dp, 0.dp, 4.dp, 0.dp),
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    // enter
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Directions,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp),
                            text = stringResource(R.string.enter)
                        )
                    }
                }
            }
        },
        onDismissRequest = viewModel::closeDirectionSelectDialog,
        confirmButton = {
            TextButton(
                onClick = viewModel::closeDirectionSelectDialog,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(stringResource(R.string.btn_close))
            }
        }
    )
}

fun MapViewModel.getAddressText(
    context: Context,
    language: String,
    responseText: (response: String) -> Unit
) {
    viewModelScope.launch {
        getCurrentLocationAddress(language)
        currentAddress.collect { state ->
            when (state) {
                is ApiState.Success -> {
                    val address = state.data ?: ReverseGeocoding()
                    responseText(address.address)
                }
                is ApiState.Error -> {
                    // 오류 처리
                    val error = state.message
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}
