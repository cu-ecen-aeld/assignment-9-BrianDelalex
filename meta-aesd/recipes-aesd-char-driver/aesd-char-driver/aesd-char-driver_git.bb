# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# TODO: Set this  with the path to your assignments rep.  Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-BrianDelalex;protocol=ssh;branch=master"

PV = "1.0+git${SRCPV}"
# TODO: set to reference a specific commit hash in your assignment repo
SRCREV = "b69f760341f45690fab64a3e2efb65a1fe4125b7"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo
S = "${WORKDIR}/git/aesd-char-driver"

inherit module
EXTRA_CFLAGS = "-std=gnu11"

EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR} CFLAGS='${EXTRA_CFLAGS}'"

inherit module update-rc.d

SRC_URI += "file://aesd-char-driver-start-stop"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "aesd-char-driver-start-stop"

# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
FILES:${PN} += "${sbindir}/aesdchar_load"
FILES:${PN} += "${sbindir}/aesdchar_unload"
FILES:${PN} += "aesd-char-driver-start-stop"
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
    install -m 0755 ${S}/aesdchar.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra

    # Installing module stop and start script
    install -d ${D}/usr/sbin
    install -m 0755 ${S}/aesdchar_load ${D}/usr/sbin
    install -m 0755 ${S}/aesdchar_unload ${D}/usr/sbin

    # Installing init script for module aesd-char-driver    
    install -d ${D}${sysconfdir}/init.d/
    install -m 0755 ${THISDIR}/files/aesd-char-driver-start-stop ${D}${sysconfdir}/init.d/
}
