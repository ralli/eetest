package de.fisp.eetest.test.util;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.RuntimeDelegate;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TestRuntimeDelegate extends RuntimeDelegate {
  @Override
  public UriBuilder createUriBuilder() {
    return null;
  }

  @Override
  public Response.ResponseBuilder createResponseBuilder() {
    return new Response.ResponseBuilder() {
      private int status;
      public Object entity;
      public String type;

      @Override
      public Response build() {
        return new Response() {
          @Override
          public Object getEntity() {
            return entity;
          }

          @Override
          public int getStatus() {
            return status;
          }

          @Override
          public MultivaluedMap<String, Object> getMetadata() {
             return null;
          }
        };
      }

      @Override
      public Response.ResponseBuilder clone() {
        return this;
      }

      @Override
      public Response.ResponseBuilder status(int status) {
        this.status = status;
        return this;
      }

      @Override
      public Response.ResponseBuilder entity(Object entity) {
        this.entity = entity;
        return this;
      }

      @Override
      public Response.ResponseBuilder type(MediaType type) {
        this.type = type.getType();
        return this;
      }

      @Override
      public Response.ResponseBuilder type(String type) {
        this.type = type;
        return this;
      }

      @Override
      public Response.ResponseBuilder variant(Variant variant) {
        return this;
      }

      @Override
      public Response.ResponseBuilder variants(List<Variant> variants) {
        return this;
      }

      @Override
      public Response.ResponseBuilder language(String language) {
        return this;
      }

      @Override
      public Response.ResponseBuilder language(Locale language) {
        return this;
      }

      @Override
      public Response.ResponseBuilder location(URI location) {
        return this;
      }

      @Override
      public Response.ResponseBuilder contentLocation(URI location) {
        return this;
      }

      @Override
      public Response.ResponseBuilder tag(EntityTag tag) {
        return this;
      }

      @Override
      public Response.ResponseBuilder tag(String tag) {
        return this;
      }

      @Override
      public Response.ResponseBuilder lastModified(Date lastModified) {
        return this;
      }

      @Override
      public Response.ResponseBuilder cacheControl(CacheControl cacheControl) {
        return this;
      }

      @Override
      public Response.ResponseBuilder expires(Date expires) {
        return this;
      }

      @Override
      public Response.ResponseBuilder header(String name, Object value) {
        return this;
      }

      @Override
      public Response.ResponseBuilder cookie(NewCookie... cookies) {
        return this;
      }
    };

  }

  @Override
  public Variant.VariantListBuilder createVariantListBuilder() {
    return null;
  }

  @Override
  public <T> T createEndpoint(Application application, Class<T> endpointType) throws IllegalArgumentException, UnsupportedOperationException {
    return null;
  }

  @Override
  public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) {
    return null;
  }
}
