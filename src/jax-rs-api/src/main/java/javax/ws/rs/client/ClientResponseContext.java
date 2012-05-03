/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package javax.ws.rs.client;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.MessageProcessingException;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.ext.MessageBodyWriter;

/**
 * Client response filter context.
 *
 * A mutable class that provides response-specific information for the filter,
 * such as message headers, message entity or request-scoped properties.
 * The exposed setters allow modification of the exposed response-specific
 * information.
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 * @since 2.0
 */
public interface ClientResponseContext {

    /**
     * Get a mutable map of request-scoped properties that can be used for communication
     * between different request/response processing components.
     *
     * May be empty, but MUST never be {@code null}. In the scope of a single
     * request/response processing a same property map instance is shared by the
     * following methods:
     * <ul>
     *     <li>{@link javax.ws.rs.client.ClientRequestContext#getProperties() }</li>
     *     <li>{@link javax.ws.rs.client.ClientResponseContext#getProperties() }</li>
     *     <li>{@link javax.ws.rs.ext.InterceptorContext#getProperties() }</li>
     * </ul>
     *
     * A request-scoped property is an application-defined property that may be
     * added, removed or modified by any of the components (user, filter,
     * interceptor etc.) that participate in a given request/response processing
     * flow.
     * <p />
     * On the client side, this property map is initialized by calling
     * {@link javax.ws.rs.client.Configuration#setProperties(java.util.Map) } or
     * {@link javax.ws.rs.client.Configuration#setProperty(java.lang.String, java.lang.Object) }
     * on the configuration object associated with the corresponding
     * {@link javax.ws.rs.client.Invocation request invocation}.
     * <p />
     * On the server side, specifying the initial values is implementation-specific.
     * <p />
     * If there are no initial properties set, the request-scoped property map is
     * initialized to an empty map.
     *
     * @return a mutable request-scoped property map.
     * @see javax.ws.rs.client.Configuration
     */
    public MultivaluedMap<String, ? extends Serializable> getProperties();

    /**
     * Get the status code associated with the response.
     *
     * @return the response status code or -1 if the status was not set.
     */
    public int getStatusCode();

    /**
     * Set a new response status code.
     *
     * @param code new status code.
     */
    public void setStatusCode(int code);

    /**
     * Get the mutable response headers multivalued map.
     *
     * @return mutable multivalued map of response headers.
     */
    public MultivaluedMap<String, String> getHeaders();

    /**
     * Get the allowed HTTP methods from the Allow HTTP header.
     *
     * @return the allowed HTTP methods, all methods will returned as upper case
     *     strings.
     */
    public Set<String> getAllowedMethods();

    /**
     * Get message date.
     *
     * @return the message date, otherwise {@code null} if not present.
     */
    public Date getDate();

    /**
     * Get the language of the entity.
     *
     * @return the language of the entity or {@code null} if not specified
     */
    public Locale getLanguage();

    /**
     * Get Content-Length value.
     *
     * @return Content-Length as integer if present and valid number. In other
     *     cases returns -1.
     */
    public int getLength();

    /**
     * Get the media type of the entity.
     *
     * @return the media type or {@code null} if not specified (e.g. there's no
     *     response entity).
     */
    public MediaType getMediaType();

    /**
     * Get any new cookies set on the response message.
     *
     * @return a read-only map of cookie name (String) to a {@link NewCookie new cookie}.
     */
    public Map<String, NewCookie> getCookies();

    /**
     * Get the entity tag.
     *
     * @return the entity tag, otherwise {@code null} if not present.
     */
    public EntityTag getEntityTag();

    /**
     * Get the last modified date.
     *
     * @return the last modified date, otherwise {@code null} if not present.
     */
    public Date getLastModified();

    /**
     * Get the location.
     *
     * @return the location URI, otherwise {@code null} if not present.
     */
    public URI getLocation();

    /**
     * Get the links attached to the message as header.
     *
     * @return links, may return empty {@link Set} if no links are present. Never
     *     returns {@code null}.
     */
    public Set<Link> getLinks();

