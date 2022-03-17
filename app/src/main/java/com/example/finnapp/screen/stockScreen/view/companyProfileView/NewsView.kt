package com.example.finnapp.screen.stockScreen.view.companyProfileView

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.company.CompanyProfile
import com.example.finnapp.ui.theme.primaryBackground
import com.example.finnapp.ui.theme.secondaryBackground
import java.util.*

@Composable
fun NewsView(
    context:Context = LocalContext.current,
    companyProfile:NetworkResult<CompanyProfile>,
    startDialog:DatePickerDialog.OnDateSetListener,
    endDialog:DatePickerDialog.OnDateSetListener,
    dateDialogStart:String,
    dateDialogEnd:String,
    calendar:Calendar
) {
    Text(
        text = "News ${companyProfile.data?.name} :",
        modifier = Modifier.padding(5.dp)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Start date news:",
                modifier = Modifier.padding(5.dp)
            )

            OutlinedTextField(
                modifier = Modifier
                    .padding(5.dp)
                    .width(180.dp)
                    .clickable {
                        DatePickerDialog(context, startDialog,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                value = dateDialogStart,
                onValueChange = { },
                enabled = false,
                shape = AbsoluteRoundedCornerShape(5.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = secondaryBackground,
                    backgroundColor = primaryBackground,
                    cursorColor = secondaryBackground
                )
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "End date news:",
                modifier = Modifier.padding(5.dp)
            )

            OutlinedTextField(
                modifier = Modifier
                    .padding(5.dp)
                    .width(180.dp)
                    .clickable {
                        DatePickerDialog(context, endDialog,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                value = dateDialogEnd,
                onValueChange = { },
                enabled = false,
                shape = AbsoluteRoundedCornerShape(5.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = secondaryBackground,
                    backgroundColor = primaryBackground,
                    cursorColor = secondaryBackground
                )
            )
        }
    }
}