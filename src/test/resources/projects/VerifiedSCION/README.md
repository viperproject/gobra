# VerifiedSCION (subset)

A subset of [VerifiedSCION](https://github.com/viperproject/VerifiedSCION), taken from commit
`1c4bae889dfc916666efda65912b3e5c0c987313` (the project's final commit; it is no longer maintained).
It is verified by `ScionTests` (`src/test/scala/viper/gobra/ProjectTests.scala`).

Only the packages `pkg/slayers/path{,/empty,/onehop,/epic}` and their transitive imports are included.
Files are copied unmodified (licensed under Apache 2.0, see `LICENSE`), except that build files and non-Go
files were dropped. To include another package, copy it and its imports from the commit above and add it to
`ScionTests.packages`.