    /**
     * Check if link for relation exists.
     *
     * @param relation link relation.
     * @return {@code true} if the for the relation link exists, {@code false}
     *     otherwise.
     */
    boolean hasLink(String relation);

    /**
     * Get the link for the relation.
     *
     * @param relation link relation.
     * @return the link for the relation, otherwise {@code null} if not present.
     */
    public Link getLink(String relation);

    /**
     * Convenience method that returns a {@link javax.ws.rs.core.Link.Builder Link.Builder}
     * for the relation.
     *
     * @param relation link relation.
     * @return the link builder for the relation, otherwise {@code null} if not
     *     present.
     */
    public Link.Builder getLinkBuilder(String relation);

    /**
     * Check if there is a non-empty entity available in the response message.
     *
     * The method returns {@code true} if the entity is present, returns
     * {@code false} otherwise.
     * <p/>
     * Note that in case the message contained an entity, but it was already
     * consumed via one of the {@code writeEntity(...)} methods without being
     * {@link #bufferEntity() buffered} or replaced using one of the
     * {@code writeEntity(...)} methods, the {@code hasEntity()} returns {@code false}.
     *
     * @return {@code true} if there is an entity present in the message,
     *     {@code false} otherwise.
     */
    public boolean hasEntity();

    /**
     * Write a new message entity. Effectively replaces the existing entity with
     * a new one.
     *
     * The method finds and invokes the proper {@link MessageBodyWriter} to
     * serialize the entity into an input stream from which it can be read again.
     *
     * @param <T> entity type.
     * @param entity the instance to write.
     * @param type the class of object that is to be written.
     * @param mediaType the entity media type.
     * @param annotations an array of the annotations attached to the entity.
     * @throws MessageProcessingException if there was en error writing
     *     the entity.
     */
    public <T> void writeEntity(
            final Class<T> type,
            final Annotation annotations[],
            final MediaType mediaType,
            final T entity);

    /**
     * Write a new message entity. Effectively replaces the existing entity with
     * a new one.
     *
     * The method finds and invokes the proper {@link MessageBodyWriter} to
     * serialize the entity into an input stream from which it can be read again.
     *
     * @param <T> entity type.
     * @param entity the instance to write.
     * @param genericType the generic type information of object that is to be
     *     written.
     * @param mediaType the entity media type.
     * @param annotations an array of the annotations attached to the entity.
     * @throws MessageProcessingException if there was en error writing
     *     the entity.
     */
    public <T> void writeEntity(
            final GenericType<T> genericType,
            final Annotation annotations[],
            final MediaType mediaType,
            final T entity);

    /**
     * Read the message entity as an instance of specified Java type using
     * a {@link javax.ws.rs.ext.MessageBodyReader} that supports mapping the
     * message entity stream onto the requested type.
     *
     * Returns {@code null} if the message does not {@link #hasEntity() contain}
     * an entity body. Unless the supplied entity type is an
     * {@link java.io.InputStream input stream}, this method automatically
     * {@link InputStream#close() closes} the consumed entity stream.
     * In case the entity was {@link #bufferEntity() bufferd} previously,
     * the buffered input stream is reset so that the entity is readable again.
     *
     * @param <T> entity instance Java type.
     * @param type the requested type of the entity.
     * @return the message entity or {@code null} if message does not contain an
     *     entity body.
     * @throws MessageProcessingException if the content of the message cannot be
     *     mapped to an entity of the requested type.
     * @see javax.ws.rs.ext.MessageBodyWriter
     * @see javax.ws.rs.ext.MessageBodyReader
     */
    public <T> T readEntity(final Class<T> type) throws MessageProcessingException;

