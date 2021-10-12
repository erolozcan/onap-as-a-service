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
package tr.com.argela.nfv.onap.serviceManager.onap.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
public class AvailabilityZone {

    String name;
    String hypervisorType;

    @JsonIgnore
    CloudRegion cloudRegion;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHypervisorType() {
        return hypervisorType;
    }

    public void setHypervisorType(String hypervisorType) {
        this.hypervisorType = hypervisorType;
    }

    public CloudRegion getCloudRegion() {
        return cloudRegion;
    }

    public void setCloudRegion(CloudRegion cloudRegion) {
        this.cloudRegion = cloudRegion;
    }

    @Override
    public String toString() {
        return "AvailabilityZone{" + "name=" + name + ", hypervisorType=" + hypervisorType + '}';
    }

}
