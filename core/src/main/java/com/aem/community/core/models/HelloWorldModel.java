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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import com.day.cq.commons.jcr.JcrUtil;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.settings.SlingSettingsService;

import java.util.ArrayList;
import java.util.List;

@Model(adaptables=Resource.class)
public class HelloWorldModel {

    public static final String ROOT_PATH = "/content/dam/AEMMaven13";
    public static final String JCR_PRIMARY_TYPE = "jcr:primaryType";
    public static final String SLING_FOLDER = "sling:Folder";
    @Inject
    private SlingSettingsService settings;

    @Inject @Named("sling:resourceType") @Default(values="No resourceType")
    protected String resourceType;

    private String message;

    @Inject
    protected ResourceResolver resourceResolver;

    @PostConstruct
    protected void init() {
        message = "\tHello World!\n";
        message += "\tThis is instance: " + settings.getSlingId() + "\n";
        message += "\tResource type is: " + resourceType + "\n";
    }

    public String getFoldersList(){
        List<String> folders = getFolders();
        return folders.stream()
                .reduce((e1, e2) -> e1 + " " + e2).get();
    }

    public List<String> getFolders() {
        ArrayList<String> returnedTitles = new ArrayList<>();
        try{

            Node node = resourceResolver.getResource(ROOT_PATH).adaptTo(Node.class);
            List<Node> allSubNodes = getAllSubFolders(node);
            for(Node currentNode : allSubNodes){
                returnedTitles.add(currentNode.getName());
            }

        }catch(Exception e){
            // My logs...
        }
        return returnedTitles;

    }

    private List<Node> getAllSubFolders(Node node){
        ArrayList<Node> returnedNodes = new ArrayList<>();
        try {
            if(isLeaf(node)){
                if(isFolder(node)) {
                    returnedNodes.add(node);
                }
            }
            else{
                NodeIterator nodes = node.getNodes();
                if(isFolder(node)){
                    returnedNodes.add(node);
                }
                while (nodes.hasNext()){
                    Node currentNode = nodes.nextNode();
                    returnedNodes.addAll(getAllSubFolders(currentNode));
                }
            }

        } catch (RepositoryException e) {
            // My logs
        }
        return returnedNodes;
    }

    private boolean isLeaf(Node node) throws RepositoryException {
        return !node.hasNodes();
    }

    private boolean isFolder(Node node) {
        boolean isFolder = false;
        try {
            isFolder = node.hasProperty(JCR_PRIMARY_TYPE)
                    && SLING_FOLDER.equals(
                            node.getProperty(JCR_PRIMARY_TYPE).getString()
            );
        }catch (RepositoryException e) {
            // My logs...
        }
        return isFolder;
    }
}
