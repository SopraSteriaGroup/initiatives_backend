package com.soprasteria.initiatives.commons.api;

import java.util.Collections;

/**
 * Utitilies for {@link AuthenticatedUser}
 *
 * @author jntakpe
 */
public class UserUtils {

    public static final AuthenticatedUser EMPTY_USER = new AuthenticatedUser(null, null, null, null, Collections.emptyList());

}