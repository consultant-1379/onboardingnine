/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2016
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.services.cm.cellmgmt.test.integration.deployment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

/**
 * Resolves artifacts and creates archives using ShrinkWrap framework.
 */
public final class ArchiveFactory {

    private static final String BASE_PROJECT_PACKAGE = "com.ericsson.oss.services.cm.cellmgmt.test.integration";

    private static final String ASSERTJ_ARTIFACT_COORDS = "org.assertj:assertj-core:jar:?";
    private static final String CMS_API_ARTIFACT_COORDS = "com.ericsson.oss.services.cm.cellprov:cell-provisioning-service-api-jar:jar:?";
    private static final String GUAVA_ARTIFACT_COORDS = "com.google.guava:guava:jar:?";
    private static final String JSCH_ARTIFACT_COORDS = "com.jcraft:jsch:jar:?";
    private static final String SFWK_DIST_ARTIFACT_COORDS = "com.ericsson.oss.itpf.sdk:service-framework-dist:jar:?";

    private static final String POM_XML = "pom.xml";
    private static final String BEANS_XML = "beans.xml";
    private static final String JBOSS_DEPLOYMENT_STRUCTURE_XML = "jboss-deployment-structure.xml";

    private static final String DRIVER_ARCHIVE = "cell-mgmt-arq-driver.ear";
    private static final String CLASSES_ARCHIVE = "cell-mgmt-arq-classes.jar";

    private ArchiveFactory() {}

    /**
     * Resolves an artifact (of any type) for given coordinates, without including any transitive dependencies.
     *
     * @param archiveType
     *            i.e. JAR ({@code JavaArchive}), EAR ({@code EnterpriseArchive}), etc.
     * @param artifactCoordinates
     *            Maven artifact coordinates ({@code groupId:artifactId:packaging:?})
     * @return package file representing the resolved artifact
     */
    public static <T extends Archive<T>> T resolveArtifactWithoutDependencies(final Class<T> archiveType, final String artifactCoordinates) {
        final File artifactFile =
                Maven.resolver().loadPomFromFile(POM_XML).resolve(artifactCoordinates).withoutTransitivity().asSingleFile();
        return ShrinkWrap.createFromZipFile(archiveType, artifactFile);
    }

    /**
     * Creates a Driver EAR, allowing the tests to be executed in a real JEE container.
     *
     * @return driver EAR that contains the integration tests, and all the classes and libraries these tests depend on.
     */
    public static EnterpriseArchive createDriverEar() {
        final EnterpriseArchive driverEar = ShrinkWrap.create(EnterpriseArchive.class, DRIVER_ARCHIVE);
        driverEar.addAsModule(createTestClassesJar());
        driverEar.addAsLibraries(createRequiredLibraries());
        driverEar.addAsManifestResource(JBOSS_DEPLOYMENT_STRUCTURE_XML);

        return driverEar;
    }

    private static JavaArchive createTestClassesJar() {
        final JavaArchive classesJar = ShrinkWrap.create(JavaArchive.class, CLASSES_ARCHIVE);
        classesJar.addPackages(true, BASE_PROJECT_PACKAGE);
        classesJar.addAsManifestResource(EmptyAsset.INSTANCE, BEANS_XML);

        return classesJar;
    }

    private static List<JavaArchive> createRequiredLibraries() {
        final List<JavaArchive> libraries = new ArrayList<>();
        libraries.add(resolveArtifactWithoutDependencies(JavaArchive.class, ASSERTJ_ARTIFACT_COORDS));
        libraries.add(resolveArtifactWithoutDependencies(JavaArchive.class, CMS_API_ARTIFACT_COORDS));
        libraries.add(resolveArtifactWithoutDependencies(JavaArchive.class, GUAVA_ARTIFACT_COORDS));
        libraries.add(resolveArtifactWithoutDependencies(JavaArchive.class, JSCH_ARTIFACT_COORDS));
        libraries.add(resolveArtifactWithoutDependencies(JavaArchive.class, SFWK_DIST_ARTIFACT_COORDS));

        return libraries;
    }

}
