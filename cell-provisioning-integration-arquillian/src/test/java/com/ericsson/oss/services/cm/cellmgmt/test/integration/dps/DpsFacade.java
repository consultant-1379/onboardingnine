/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2016
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.services.cm.cellmgmt.test.integration.dps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.exceptions.MoNotFoundException;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.TimeAssistant;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn;

/**
 * Provides a simplified way to use {@link DataPersistenceService}.
 */
@Stateless
public class DpsFacade {

    public static final boolean NULL_ALLOWED = true;
    public static final boolean NULL_FORBIDDEN = false;

    private static final int NOTIFICATION_LOCK_WINDOW_MILLISECS = 250;

    @Inject
    private Logger logger;

    @EJB(lookup = DataPersistenceService.JNDI_LOOKUP_NAME)
    private DataPersistenceService dps;

    // --- MO operations --- //

    public ManagedObject findMo(final Fdn fdn, final boolean allowNull) {
        final ManagedObject mo = getLiveBucket().findMoByFdn(fdn.toString());

        if (mo == null && allowNull) {
            logger.warn("Could not find '{}' MO.", fdn);
        } else if (mo == null && !allowNull) {
            throw new MoNotFoundException(fdn);
        }

        logger.trace("FDN '{}' resolved to '{}' MO.", fdn, mo);
        return mo;
    }

    public boolean moExists(final Fdn fdn) {
        return findMo(fdn, NULL_ALLOWED) == null ? false : true;
    }

    public String createMo(final Mo moDto) {
        final ManagedObject parentMo = getParentMo(moDto.getParentFdn());

        final String fdn = getLiveBucket().getManagedObjectBuilder()
                .parent(parentMo)
                .type(moDto.getType())
                .name(moDto.getName())
                .addAttributes(moDto.getAttributes())
                .create().getFdn();

        logger.info("Created '{}' MO.", fdn);
        return fdn;
    }

    public String createMibRootMo(final Mo moDto) {
        final ManagedObject parentMo = getParentMo(moDto.getParentFdn());

        final String fdn = getLiveBucket().getMibRootBuilder()
                .parent(parentMo)
                .type(moDto.getType())
                .name(moDto.getName())
                .namespace(moDto.getNamespace())
                .addAttributes(moDto.getAttributes())
                .create().getFdn();

        logger.info("Created '{}' MIB Root MO.", fdn);
        return fdn;
    }

    public void deleteMo(final Fdn fdn) {
        deleteMo(fdn, NOTIFICATION_LOCK_WINDOW_MILLISECS);
    }

    public void deleteMo(final Fdn fdn, final int leadTime) {
        TimeAssistant.sleep(leadTime); // prevents potential OptimisticLockException
        final ManagedObject moToDelete = findMo(fdn, NULL_ALLOWED);

        if (moToDelete != null) {
            getLiveBucket().deletePo(moToDelete);
            logger.info("Deleted '{}' MO.", fdn);
        }
    }

    public void deleteMos(final List<Fdn> fdns) {
        deleteMos(fdns, NOTIFICATION_LOCK_WINDOW_MILLISECS);
    }

    public void deleteMos(final List<Fdn> fdns, final int leadTime) {
        for (final Fdn fdn : fdns) {
            deleteMo(fdn, leadTime);
        }
    }

    public void deleteMos(final Fdn... fdns) {
        deleteMos(Arrays.asList(fdns));
    }

    // --- MO Attribute operations --- //

    public <AttrType> AttrType readAttribute(final Fdn fdn, final String attributeName) {
        final ManagedObject mo = findMo(fdn, NULL_FORBIDDEN);
        final AttrType attributeValue = mo.getAttribute(attributeName);

        logger.debug("Read '{}.{}' : '{}'.", fdn, attributeName, attributeValue);
        return attributeValue;
    }

    public void setAttribute(final Fdn fdn, final String attributeName, final Object attributeValue) {
        final ManagedObject mo = findMo(fdn, NULL_FORBIDDEN);
        mo.setAttribute(attributeName, attributeValue);
        logger.info("Wrote '{}.{}' = '{}'.", attributeName, attributeValue, fdn);
    }

    public void setAttributes(final Fdn fdn, final Map<String, Object> attributes) {
        final ManagedObject mo = findMo(fdn, NULL_FORBIDDEN);
        mo.setAttributes(attributes);
        for (final Entry entry : attributes.entrySet()) {
            logger.info("Wrote '{}.{}' = '{}'.", entry.getKey(), entry.getValue(), fdn);
        }
    }

    // --- Other operations --- //

    public void executeMoAction(final Fdn fdn, final String action, final Map<String, Object> options) {
        final ManagedObject functionMo = findMo(fdn, NULL_FORBIDDEN);

        logger.info("Executing '{}' MO action...", action);
        functionMo.performAction(action, options);
    }

    // --- Private operations --- //

    private DataBucket getLiveBucket() {
        return dps.getLiveBucket();
    }

    private ManagedObject getParentMo(final Fdn parentFdn) {
        if (parentFdn == null) {
            return null;

        } else {
            return getLiveBucket().findMoByFdn(parentFdn.toString());
        }
    }

}
