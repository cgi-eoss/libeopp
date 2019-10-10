package com.cgi.eoss.eopp.identifier;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;

/**
 * <p>Utility methods for working with {@link Identifier} objects.</p>
 */
public final class Identifiers {

    private Identifiers() {
        // no-op for utility class
    }

    /**
     * @return The {@link Identifier} parsed from the given "<code>{identifier}:{version}</code>" string.
     */
    public static Identifier parse(String unifiedIdentifier) {
        String[] idFields = unifiedIdentifier.split(":");
        if (idFields.length != 2) {
            throw new IllegalArgumentException(String.format("Unified identifier '%s' did not match the {identifier}:{version} format", unifiedIdentifier));
        }
        return Identifier.newBuilder().setIdentifier(idFields[0]).setVersion(idFields[1]).build();
    }

    /**
     * @return The {@link Identifier} extracted from the given proto message's <code>identifier</code> and
     * <code>version</code> fields.
     */
    public static Identifier from(Message message) {
        Descriptors.FieldDescriptor identifierField = message.getDescriptorForType().findFieldByName("identifier");
        Descriptors.FieldDescriptor versionField = message.getDescriptorForType().findFieldByName("version");
        if (identifierField == null || versionField == null) {
            String missingFields = identifierField == null ? " identifier" : "";
            missingFields += versionField == null ? " version" : "";
            throw new IllegalArgumentException(String.format("Message did not contain required field(s):%s", missingFields));
        }
        return Identifier.newBuilder()
                .setIdentifier(message.getField(identifierField).toString())
                .setVersion(message.getField(versionField).toString())
                .build();
    }

}
