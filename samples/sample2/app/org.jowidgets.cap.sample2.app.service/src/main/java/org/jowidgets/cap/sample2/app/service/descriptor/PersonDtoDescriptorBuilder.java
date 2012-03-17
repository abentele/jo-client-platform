/*
 * Copyright (c) 2011, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.cap.sample2.app.service.descriptor;

import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.cap.common.api.bean.IBeanPropertyBluePrint;
import org.jowidgets.cap.common.tools.bean.BeanDtoDescriptorBuilder;
import org.jowidgets.cap.sample2.app.common.bean.IPerson;
import org.jowidgets.cap.sample2.app.service.lookup.CountriesLookUpService;
import org.jowidgets.cap.sample2.app.service.lookup.GenderLookUpService;
import org.jowidgets.cap.sample2.app.service.lookup.RolesLookUpService;

public class PersonDtoDescriptorBuilder extends BeanDtoDescriptorBuilder {

	public PersonDtoDescriptorBuilder() {
		super(IPerson.class);

		setLabelSingular("User");
		setLabelPlural("Users");

		IBeanPropertyBluePrint propertyBp;

		propertyBp = addProperty(IBean.ID_PROPERTY);
		propertyBp.setLabel("Id");
		propertyBp.setDescription("The users technical identifier");

		propertyBp = addProperty(IPerson.LOGIN_NAME_PROPERTY);
		propertyBp.setLabel("Login");
		propertyBp.setLabelLong("Login name");
		propertyBp.setDescription("The users login name");
		propertyBp.setMandatory(true);

		propertyBp = addProperty(IPerson.NAME_PROPERTY);
		propertyBp.setLabel("Name");
		propertyBp.setDescription("The users name");
		propertyBp.setMandatory(true);

		propertyBp = addProperty(IPerson.LAST_NAME_PROPERTY);
		propertyBp.setLabel("Lastname");
		propertyBp.setDescription("The users lastname");
		propertyBp.setMandatory(true);

		propertyBp = addProperty(IPerson.GENDER_PROPERTY);
		propertyBp.setLabel("Gender");
		propertyBp.setDescription("The users gender");
		propertyBp.setValueRange(GenderLookUpService.valueRange());
		propertyBp.setMandatory(true);

		propertyBp = addProperty(IPerson.COUNTRY_ID_PROPERTY);
		propertyBp.setLabel("Country");
		propertyBp.setDescription("The country the user lives");
		propertyBp.setLookUpValueRange(CountriesLookUpService.LOOK_UP_ID);

		propertyBp = addProperty(IPerson.ROLE_IDS_PROPERTY);
		propertyBp.setLabel("Roles");
		propertyBp.setDescription("The users roles");
		propertyBp.setElementValueType(Long.class);
		propertyBp.setLookUpValueRange(RolesLookUpService.LOOK_UP_ID);
		propertyBp.setSortable(false);
		propertyBp.setFilterable(true);

		propertyBp = addProperty(IPerson.ACTIVE_PROPERTY);
		propertyBp.setLabel("Active");
		propertyBp.setDescription("Determines if the person is active");
		propertyBp.setDefaultValue(Boolean.TRUE);
		propertyBp.setMandatory(true);
		propertyBp.setEditable(false);

		propertyBp = addProperty(IBean.VERSION_PROPERTY);
		propertyBp.setLabel("Version");
		propertyBp.setDescription("The version of the dataset");

	}
}
