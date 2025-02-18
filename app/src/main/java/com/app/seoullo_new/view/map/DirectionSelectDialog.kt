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
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.app.domain.model.DirectionRequest
import com.app.domain.model.ReverseGeocoding
import com.app.domain.model.common.ApiState
import com.app.domain.model.theme.Language
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Constants.FocusedField
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.utils.Util.getLanguageCode
import com.app.seoullo_new.view.util.singleClickable
import com.app.seoullo_new.view.util.theme.LocalLanguage
import kotlinx.coroutines.launch

/** 위치 선택 다이얼로그 */
@Composable
fun DirectionSelectDialog(
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val language = LocalLanguage.current

    var startingRequest by remember { mutableStateOf(DirectionRequest()) }
    var destinationRequest by remember {
        mutableStateOf(
            DirectionRequest(
                lat = viewModel.latLng.lat,
                lng = viewModel.latLng.lng,
                address = viewModel.latLng.address.ifEmpty { "" },
                placeId = viewModel.latLng.placeId.ifEmpty { "" }
            )
        )
    }

    Logging.e(startingRequest)
    Logging.e(destinationRequest)

    // 현재 Focus 상태 관리
    var focusedField by remember { mutableStateOf<FocusedField?>(null) }

    val currentLocationErrorMessage = stringResource(R.string.current_location_error)
    val enterErrorMessage = stringResource(R.string.enter_error)

    // 자동완성 관련
    val autocompleteResults by viewModel.autocompleteResults.collectAsStateWithLifecycle()

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
                            val tempRequest = startingRequest
                            startingRequest = destinationRequest
                            destinationRequest = tempRequest
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SwapVert,
                            contentDescription = null
                        )
                    }
                    Column {
                        // starting
                        AutoCompleteTextField(
                            label = stringResource(R.string.starting),
                            supportingText = stringResource(R.string.starting_supporting),
                            value = startingRequest.address,
                            onValueChange = { newAddress ->
                                startingRequest = startingRequest.copy(
                                    lat = 0.0,
                                    lng = 0.0,
                                    address = newAddress,
                                    placeId = ""
                                )
                                if (newAddress.isNotEmpty()) {
                                    viewModel.onAutoCompletePlaces(
                                        input = newAddress,
                                        languageCode = getLanguageCode(
                                            context = context,
                                            language = language
                                        )
                                    )
                                } else {
                                    viewModel.clearAutoCompleteResults()
                                }
                            },
                            onClearClick = {
                                startingRequest = DirectionRequest()
                                viewModel.clearAutoCompleteResults()
                            },
                            onItemSelected = { place ->
                                startingRequest = startingRequest.copy(
                                    lat = 0.0,
                                    lng = 0.0,
                                    address = "${place.displayName} - ${place.address}",
                                    placeId = place.placeId
                                )
                                viewModel.clearAutoCompleteResults()
                            },
                            autocompleteResults = autocompleteResults,
                            focusedField = focusedField,
                            fieldType = FocusedField.STARTING,
                            setFocusedField = { focusedField = it }
                        )

                        // destination
                        AutoCompleteTextField(
                            label = stringResource(R.string.destination),
                            supportingText = stringResource(R.string.destination_supporting),
                            value = destinationRequest.address,
                            onValueChange = { newAddress ->
                                destinationRequest = destinationRequest.copy(
                                    lat = 0.0,
                                    lng = 0.0,
                                    address = newAddress,
                                    placeId = ""
                                )
                                if (newAddress.isNotEmpty()) {
                                    viewModel.onAutoCompletePlaces(
                                        input = newAddress,
                                        languageCode = getLanguageCode(
                                            context = context,
                                            language = language
                                        )
                                    )
                                } else {
                                    viewModel.clearAutoCompleteResults()
                                }
                            },
                            onClearClick = {
                                destinationRequest = DirectionRequest()
                                viewModel.clearAutoCompleteResults()
                            },
                            onItemSelected = { place ->
                                destinationRequest = destinationRequest.copy(
                                    lat = 0.0,
                                    lng = 0.0,
                                    address = "${place.displayName} - ${place.address}",
                                    placeId = place.placeId
                                )
                                viewModel.clearAutoCompleteResults()
                            },
                            autocompleteResults = autocompleteResults,
                            focusedField = focusedField,
                            fieldType = FocusedField.DESTINATION,
                            setFocusedField = { focusedField = it }
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .singleClickable {
                                if (focusedField != null) {
                                    viewModel.getAddressText(
                                        context = context,
                                        languageCode = getLanguageCode(
                                            context = context,
                                            language = language
                                        )
                                    ) { address ->
                                        if (focusedField == FocusedField.STARTING) {
                                            startingRequest = startingRequest.copy(
                                                lat = 0.0,
                                                lng = 0.0,
                                                address = address.address,
                                                placeId = address.placeId
                                            )
                                        } else if (focusedField == FocusedField.DESTINATION) {
                                            destinationRequest = destinationRequest.copy(
                                                lat = 0.0,
                                                lng = 0.0,
                                                address = address.address,
                                                placeId = address.placeId
                                            )
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, currentLocationErrorMessage, Toast.LENGTH_SHORT).show()
                                }
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.NearMe,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp),
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
                                if (startingRequest.address.isNotEmpty() && destinationRequest.address.isNotEmpty()) {
                                    if (BuildConfig.DEBUG) {
                                        viewModel.getFakeDirection(context)
                                    } else {
                                        viewModel.getDirection(
                                            destination = destinationRequest,
                                            starting = startingRequest,
                                            languageCode = getLanguageCode(
                                                context = context,
                                                language = language
                                            )
                                        )
                                    }
                                } else {
                                    Toast.makeText(context, enterErrorMessage, Toast.LENGTH_SHORT).show()
                                }
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
        onDismissRequest = {
            viewModel.clearAutoCompleteResults()
            viewModel.closeDirectionSelectDialog()
        },
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
    languageCode: String,
    responseText: (response: ReverseGeocoding) -> Unit
) {
    viewModelScope.launch {
        getCurrentLocationAddress(
            languageCode = languageCode
        )
        currentAddress.collect { state ->
            when (state) {
                is ApiState.Success -> {
                    val address = state.data ?: ReverseGeocoding()
                    responseText(address)
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
