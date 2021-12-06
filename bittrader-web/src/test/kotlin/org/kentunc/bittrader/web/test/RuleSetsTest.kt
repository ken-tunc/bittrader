package org.kentunc.bittrader.web.test

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.junit.ArchTests
import org.kentunc.bittrader.test.rules.LayerDependencyRulesTest
import org.kentunc.bittrader.web.BittraderWebApplication

@AnalyzeClasses(
    packagesOf = [BittraderWebApplication::class],
    importOptions = [ImportOption.DoNotIncludeTests::class]
)
class RuleSetsTest {

    @ArchTest
    private val layerDependencyRules = ArchTests.`in`(LayerDependencyRulesTest::class.java)
}
