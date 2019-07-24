package viper.gobra

import java.nio.file.Path

import viper.silver

class TestAnnotationParser extends silver.testing.TestAnnotationParser {
  def parse(file: Path)
  : (List[silver.testing.TestAnnotationParseError],
    List[silver.testing.TestAnnotation]) =

    parseAnnotations(file)
}
