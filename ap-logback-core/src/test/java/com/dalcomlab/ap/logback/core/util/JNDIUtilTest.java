package com.dalcomlab.ap.logback.core.util;

import com.dalcomlab.ap.logback.core.testUtil.MockInitialContextFactory;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Hashtable;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class JNDIUtilTest {

	@Test
	public void ensureJavaNameSpace() throws NamingException {

		try {
			Context ctxt = JNDIUtil.getInitialContext();
			JNDIUtil.lookupString(ctxt, "ldap:...");
		} catch (NamingException e) {
			String excaptionMsg = e.getMessage();
			if (excaptionMsg.startsWith(JNDIUtil.RESTRICTION_MSG))
				return;
			else {
				fail("unexpected exception " + e);
			}
		}

		fail("Should aNot yet implemented");
	}

	@Test
	public void testToStringCast() throws NamingException {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put(CoreTestConstants.JAVA_NAMING_FACTORY_INITIAL, MockInitialContextFactory.class.getCanonicalName());
		Context ctxt = JNDIUtil.getInitialContext(props);
		String x = JNDIUtil.lookupString(ctxt, "java:comp:/inexistent");
		assertNull(x);
	}

	public String castToString(Object input) {
		String a = (String) input;
		return a;
	}

}
