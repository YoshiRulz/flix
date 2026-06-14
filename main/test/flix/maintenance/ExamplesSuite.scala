/*
 * Copyright 2026 YoshiRulz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package flix.maintenance

import ca.uwaterloo.flix.TestUtils
import ca.uwaterloo.flix.api.{CompilerConstants, Flix}
import ca.uwaterloo.flix.language.ast.shared.SecurityContext
import ca.uwaterloo.flix.util.Validation
import org.scalatest.DoNotDiscover
import org.scalatest.funsuite.AnyFunSuite

import java.io.IOException
import java.nio.file.{Files, FileSystems, FileVisitResult, Path, PathMatcher, Paths, SimpleFileVisitor}
import java.nio.file.attribute.BasicFileAttributes
import scala.jdk.CollectionConverters.*

@DoNotDiscover
class ExamplesSuite extends AnyFunSuite with TestUtils {
  test("langcensus") {
    val dirpath = Paths.get("examples/apps/langcensus")
    buildProject(dirpath)
  }

  test("tic-tac-toe") {
    val dirpath = Paths.get("examples/apps/tic-tac-toe")
    buildProject(dirpath)
  }

  test("weather") {
    val dirpath = Paths.get("examples/apps/weather")
    buildProject(dirpath)
  }

  test("hello-library") {
    val dirpath = Paths.get("examples/package-manager/hello-library")
    buildProject(dirpath)
  }

  test("hello-world") {
    val dirpath = Paths.get("examples/package-manager/hello-world")
    buildProject(dirpath)
  }

  test("minimal-project") {
    val dirpath = Paths.get("examples/package-manager/minimal-project")
    buildProject(dirpath)
  }

  test("project-with-deps") {
    val dirpath = Paths.get("examples/package-manager/project-with-deps")
    buildProject(dirpath)
  }

  private def buildProject(dirpath: Path): Unit = {
    val flix = new Flix()
    flix.compile()
    val pathMatcher: PathMatcher = FileSystems.getDefault.getPathMatcher("glob:**/*.flix")
    Files.walkFileTree(dirpath, new SimpleFileVisitor[Path] {
      override def visitFile(path: Path, attrs: BasicFileAttributes): FileVisitResult = {
        if (pathMatcher.matches(path)) flix.addFile(path)
        FileVisitResult.CONTINUE
      }

      override def visitFileFailed(file: Path, exc: IOException): FileVisitResult = FileVisitResult.CONTINUE
    })
    assert(flix.compile().isInstanceOf[Validation.Success[?, ?]])
  }
}
