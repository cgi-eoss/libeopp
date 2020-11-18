package com.cgi.eoss.eopp.testing.pitest

import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.pitest.mutationtest.commandline.OptionsParser
import org.pitest.mutationtest.commandline.PluginFilter
import org.pitest.mutationtest.config.PluginServices
import org.pitest.mutationtest.tooling.EntryPoint
import org.pitest.util.Unchecked
import java.io.BufferedInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipInputStream
import kotlin.streams.toList

@RunWith(JUnit4::class)
class PitestCoverageTest {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val pitestCoverageTest = PitestCoverageTest()
            pitestCoverageTest.runPitestCoverageTest()
        }
    }

    @Test
    fun runPitestCoverageTest() {
        val inputData = System.getProperty("libeopp.pitest.data")
        check(!(inputData == null || !Files.isReadable(Paths.get(inputData)))) {
            "Inputs for pitest coverage not found or readable: $inputData"
        }
        val targetClasses = System.getProperty("libeopp.pitest.targetClasses")
        val targetTests = System.getProperty("libeopp.pitest.targetTests")

        val pitestInputs = System.getenv("TEST_TMPDIR")?.let(Paths::get)
            ?: Files.createTempDirectory("pitest-inputs-")
        val inputDataPath = Paths.get(inputData)

        extractZip(inputDataPath, pitestInputs)

        // Unjar the classes under test
        val classesDir = Files.createDirectories(pitestInputs.resolve("classes"))
        Files.walk(pitestInputs.resolve("libraries"))
            .filter(Files::isRegularFile)
            .forEach { path -> extractZip(path!!, classesDir) }
        val testClassesDir = Files.createDirectories(pitestInputs.resolve("test_classes"))
        Files.walk(pitestInputs.resolve("test_libraries"))
            .filter(Files::isRegularFile)
            .forEach { path -> extractZip(path!!, testClassesDir) }

        val classPathElements: List<String> =
            Files.walk(pitestInputs.resolve("test_dependencies"))
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .toList()

        val reportDir = System.getenv("TEST_UNDECLARED_OUTPUTS_DIR")?.let(Paths::get)
            ?: Files.createTempDirectory("pitest-outputs-")

        val plugins = PluginServices.makeForContextLoader()
        val parser = OptionsParser(PluginFilter(plugins))
        val pr = parser.parse(
            arrayOf(
                // strap into the config we're passed from the bazel rule
                "--sourceDirs=${pitestInputs.resolve("srcs")},${pitestInputs.resolve("test_srcs")}",
                "--targetClasses=$targetClasses",
                "--targetTests=$targetTests",
                "--classPath=${classPathElements.joinToString(",")},$classesDir,$testClassesDir",
                "--mutableCodePaths=$classesDir",
                "--testPlugin=junit",
                "--includeLaunchClasspath=true",
                // configure outputs
                "--reportDir=$reportDir",
                "--timestampedReports=false",
                "--outputFormats=HTML,XML",
                "--exportLineCoverage=true",
                // TODO configure thresholds for test failure
                "--mutationThreshold=0",
                "--coverageThreshold=0"
            )
        )

        check(pr.isOk) {
            parser.printHelp()
            pr.errorMessage.get()
        }

        val data = pr.options

        val e = EntryPoint()
        val result = e.execute(null, data, plugins, emptyMap())

        if (result.error.isPresent) throw Unchecked.translateCheckedException(result.error.get())

        val stats = result.statistics.get()

        if (data.coverageThreshold != 0 && stats.coverageSummary.coverage < data.coverageThreshold) {
            fail("Line coverage of ${stats.coverageSummary.coverage} is below threshold of ${data.coverageThreshold}")
        }

        if (data.mutationThreshold != 0 && stats.mutationStatistics.percentageDetected < data.mutationThreshold) {
            fail("Mutation score of ${stats.mutationStatistics.percentageDetected} is below threshold of ${data.mutationThreshold}")
        }

        if (data.maximumAllowedSurvivors > 0 && stats.mutationStatistics.totalSurvivingMutations > data.maximumAllowedSurvivors) {
            fail("Had ${stats.mutationStatistics.totalSurvivingMutations} surviving mutants, but only ${data.maximumAllowedSurvivors} survivors allowed")
        }
    }

    private fun extractZip(zipFile: Path, targetDir: Path) {
        ZipInputStream(BufferedInputStream(Files.newInputStream(zipFile))).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                val path = targetDir.resolve(entry.name).normalize()
                if (!path.startsWith(targetDir)) {
                    throw IOException("Invalid ZIP")
                }
                if (entry.isDirectory) {
                    Files.createDirectories(path)
                } else {
                    Files.createDirectories(path.parent)
                    Files.newOutputStream(path).use { os ->
                        val buffer = ByteArray(1024)
                        var len: Int
                        while (zis.read(buffer).also { len = it } > 0) {
                            os.write(buffer, 0, len)
                        }
                    }
                }
                entry = zis.nextEntry
            }
            zis.closeEntry()
        }
    }
}