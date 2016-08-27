import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/test")
@Consumes("application/json")
public class Test {
	public static Connection connection = null;

	// Name of server computer (set in the constructor, and returned in custom
	// HTTP headers from our RESTful methods).
	private String hostName;

	// Constructor, sets the name of the server computer.

	public Test() throws SQLException {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager
					.getConnection("jdbc:mysql://192.168.99.53:3306/mydatabase?user=finley&password=password");
			hostName = InetAddress.getLocalHost().getHostName();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException ex) {
			hostName = "Unknown";
		} finally {
		}
	}

	@GET
	@Produces("application/json")
	public static List<Books> queryData() throws SQLException {

		String sql = "SELECT Isbn, Title, Price FROM mydatabase.Books";

		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		List<Books> books = new ArrayList<Books>();
		while (rs.next() != false) {
			Books book = new Books();
			book.setIsbn(rs.getInt(1));
			book.setTitle(rs.getString(2));
			book.setPrice(rs.getBigDecimal(3));
			System.out.println("ISBN:  " + rs.getInt(1));
			System.out.println("Title: " + rs.getString(2));
			System.out.println("Price: " + rs.getBigDecimal(3));
			System.out.println();
			books.add(book);
		}
		return books;

	}

	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
	public Response insertProductObject(Books product) throws SQLException {

		// Try to get the "products" collection from application state.
		List<Books> books = Test.queryData();

		String sql = "Insert into mydatabase.Books values(" + product.getIsbn() + ",'" + product.getTitle() + "',"
				+ product.getPrice() + ")";
		System.out.println(sql);
		Statement st = connection.createStatement();
		int rs = st.executeUpdate(sql);

		if (rs > 0) {
			System.out.println("�nserted into the database");
		}
		URI uri = null;
		try {
			uri = new URI("/" + product.getIsbn());
		} catch (URISyntaxException ex) {
		}

		// Build and return an HTTP response. Note the following points:
		// - The created() method implicitly sets the HTTP status code to 201.
		// - The created() method takes a parameter that indicates the URI of
		// the newly inserted Product object.
		// - The entity() method returns the inserted Product object (as a
		// string).
		//
		// Note, Response.created(uri) is equivalent to
		// Response.status(Status.CREATED).location(uri).
		return Response.created(uri).entity(product.toString()).header("Custom_HostName", hostName)
				.header("Custom_Timestamp", new Date().toString()).build();
	}
}
