package boris.service.resources;

import static junit.framework.Assert.assertEquals;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import boris.model.User;
import boris.service.representations.UserRepresentation;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class UsersResourceTest extends ResourceTest {

	private final WebResource usersResource = resource().path("users");

	private static final MultivaluedMap<String, String> FORM = new MultivaluedMapImpl();
	static {
		FORM.add("username", "testuser");
		FORM.add("realname", "Test User");
	}

	@Test
	public void postToUsersMustCreateUser() {
		usersResource.post(FORM);
		assertEquals("Test User", getUserRepository().getUser("testuser")
				.getRealname());
	}

	@Test
	public void postDuplicateUserMustFail() {
		usersResource.post(FORM);
		try {
			usersResource.post(FORM);
		} catch (UniformInterfaceException e) {
			assertEquals(ClientResponse.Status.FORBIDDEN, e.getResponse().getClientResponseStatus());
		}
	}

	@Test
	public void getUserMustReturnUser() {
		User expected = getUserRepository().createUser("testuser", "Test User");
		UserRepresentation actual = usersResource.path("testuser").get(UserRepresentation.class);
		assertEquals(expected.getUsername(), actual.getUsername());
		assertEquals(expected.getRealname(), actual.getRealname());
	}
}
