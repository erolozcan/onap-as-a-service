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
package tr.com.argela.nfv.onap.serviceManager.onap.rest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tr.com.argela.nfv.onap.serviceManager.onap.OnapUtil;
import tr.com.argela.nfv.onap.serviceManager.onap.adaptor.model.OnapRequest;
import tr.com.argela.nfv.onap.serviceManager.onap.adaptor.OnapAdaptor;
import tr.com.argela.nfv.onap.serviceManager.onap.adaptor.model.OnapRequestParameters;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@RestController
public class CloudService {

    @Autowired
    OnapAdaptor adaptor;

    Logger log = LoggerFactory.getLogger(CloudService.class);

    @GetMapping(path = "/cloud/complexs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCloudComplexs() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_COMPLEXS);
        log.info("[Cloud][Complexs][Get] size:" + adaptor.getResponseSize(data, "complex"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/cloud/complexs/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCloudComplex(@PathVariable String name) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_COMPLEX_NAME.name(), name);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_COMPLEXS);
        log.info("[Cloud][Complex][Get] " + parameters + " , result : " + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/cloud/complex/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCloudComplex(@PathVariable String name) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_COMPLEX_NAME.name(), name);
        String data = (String) adaptor.call(OnapRequest.CLOUD_COMPLEX_CREATE, parameters);
        log.info("[Cloud][Complex][Create] " + parameters + " , response:" + data);
        return ResponseEntity.ok(data);
    }

