/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.sqoop.manager.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;

import org.apache.sqoop.SqoopOptions;
import org.apache.sqoop.TestFreeFormQueryImport;
import org.apache.sqoop.testcategories.thirdpartytest.MysqlTest;
import org.junit.experimental.categories.Category;

/**
 * Test free form query import with the MySQL db.
 */
@Category(MysqlTest.class)
public class MySQLFreeFormQueryTest extends TestFreeFormQueryImport {

  public static final Log LOG = LogFactory.getLog(
      MySQLFreeFormQueryTest.class.getName());
  private MySQLTestUtils mySQLTestUtils = new MySQLTestUtils();

  @Override
  protected Log getLogger() {
    return LOG;
  }

  @Override
  protected boolean useHsqldbTestServer() {
    return false;
  }

  @Override
  protected String getConnectString() {
    return mySQLTestUtils.getMySqlConnectString();
  }

  @Override
  protected SqoopOptions getSqoopOptions(Configuration conf) {
    SqoopOptions opts = new SqoopOptions(conf);
    opts.setUsername(mySQLTestUtils.getUserName());
    mySQLTestUtils.addPasswordIfIsSet(opts);
    return opts;
  }

  @Override
  protected void dropTableIfExists(String table) throws SQLException {
    Connection conn = getManager().getConnection();
    PreparedStatement statement = conn.prepareStatement(
        "DROP TABLE IF EXISTS " + table,
        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    try {
      statement.executeUpdate();
      conn.commit();
    } finally {
      statement.close();
    }
  }
}
