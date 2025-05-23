import org.openurp.parent.Dependencies.*
import org.openurp.parent.Settings.*

ThisBuild / organization := "org.openurp.tool"
ThisBuild / version := "0.0.1-SNAPSHOT"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/openurp/tool"),
    "scm:git@github.com:openurp/tool.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id = "chaostone",
    name = "Tihua Duan",
    email = "duantihua@gmail.com",
    url = url("http://github.com/duantihua")
  )
)

ThisBuild / description := "OpenURP Tool"
ThisBuild / homepage := Some(url("http://openurp.github.io/tool/index.html"))

val beangle_bui_bootstrap = "org.beangle.bui" % "beangle-bui-bootstrap" % "0.0.5"
val beangle_commons = "org.beangle.commons" % "beangle-commons" % "5.6.28-SNAPSHOT"

val commonLibs = Seq(beangle_commons, beangle_ems_app, beangle_model, beangle_cdi, beangle_jdbc, logback_classic,
  spring_context, spring_beans, spring_tx, spring_jdbc,
  hibernate_core, hibernate_jcache, caffeine_jcache)

lazy val root = (project in file("."))
  .enablePlugins(WarPlugin, UndertowPlugin, TomcatPlugin)
  .settings(
    name := "openurp-tool-webapp",
    common,
    libraryDependencies ++= commonLibs,
    libraryDependencies ++= Seq(beangle_bui_bootstrap),
    libraryDependencies ++= Seq(beangle_webmvc, beangle_doc_transfer),
    libraryDependencies ++= Seq(freemarker, beangle_template),
    libraryDependencies ++= Seq(protobuf, beangle_serializer)
  )
