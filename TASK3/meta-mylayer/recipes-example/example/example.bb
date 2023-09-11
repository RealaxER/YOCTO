SUMMARY = "bitbake-layers recipe"
LICENSE = "MIT"
SECTION = "examples"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

python do_display_banner() {
    bb.plain("***********************************************");
    bb.plain("*                                             *");
    bb.plain("*  Example recipe created by bitbake-layers   *");
    bb.plain("*                                             *");
    bb.plain("***********************************************");
}

SRC_URI = "file://hello.c"

S = "${WORKDIR}"

do_compile() {
        ${CC} hello.c ${LDFLAGS} -o example
}

do_install() {
        install -d ${D}${bindir}
        install -m 0755 example ${D}${bindir}
}


