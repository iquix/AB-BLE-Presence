/**
 *  DTH for AB BLE Presence Sensor (Version 0.1.0)
 *  Copyright 2021 iquix (Jaewon Park)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
    definition (name: "AB BLE Presence Sensor", namespace: "iquix", author: "iquix") {
        capability "Presence Sensor"
        capability "Sensor"
        capability "Health Check"

        command "see"
        command "checkPresence", ["Number"]
    }
    tiles {
        standardTile("presence", "device.presence", width: 2, height: 2, canChangeBackground: true) {
            state("not present", label:'not present', icon:"st.presence.tile.not-present", backgroundColor:"#ffffff", action:"arrived")
            state("present", label:'present', icon:"st.presence.tile.present", backgroundColor:"#00A0DC", action:"departed")
        }
        main "presence"
        details "presence"
    }
}

def parse(String description) {
}

def installed() {
    log.trace "installed()"
    sendEvent(name: "DeviceWatch-DeviceStatus", value: "online")
    sendEvent(name: "healthStatus", value: "online")
    sendEvent(name: "DeviceWatch-Enroll", value: [protocol: "cloud", scheme:"untracked"].encodeAsJson(), displayed: false)
    see()
}

def see() {
    //log.trace "see()"
    state.last_seen = now()
    sendEvent(name: "presence", value: "present")
}

def checkPresence(considerPresent) {
    //log.trace "checkPresence(${considerPresent})"
    if (now()-state.last_seen > considerPresent*1000) {
        //log.debug "marking as not present"
        sendEvent(name: "presence", value: "not present")
    }
}