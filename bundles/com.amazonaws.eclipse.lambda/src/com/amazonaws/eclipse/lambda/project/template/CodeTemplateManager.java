/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
package com.amazonaws.eclipse.lambda.project.template;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;

import com.amazonaws.eclipse.lambda.LambdaPlugin;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class CodeTemplateManager {

    private static final String CODE_TEMPLATE_DIR_BASEDIR = "code-template";
    private static final String BLUEPRINT_CONFIG_PATH = "serverless/blueprint/blueprint.json";
    private static final String SERVERLESS_README_FILE_PATH = "serverless/README.html";

    private final Configuration freemarkerCfg;

    private CodeTemplateManager() {
        freemarkerCfg = setupFreemarker();
    }

    public static String processTemplateWithData(Template template,
            Object templateData) throws TemplateException, IOException {
        StringWriter sw = new StringWriter();
        template.process(templateData, sw);
        sw.flush();
        return sw.toString();
    }

    public Template getTemplate(String templatePath) {
        try {
            return freemarkerCfg.getTemplate(templatePath);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load Freemarker template from " + templatePath, e);
        }
    }

    public Template getServerlessInputClassTemplate() {
        try {
            return freemarkerCfg.getTemplate("serverless/serverless-input.ftl");
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load Serverless Input class template", e);
        }
    }

    public Template getServerlessOutputClassTemplate() {
        try {
            return freemarkerCfg.getTemplate("serverless/serverless-output.ftl");
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load Serverless Input class template", e);
        }
    }

    public Template getServerlessHandlerClassTemplate() {
        try {
            return freemarkerCfg.getTemplate("serverless/serverless-handler.ftl");
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load Serverless Input class template", e);
        }
    }

    /**
     * Handler class template
     */
    public Template getHandlerClassTemplate() {
        try {
            return freemarkerCfg.getTemplate("handler/handler-class-template.ftl");
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load handler class template.", e);
        }
    }

    /**
     * Stream handler class template
     */
    public Template getStreamHandlderClassTemplate() {
        try {
            return freemarkerCfg.getTemplate("handler/stream-handler-class-template.ftl");
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load stream handler class template.", e);
        }
    }

    /**
     * Handler test code template
     */
    public Template getHandlerTestClassTemplate() {
        try {
            return freemarkerCfg.getTemplate("test-class/handler-test-code-template.ftl");
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load handler test code template.", e);
        }
    }

    /**
     * Stream handler test code template
     */
    public Template getStreamHandlerTestClassTemplate() {
        try {
            return freemarkerCfg.getTemplate("test-class/stream-handler-test-code-template.ftl");
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load stream handler test code template.", e);
        }
    }

    /**
     * {@code TestContext} class template.
     */
    public Template getTestContextTemplate() throws IOException {
        return freemarkerCfg.getTemplate("test-class/test-context.ftl");
    }

    /**
     * {@code TestUtils} class template.
     */
    public Template getTestUtilsTemplate() throws IOException {
        return freemarkerCfg.getTemplate("test-class/test-utils.ftl");
    }

    /**
     * Event input json files
     */
    public Template getTestInputJsonFileTemplate(String filename) {
        try {
            return freemarkerCfg.getTemplate("test-resource/" + filename);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load handler test code template.", e);
        }
    }

    public Template getReadmeHtmlFileTemplate() {
        try {
            return freemarkerCfg.getTemplate("README/README.ftl");
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load README.html template.", e);
        }
    }

    private Configuration setupFreemarker() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);
        try {
            cfg.setDirectoryForTemplateLoading(getCodeTemplateBasedir());
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to setup freemarker template directory.", e);
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        return cfg;
    }

    public File getCodeTemplateBasedir() {
        Bundle bundle = LambdaPlugin.getDefault().getBundle();
        File file = null;
        try {
            URL bundleBaseUrl = FileLocator.toFileURL(bundle.getEntry(CODE_TEMPLATE_DIR_BASEDIR));
            URI bundleBaseUri = new URI(bundleBaseUrl.getProtocol(), bundleBaseUrl.getPath(), null);
            file = new File(bundleBaseUri);
        } catch (IOException e) {
            throw new RuntimeException("Failed to find plugin bundle root.", e);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to find plugin bundle root.", e);
        }
        return file;
    }

    public File getBlueprintConfigFile() {
        return new File(getCodeTemplateBasedir(), BLUEPRINT_CONFIG_PATH);
    }

    public File getServerlessReadmeFile() {
        return new File(getCodeTemplateBasedir(), SERVERLESS_README_FILE_PATH);
    }

    private static final CodeTemplateManager INSTANCE = new CodeTemplateManager();
    public static CodeTemplateManager getInstance() {
        return INSTANCE;
    }

}
