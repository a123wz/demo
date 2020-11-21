package org.demo.drools;

import org.apache.flink.api.common.resources.Resource;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;

public class KiaSessionConfig {

    private static final String RULES_PATH = "drools/";

    public KieFileSystem kieFileSystem() throws IOException {
        KieFileSystem kieFileSystem = getKieServices().newKieFileSystem();
//        for (Resource file : getRuleFiles()) {
//            kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + file.getFilename(), "UTF-8"));
//        }
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "test.drl", "UTF-8"));
        return kieFileSystem;
    }

//    private Resource[] getRuleFiles() throws IOException {
//
//        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//        final Resource[] resources = resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "**/*.*");
//        return resources;
//
//    }

    public KieContainer kieContainer() throws IOException {

        final KieRepository kieRepository = getKieServices().getRepository();
        kieRepository.addKieModule(new KieModule() {
            public ReleaseId getReleaseId() {
                return kieRepository.getDefaultReleaseId();
            }
        });

        KieBuilder kieBuilder = getKieServices().newKieBuilder(kieFileSystem());
        kieBuilder.buildAll();
        return getKieServices().newKieContainer(kieRepository.getDefaultReleaseId());

    }

    private KieServices getKieServices() {
        return KieServices.Factory.get();
    }

    public KieBase kieBase() throws IOException {
        return kieContainer().getKieBase();
    }

    public KieSession kieSession() throws IOException {
        return kieContainer().newKieSession();
    }
}
