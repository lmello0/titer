package com.lmello.titer;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static org.assertj.core.api.Assertions.assertThatNoException;

public class ModularityTest {

    ApplicationModules modules = ApplicationModules.of(TiterApplication.class);

    @Test
    void verifiesModuleStructure() {
        modules.verify();
    }

    @Test
    void rendersDiagram() {
        new Documenter(modules)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
    }

    @Test
    void printModuleInfo() {
        modules.forEach(System.out::println);
    }

    @Test
    void noCircularDependencies() {
        assertThatNoException().isThrownBy(modules::verify);
    }
}
