/*
# Copyright © 2021 Argela Technologies
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
 */
package tr.com.argela.nfv.onap.serviceManager.onap.rest.model.serverInstance.k8s;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tr.com.argela.nfv.onap.serviceManager.onap.rest.model.serverInstance.Server;
import tr.com.argela.nfv.onap.serviceManager.onap.rest.model.serverInstance.ServerType;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
public class HelmInstance extends Server {

    public HelmInstance() {
        super(ServerType.POD);
    }

    ResourceBundle resourceBundle;
    String namespace, profileName, releaseName;
    Map<String, List<String>> k8sObjects = new HashMap<>();

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Map<String, List<String>> getK8sObjects() {
        return k8sObjects;
    }

    public void setK8sObjects(Map<String, List<String>> k8sObjects) {
        this.k8sObjects = k8sObjects;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

}
