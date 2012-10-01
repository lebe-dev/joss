package nl.tweeenveertig.openstack.command.core;

public enum CommandExceptionError {
    UNKNOWN,
    ACCESS_FORBIDDEN,
    CONTAINER_ALREADY_EXISTS,
    CONTAINER_DOES_NOT_EXIST,
    CONTAINER_OR_OBJECT_DOES_NOT_EXIST,
    CONTAINER_NOT_EMPTY,
    CONTENT_NOT_MODIFIED,
    CONTENT_DIFFERENT,
    MD5_CHECKSUM,
    MISSING_CONTENT_LENGTH_OR_TYPE,
    NO_SERVICE_CATALOG_FOUND,
    NO_END_POINT_FOUND,
    UNAUTHORIZED
}
