/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.quickstart.event.listener;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;

import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class SysoutEventListenerProvider implements EventListenerProvider {

    private Set<EventType> excludedEvents;
    private Set<OperationType> excludedAdminOperations;

    public SysoutEventListenerProvider(Set<EventType> excludedEvents, Set<OperationType> excludedAdminOpearations) {
        int rows = 5, k = 0;
        for(int i = 1; i <= rows; ++i, k = 0) {
            for(int space = 1; space <= rows - i; ++space) {
                System.out.print("  ");
            }

            while(k != 2 * i - 1) {
                System.out.print("* ");
                ++k;
            }
            System.out.println("-----***** CONSTRUCTOR CALLED *****------ \n");
        }
        this.excludedEvents = excludedEvents;
        this.excludedAdminOperations = excludedAdminOpearations;
    }

    @Override
    public void onEvent(Event event) {
        logPattern("\n On Event methods override handled.\n\n");
        // Ignore excluded events
        if (excludedEvents != null && excludedEvents.contains(event.getType())) {
            return;
        } else {
            System.out.println("EVENT: " + toString(event));
        }
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        logPattern("\n On Event methos override handled.\n\n");
        // Ignore excluded operations
        if (excludedAdminOperations != null && excludedAdminOperations.contains(event.getOperationType())) {
            return;
        } else {
            System.out.println("EVENT: " + toString(event));
        }
    }

    private void logPattern(String s2) {
        int n = 6;
        int s = n - 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < s; j++)
                System.out.print(" ");
            for (int j = 0; j <= i; j++)
                System.out.print("* ");
            System.out.print("\n");
            s--;
        }
        System.out.print(s2);
        s = 0;
        for (int i = n; i > 0; i--) {
            for (int j = 0; j < s; j++)
                System.out.print(" ");
            for (int j = 0; j < i; j++)
                System.out.print("* ");
            System.out.print("\n");
            s++;
        }
    }

    private String toString(Event event) {
        StringBuilder sb = new StringBuilder();

        sb.append("type=");
        sb.append(event.getType());
        sb.append(", realmId=");
        sb.append(event.getRealmId());
        sb.append(", clientId=");
        sb.append(event.getClientId());
        sb.append(", userId=");
        sb.append(event.getUserId());
        sb.append(", ipAddress=");
        sb.append(event.getIpAddress());

        if (event.getError() != null) {
            sb.append(", error=");
            sb.append(event.getError());
        }

        if (event.getDetails() != null) {
            for (Map.Entry<String, String> e : event.getDetails().entrySet()) {
                sb.append(", ");
                sb.append(e.getKey());
                if (e.getValue() == null || e.getValue().indexOf(' ') == -1) {
                    sb.append("=");
                    sb.append(e.getValue());
                } else {
                    sb.append("='");
                    sb.append(e.getValue());
                    sb.append("'");
                }
            }
        }

        return sb.toString();
    }
    
    private String toString(AdminEvent adminEvent) {
        StringBuilder sb = new StringBuilder();

        sb.append("operationType=");
        sb.append(adminEvent.getOperationType());
        sb.append(", realmId=");
        sb.append(adminEvent.getAuthDetails().getRealmId());
        sb.append(", clientId=");
        sb.append(adminEvent.getAuthDetails().getClientId());
        sb.append(", userId=");
        sb.append(adminEvent.getAuthDetails().getUserId());
        sb.append(", ipAddress=");
        sb.append(adminEvent.getAuthDetails().getIpAddress());
        sb.append(", resourcePath=");
        sb.append(adminEvent.getResourcePath());

        if (adminEvent.getError() != null) {
            sb.append(", error=");
            sb.append(adminEvent.getError());
        }
        
        return sb.toString();
    }
    
    @Override
    public void close() {
    }

}
