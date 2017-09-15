package org.javaswift.joss.command.shared.container;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoredObjectListElement {

    public String subdir;

    public String name;

    public String hash;

    public long bytes;

    @JsonProperty(value="content_type")
    public String contentType;

    @JsonProperty(value="last_modified")
    public String lastModified;

}
