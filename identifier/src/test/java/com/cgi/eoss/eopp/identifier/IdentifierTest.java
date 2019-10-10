package com.cgi.eoss.eopp.identifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class IdentifierTest {

    @Test
    public void testParse() {
        Identifier identifier = Identifiers.parse("myId:myVersion");
        assertThat(identifier).isEqualTo(Identifier.newBuilder().setIdentifier("myId").setVersion("myVersion").build());
    }

    @Test
    public void testParseFailure() {
        try {
            Identifiers.parse("invalidIdentifier");
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageThat().isEqualTo("Unified identifier 'invalidIdentifier' did not match the {identifier}:{version} format");
        }
    }

    @Test
    public void testFrom() {
        Identifier identifier = Identifiers.from(IdentifierTestMessage.newBuilder().setIdentifier("myId").setVersion("myVersion").build());
        assertThat(identifier).isEqualTo(Identifier.newBuilder().setIdentifier("myId").setVersion("myVersion").build());
    }

    @Test
    public void testFromFailures() {
        try {
            Identifiers.from(IdentifierTestMissingVersion.newBuilder()
                    .setIdentifier("myId")
                    .setNotAVersion("myVersion")
                    .build());
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageThat().isEqualTo("Message did not contain required field(s): version");
        }

        try {
            Identifiers.from(IdentifierTestMissingIdentifier.newBuilder()
                    .setNotAnIdentifier("myId")
                    .setVersion("myVersion")
                    .build());
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageThat().isEqualTo("Message did not contain required field(s): identifier");
        }

        try {
            Identifiers.from(IdentifierTestMissingBoth.newBuilder()
                    .setNotAnIdentifier("myId")
                    .setNotAVersion("myVersion")
                    .build());
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageThat().isEqualTo("Message did not contain required field(s): identifier version");
        }
    }

}