# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "Unknown"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f098732a73b5f6f3430472f5b094ffdb"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-BrianDelalex.git;protocol=ssh;branch=main"

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "7c7832a65a96d935a06dd4d4ce0612b909be56f0"

S = "${WORKDIR}/git"

inherit module
EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/misc-modules"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

inherit module update-rc.d

SRC_URI += "file://S98miscmodules"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "S98miscmodules"

FILES:${PN} += "${sbindir}/module_load"
FILES:${PN} += "${sbindir}/module_unload"
FILES:${PN} += "S98miscmodules"
FILES:${PN} += "${sysconfdir}"
FILES:${PN} += "${sysconfdir}/init.d"


do_configure () {
	:
}

do_compile () {
	oe_runmake
}

do_install () {
    # Installing module
    install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra
    install -m 0755 ${S}/misc-modules/hello.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra
    install -m 0755 ${S}/misc-modules/faulty.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra

    # Installing module stop and start script
    install -d ${D}/usr/sbin
    install -m 0755 ${S}/misc-modules/module_load ${D}/usr/sbin
    install -m 0755 ${S}/misc-modules/module_unload ${D}/usr/sbin

    # Installing init script for misc-module    
    install -d ${D}${sysconfdir}/init.d/
    install -m 0755 ${THISDIR}/files/S98miscmodules ${D}${sysconfdir}/init.d/
}