    /**
     * Read the message entity as an instance of specified Java type using
     * a {@link javax.ws.rs.ext.MessageBodyReader} that supports mapping the
     * message entity stream onto the requested type.
     *
     * Returns {@code null} if the message does not {@link #hasEntity() contain}
     * an entity body. Unless the supplied entity type is an
     * {@link java.io.InputStream input stream}, this method automatically
     * {@link InputStream#close() closes} the consumed entity stream.
     * In case the entity was {@link #bufferEntity() bufferd} previously,
     * the buffered input stream is reset so that the entity is readable again.
     *
     * @param <T> entity instance Java type.
     * @param type the requested generic type of the entity.
     * @return the message entity or {@code null} if message does not contain an
     *     entity body.
     * @throws MessageProcessingException if the content of the message cannot be
     *     mapped to an entity of the requested type.
     * @see javax.ws.rs.ext.MessageBodyWriter
     * @see javax.ws.rs.ext.MessageBodyReader
     */
    public <T> T readEntity(final GenericType<T> type) throws MessageProcessingException;

    /**
     * Read the message entity as an instance of specified Java type using
     * a {@link javax.ws.rs.ext.MessageBodyReader} that supports mapping the
     * message entity stream onto the requested type.
     *
     * Returns {@code null} if the message does not {@link #hasEntity() contain}
     * an entity body. Unless the supplied entity type is an
     * {@link java.io.InputStream input stream}, this method automatically
     * {@link InputStream#close() closes} the consumed entity stream.
     * In case the entity was {@link #bufferEntity() bufferd} previously,
     * the buffered input stream is reset so that the entity is readable again.
     *
     * @param <T> entity instance Java type.
     * @param type the requested type of the entity.
     * @param annotations annotations attached to the entity.
     * @return the message entity or {@code null} if message does not contain an
     *     entity body.
     * @throws MessageProcessingException if the content of the message cannot be
     *     mapped to an entity of the requested type.
     * @see javax.ws.rs.ext.MessageBodyWriter
     * @see javax.ws.rs.ext.MessageBodyReader
     */
    public <T> T readEntity(final Class<T> type, final Annotation[] annotations) throws MessageProcessingException;

    /**
     * Read the message entity as an instance of specified Java type using
     * a {@link javax.ws.rs.ext.MessageBodyReader} that supports mapping the
     * message entity stream onto the requested type.
     *
     * Returns {@code null} if the message does not {@link #hasEntity() contain}
     * an entity body. Unless the supplied entity type is an
     * {@link java.io.InputStream input stream}, this method automatically
     * {@link InputStream#close() closes} the consumed entity stream.
     * In case the entity was {@link #bufferEntity() bufferd} previously,
     * the buffered input stream is reset so that the entity is readable again.
     *
     * @param <T> entity instance Java type.
     * @param type the requested generic type of the entity.
     * @param annotations annotations attached to the entity.
     * @return the message entity or {@code null} if message does not contain an
     *     entity body.
     * @throws MessageProcessingException if the content of the message cannot be
     *     mapped to an entity of the requested type.
     * @see javax.ws.rs.ext.MessageBodyWriter
     * @see javax.ws.rs.ext.MessageBodyReader
     */
    public <T> T readEntity(final GenericType<T> type, final Annotation[] annotations) throws MessageProcessingException;

    /**
     * Buffer the original message entity stream.
     *
     * In case the original message entity input stream is open, all the bytes of
     * the stream are read and stored in memory. The original entity input stream
     * is automatically {@link InputStream#close() closed} afterwards.
     * <p />
     * This operation is idempotent, i.e. it can be invoked multiple times with
     * the same effect which also means that calling the {@code bufferEntity()}
     * method on an already buffered (and thus closed) message instance is legal
     * and has no further effect.
     * <p />
     * Note that if the message entity has been replaced using one of the
     * {@code writeEntity(...)} methods, such entity input stream is buffered
     * already and a subsequent call to {@code bufferEntity()} has no effect.
     *
     * @throws MessageProcessingException if there is an error buffering the
     *     message entity.
     * @since 2.0
     */
    public void bufferEntity() throws MessageProcessingException;

    /**
     * Get the entity input stream.
     *
     * @return entity input stream.
     */
    public InputStream getEntityStream();

    /**
     * Set a new entity input stream.
     *
     * @param input new entity input stream.
     */
    public void setEntityStream(InputStream input);
}