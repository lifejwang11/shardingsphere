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

package org.apache.shardingsphere.infra.binder.statement.dcl;

import org.apache.shardingsphere.infra.binder.context.statement.CommonSQLStatementContext;
import org.apache.shardingsphere.infra.binder.context.statement.dcl.DenyUserStatementContext;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.SimpleTableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.TableNameSegment;
import org.apache.shardingsphere.sql.parser.sql.common.value.identifier.IdentifierValue;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.sqlserver.dcl.SQLServerDenyUserStatement;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DenyUserStatementContextTest {
    
    @Test
    void assertNewInstance() {
        SQLServerDenyUserStatement sqlServerDenyUserStatement = mock(SQLServerDenyUserStatement.class);
        SimpleTableSegment table = new SimpleTableSegment(new TableNameSegment(0, 0, new IdentifierValue("tbl_1")));
        when(sqlServerDenyUserStatement.getTable()).thenReturn(table);
        DenyUserStatementContext actual = new DenyUserStatementContext(sqlServerDenyUserStatement);
        assertThat(actual, instanceOf(CommonSQLStatementContext.class));
        assertThat(actual.getSqlStatement(), is(sqlServerDenyUserStatement));
        assertThat(actual.getAllTables().stream().map(each -> each.getTableName().getIdentifier().getValue()).collect(Collectors.toList()), is(Collections.singletonList("tbl_1")));
    }
}
