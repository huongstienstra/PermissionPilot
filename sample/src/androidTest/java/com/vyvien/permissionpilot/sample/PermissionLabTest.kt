package com.vyvien.permissionpilot.sample

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PermissionLabTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun permissionLabRendersInitialCameraState() {
        composeRule.onNodeWithTag(SampleTestTags.PermissionLab)
            .assertIsDisplayed()
        composeRule.onNodeWithText("PermissionPilot")
            .assertIsDisplayed()
        composeRule.onNodeWithTag(SampleTestTags.PermissionSummaryTitle)
            .assertTextEquals("Camera")
        composeRule.onNodeWithTag(SampleTestTags.RequiredPermissions)
            .assertIsDisplayed()
    }

    @Test
    fun selectingPhotoLibraryUpdatesSummary() {
        composeRule.onNodeWithText("Photo library")
            .performClick()

        composeRule.onNodeWithTag(SampleTestTags.PermissionSummaryTitle)
            .assertTextEquals("Photo library")
        composeRule.onNodeWithTag(SampleTestTags.RequiredPermissions)
            .assertIsDisplayed()
    }

    @Test
    fun deniedPermissionShowsActionCard() {
        composeRule.onNodeWithTag(SampleTestTags.PermissionAction)
            .assertIsDisplayed()
        composeRule.onNodeWithText("Request")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Refresh")
            .assertIsDisplayed()
    }
}