    @GetMapping(path = "/cloud/regions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCloudRegions() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_REGIONS);
        log.info("[Cloud][Regions][Get] size:" + adaptor.getResponseSize(data, "cloud-region"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/cloud/region/{cloudOwner}/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCloudRegion(@PathVariable String cloudOwner, @PathVariable String name) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_NAME.name(), name);
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_REGION, parameters);
        log.info("[Cloud][Region][Get] " + parameters + " , response : " + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/cloud/openstack/{cloudOwner}/{name}/{regionName}/{complexName}/{osDomain}/{osDefaultProject}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createOpenstackRegion(@PathVariable String cloudOwner, @PathVariable String name, @PathVariable String regionName, @PathVariable String complexName,
            @PathVariable String osDomain, @PathVariable String osDefaultProject,
            @RequestParam(name = "keystoneURL") String osKeystoneURL, @RequestParam(name = "user") String osUser, @RequestParam(name = "password") String osPassword) throws IOException {

        return ResponseEntity.ok(createRegion(OnapRequest.CLOUD_REGION_CREATE.getPayloadFilePath(), name, regionName, cloudOwner, complexName, osDomain, osDefaultProject, osKeystoneURL, osUser, osPassword));
    }

    public String createRegion(String payload, String name, String regionName, String cloudOwner, String complexName,
            String osDomain, String osDefaultProject,
            String osKeystoneURL, String osUser, String osPassword) throws IOException {
        Map<String, String> parameters = new HashMap<>();

        UUID esrUUID = UUID.randomUUID();

        parameters.put(OnapRequestParameters.CLOUD_ESR_UUID.name(), esrUUID.toString());
        parameters.put(OnapRequestParameters.CLOUD_NAME.name(), name);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), regionName);
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_COMPLEX_NAME.name(), complexName);
        parameters.put(OnapRequestParameters.CLOUD_COMPLEX_NAME.name(), complexName);
        parameters.put(OnapRequestParameters.CLOUD_OS_KEYSTONE_URL.name(), osKeystoneURL);
        parameters.put(OnapRequestParameters.CLOUD_OS_USER.name(), osUser);
        parameters.put(OnapRequestParameters.CLOUD_OS_PASSWORD.name(), osPassword);
        parameters.put(OnapRequestParameters.CLOUD_OS_DOMAIN.name(), osDomain);
        parameters.put(OnapRequestParameters.CLOUD_OS_PROJECT.name(), osDefaultProject);
        String data = (String) adaptor.call(OnapRequest.CLOUD_REGION_CREATE, parameters, null, payload);
        log.info("[Cloud][CloudRegion][Create] " + parameters + " , response : " + data);
        return data;
    }

    @PutMapping(path = "/cloud/k8s/{cloudOwner}/{name}/{complex}/{namespace}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createK8SRegion(
            @PathVariable String cloudOwner,
            @PathVariable String name,
            @PathVariable String complex,
            @PathVariable String namespace,
            @RequestBody String kubeconfig
    ) throws IOException {

        String body = "{\"cloud-region\":\"" + name + "\",\"cloud-owner\":\"" + cloudOwner + "\",\"other-connectivity-list\":{\"connectivity-records\":[{\"ssl-initiator\":\"false\"}]}};type=application/json";
        File kubeConfigFile = OnapUtil.writeStringToTmpFile(kubeconfig, "k8s_config", ".json");
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_NAME.name(), name);
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_COMPLEX_NAME.name(), complex);

        Map<String, Object> files = new HashMap<>();
        files.put("metadata", body);
        files.put("file", kubeConfigFile);

        String data = (String) adaptor.call(OnapRequest.CLOUD_K8S_MSB_ADD_KUBECONFIG, parameters, files);

        kubeConfigFile.delete();
        String data2 = (String) createRegion("payloads/cloud/region_k8s_create.json", name, name, cloudOwner, complex, null, namespace, null, null, null);
        log.info("[Cloud][K8S][Create] " + parameters);
        return ResponseEntity.ok(data);
    }

    @PutMapping(path = "/cloud/region-complex/{cloudOwner}/{name}/{complex}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCloudRegionComplexRelations(
            @PathVariable String cloudOwner,
            @PathVariable String name,
            @PathVariable String complex
    ) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_COMPLEX_NAME.name(), complex);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), name);
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        String data = (String) adaptor.call(OnapRequest.CLOUD_REGION_COMPLEX_Relations, parameters);
        log.info("[Cloud][ComplexRegionRelation][Create] " + parameters + " , response:" + data);
        return ResponseEntity.ok(data);
    }

    @GetMapping(path = "/cloud/tenants/{cloudOwner}/{cloudRegion}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCloudTenants(@PathVariable String cloudOwner, @PathVariable String cloudRegion) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_TENANTS, parameters);
        log.info("[Cloud][Tenants][Get] " + parameters + " , size:" + adaptor.getResponseSize(data, "tenant"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/cloud/tenant/{cloudOwner}/{cloudRegion}/{tenantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCloudTenant(@PathVariable String cloudOwner, @PathVariable String cloudRegion, @PathVariable String tenantId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_ID.name(), tenantId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_TENANT, parameters);
        log.info("[Cloud][Tenant][Get] " + parameters + ", response : " + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/cloud/tenants/{cloudOwner}/{cloudRegion}/{tenantId}/{tenantName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCloudTenant(
            @PathVariable String cloudOwner,
            @PathVariable String cloudRegion,
            @PathVariable String tenantId,
            @PathVariable String tenantName
    ) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_ID.name(), tenantId);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_NAME.name(), tenantName);
        String data = (String) adaptor.call(OnapRequest.CLOUD_TENANT_CREATE, parameters);
        log.info("[Cloud][Tenant][Create] " + parameters + " , response : " + data);
        return ResponseEntity.ok(data);
    }

    @GetMapping(path = "/cloud/availability-zones/{cloudOwner}/{cloudRegion}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCloudAvailabilityZones(@PathVariable String cloudOwner, @PathVariable String cloudRegion) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_AVAILABILITY_ZONES, parameters);
        log.info("[Cloud][AvailabilityZones][Get] " + parameters + " , size:" + adaptor.getResponseSize(data, "availability-zone"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/cloud/availability-zone/{cloudOwner}/{cloudRegion}/{availabilityZone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCloudAvailabilityZone(@PathVariable String cloudOwner, @PathVariable String cloudRegion, @PathVariable String availibilityZone) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        parameters.put(OnapRequestParameters.CLOUD_AVAILABILITY_ZONE.name(), availibilityZone);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_AVAILABILITY_ZONE, parameters);
        log.info("[Cloud][AvailabilityZone][Get] " + parameters + " , " + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/cloud/availability-zone/{cloudOwner}/{cloudRegion}/{availabilityZone}/{hypervisorType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCloudAvailibilityZone(
            @PathVariable String cloudOwner,
            @PathVariable String cloudRegion,
            @PathVariable String azName,
            @PathVariable String azHypervisorType
    ) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        parameters.put(OnapRequestParameters.CLOUD_AVAILABILITY_ZONE.name(), azName);
        parameters.put(OnapRequestParameters.CLOUD_HYPERVISOR_TYPE.name(), azHypervisorType);
        String data = (String) adaptor.call(OnapRequest.CLOUD_AVAILABILITY_ZONE_CREATE, parameters);
        log.info("[Cloud][AvailabilityZone][Create] " + parameters + " , response : " + data);
        return ResponseEntity.ok(data);
    }

    @GetMapping(path = "/cloud/vserver/{cloudOwner}/{regionName}/{tenantId}/{vServerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVServerDetail(
            @PathVariable(required = true) String cloudOwner,
            @PathVariable(required = true) String regionName,
            @PathVariable(required = true) String tenantId,
            @PathVariable(required = true) String vServerId
    ) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), regionName);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_ID.name(), tenantId);
        parameters.put(OnapRequestParameters.CLOUD_VSERVER_ID.name(), vServerId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_VSERVER_DETAIL, parameters);
        log.info("[Cloud][VServer][Get] " + parameters);
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/cloud/flavor/{cloudOwner}/{regionName}/{flavorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getFlavorDetail(
            @PathVariable(required = true) String cloudOwner,
            @PathVariable(required = true) String regionName,
            @PathVariable(required = true) String flavorId
    ) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), regionName);
        parameters.put(OnapRequestParameters.CLOUD_OS_FLAVOR_ID.name(), flavorId);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_VSERVER_FLAVOR_DETAIL, parameters);
        log.info("[Cloud][Flavor][Get] " + parameters);
        return ResponseEntity.ok(data.toString());
    }
}
