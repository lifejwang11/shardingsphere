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

package org.apache.shardingsphere.transaction.base.seata.at;

import io.seata.core.context.RootContext;
import org.apache.shardingsphere.infra.database.core.datasource.DataSourceMetaData;
import org.apache.shardingsphere.infra.executor.sql.hook.SQLExecutionHook;

import java.util.List;

/**
 * Seata transactional SQL execution hook.
 */
public final class SeataTransactionalSQLExecutionHook implements SQLExecutionHook {
    
    private boolean seataBranch;
    
    @Override
    public void start(final String dataSourceName, final String sql, final List<Object> params, final DataSourceMetaData dataSourceMetaData, final boolean isTrunkThread) {
        if (isTrunkThread) {
            if (RootContext.inGlobalTransaction()) {
                SeataXIDContext.set(RootContext.getXID());
            }
        } else if (!RootContext.inGlobalTransaction() && !SeataXIDContext.isEmpty()) {
            RootContext.bind(SeataXIDContext.get());
            seataBranch = true;
        }
    }
    
    @Override
    public void finishSuccess() {
        if (seataBranch) {
            RootContext.unbind();
        }
    }
    
    @Override
    public void finishFailure(final Exception cause) {
        if (seataBranch) {
            RootContext.unbind();
        }
    }
}
