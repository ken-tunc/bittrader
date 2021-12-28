package org.kentunc.bittrader.candle.api.test

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.junit.ArchTests
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import org.kentunc.bittrader.candle.api.BittraderCandleApiApplication
import org.kentunc.bittrader.test.rules.LayerDependencyRulesTest
import org.springframework.transaction.annotation.Transactional

@AnalyzeClasses(
    packagesOf = [BittraderCandleApiApplication::class],
    importOptions = [ImportOption.DoNotIncludeTests::class]
)
internal class RuleSetsTest {

    @ArchTest
    private val layerDependencyRules = ArchTests.`in`(LayerDependencyRulesTest::class.java)

    @ArchTest
    private val transactionalAnnotationRule =
        methods().that().areDeclaredInClassesThat().haveSimpleNameEndingWith("Interactor")
            .should().beAnnotatedWith(Transactional::class.java)
}
