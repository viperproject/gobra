# Resources
This folder contains resources that are included in the Gobra jar file.

## Built-Ins
The `.gobra` files in the `builtin` folder form a package and thus need to have the same package clause and be located in the top-level directory (i.e. subdirectories are ignored).
Members declared in these files will be available without any import in the file being verified and can be accessed without qualifier.

## Stubs
The `.gobra` files contained in the `stubs` folder are particularly noteworthy:
Gobra uses them when resolving package imports.
Hence, predefined specification for imported libraries can be provided this way.
Note that the `-I` command line option takes higher precendence in the package resolution and might overrule the provided stubs.

### Example
The directory `stubs/github.com/scionproto/scion/go/lib/assert` provides a very simple assertion library, similar to the one SCION has used in the past.
This library can be imported as follows:
```
import stubAssert "github.com/scionproto/scion/go/lib/assert"
``` 
Note that the import path directly corresponds to the directory structure in `stubs`.
As `assert` is a reserved keyword in Gobra and the implicit qualifier would be `assert`, the library is imported with the qualifier `stubAssert`.
The implicit qualifier corresponds to the last path component (here `assert`) and is not related to the package clause used in Gobra files located in the `assert` folder.
