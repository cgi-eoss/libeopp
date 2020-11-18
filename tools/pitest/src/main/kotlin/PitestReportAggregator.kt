package com.cgi.eoss.eopp.testing.pitest

import org.pitest.aggregate.ReportAggregator
import org.pitest.mutationtest.config.DirectoryResultOutputStrategy
import org.pitest.mutationtest.config.UndatedReportDirCreationStrategy
import java.io.BufferedInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipInputStream

class PitestReportAggregator {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            check(!Files.exists(Paths.get(args[0])))

            val outputPath = Paths.get(args[0])
            val workspacePath = Paths.get(args[1])
            val classesPath = Paths.get(args[2])
            val reportPaths = args.slice(3 until args.size).map { Paths.get(it).toAbsolutePath() }

            writeReport(outputPath, workspacePath, classesPath, reportPaths)
        }

        private fun writeReport(outputPath: Path, workspacePath: Path, classesPath: Path, reportPaths: List<Path>) {
            val pitestInputs = System.getenv("TEST_TMPDIR")?.let(Paths::get)
                ?: Files.createTempDirectory("pitest-inputs-")

            val reportAggregator = ReportAggregator.builder()

            reportPaths.forEachIndexed { idx, reportZip ->
                val reportUnzip = Files.createDirectories(pitestInputs.resolve(idx.toString()))
                val lineCoverage = reportUnzip.resolve("linecoverage.xml")
                val mutationResults = reportUnzip.resolve("mutations.xml")
                ZipInputStream(BufferedInputStream(Files.newInputStream(reportZip))).use { zis ->
                    var entry = zis.nextEntry
                    while (entry != null) {
                        when (entry.name) {
                            "linecoverage.xml" -> Files.copy(zis, lineCoverage)
                            "mutations.xml" -> Files.copy(zis, mutationResults)
                        }
                        entry = zis.nextEntry
                    }
                    zis.closeEntry()
                }
                reportAggregator.addLineCoverageFile(lineCoverage.toFile())
                reportAggregator.addMutationResultsFile(mutationResults.toFile())
            }

            reportAggregator
                .sourceCodeDirectories(listOf(workspacePath.toFile()))
                .compiledCodeDirectories(listOf(classesPath.toFile()))
                .resultOutputStrategy(
                    DirectoryResultOutputStrategy(
                        outputPath.toString(),
                        UndatedReportDirCreationStrategy()
                    )
                )

            reportAggregator.build()
                .aggregateReport()

            println("  Pitest aggregate report generated: file://$outputPath/index.html")
        }
    }
}