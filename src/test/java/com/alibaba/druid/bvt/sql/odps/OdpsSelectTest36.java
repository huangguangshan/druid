/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.bvt.sql.odps;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import junit.framework.TestCase;

import java.util.List;

public class OdpsSelectTest36 extends TestCase {
    public void test_select() throws Exception {
        // 1095288847322
        String sql = "select *\n" +
                "from (\n" +
                "\tselect 1\n" +
                ") \n" +
                "union all\n" +
                "select 2;";//
        assertEquals("SELECT *\n" +
                "FROM (\n" +
                "\tSELECT 1\n" +
                ")\n" +
                "UNION ALL\n" +
                "SELECT 2;", SQLUtils.formatOdps(sql));

        assertEquals("select *\n" +
                "from (\n" +
                "\tselect 1\n" +
                ")\n" +
                "union all\n" +
                "select 2;", SQLUtils.formatOdps(sql, SQLUtils.DEFAULT_LCASE_FORMAT_OPTION));

        List<SQLStatement> statementList = SQLUtils.parseStatements(sql, JdbcConstants.ODPS);
        SQLStatement stmt = statementList.get(0);

        assertEquals(1, statementList.size());

        SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(JdbcConstants.ODPS);
        stmt.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
//      System.out.println("coditions : " + visitor.getConditions());
//      System.out.println("orderBy : " + visitor.getOrderByColumns());

        assertEquals(0, visitor.getTables().size());
        assertEquals(0, visitor.getColumns().size());
        assertEquals(0, visitor.getConditions().size());

//        System.out.println(SQLUtils.formatOdps(sql));

//        assertTrue(visitor.getColumns().contains(new Column("abc", "name")));
    }

}
