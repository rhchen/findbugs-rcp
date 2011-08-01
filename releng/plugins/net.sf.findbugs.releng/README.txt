
How to build from source ?

To build from sources, you need eclipse, cdt and rse binary

1. Edit build.sh

variable "eclipseDist" point to eclipse
variable "eclipseDeltaDist" point to eclipse delta pack
variable "cdtDist" point ot cdt
variable "rseDist" point to rse
varibale "workDir" point to your path where build occurs

set JAVA_HOME in build.sh, point to you JDK install

2. Edit build.properties

variable "buildDirectory" point to the path reference "buildDirectory" in build.sh
variable "base" point to the path reference "baseLocation" in build.sh

3. run build.sh