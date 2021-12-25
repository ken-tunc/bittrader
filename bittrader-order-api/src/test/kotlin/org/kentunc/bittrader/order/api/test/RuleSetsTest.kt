package org.kentunc.bittrader.order.api.test

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.junit.ArchTests
import org.kentunc.bittrader.order.api.BittraderOrderApiApplication
import org.kentunc.bittrader.test.rules.LayerDependencyRulesTest

@AnalyzeClasses(
    packagesOf = [BittraderOrderApiApplication::class],
    importOptions = [ImportOption.DoNotIncludeTests::class]
)
internal class RuleSetsTest {

    @ArchTest
    private val layerDependencyRules = ArchTests.`in`(LayerDependencyRulesTest::class.java)
}
