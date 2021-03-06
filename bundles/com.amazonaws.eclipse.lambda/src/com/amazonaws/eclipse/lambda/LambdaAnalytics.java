/*
 * Copyright 2015-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.eclipse.lambda;

import com.amazonaws.eclipse.core.AwsToolkitCore;
import com.amazonaws.eclipse.core.mobileanalytics.ToolkitAnalyticsManager;
import com.amazonaws.eclipse.core.mobileanalytics.ToolkitEvent.ToolkitEventBuilder;
import com.amazonaws.eclipse.lambda.project.wizard.model.LambdaFunctionWizardDataModel;
import com.amazonaws.eclipse.lambda.project.wizard.model.NewServerlessProjectDataModel;
import com.amazonaws.eclipse.lambda.project.wizard.model.PredefinedHandlerInputType;

public final class LambdaAnalytics {

    /*
     * Upload function wizard
     */
    private static final String EVENT_TYPE_UPLOAD_FUNCTION_WIZARD = "Lambda-UploadFunctionWizard";

    // OpenedFrom -> ProjectContextMenu/FileEditorContextMenu
    private static final String ATTR_NAME_OPENED_FROM = "OpenedFrom";
    private static final String ATTR_VALUE_PROJECT_CONTEXT_MENU = "ProjectContextMenu";
    private static final String ATTR_VALUE_FILE_EDITOR_CONTEXT_MENU = "FileEditorContextMenu";
    private static final String ATTR_VALUE_UPLOAD_BEFORE_INVOKE = "UploadBeforeInvoke";

    private static final String ATTR_NAME_CHANGE_SELECTION = "ChangeSelection";
    private static final String ATTR_VALUE_REGION_SELECTION_COMBO = "RegionSelectionCombo";
    private static final String ATTR_VALUE_FUNCTION_HANDLER_SELECTION_COMBO = "FunctionHandlerSelectionCombo";
    private static final String ATTR_VALUE_IAM_ROLE_SELECTION_COMBO = "IamRoleSelectionCombo";
    private static final String ATTR_VALUE_S3_BUCKET_SELECTION_COMBO = "S3BucketSelectionCombo";

    private static final String ATTR_NAME_CLICK = "Click";
    private static final String ATTR_VALUE_CREATE_IAM_ROLE_BUTTON = "CreateIamRoleButton";
    private static final String ATTR_VALUE_CREATE_S3_BUCKET_BUTTON = "CreateS3BucketButton";

    private static final String METRIC_NAME_UPLOAD_TOTAL_TIME_DURATION_MS = "UploadTotalTimeDurationMs";
    private static final String METRIC_NAME_UPLOAD_S3_BUCKET_TIME_DURATION_MS = "UploadS3BucketTimeDurationMs";
    private static final String METRIC_NAME_EXPORTED_JAR_SIZE = "ExportedJarSize";
    private static final String METRIC_NAME_UPLOAD_S3_BUCKET_SPEED = "UploadSpeed";

    private static final String METRIC_NAME_IS_CREATING_NEW_FUNCTION = "IsCreatingNewFunction";
    private static final String METRIC_NAME_VALID_FUNCTION_HANDLER_CLASS_COUNT = "ValidFunctionHandlerClassCount";
    private static final String METRIC_NAME_LOAD_IAM_ROLE_TIME_DURATION_MS = "LoadIamRoleTimeDurationMs";
    private static final String METRIC_NAME_LOAD_S3_BUCKET_TIME_DURATION_MS = "LoadS3BucketTimeDurationMs";

    /*
     * Invoke function dialog
     */
    private static final String EVENT_TYPE_INVOKE_FUNCTION_DIALOG = "Lambda-InvokeFunctionDialog";

    // Change selection
    private static final String ATTR_VALUE_INVOKE_INPUT_FILE_SELECTION_COMBO = "InvokeInputFileSelectionCombo";

    private static final String METRIC_NAME_IS_INVOKE_INPUT_MODIFIED = "IsInvokeInputModified";
    private static final String METRIC_NAME_IS_PROJECT_MODIFIED_AFTER_LAST_INVOKE = "IsProjectModifiedAfterLastInvoke";
    private static final String METRIC_NAME_FUNCTION_LOG_LENGTH = "FunctionLogLength";
    private static final String METRIC_NAME_SHOW_LIVE_LOG = "ShowLiveLog";

    /*
     * New wizard
     */
    private static final String EVENT_TYPE_NEW_LAMBDA_PROJECT_WIZARD = "Lambda-NewLambdaProjectWizard";
    private static final String EVENT_TYPE_NEW_LAMBDA_FUNCTION_WIZARD = "Lambda-NewLambdaFunctionWizard";
    private static final String EVENT_TYPE_NEW_SERVERLESS_PROJECT_WIZARD = "Lambda-NewServerlessProjectWizard";
    private static final String EVENT_TYPE_DEPLOY_SERVERLESS_PROJECT_WIZARD = "Lambda-DeployServerlessProjectWizard";

    private static final String ATTR_NAME_FUNCTION_INPUT_TYPE = "FunctionInputType";
    private static final String ATTR_NAME_FUNCTION_OUTPUT_TYPE = "FunctionOutputType";
    private static final String ATTR_NAME_BLUEPRINT_NAME = "BlueprintName";
    private static final String ATTR_NAME_IS_IMPORT_TEMPLATE = "IsImportTemplate";

    // End result -> Succeeded/Failed/Canceled
    private static final String ATTR_NAME_END_RESULT = "EndResult";
    private static final String ATTR_VALUE_SUCCEEDED = "Succeeded";
    private static final String ATTR_VALUE_FAILED = "Failed";
    private static final String ATTR_VALUE_CANCELED = "Canceled";

    private static final ToolkitAnalyticsManager ANALYTICS = AwsToolkitCore.getDefault().getAnalyticsManager();

    /*
     * Analytics for Lambda-NewLambdaProjectWizard
     */
    public static void trackProjectCreationSucceeded() {
        publishEventWithAttributes(EVENT_TYPE_NEW_LAMBDA_PROJECT_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_SUCCEEDED);
    }

    public static void trackProjectCreationFailed() {
        publishEventWithAttributes(EVENT_TYPE_NEW_LAMBDA_PROJECT_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_FAILED);
    }

    public static void trackProjectCreationCanceled() {
        publishEventWithAttributes(EVENT_TYPE_NEW_LAMBDA_PROJECT_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_CANCELED);
    }

    public static void trackNewProjectAttributes(LambdaFunctionWizardDataModel dataModel) {
        String inputType = dataModel.getLambdaFunctionDataModel().getInputTypeEnum() == PredefinedHandlerInputType.CUSTOM
                ? dataModel.getLambdaFunctionDataModel().getInputName()
                : dataModel.getLambdaFunctionDataModel().getInputTypeEnum().getFqcn();
        String outputType = dataModel.getLambdaFunctionDataModel().getOutputName();

        publishEventWithAttributes(EVENT_TYPE_NEW_LAMBDA_PROJECT_WIZARD, ATTR_NAME_FUNCTION_INPUT_TYPE, inputType, ATTR_NAME_FUNCTION_OUTPUT_TYPE, outputType);
    }

    /*
     * Analytics for Lambda-NewLambdaFunctionWizard
     */
    public static void trackLambdaFunctionCreationFailed() {
        publishEventWithAttributes(EVENT_TYPE_NEW_LAMBDA_FUNCTION_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_FAILED);
    }

    public static void trackLambdaFunctionCreationSucceeded() {
        publishEventWithAttributes(EVENT_TYPE_NEW_LAMBDA_FUNCTION_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_SUCCEEDED);
    }

    public static void trackLambdaFunctionCreationCanceled() {
        publishEventWithAttributes(EVENT_TYPE_NEW_LAMBDA_FUNCTION_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_CANCELED);
    }

    /*
     * Analytics for Lambda-NewServerlessProjectWizard
     */
    public static void trackServerlessProjectCreationSucceeded() {
        publishEventWithAttributes(EVENT_TYPE_NEW_SERVERLESS_PROJECT_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_SUCCEEDED);
    }

    public static void trackServerlessProjectCreationFailed() {
        publishEventWithAttributes(EVENT_TYPE_NEW_SERVERLESS_PROJECT_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_FAILED);
    }

    public static void trackServerlessProjectCreationCanceled() {
        publishEventWithAttributes(EVENT_TYPE_NEW_SERVERLESS_PROJECT_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_CANCELED);
    }

    public static void trackServerlessProjectSelection(NewServerlessProjectDataModel dataModel) {
        if (dataModel.isUseBlueprint()) {
            publishEventWithAttributes(EVENT_TYPE_NEW_SERVERLESS_PROJECT_WIZARD, ATTR_NAME_BLUEPRINT_NAME, dataModel.getBlueprintName());
        }
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_NEW_SERVERLESS_PROJECT_WIZARD)
                .addBooleanMetric(ATTR_NAME_IS_IMPORT_TEMPLATE, !dataModel.isUseBlueprint())
                .build());
    }

    /*
     * Analytics for Lambda-DeployServerlessProjectWizard
     */
    public static void trackDeployServerlessProjectSucceeded() {
        publishEventWithAttributes(EVENT_TYPE_DEPLOY_SERVERLESS_PROJECT_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_SUCCEEDED);
    }

    public static void trackDeployServerlessProjectFailed() {
        publishEventWithAttributes(EVENT_TYPE_DEPLOY_SERVERLESS_PROJECT_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_FAILED);
    }

    public static void trackDeployServerlessProjectCanceled() {
        publishEventWithAttributes(EVENT_TYPE_DEPLOY_SERVERLESS_PROJECT_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_CANCELED);
    }

    /*
     * Analytics for Lambda-InvokeFunctionDialog
     */
    public static void trackInvokeDialogOpenedFromProjectContextMenu() {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_INVOKE_FUNCTION_DIALOG)
                .addAttribute(ATTR_NAME_OPENED_FROM, ATTR_VALUE_PROJECT_CONTEXT_MENU)
                .build());
    }

    public static void trackIsProjectModifiedAfterLastInvoke(boolean isModified) {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_INVOKE_FUNCTION_DIALOG)
                .addBooleanMetric(METRIC_NAME_IS_PROJECT_MODIFIED_AFTER_LAST_INVOKE, isModified)
                .build());
    }

    public static void trackInvokeSucceeded() {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_INVOKE_FUNCTION_DIALOG)
                .addAttribute(ATTR_NAME_END_RESULT, ATTR_VALUE_SUCCEEDED)
                .build());
    }

    public static void trackInvokeFailed() {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_INVOKE_FUNCTION_DIALOG)
                .addAttribute(ATTR_NAME_END_RESULT, ATTR_VALUE_FAILED)
                .build());
    }

    public static void trackInvokeCanceled() {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_INVOKE_FUNCTION_DIALOG)
                .addAttribute(ATTR_NAME_END_RESULT, ATTR_VALUE_CANCELED)
                .build());
    }

    public static void trackIsInvokeInputModified(boolean isModified) {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_INVOKE_FUNCTION_DIALOG)
                .addBooleanMetric(METRIC_NAME_IS_INVOKE_INPUT_MODIFIED, isModified)
                .build());
    }

    public static void trackInputJsonFileSelectionChange() {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_INVOKE_FUNCTION_DIALOG)
                .addAttribute(ATTR_NAME_CHANGE_SELECTION, ATTR_VALUE_INVOKE_INPUT_FILE_SELECTION_COMBO)
                .build());
    }

    public static void trackInvokeDialogOpenedFromEditorContextMenu() {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_INVOKE_FUNCTION_DIALOG)
                .addAttribute(ATTR_NAME_OPENED_FROM, ATTR_VALUE_FILE_EDITOR_CONTEXT_MENU)
                .build());
    }

    public static void trackFunctionLogLength(long length) {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_INVOKE_FUNCTION_DIALOG)
                .addMetric(METRIC_NAME_FUNCTION_LOG_LENGTH, length)
                .build());
    }

    public static void trackIsShowLiveLog(boolean showLiveLog) {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_INVOKE_FUNCTION_DIALOG)
                .addBooleanMetric(METRIC_NAME_SHOW_LIVE_LOG, showLiveLog)
                .build());
    }

    /*
     * Analytics for Lambda-UploadFunctionWizard
     */
    public static void trackUploadWizardOpenedFromEditorContextMenu() {
        publishEventWithAttributes(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD, ATTR_NAME_OPENED_FROM, ATTR_VALUE_FILE_EDITOR_CONTEXT_MENU);
    }

    public static void trackUploadWizardOpenedBeforeFunctionInvoke() {
        publishEventWithAttributes(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD, ATTR_NAME_OPENED_FROM, ATTR_VALUE_UPLOAD_BEFORE_INVOKE);
    }

    public static void trackUploadWizardOpenedFromProjectContextMenu() {
        publishEventWithAttributes(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD, ATTR_NAME_OPENED_FROM, ATTR_VALUE_PROJECT_CONTEXT_MENU);
    }

    public static void trackFunctionHandlerComboSelectionChange() {
        publishEventWithAttributes(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD, ATTR_NAME_CHANGE_SELECTION, ATTR_VALUE_FUNCTION_HANDLER_SELECTION_COMBO);
    }

    public static void trackRoleComboSelectionChange() {
        publishEventWithAttributes(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD, ATTR_NAME_CHANGE_SELECTION, ATTR_VALUE_IAM_ROLE_SELECTION_COMBO);
    }

    public static void trackS3BucketComboSelectionChange() {
        publishEventWithAttributes(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD, ATTR_NAME_CHANGE_SELECTION, ATTR_VALUE_S3_BUCKET_SELECTION_COMBO);
    }

    public static void trackRegionComboChangeSelection() {
        publishEventWithAttributes(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD, ATTR_NAME_CHANGE_SELECTION, ATTR_VALUE_REGION_SELECTION_COMBO);
    }

    public static void trackClickCreateNewRoleButton() {
        publishEventWithAttributes(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD, ATTR_NAME_CLICK, ATTR_VALUE_CREATE_IAM_ROLE_BUTTON);
    }

    public static void trackClickCreateNewBucketButton() {
        publishEventWithAttributes(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD, ATTR_NAME_CLICK, ATTR_VALUE_CREATE_S3_BUCKET_BUTTON);
    }

    public static void trackLoadRoleTimeDuration(long duration) {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD)
                .addMetric(METRIC_NAME_LOAD_IAM_ROLE_TIME_DURATION_MS, duration)
                .build());
    }

    public static void trackLoadBucketTimeDuration(long duration) {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD)
                .addMetric(METRIC_NAME_LOAD_S3_BUCKET_TIME_DURATION_MS, (double)duration)
                .build());
    }

    public static void trackMetrics(boolean isCreatingNewFunction, int validFunctionHandlerClassCount) {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD)
                .addBooleanMetric(METRIC_NAME_IS_CREATING_NEW_FUNCTION, isCreatingNewFunction)
                .addMetric(METRIC_NAME_VALID_FUNCTION_HANDLER_CLASS_COUNT, (double)validFunctionHandlerClassCount)
                .build());
    }

    public static void trackUploadSucceeded() {
        publishEventWithAttributes(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_SUCCEEDED);
    }

    public static void trackUploadFailed() {
        publishEventWithAttributes(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_FAILED);
    }

    public static void trackUploadCanceled() {
        publishEventWithAttributes(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD, ATTR_NAME_END_RESULT, ATTR_VALUE_CANCELED);
    }

    public static void trackUploadTotalTime(long uploadTotalTime) {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD)
                .addMetric(METRIC_NAME_UPLOAD_TOTAL_TIME_DURATION_MS, (double)uploadTotalTime)
                .build());
    }

    public static void trackUploadS3BucketTime(long uploadS3BucketTime) {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD)
                .addMetric(METRIC_NAME_UPLOAD_S3_BUCKET_TIME_DURATION_MS, (double)uploadS3BucketTime)
                .build());
    }

    public static void trackExportedJarSize(long exportedJarSize) {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD)
                .addMetric(METRIC_NAME_EXPORTED_JAR_SIZE, (double)exportedJarSize)
                .build());
    }

    public static void trackUploadS3BucketSpeed(double uploadS3BucketSpeed) {
        ANALYTICS.publishEvent(ANALYTICS.eventBuilder()
                .setEventType(EVENT_TYPE_UPLOAD_FUNCTION_WIZARD)
                .addMetric(METRIC_NAME_UPLOAD_S3_BUCKET_SPEED, uploadS3BucketSpeed)
                .build());
    }

    private static void publishEventWithAttributes(String eventType, String... attributes) {
        ToolkitEventBuilder builder = ANALYTICS.eventBuilder().setEventType(eventType);
        for (int i = 0; i < attributes.length; i += 2) {
            builder.addAttribute(attributes[i], attributes[i + 1]);
        }
        ANALYTICS.publishEvent(builder.build());
    }

}
