/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.validator;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.ImplementationId;
import org.openmrs.api.AdministrationService;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.test.Verifies;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

public class ImplementationIdValidatorTest extends BaseContextSensitiveTest {
	
	/**
	 * @see {@link ImplementationIdValidator#validate(Object,Errors)}
	 */
	@Test
	@Verifies(value = "should fail validation if implementation id is null", method = "validate(Object,Errors)")
	public void validate_shouldFailValidationIfImplementationIdIsNull() {
		ImplementationId implementationId = new ImplementationId();
		implementationId.setPassphrase("PASSPHRASE");
		implementationId.setDescription("Description");
		
		Errors errors = new BindException(implementationId, "implementationId");
		new ImplementationIdValidator().validate(implementationId, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("implementationId"));
		Assert.assertFalse(errors.hasFieldErrors("passphrase"));
		Assert.assertFalse(errors.hasFieldErrors("description"));
	}
	
	/**
	 * @see {@link ImplementationIdValidator#validate(Object,Errors)}
	 */
	@Test
	@Verifies(value = "should pass validation if description is null", method = "validate(Object,Errors)")
	public void validate_shouldPassValidationIfDescriptionIsNull() {
		ImplementationId implementationId = new ImplementationId();
		implementationId.setImplementationId("IMPL_ID");
		implementationId.setPassphrase("PASSPHRASE");
		
		Errors errors = new BindException(implementationId, "implementationId");
		new ImplementationIdValidator().validate(implementationId, errors);
		
		Assert.assertFalse(errors.hasFieldErrors("description"));
		Assert.assertFalse(errors.hasFieldErrors("implementationId"));
		Assert.assertFalse(errors.hasFieldErrors("passphrase"));
	}
	
	/**
	 * @see {@link ImplementationIdValidator#validate(Object,Errors)}
	 */
	@Test
	@Verifies(value = "should fail validation if pass phrase is null", method = "validate(Object,Errors)")
	public void validate_shouldFailValidationIfPassPhraseIsNull() {
		ImplementationId implementationId = new ImplementationId();
		implementationId.setImplementationId("IMPL_ID");
		implementationId.setDescription("Description");
		
		Errors errors = new BindException(implementationId, "implementationId");
		new ImplementationIdValidator().validate(implementationId, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("passphrase"));
		Assert.assertFalse(errors.hasFieldErrors("implementationId"));
		Assert.assertFalse(errors.hasFieldErrors("description"));
	}
	
	@Test()
	@Verifies(value = "should fail if given empty implementationId object", method = "validate(Object,Errors)")
	public void validate_shouldFailIfGivenEmptyImplementationIdObject() throws Exception {
		// save a blank impl id. exception thrown
		ImplementationId implementationId = new ImplementationId();
		Errors errors = new BindException(implementationId, "implementationId");
		new ImplementationIdValidator().validate(implementationId, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("passphrase"));
		Assert.assertTrue(errors.hasFieldErrors("implementationId"));
		Assert.assertFalse(errors.hasFieldErrors("description"));
	}
	
	/**
	 * @see {@link AdministrationService#setImplementationId(ImplementationId)}
	 */
	@Test
	@Verifies(value = "should fail if given a caret in the implementationId code", method = "validate(Object,Errors)")
	public void validate_shouldFailIfGivenACaretInTheImplementationIdCode() throws Exception {
		ImplementationId invalidId = new ImplementationId();
		invalidId.setImplementationId("caret^caret");
		invalidId.setName("an invalid impl id for a unit test");
		invalidId.setPassphrase("some valid passphrase");
		invalidId.setDescription("Some valid description");
		Errors errors = new BindException(invalidId, "implementationId");
		new ImplementationIdValidator().validate(invalidId, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("implementationId"));
		Assert.assertEquals("ImplementationId.implementationId.invalidcharacter", errors.getFieldError("implementationId")
		        .getCode());
	}
	
	/**
	 * @see {@link AdministrationService#setImplementationId(ImplementationId)}
	 */
	@Test
	@Verifies(value = "should fail if given a pipe in the implementationId code", method = "validate(Object,Errors)")
	public void validate_shouldFailIfGivenAPipeInTheImplementationIdCode() throws Exception {
		// save an impl id with an invalid hl7 code
		ImplementationId invalidId2 = new ImplementationId();
		invalidId2.setImplementationId("pipe|pipe");
		invalidId2.setName("an invalid impl id for a unit test");
		invalidId2.setPassphrase("some valid passphrase");
		invalidId2.setDescription("Some valid description");
		Errors errors = new BindException(invalidId2, "implementationId");
		new ImplementationIdValidator().validate(invalidId2, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("implementationId"));
		Assert.assertEquals("ImplementationId.implementationId.invalidcharacter", errors.getFieldError("implementationId")
		        .getCode());
	}
	
}
