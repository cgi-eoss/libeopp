java_binary(
    name = "MavenPublisher",
    srcs = [
        "ByteStreams.java",
        "MavenPublisher.java",
    ],
    javacopts = [
        "-source",
        "8",
        "-target",
        "8",
    ],
    jvm_flags = [
        "-Djava.util.logging.SimpleFormatter.format='%1$$tFT%1$$tT.%1$$tL %4$$-7s %5$$s%n'",
    ],
    main_class = "rules.jvm.external.maven.MavenPublisher",
    visibility = ["//visibility:public"],
)
