package org.kentunc.bittrader.test.rules

import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.Architectures
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LayerDependencyRulesTest {

    companion object {
        private const val DOMAIN_LAYER = "Domain"
        private const val APPLICATION_LAYER = "Application"
        private const val PRESENTATION_LAYER = "Presentation"
        private const val INFRASTRUCTURE_LAYER = "Infrastructure"
        private const val OTHERS = "Others"
    }

    @ArchTest
    private val layeredArchitectureTest: Architectures.LayeredArchitecture =
        layeredArchitecture().withOptionalLayers(true)
            .layer(DOMAIN_LAYER).definedBy("..domain..")
            .layer(APPLICATION_LAYER).definedBy("..application..")
            .layer(PRESENTATION_LAYER).definedBy("..presentation..")
            .layer(INFRASTRUCTURE_LAYER).definedBy("..infrastructure..")
            .layer(OTHERS).definedBy("..test..")
            .whereLayer(APPLICATION_LAYER).mayOnlyBeAccessedByLayers(PRESENTATION_LAYER, OTHERS)
            .whereLayer(PRESENTATION_LAYER).mayOnlyBeAccessedByLayers(INFRASTRUCTURE_LAYER, OTHERS)
            .whereLayer(INFRASTRUCTURE_LAYER).mayOnlyBeAccessedByLayers(OTHERS)
            .whereLayer(OTHERS).mayNotBeAccessedByAnyLayer()
}
