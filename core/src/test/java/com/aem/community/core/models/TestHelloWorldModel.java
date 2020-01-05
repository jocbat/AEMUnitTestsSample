/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aem.community.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Inject;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junitx.framework.Assert.assertEquals;


/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@RunWith(MockitoJUnitRunner.class)
public class TestHelloWorldModel {
    public static final String ROOT_PATH = "/content/dam/AEMMaven13";
    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_OAK);

    private HelloWorldModel hello;
    
    @Before
    public void setup() throws Exception {
        hello = new HelloWorldModel();
        hello.resourceResolver = context.resourceResolver();
    }

    @Test
    public void given_application_dam_folder_getFolders_returns_all_folders() {
        context.load().json("/dam_folders_normal_case.json",
                ROOT_PATH);

        List<String> folders = hello.getFolders();

        assertEquals(9, folders.size());
        assertTrue(folders.contains("folder1"));
        assertTrue(folders.contains("subfolder11"));
        assertTrue(folders.contains("subfolder111"));
        assertTrue(folders.contains("subfolder12"));
        assertTrue(folders.contains("subfolder13"));
        assertTrue(folders.contains("folder2"));
        assertTrue(folders.contains("subfolder21"));
        assertTrue(folders.contains("subfolder22"));
        assertTrue(folders.contains("folder3"));
    }

    @Test
    public void given_empty_dam_folder_getFolders_returns_empty_folders_list() {
        context.load().json("/dam_folders_empty_root.json",
                ROOT_PATH);

        List<String> folders = hello.getFolders();

        assertEquals(0, folders.size());
    }
}
