package com.app.seoullo_new.view.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import com.app.domain.model.PlacesAutoComplete
import com.app.domain.model.common.ApiState
import com.app.seoullo_new.utils.Constants.FocusedField

/**
 * 시작지, 목적지 TextField.
 * 사용자 입력 시 AutoComplete API
 * */
@Composable
fun AutoCompleteTextField(
    modifier: Modifier = Modifier,
    label: String,  // 제목
    supportingText: String,
    value: String,  // text
    onValueChange: (String) -> Unit,    // text change
    onClearClick: () -> Unit,   // clear button click
    onItemSelected: (PlacesAutoComplete.Item) -> Unit,  // DropdownMenuItem
    autocompleteResults: ApiState<PlacesAutoComplete>,  // API Result
    focusedField: FocusedField?,    // Focus 상태 관리
    fieldType: FocusedField,    // 시작지, 목적지 구분
    setFocusedField: (FocusedField?) -> Unit,   // FocusedField 세팅
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Box {
        OutlinedTextField(
            modifier = modifier
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) setFocusedField(fieldType)
                }
                .onGloballyPositioned { coordinates ->
                    // textField의 길이 구하기
                    textFieldSize = coordinates.size.toSize()
                },
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = it.isNotEmpty()
            },
            label = { Text(text = label) },
            supportingText = { Text(text = supportingText) },
            singleLine = true,
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = onClearClick) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                    }
                }
            }
        )

        when (autocompleteResults) {
            is ApiState.Success -> {
                val items = autocompleteResults.data?.items ?: emptyList()
                // API 성공 시 + 해당 TextField Focus 여부(없으면 DropdownMenu 위아래 2개 보임)
                expanded = items.isNotEmpty() && focusedField == fieldType
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    properties = PopupProperties(focusable = false),
                    modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.displayName) },
                            onClick = {
                                onItemSelected(item)
                                expanded = false
                            }
                        )
                    }
                }
            }
            is ApiState.Error -> {
                expanded = false
            }
            else -> {}
        }
    }
}