package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.command.account.ListContainersCommand;
import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.exception.NotFoundException;
import nl.tweeenveertig.openstack.instructions.ListInstructions;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoredObject;
import nl.tweeenveertig.openstack.util.ClasspathTemplateResource;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListObjectsCommandTest extends BaseCommandTest {

    public static final ListInstructions listInstructions = new ListInstructions()
            .setMarker(null)
            .setLimit(10);

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void listObjects() throws IOException {
        new ListObjectsCommand(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions).call();
    }

    @Test
    public void listObjectsWithNoneThere() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        new ListObjectsCommand(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions).call();
    }

    @Test (expected = NotFoundException.class)
    public void containerDoesNotExist() throws IOException {
        checkForError(404, new ListObjectsCommand(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new ListObjectsCommand(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ListObjectsCommand(this.account, httpClient, defaultAccess, account.getContainer("containername"), listInstructions));
    }

    @Test
    public void queryParameters() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        new ListObjectsCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"),
                new ListInstructions().setMarker("dogs").setLimit(10)).call();
        verify(httpClient).execute(requestArgument.capture());
        String assertQueryParameters = "?marker=dogs&limit=10";
        String uri = requestArgument.getValue().getURI().toString();
        assertTrue(uri+" must contain "+assertQueryParameters, uri.contains(assertQueryParameters));
    }

    @Test
    public void setHeaderValues() throws IOException {
        when(httpEntity.getContent()).thenReturn(
                IOUtils.toInputStream(new ClasspathTemplateResource("/sample-object-list.json").loadTemplate()));
        when(statusLine.getStatusCode()).thenReturn(204);
        Collection<StoredObject> objects =
                new ListObjectsCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), listInstructions).call();
        assertEquals(4, objects.size());
        StoredObject object = objects.iterator().next();
        assertEquals("image/jpeg", object.getContentType());
        assertEquals("c8fc5698c0b3ca145f2c98937cbd9ff2", object.getEtag());
        assertEquals("Wed, 05 Dec 2012 13:57:00 GMT", object.getLastModified());
        assertEquals(22979, object.getContentLength());
    }

}
