/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.mode.manager.cluster.coordinator.registry.config.watcher;

import org.apache.shardingsphere.infra.rule.event.GovernanceEvent;
import org.apache.shardingsphere.infra.util.spi.ShardingSphereServiceLoader;
import org.apache.shardingsphere.metadata.persist.node.GlobalNode;
import org.apache.shardingsphere.mode.event.DataChangedEvent;
import org.apache.shardingsphere.mode.event.DataChangedEvent.Type;
import org.apache.shardingsphere.mode.manager.cluster.coordinator.registry.NewGovernanceWatcher;
import org.apache.shardingsphere.mode.spi.GlobalRuleConfigurationEventBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * TODO Rename GlobalRuleChangedWatcher when metadata structure adjustment completed. #25485
 * Global rule changed watcher.
 */
public final class NewGlobalRuleChangedWatcher implements NewGovernanceWatcher<GovernanceEvent> {
    
    private static final Collection<GlobalRuleConfigurationEventBuilder> EVENT_BUILDERS = ShardingSphereServiceLoader.getServiceInstances(GlobalRuleConfigurationEventBuilder.class);
    
    @Override
    public Collection<String> getWatchingKeys(final String databaseName) {
        return Collections.singleton(GlobalNode.getGlobalRuleNode());
    }
    
    @Override
    public Collection<Type> getWatchingTypes() {
        return Arrays.asList(Type.ADDED, Type.UPDATED, Type.DELETED);
    }
    
    @Override
    public Optional<GovernanceEvent> createGovernanceEvent(final DataChangedEvent event) {
        return createGlobalRuleEvent(event);
    }
    
    private Optional<GovernanceEvent> createGlobalRuleEvent(final DataChangedEvent event) {
        for (GlobalRuleConfigurationEventBuilder each : EVENT_BUILDERS) {
            Optional<GovernanceEvent> result = each.build(event);
            if (!result.isPresent()) {
                continue;
            }
            return result;
        }
        return Optional.empty();
    }
}
