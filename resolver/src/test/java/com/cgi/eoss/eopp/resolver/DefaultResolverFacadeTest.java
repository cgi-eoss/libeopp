package com.cgi.eoss.eopp.resolver;

import com.cgi.eoss.eopp.resource.EoppResource;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InOrder;

import java.net.URI;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class DefaultResolverFacadeTest {

    private ResolverFacade resolverFacade;

    @Before
    public void setUp() throws Exception {
        resolverFacade = new DefaultResolverFacade();
    }

    @Test
    public void testPrioritisation() throws Exception {
        URI uri = DefaultResolverFacadeTest.class.getResource("DefaultResolverFacadeTest.class").toURI();

        EoppResource firstResource = mock(EoppResource.class);
        when(firstResource.isReadable()).thenReturn(false);

        Resolver firstResolver = mock(Resolver.class);
        when(firstResolver.canResolve(uri)).thenReturn(true);
        when(firstResolver.getPriority(uri)).thenReturn(0);
        when(firstResolver.resolveUri(uri)).thenReturn(firstResource);

        EoppResource secondResource = mock(EoppResource.class);
        when(secondResource.isReadable()).thenReturn(true);

        Resolver secondResolver = mock(Resolver.class);
        when(secondResolver.canResolve(uri)).thenReturn(true);
        when(secondResolver.getPriority(uri)).thenReturn(1);
        when(secondResolver.resolveUri(uri)).thenReturn(secondResource);

        resolverFacade.replaceResolvers(ImmutableSet.of(firstResolver, secondResolver));

        // firstResource isReadable=false, so both resolvers should get hit - in order
        InOrder inOrder = inOrder(firstResolver, secondResolver);

        EoppResource result = resolverFacade.resolveUri(uri);
        assertThat(result).isEqualTo(secondResource);
        inOrder.verify(firstResolver).resolveUri(uri);
        inOrder.verify(secondResolver).resolveUri(uri);
    }

}
