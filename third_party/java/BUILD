# Re-export this without building into runtime classpaths, e.g. to use @org.springframework.lang.NonNullApi
java_library(
    name = "org_springframework_spring_core_neverlink",
    neverlink = True,
    visibility = ["//visibility:public"],
    exports = ["@maven//:org_springframework_spring_core"],
)
