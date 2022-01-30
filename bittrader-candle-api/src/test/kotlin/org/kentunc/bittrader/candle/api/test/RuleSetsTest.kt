package org.kentunc.bittrader.candle.api.test

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.junit.ArchTests
import org.kentunc.bittrader.candle.api.BittraderCandleApiApplication
import org.kentunc.bittrader.test.rules.LayerDependencyRulesTest

@AnalyzeClasses(
    packagesOf = [BittraderCandleApiApplication::class],
    importOptions = [ImportOption.DoNotIncludeTests::class]
)
internal class RuleSetsTest {

    @ArchTest
    private val layerDependencyRules = ArchTests.`in`(LayerDependencyRulesTest::class.java)
}
