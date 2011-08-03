#!/bin/bash

# Build for eclipse 3.6.2
#eclipseDist="/home/rhchen/Downloads/eclipse/eclipse/eclipse-SDK-3.6.2-linux-gtk.tar.gz"
#eclipseDeltaDist="/home/rhchen/Downloads/eclipse/eclipse/eclipse-3.6.2-delta-pack.zip"
#cdtDist="/home/rhchen/Downloads/eclipse/cdt/cdt-master-7.0.2.zip"
#rseDist="/home/rhchen/Downloads/eclipse/rse/RSE-SDK-3.2.2.zip"

# Build for eclipse 3.7
eclipseDist="/home/rhchen/Downloads/eclipse/eclipse/eclipse-SDK-3.7-linux-gtk.tar.gz"
eclipseDeltaDist="/home/rhchen/Downloads/eclipse/eclipse/eclipse-3.7-delta-pack.zip"

workDir="/home/rhchen/works/build/findbugs"
rm -Rf ${workDir}
mkdir -p ${workDir}
echo "workdir is ${workDir}"

buildDirectory=${workDir}/buildDirectory
mkdir -p ${buildDirectory}
echo "buildDirectory is ${buildDirectory}"

baseLocation=${workDir}/baseLocation
mkdir -p ${baseLocation}
echo "baseLocation is ${baseLocation}"

buildConfiguration=${workDir}/buildConfiguration
mkdir -p ${buildConfiguration}
echo "buildConfiguration is ${buildConfiguration}"

srcDir=${workDir}/src
mkdir -p ${srcDir}
echo "srcdir is ${srcDir}"

# Unzip required plugins
cd ${baseLocation}
tar -xzvf ${eclipseDist}

unzip -o -d delta ${eclipseDeltaDist}
cp -Rf delta/eclipse/plugins/* eclipse/plugins/
cp -Rf delta/eclipse/features/* eclipse/features/
rm -Rf delta

# Checkout source codes
echo "Fetch Source Codes from github ....."

cd ${srcDir}
git clone git://github.com/rhchen/findbugs-rcp.git

cp -Rf ${srcDir}/findbugs-rcp/rcp/* ${buildDirectory}
#cp -Rf ${srcDir}/findbugs-rcp/libs/* ${baseLocation}/eclipse
cp -Rf ${srcDir}/findbugs-rcp/libs/* ${buildDirectory}
cp -Rf ${srcDir}/findbugs-rcp/releng/* ${buildDirectory}

# Prepareing build environment
export JAVA_HOME=/home/rhchen/works/tools/jdk17
export PATH=$JAVA_HOME/bin:$PATH
echo "JAVA_HOME = ${JAVA_HOME}"
`java -version`

cp ${buildDirectory}/plugins/net.sf.findbugs.releng/featureBuild/build.properties ${buildConfiguration}

echo "Start build findbugs-rcp"

# Feature Build For Eclipse 3.7
java -jar ${baseLocation}/eclipse/plugins/org.eclipse.equinox.launcher_1.2.0.v20110502.jar \
	-application org.eclipse.ant.core.antRunner \
	-buildfile ${baseLocation}/eclipse/plugins/org.eclipse.pde.build_3.7.0.v20110512-1320/scripts/build.xml \
	-Dbuilder=${buildConfiguration}/ \
	-DbuildDirectory=${buildDirectory} \
	-Dbase=${baseLocation}

# Feature Build for Eclipse 3.6.2
#java -jar ${baseLocation}/eclipse/plugins/org.eclipse.equinox.launcher_1.1.1.R36x_v20101122_1400.jar -application org.eclipse.ant.core.antRunner -buildfile ${baseLocation}/eclipse/plugins/org.eclipse.pde.build_3.6.2.R36x_20110203/scripts/build.xml -Dbuilder=${buildConfiguration}/
