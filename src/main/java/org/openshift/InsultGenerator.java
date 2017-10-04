package org.openshift;

import java.sql.*;

public class InsultGenerator {

	public String generateInsult() {
		String vowels = "AEIOU";
		String article = "an";
		String theInsult = "";

		try {
		    String databaseUrl = "jdbc:postgresql://";
		    databaseUrl += System.getenv("POSTGRESQL_SERVICE_HOST");
		    databaseUrl += "/" + System.getenv("POSTGRESQL_DATABSE");

            String username = System.getenv("POSTGRESQL_USER");
            String password = System.getenv("PGPASSWORD");

            Connection connection = DriverManager.getConnection(databaseUrl, username, password);
            if (connection != null) {
                String sql = "select a.string as first, b.string as second, c.string as noun from short_adjective a, long_adjective b, noun c order by random() limit 1";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    if (vowels.indexOf(rs.getString("first").charAt(0)) == -1) {
                        article = "a";
                    }
                    theInsult = String.format("Thou art %s %s %s %s!", article, rs.getString("first"), rs.getString("second"), rs.getString("noun"));
                }
                rs.close();
                connection.close();
            }
        }
        catch (SQLException e) {
            return "Database connection problem";
        }

        return theInsult;
    }

}
