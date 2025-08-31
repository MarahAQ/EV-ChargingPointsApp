package com.example.chargingpointsapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringHelperTest {

    @Test
    public void testValidEmails() {
        assertTrue("Valid email failed", StringHelper.isValidEmail("test@example.com"));
        assertTrue("Valid email failed", StringHelper.isValidEmail("user.name123@domain.co"));
        assertTrue("Valid email failed", StringHelper.isValidEmail("first.last@company.io"));
    }

    @Test
    public void testInvalidEmails() {
        assertFalse("Invalid email passed", StringHelper.isValidEmail("invalid-email")); // ❌ No @ symbol
        assertFalse("Invalid email passed", StringHelper.isValidEmail("user@domain")); // ❌ No domain extension
        assertFalse("Invalid email passed", StringHelper.isValidEmail("user@.com")); // ❌ No domain name
    }
}