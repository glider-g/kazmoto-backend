package kz.kazmoto.rest.filter;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ext.Provider;

@Provider
public class RestRolesFeature extends RolesAllowedDynamicFeature {
}
